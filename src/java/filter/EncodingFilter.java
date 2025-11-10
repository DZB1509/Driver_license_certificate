/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/AdvancedFilter.java to edit this template
 */
package filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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

/**
 *
 * @author ADMIN
 */
public class EncodingFilter implements Filter {
    private static final boolean debug = false;
    private static final String ENCODING = "UTF-8";
    private FilterConfig filterConfig = null;

    public EncodingFilter() {
    }

    /**
     * Called before the filter chain processes the request.
     * Sets the character encoding for the request and response.
     */
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("EncodingFilter: DoBeforeProcessing");
        }

        // Cast to HttpServletRequest and HttpServletResponse
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Set character encoding for the request
        if (httpRequest.getCharacterEncoding() == null) {
            httpRequest.setCharacterEncoding(ENCODING);
        }

        // Set character encoding and content type for the response
        httpResponse.setCharacterEncoding(ENCODING);
        httpResponse.setContentType("text/html;charset=" + ENCODING);
    }

    /**
     * Called after the filter chain processes the request (optional).
     * No specific action needed for encoding filter after processing.
     */
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("EncodingFilter: DoAfterProcessing");
        }
    }

    /**
     * Main filter method to process the request and response.
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     * @throws IOException if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Đặt encoding cho request
        request.setCharacterEncoding("UTF-8");

        // Đặt encoding và Content-Type cho response
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("text/html; charset=UTF-8");

        // Tiếp tục chuỗi filter
        chain.doFilter(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    /**
     * Set the filter configuration object for this filter.
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter.
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Init method for this filter.
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null && debug) {
            log("EncodingFilter: Initializing filter");
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("EncodingFilter()");
        }
        StringBuilder sb = new StringBuilder("EncodingFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return sb.toString();
    }

    /**
     * Log a message to the servlet context if filterConfig is available.
     */
    private void log(String msg) {
        if (filterConfig != null) {
            filterConfig.getServletContext().log(msg);
        }
    }
}
