/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;

import model.dao.ResultDAO;
import model.dao.CertificateDAO;
import model.entity.Result;
import model.entity.Certificate;
import java.util.*;

/**
 *
 * @author ADMIN
 */
public class ResultService {
    private NotificationService notificationService;

    public ResultService() {
        this.notificationService = new NotificationService();
    }

    // Get result by ID
    public Result getResultById(int resultId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.getResultById(resultId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all results for a user
    public List<Result> getResultsByUserId(int userId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.getResultsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get passed results for a user
    public List<Result> getPassedResultsByUserId(int userId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            List<Result> allResults = resultDAO.getResultsByUserId(userId);
            List<Result> passedResults = new ArrayList<>();
            for (Result result : allResults) {
                if (result.isPassStatus()) {
                    passedResults.add(result);
                }
            }
            return passedResults;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get failed results for a user
    public List<Result> getFailedResultsByUserId(int userId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            List<Result> allResults = resultDAO.getResultsByUserId(userId);
            List<Result> failedResults = new ArrayList<>();
            for (Result result : allResults) {
                if (!result.isPassStatus()) {
                    failedResults.add(result);
                }
            }
            return failedResults;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all results for an exam
    public List<Result> getResultsByExamId(int examId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.getResultsByExamId(examId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get result for a specific user in a specific exam
    public Result getResultByUserAndExam(int userId, int examId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.getResultByUserAndExam(userId, examId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create a new result with validation
    public boolean createResult(Result result) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (result.getExamID() <= 0 || result.getUserID() <= 0 || result.getScore() < 0 || result.getScore() > 999.99) {
                System.out.println("Invalid result data");
                return false;
            }
            ResultDAO resultDAO = new ResultDAO();
            boolean success = resultDAO.createResult(result);
            if (success && result.isPassStatus()) {
                issueCertificate(result);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update result with validation and certificate check
    public boolean updateResult(Result result) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (result.getResultID() <= 0 || result.getExamID() <= 0 || result.getUserID() <= 0 || result.getScore() < 0 || result.getScore() > 999.99) {
                System.out.println("Invalid result data for update");
                return false;
            }
            ResultDAO resultDAO = new ResultDAO();
            boolean wasPassing = resultDAO.getResultById(result.getResultID()).isPassStatus();
            boolean success = resultDAO.updateResult(result);
            if (success && !wasPassing && result.isPassStatus()) {
                issueCertificate(result);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update result score and pass status
    public boolean updateResultScore(int resultId, double score, boolean passStatus) {
        try {
            if (resultId <= 0 || score < 0 || score > 999.99) {
                System.out.println("Invalid result ID or score");
                return false;
            }
            ResultDAO resultDAO = new ResultDAO();
            Result existingResult = resultDAO.getResultById(resultId);
            if (existingResult == null) {
                System.out.println("Result not found");
                return false;
            }
            boolean wasPassing = existingResult.isPassStatus();
            boolean success = resultDAO.updateResultScore(resultId, score, passStatus);
            if (success && !wasPassing && passStatus) {
                issueCertificate(existingResult);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countAllResults() {
        ResultDAO resultDAO = new ResultDAO();
        try {
            return resultDAO.countAllResults();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int countPassResults() {
        ResultDAO resultDAO = new ResultDAO();    
        try {
            return resultDAO.countPassResults();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int countResultsByExamId(int examId) {
        ResultDAO resultDAO = new ResultDAO();
        try {
            return resultDAO.countResultsByExamId(examId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int countResultsBySupervisor(int supervisorId) {
        ResultDAO resultDAO = new ResultDAO();
        try {
            return resultDAO.countResultsBySupervisor(supervisorId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int countPassResultsBySupervisor(int supervisorId) {
        ResultDAO resultDAO = new ResultDAO();
        try {
            return resultDAO.countPassResultsBySupervisor(supervisorId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // Delete result
    public boolean deleteResult(int resultId) {
        try {
            if (resultId <= 0) {
                System.out.println("Invalid result ID");
                return false;
            }
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.deleteResult(resultId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get results with user details (with pagination)
    public List<Object[]> getResultsWithUserDetails(int examId, int page, int pageSize) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.getResultsWithUserDetails(examId, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Count passed students for an exam
    public int countPassedStudentsByExamId(int examId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.countPassedStudentsByExamId(examId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Calculate average score for an exam
    public double getAverageScoreByExamId(int examId) {
        try {
            ResultDAO resultDAO = new ResultDAO();
            return resultDAO.getAverageScoreByExamId(examId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // Private method to issue certificate and send notification
    private void issueCertificate(Result result) {
        try {
            CertificateDAO certificateDAO = new CertificateDAO();
            Certificate existingCert = certificateDAO.getCertificatesByUserId(result.getUserID()).stream()
                    .filter(c -> c.getExpirationDate().after(new Date()))
                    .findFirst().orElse(null);
            if (existingCert == null) {
                Certificate cert = new Certificate();
                cert.setUserID(result.getUserID());
                cert.setIssuedDate(new Date());
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.YEAR, 1); // Giả định chứng chỉ có hiệu lực 1 năm
                cert.setExpirationDate(cal.getTime());
                cert.setCertificateCode(generateCertificateCode(result.getUserID()));
                certificateDAO.createCertificate(cert);

                // Gửi thông báo
                notificationService.sendNotification(result.getUserID(), 
                    "Congratulations! You have passed the exam and received a certificate. Code: " + cert.getCertificateCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Private method to generate unique certificate code
    private String generateCertificateCode(int userId) {
        return "CERT-" + userId + "-" + System.currentTimeMillis();
    }
}