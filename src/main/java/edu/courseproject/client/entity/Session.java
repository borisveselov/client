package edu.courseproject.client.entity;

public class Session {
    private static User user;

    private Session() {
    }

    public static User getClient() {
        return user;
    }

    public static void setClient(User user) {
        Session.user = user;
    }
}
