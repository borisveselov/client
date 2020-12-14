package edu.courseproject.client.generator;

public class GeneratorLogin {
    private static final String DEFAULT_LOGIN = "logis11ik";
    private static int counter;

    private GeneratorLogin() {
    }

    public static String generateLogin() {
        ++counter;
        String newLogin = DEFAULT_LOGIN + counter;
        return newLogin;
    }
}
