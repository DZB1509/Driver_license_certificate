/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;

import model.dao.CourseDAO;
import model.entity.Course;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class CourseService {
    private CourseDAO courseDAO;

    public CourseService() throws Exception {
        this.courseDAO = new CourseDAO();
    }

    // Lấy tất cả các khóa học với phân trang
    public List<Course> getAllCourses(int page, int pageSize) {
        try {
            List<Course> courses = courseDAO.getAllCourses();
            if (courses == null || courses.isEmpty()) {
                System.out.println("No courses found");
                return new ArrayList<>();
            }
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, courses.size());
            if (start >= courses.size()) {
                return new ArrayList<>();
            }
            return courses.subList(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Lấy thông tin khóa học theo ID
    public Course getCourseById(int courseId) {
        try {
            if (courseId <= 0) {
                System.out.println("Invalid course ID: " + courseId);
                return null;
            }
            return courseDAO.getCourseById(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Lấy thông tin khóa học theo teacherID
    public List<Course> getCoursesByTeacherID(int teacherID) {
        try {
            return courseDAO.getCoursesByTeacherID(teacherID);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Lấy danh sách khóa học mà user đã đăng ký (Approved)
    public List<Course> getCoursesByUserId(int userId) {
        try {
            if (userId <= 0) {
                System.out.println("Invalid user ID: " + userId);
                return new ArrayList<>();
            }
            List<Course> allCourses = courseDAO.getAllCourses();
            List<Course> userCourses = new ArrayList<>();
            for (Course course : allCourses) {
                if (courseDAO.isUserEnrolledInCourse(userId, course.getCourseID()) && 
                    ("Active".equals(course.getStatus()) || "Completed".equals(course.getStatus()))) {
                    userCourses.add(course);
                }
            }
            return userCourses;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Tạo mới khóa học
    public boolean createCourse(Course course) {
        try {
            if (course == null) {
                System.out.println("Course cannot be null");
                return false;
            }
            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                System.out.println("Course name cannot be empty");
                return false;
            }
            if (course.getStartDate() == null || course.getEndDate() == null) {
                System.out.println("Start date and end date cannot be null");
                return false;
            }
            if (course.getStartDate().after(course.getEndDate())) {
                System.out.println("Start date must be before end date");
                return false;
            }
            if (course.getTeacherID() <= 0) {
                System.out.println("Invalid teacher ID: " + course.getTeacherID());
                return false;
            }
            if (!isValidStatus(course.getStatus())) {
                System.out.println("Invalid status: " + course.getStatus());
                return false;
            }
            return courseDAO.createCourse(course);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin khóa học
    public boolean updateCourse(Course course) {
        try {
            if (course == null) {
                System.out.println("Course cannot be null");
                return false;
            }
            if (course.getCourseID() <= 0) {
                System.out.println("Invalid course ID: " + course.getCourseID());
                return false;
            }
            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                System.out.println("Course name cannot be empty");
                return false;
            }
            if (course.getStartDate() == null || course.getEndDate() == null) {
                System.out.println("Start date and end date cannot be null");
                return false;
            }
            if (course.getStartDate().after(course.getEndDate())) {
                System.out.println("Start date must be before end date");
                return false;
            }
            if (course.getTeacherID() <= 0) {
                System.out.println("Invalid teacher ID: " + course.getTeacherID());
                return false;
            }
            if (!isValidStatus(course.getStatus())) {
                System.out.println("Invalid status: " + course.getStatus());
                return false;
            }
            return courseDAO.updateCourse(course);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa khóa học
    public boolean deleteCourse(int courseId) {
        try {
            if (courseId <= 0) {
                System.out.println("Invalid course ID: " + courseId);
                return false;
            }
            return courseDAO.deleteCourse(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra trạng thái hợp lệ
    private boolean isValidStatus(String status) {
        return status != null && ("Active".equals(status) || "Inactive".equals(status) || 
                "Completed".equals(status) || "Cancelled".equals(status));
    }
}  
