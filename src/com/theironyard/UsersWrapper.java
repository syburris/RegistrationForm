package com.theironyard;

import java.util.ArrayList;

/**
 * Created by stevenburris on 10/17/16.
 */
public class UsersWrapper {
    ArrayList<User> users = new ArrayList<>();

    public UsersWrapper() {
    }

    public UsersWrapper(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
