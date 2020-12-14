package edu.courseproject.client.generator;

public class GeneratorPassword {
    private static final String DEFAULT_PASSWORD = "SK45lad";
    private static int counter;

    private GeneratorPassword() {
    }

    public static String generatePassword() {
        ++counter;
        String newPas = DEFAULT_PASSWORD + counter;
        return newPas;
    }
}
