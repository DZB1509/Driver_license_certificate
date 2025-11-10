/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;
import model.dao.ExamDAO;
import model.entity.Exam;
import model.dao.ResultDAO;
import model.entity.Result;
import java.sql.SQLException;
import java.util.*;
import javax.sql.DataSource;
import model.entity.User;
/**
 *
 * @author ADMIN
 */
public class ExamService {
    private ExamDAO examDAO;
    private ResultDAO resultDAO;

    public ExamService() {
        this.examDAO = new ExamDAO();
        this.resultDAO = new ResultDAO();
    }

    // Create a new exam with validation
    public boolean createExam(Exam exam) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (exam.getCourseID() <= 0 || exam.getSupervisorID() < 0 || exam.getDate() == null || exam.getRoom() == null || exam.getStatus() == null) {
                System.out.println("Invalid exam data");
                return false;
            }
            ExamDAO examDAO = new ExamDAO();
            return examDAO.createExam(exam);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get exam by ID
    public Exam getExamById(int examId) {
    try {
        return examDAO.getExamById(examId);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    // Get all exams
    public List<Exam> getAllExams() {
        try {
            ExamDAO examDAO = new ExamDAO();
            return examDAO.getAllExams();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get exams by course ID
    public List<Exam> getExamsByCourseId(int courseId) {
        try {
            ExamDAO examDAO = new ExamDAO();
            return examDAO.getExamsByCourseId(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Exam> getExamsByDate(java.sql.Date date) {
    try {
        return examDAO.getExamsByDate(date);
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    
    public List<Exam> getExamsByDateRange(java.sql.Date start, java.sql.Date end) throws Exception {
    try {
        return examDAO.getExamsByDateRange(start, end);
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    // Get exams by supervisor ID
    public List<Exam> getExamsBySupervisorId(int supervisorId) {
        try {
            ExamDAO examDAO = new ExamDAO();
            return examDAO.getExamsBySupervisorId(supervisorId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Exam> getUpcomingExams(java.sql.Date fromDate, int page, int pageSize) {
    try {
        return examDAO.getUpcomingExams(fromDate, page, pageSize);
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    
    public int getUpcomingExamsCount(java.sql.Date fromDate) {
    try {
        return examDAO.getUpcomingExamsCount(fromDate);
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
}
    
    public List<User> getStudentsByExamId(int examId) throws Exception {
    try {
        UserService userService = new UserService();
        return userService.getStudentsByExamId(examId);
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    
    public boolean assignSupervisor(int examId, int supervisorId) {
    try {
        if (supervisorId <= 0) {
            System.out.println("Invalid supervisor ID");
            return false;
        }
        Exam exam = getExamById(examId);
        if (exam != null) {
            exam.setSupervisorID(supervisorId);
            return examDAO.updateExam(exam);
        }
        return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    // Update exam with validation
    public boolean updateExam(Exam exam) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (exam.getExamID() <= 0 || exam.getCourseID() <= 0 || exam.getSupervisorID() < 0 || exam.getDate() == null || exam.getRoom() == null || exam.getStatus() == null) {
                System.out.println("Invalid exam data for update");
                return false;
            }
            ExamDAO examDAO = new ExamDAO();
            return examDAO.updateExam(exam);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update exam status with validation
    public boolean updateExamStatus(int examId, String status) {
        try {
            if (examId <= 0 || status == null || status.trim().isEmpty()) {
                System.out.println("Invalid exam ID or status");
                return false;
            }
            ExamDAO examDAO = new ExamDAO();
            return examDAO.updateExamStatus(examId, status);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateExamSchedule(int examId, java.sql.Date date, String room) {
    try {
        Exam exam = getExamById(examId);
        if (exam != null) {
            exam.setDate(date);
            exam.setRoom(room);
            return examDAO.updateExam(exam);
        }
        return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
    // Delete exam with check
    public boolean deleteExam(int examId) {
        try {
            ExamDAO examDAO = new ExamDAO();
            ResultDAO resultDAO = new ResultDAO();
            // Kiểm tra xem kỳ thi có kết quả nào chưa
            List<Result> results = resultDAO.getResultsByExamId(examId);
            if (!results.isEmpty()) {
                System.out.println("Cannot delete exam with existing results");
                return false;
            }
            return examDAO.deleteExam(examId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Hủy đăng ký kỳ thi cho user
    public boolean cancelExamRegistration(int examId, int userId) {
        try {
            if (examId <= 0 || userId <= 0) {
                System.out.println("Invalid exam ID or user ID: examId=" + examId + ", userId=" + userId);
                return false;
            }

            // Lấy thông tin kỳ thi
            Exam exam = examDAO.getExamById(examId);
            if (exam == null) {
                System.out.println("Exam not found with ID: " + examId);
                return false;
            }

            // Kiểm tra trạng thái kỳ thi
            String examStatus = exam.getStatus();
            if ("In Progress".equals(examStatus) || "Completed".equals(examStatus)) {
                System.out.println("Cannot cancel registration for an exam that is in progress or completed: " + examStatus);
                return false;
            }

            // Kiểm tra xem user đã đăng ký kỳ thi chưa (dựa trên Results)
            Result result = resultDAO.getResultByUserAndExam(userId, examId);
            if (result == null) {
                System.out.println("User " + userId + " is not registered for exam " + examId);
                return false;
            }

            // Xóa bản ghi trong Results để hủy đăng ký
            boolean deleted = resultDAO.deleteResult(result.getResultID());
            if (deleted) {
                System.out.println("Successfully cancelled registration for user " + userId + " in exam " + examId);
                return true;
            } else {
                System.out.println("Failed to delete registration record for user " + userId + " in exam " + examId);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Exam> getCompletedExams() {
    try {
        return examDAO.getExamsByStatus("Completed");
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}

    // Get future exams for a course
    public List<Exam> getFutureExamsByCourseId(int courseId) {
        try {
            ExamDAO examDAO = new ExamDAO();
            List<Exam> allExams = examDAO.getExamsByCourseId(courseId);
            List<Exam> futureExams = new ArrayList<>();
            Date today = new Date();
            for (Exam exam : allExams) {
                if (exam.getDate().after(today)) {
                    futureExams.add(exam);
                }
            }
            return futureExams;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Check if a student can register for an exam
    public boolean canStudentRegisterForExam(int userId, int examId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            ExamDAO examDAO = new ExamDAO();
            // Kiểm tra kỳ thi tồn tại và chưa diễn ra
            Exam exam = examDAO.getExamById(examId);
            if (exam == null || exam.getDate().before(new Date())) {
                System.out.println("Exam does not exist or has already occurred");
                return false;
            }
            Result existingResult = resultDAO.getResultByUserAndExam(userId, examId);
            return existingResult == null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get exams with statistics (number of registrations, pass rate) with pagination
    public List<Map<String, Object>> getExamsWithStatistics(int page, int pageSize) {
        List<Map<String, Object>> examStats = new ArrayList<>();
        try {
            ExamDAO examDAO = new ExamDAO();
            ResultDAO resultDAO = new ResultDAO();
            List<Exam> exams = examDAO.getAllExams();
            // Phân trang thủ công
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, exams.size());
            if (start >= exams.size()) {
                return examStats; // Trả về danh sách rỗng nếu vượt quá
            }

            for (int i = start; i < end; i++) {
                Exam exam = exams.get(i);
                Map<String, Object> examStat = new HashMap<>();
                examStat.put("exam", exam);

                List<Result> results = resultDAO.getResultsByExamId(exam.getExamID());
                int totalStudents = results.size();
                int passedStudents = 0;

                for (Result result : results) {
                    if (result.isPassStatus()) {
                        passedStudents++;
                    }
                }

                double passRate = totalStudents > 0 ? (double) passedStudents / totalStudents * 100 : 0;
                examStat.put("totalStudents", totalStudents);
                examStat.put("passedStudents", passedStudents);
                examStat.put("passRate", passRate);

                examStats.add(examStat);
            }
            return examStats;
        } catch (Exception e) {
            e.printStackTrace();
            return examStats;
        }
    }

    // Approve exam by supervisor with validation
    public boolean approveExam(int examId, int supervisorId) {
        try {
            ExamDAO examDAO = new ExamDAO();
            Exam exam = examDAO.getExamById(examId);
            if (exam == null) {
                System.out.println("Exam not found");
                return false;
            }
            if (supervisorId <= 0) {
                System.out.println("Invalid supervisor ID");
                return false;
            }
            exam.setSupervisorID(supervisorId);
            exam.setStatus("Approved");
            return examDAO.updateExam(exam);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register student for exam with validation
    public boolean registerStudentForExam(int userId, int examId) {
        try {
            if (!canStudentRegisterForExam(userId, examId)) {
                System.out.println("Student cannot register for this exam");
                return false;
            }
            Result result = new Result();
            result.setExamID(examId);
            result.setUserID(userId);
            result.setScore(0.0); // Default score
            result.setPassStatus(false); // Default pass status

            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.createResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int countExamsByStatus(String status) {
    try {
        return examDAO.countExamsByStatus(status);
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
    }
}
    
    public int countExamsByStatusAndSupervisor(String status, int supervisorId) {
    try {
        return examDAO.countExamsByStatusAndSupervisor(status, supervisorId);
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
    }
}
    
    public List<Exam> getExamsByDateRangeAndSupervisor(java.sql.Date start, java.sql.Date end, int supervisorId) {
    try {
        return examDAO.getExamsByDateRangeAndSupervisor(start, end, supervisorId);
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
}
