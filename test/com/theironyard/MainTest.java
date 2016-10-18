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

}