/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;

import java.sql.SQLException;
import model.dao.CertificateDAO;
import model.entity.Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CertificateService {
    private CertificateDAO certificateDAO;

    public CertificateService() {
        this.certificateDAO = new CertificateDAO();
    }

    // Generate a new certificate for a user свободы passed the exam
    public Certificate generateCertificate(int userId) {
    try {
        if (userId <= 0) {
            System.out.println("Invalid user ID");
            return null;
        }
        if (certificateDAO.userHasValidCertificate(userId)) {
            System.out.println("User already has a valid certificate");
            return null;
        }
        String certificateCode = generateUniqueCertificateCode();
        Date issuedDate = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(issuedDate);
        cal.add(Calendar.YEAR, 1);
        Date expirationDate = new Date(cal.getTimeInMillis());

        Certificate certificate = new Certificate();
        certificate.setUserID(userId);
        certificate.setIssuedDate(issuedDate);
        certificate.setExpirationDate(expirationDate);
        certificate.setCertificateCode(certificateCode);

        boolean success = certificateDAO.createCertificate(certificate);
        return success ? certificate : null;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    // Generate a unique certificate code
    private String generateUniqueCertificateCode() {
        Calendar cal = Calendar.getInstance();
        String prefix = "DL" + cal.get(Calendar.YEAR);
        String randomSuffix = String.format("%06d", (int) (Math.random() * 1000000));
        String code = prefix + "-" + randomSuffix;

        // Kiểm tra trùng lặp
        while (certificateDAO.getCertificateByCode(code) != null) {
            randomSuffix = String.format("%06d", (int) (Math.random() * 1000000));
            code = prefix + "-" + randomSuffix;
        }
        return code;
    }

    // Get a certificate by its ID
    public Certificate getCertificateById(int certificateId) {
        try {
            if (certificateId <= 0) {
                System.out.println("Invalid certificate ID");
                return null;
            }
            return certificateDAO.getCertificateById(certificateId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPendingCertificatesCount() {
    try {
        return certificateDAO.getPendingCertificatesCount();
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
}
    
    public List<Certificate> getRecentCertificates() {
    try {
        return certificateDAO.getRecentCertificates();
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    
    // Get all certificates for a user
    public List<Certificate> getCertificatesByUserId(int userId) {
        try {
            if (userId <= 0) {
                System.out.println("Invalid user ID");
                return new ArrayList<>();
            }
            return certificateDAO.getCertificatesByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Check if a certificate is valid
    public boolean isCertificateValid(String certificateCode) {
        try {
            if (certificateCode == null || certificateCode.trim().isEmpty()) {
                System.out.println("Invalid certificate code");
                return false;
            }
            Certificate certificate = certificateDAO.getCertificateByCode(certificateCode);
            if (certificate == null) {
                return false;
            }
            Date currentDate = new Date(System.currentTimeMillis());
            Date expirationDate = certificate.getExpirationDate();
            return !currentDate.after(expirationDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all certificates in the system (with pagination)
    public List<Certificate> getAllCertificates(int page, int pageSize) {
        try {
            if (page < 1 || pageSize <= 0) {
                System.out.println("Invalid page or page size");
                return new ArrayList<>();
            }
            List<Certificate> allCerts = certificateDAO.getAllCertificates();
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, allCerts.size());
            if (start >= allCerts.size()) {
                return new ArrayList<>();
            }
            return allCerts.subList(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get certificates by status (recently issued, about to expire, expired)
    public List<Certificate> getCertificatesByStatus(String status, int page, int pageSize) {
        try {
            if (status == null || page < 1 || pageSize <= 0) {
                System.out.println("Invalid status, page, or page size");
                return new ArrayList<>();
            }
            List<Certificate> allCerts = certificateDAO.getAllCertificates();
            List<Certificate> filteredCerts = new ArrayList<>();
            Date currentDate = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DAY_OF_YEAR, 30); // 30 ngày sau
            Date thirtyDaysLater = new Date(cal.getTimeInMillis());
            
            for (Certificate cert : allCerts) {
                Date issuedDate = cert.getIssuedDate();
                Date expirationDate = cert.getExpirationDate();
                cal.setTime(currentDate);
                cal.add(Calendar.DAY_OF_YEAR, -30);
                Date thirtyDaysAgo = new Date(cal.getTimeInMillis());
                
                switch (status.toLowerCase()) {
                    case "recent":
                        if (issuedDate.after(thirtyDaysAgo)) {
                            filteredCerts.add(cert);
                        }
                        break;
                    case "expiring":
                        if (expirationDate.after(currentDate) && expirationDate.before(thirtyDaysLater)) {
                            filteredCerts.add(cert);
                        }
                        break;
                    case "expired":
                        if (expirationDate.before(currentDate)) {
                            filteredCerts.add(cert);
                        }
                        break;
                    default:
                        return getAllCertificates(page, pageSize);
                }
            }

            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, filteredCerts.size());
            if (start >= filteredCerts.size()) {
                return new ArrayList<>();
            }
            return filteredCerts.subList(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Certificate> getCertificatesByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
    try {
        return certificateDAO.getCertificatesByDateRange(startDate, endDate);
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    
    public boolean approveCertificate(int certificateId, String result) {
    try {
        Certificate certificate = certificateDAO.getCertificateById(certificateId);
        if (certificate != null) {
            return certificateDAO.updateCertificate(certificate);
        }
        return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    // Get certificates with user details (with pagination)
    public List<Object[]> getCertificatesWithUserDetails(int page, int pageSize) {
        try {
            if (page < 1 || pageSize <= 0) {
                System.out.println("Invalid page or page size");
                return new ArrayList<>();
            }
            return certificateDAO.getCertificatesWithUserDetails(page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public int countAllCertificates() {
        try {
            return certificateDAO.countAllCertificates();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int countCertificatesIssuedThisMonth() {
        try {
            return certificateDAO.countCertificatesIssuedThisMonth();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int countCertificatesIssuedLastMonth() {
        try {
            return certificateDAO.countCertificatesIssuedLastMonth();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int countCertificatesBySupervisor(int supervisorId) {
        try {
            return certificateDAO.countCertificatesBySupervisor(supervisorId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // Update a certificate
    public boolean updateCertificate(Certificate certificate) {
        try {
            if (certificate == null || certificate.getCertificateID() <= 0 || certificate.getUserID() <= 0 || 
                certificate.getIssuedDate() == null || certificate.getExpirationDate() == null || 
                certificate.getCertificateCode() == null) {
                System.out.println("Invalid certificate data");
                return false;
            }
            Date issuedDate = certificate.getIssuedDate();
            Date expirationDate = certificate.getExpirationDate();
            if (issuedDate.after(expirationDate)) {
                System.out.println("Issued date must be before expiration date");
                return false;
            }
            return certificateDAO.updateCertificate(certificate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Renew an expired certificate
    public Certificate renewCertificate(int certificateId) {
        try {
            if (certificateId <= 0) {
                System.out.println("Invalid certificate ID");
                return null;
            }
            Certificate certificate = certificateDAO.getCertificateById(certificateId);
            if (certificate == null) {
                System.out.println("Certificate not found");
                return null;
            }
            Date newIssuedDate = new Date(System.currentTimeMillis()); // Ngày hiện tại
            Calendar cal = Calendar.getInstance();
            cal.setTime(newIssuedDate);
            cal.add(Calendar.YEAR, 1); // Cộng 1 năm
            Date newExpirationDate = new Date(cal.getTimeInMillis());

            certificate.setIssuedDate(newIssuedDate);
            certificate.setExpirationDate(newExpirationDate);
            boolean success = certificateDAO.updateCertificate(certificate);
            return success ? certificate : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Revoke a certificate
    public boolean revokeCertificate(int certificateId) {
        try {
            if (certificateId <= 0) {
                System.out.println("Invalid certificate ID");
                return false;
            }
            Certificate certificate = certificateDAO.getCertificateById(certificateId);
            if (certificate == null) {
                System.out.println("Certificate not found");
                return false;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.add(Calendar.DAY_OF_YEAR, -1); // Trừ 1 ngày để hết hạn ngay lập tức
            Date newExpirationDate = new Date(cal.getTimeInMillis());
            
            certificate.setExpirationDate(newExpirationDate);
            return certificateDAO.updateCertificate(certificate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean userHasValidCertificate(int userId) {
        try {
            return certificateDAO.userHasValidCertificate(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a certificate
    public boolean deleteCertificate(int certificateId) {
        try {
            if (certificateId <= 0) {
                System.out.println("Invalid certificate ID");
                return false;
            }
            return certificateDAO.deleteCertificate(certificateId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}