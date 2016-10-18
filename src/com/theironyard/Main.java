package com.theironyard;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                (request, response) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer serializer = new JsonSerializer();
                    UserWrapper wrapper = new UserWrapper(users);
                    return serializer.deep(true).serialize(wrapper);
                }
        );

        Spark.post(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    User user = parser.parse(body);
                    insertUser(conn, user);
                    return null;
                }
        );

        Spark.put(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    User user = parser.parse(body);
                    updateUser(conn,user);
                    return null;
                }
        );
    }

    public static void createTables (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, user.name);
        stmt.setString(2, user.address);
        stmt.setString(3, user.email);
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

    public static void updateUser(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET name = ?, address = ?, email = ? " +
                "WHERE id = ?");
        stmt.setString(1,user.name);
        stmt.setString(2,user.address);
        stmt.setString(3, user.email);
        stmt.setInt(4,user.id);
        stmt.execute();
    }

    public static void deleteUser(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }
}
