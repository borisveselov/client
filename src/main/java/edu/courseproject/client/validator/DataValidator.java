package edu.courseproject.client.validator;

import java.util.regex.Pattern;

public class DataValidator {
    private final static DataValidator INSTANCE = new DataValidator();
    //todo regex_phone
    private final static String REGEX_PHONE = "^(\\(?\\d{2}\\)?[\\- ]?)?[\\d\\-]{7,9}$";
    private final static String REGEX_NAME = "[а-яА-Я]+";
    private final static String REGEX_EMAIL ="(\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,6})";
    private DataValidator() {
    }

    public static DataValidator getInstance() {
        return INSTANCE;
    }

    public boolean isPhoneValid(String phoneField) {
        return Pattern.matches(REGEX_PHONE, phoneField);
    }

    public boolean isNameValid(String name) {
        return Pattern.matches(REGEX_NAME, name);
    }

    public boolean isEmailValid(String email){
        return Pattern.matches(REGEX_EMAIL,email);
    }
}