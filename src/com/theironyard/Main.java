package com.theironyard;

import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();
    }

    public static void createTables (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, String name, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("name");
            String address = results.getString("address");
            String email = results.getString("email");
            User user = new User(id,name,address,email);
            users.add(user);
        }
        return users;
    }

    public static void updateUser(Connection conn, int id, String name, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET name = ?, address = ?, email = ? " +
                "WHERE id = ?");
        stmt.setString(1,name);
        stmt.setString(2,address);
        stmt.setString(3, email);
        stmt.setInt(4,id);
        stmt.execute();
    }

    public static void deleteUser(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }
}
