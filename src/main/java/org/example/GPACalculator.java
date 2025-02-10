package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class GPACalculator {
    private static final String URL = "jdbc:postgresql://localhost:5432/StudentDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "your_password";

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Student Name:");
        String name = scanner.nextLine();

        System.out.println("Enter Number of Courses:");
        int numCourses = scanner.nextInt();
        scanner.nextLine();

        double totalPoints = 0;
        int totalCredits = 0;

        for (int i = 0; i < numCourses; i++) {
            System.out.println("Enter Course Name:");
            String course = scanner.nextLine();

            System.out.println("Enter Grade (A, B, C, D, F):");
            String grade = scanner.nextLine().toUpperCase();

            System.out.println("Enter Credit Hours:");
            int credits = scanner.nextInt();
            scanner.nextLine();

            double gradePoints = getGradePoints(grade);
            totalPoints += gradePoints * credits;
            totalCredits += credits;
        }

        double gpa = (totalCredits == 0) ? 0.0 : totalPoints / totalCredits;
        System.out.printf("Calculated GPA: %.2f%n", gpa);

        saveToDatabase(name, totalCredits, totalPoints, gpa);
        scanner.close();
    }

    private double getGradePoints(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C-": return 2.3;
            case "C": return 2.0;
            case "C+": return 1.7;
            case "D-": return 1.3;
            case "D": return 1.0;
            case "D+": return 0.7;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    private void saveToDatabase(String name, int totalCredits, double totalPoints, double gpa) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO students (name, total_credits, total_points, gpa) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, totalCredits);
            stmt.setDouble(3, totalPoints);
            stmt.setDouble(4, gpa);
            stmt.executeUpdate();
            System.out.println("Student GPA saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

