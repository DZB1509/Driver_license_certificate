package filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.util.Enumeration;

public class UserAuthFilter implements Filter {
    
    private static final boolean debug = true;
    private static final Logger LOGGER = Logger.getLogger(UserAuthFilter.class.getName());
    private FilterConfig filterConfig = null;
    private FileHandler fileHandler;

    public UserAuthFilter() {
    }    
    
    private void doBeforeProcessing(RequestWrapper request, ResponseWrapper response)
            throws IOException, ServletException {
        if (debug) {
            log("UserAuthFilter:DoBeforeProcessing");
        }
    }    
    
    private void doAfterProcessing(RequestWrapper request, ResponseWrapper response)
            throws IOException, ServletException {
        if (debug) {
            log("UserAuthFilter:DoAfterProcessing");
        }
        response.addHeader("X-Filtered-By", "UserAuthFilter");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) request);
        ResponseWrapper wrappedResponse = new ResponseWrapper((HttpServletResponse) response);

        if (debug) {
            log("UserAuthFilter: Processing request");
        }

        doBeforeProcessing(wrappedRequest, wrappedResponse);

        HttpServletRequest httpRequest = wrappedRequest;
        HttpServletResponse httpResponse = wrappedResponse;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String loginURI = contextPath + "/login";

        // Bỏ qua các yêu cầu đến tài nguyên tĩnh (CSS, JS, hình ảnh, v.v.)
        if (requestURI.endsWith(".css") || requestURI.endsWith(".js") || 
            requestURI.endsWith(".png") || requestURI.endsWith(".jpg") || 
            requestURI.endsWith(".jpeg") || requestURI.endsWith(".gif")) {
            chain.doFilter(wrappedRequest, wrappedResponse);
            return;
        }

        LOGGER.info("Request URI: " + requestURI + " | Method: " + httpRequest.getMethod() + 
                    " | IP: " + httpRequest.getRemoteAddr());

        // Bỏ qua filter cho trang login
        if (requestURI.equals(loginURI)) {
            chain.doFilter(wrappedRequest, wrappedResponse);
            return;
        }

        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            LOGGER.warning("Unauthorized access attempt to: " + requestURI);
            httpResponse.sendRedirect(loginURI + "?error=Please login first");
            return;
        }

        // Lấy thông tin user
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // Kiểm tra phân quyền dựa trên URI
        if (requestURI.startsWith(contextPath + "/student/") && !"student".equalsIgnoreCase(role)) {
            LOGGER.warning("Access denied for user ID: " + userId + " with role: " + role + " to: " + requestURI);
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Only students can access this resource.");
            return;
        } else if (requestURI.startsWith(contextPath + "/admin/") && !"admin".equalsIgnoreCase(role)) {
            LOGGER.warning("Access denied for user ID: " + userId + " with role: " + role + " to: " + requestURI);
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Only admins can access this resource.");
            return;
        }

        LOGGER.info("User ID: " + userId + " with role: " + role + " authorized to access: " + requestURI);

        Throwable problem = null;
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Throwable t) {
            problem = t;
            LOGGER.severe("Error in filter chain: " + t.getMessage());
            t.printStackTrace();
        }

        doAfterProcessing(wrappedRequest, wrappedResponse);

        if (problem != null) {
            if (problem instanceof ServletException) throw (ServletException) problem;
            if (problem instanceof IOException) throw (IOException) problem;
            sendProcessingError(problem, wrappedResponse);
        }
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void destroy() { 
        if (fileHandler != null) {
            fileHandler.close();
        }
        LOGGER.info("UserAuthFilter destroyed.");
    }

    public void init(FilterConfig filterConfig) throws ServletException {        
        this.filterConfig = filterConfig;
        try {
            // Sử dụng thư mục logs mặc định của Tomcat
            String tomcatLogDir = System.getProperty("catalina.base") + File.separator + "logs";
            File logDir = new File(tomcatLogDir);
            if (!logDir.exists() && !logDir.mkdirs()) {
                LOGGER.warning("Cannot create log directory: " + tomcatLogDir + ". Falling back to console logging.");
            } else {
                String logFilePath = tomcatLogDir + File.separator + "user_auth_filter.log";
                fileHandler = new FileHandler(logFilePath, true);
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);
                LOGGER.info("UserAuthFilter initialized. Log file: " + logFilePath);
            }
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            LOGGER.warning("Failed to initialize file logging: " + e.getMessage() + ". Falling back to console logging.");
        }
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("UserAuthFilter()");
        }
        StringBuffer sb = new StringBuffer("UserAuthFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        LOGGER.severe("Processing error: " + t.getMessage() + "\n" + getStackTrace(t));
        try {
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            pw.print("<html><head><title>Error</title></head><body>");
            pw.print("<h1>Internal Server Error</h1>");
            pw.print("<p>An unexpected error occurred. Please contact the administrator.</p>");
            pw.print("</body></html>");
            pw.close();
        } catch (IOException e) {
            LOGGER.severe("Failed to send error response: " + e.getMessage());
        }
    }
    
    public static String getStackTrace(Throwable t) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            return sw.toString();
        } catch (IOException e) {
            return t.toString();
        }
    }
    
    public void log(String msg) {
        if (filterConfig != null) {
            filterConfig.getServletContext().log(msg);
        }        
    }

    class RequestWrapper extends HttpServletRequestWrapper {
        private Map<String, String[]> localParams = null;

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public void setParameter(String name, String[] values) {
            if (debug) {
                System.out.println("UserAuthFilter::setParameter(" + name + "=" + values + ")");
            }
            if (localParams == null) {
                localParams = new HashMap<>(getRequest().getParameterMap());
            }
            localParams.put(name, values);
        }

        @Override
        public String getParameter(String name) {
            if (localParams == null) return getRequest().getParameter(name);
            String[] val = localParams.get(name);
            return (val != null && val.length > 0) ? val[0] : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            if (localParams == null) return getRequest().getParameterValues(name);
            return localParams.get(name);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            if (localParams == null) return getRequest().getParameterNames();
            return Collections.enumeration(localParams.keySet());
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            if (localParams == null) return getRequest().getParameterMap();
            return localParams;
        }
    }

    class ResponseWrapper extends HttpServletResponseWrapper {
        public ResponseWrapper(HttpServletResponse response) {
            super(response);            
        }
    }
}