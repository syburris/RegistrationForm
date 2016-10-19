package com.theironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by stevenburris on 10/17/16.
 */
public class MainTest {

    public Connection startConnection () throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        User user = new User(1,"Steven", "alskdjflksjflksdj", "email@email.com");
        Main.insertUser(conn,user);
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(!users.isEmpty());
    }

    @Test
    public void testUpdate() throws SQLException {
        Connection conn = startConnection();
        User user = new User(1, "Steven", "alskdjf", "email@emial.com");
        User newUser = new User(1, "Allan", "alskdjflk", "email@email.com");
        Main.insertUser(conn,user);
        Main.updateUser(conn,newUser);
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(!users.isEmpty());
        assertTrue(users.get(0).username == "Allan");
    }

    @Test
    public void testDelete() throws SQLException {
        Connection conn = startConnection();
        User user = new User(1, "Steven", "alskdjf", "email@emial.com");
        User user2 = new User(2, "Allan", "alskdjflk", "email@email.com");
        Main.insertUser(conn, user);
        Main.insertUser(conn, user2);
        Main.deleteUser(conn, 1);
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(users.get(0).username == "Allan");
    }

}