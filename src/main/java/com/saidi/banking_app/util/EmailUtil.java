package com.saidi.banking_app.util;

public class EmailUtil {

    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + "\n\n Please click the link below to verify your account. \n\n" + verificationUrl(host,token);
    }

    public static String verificationUrl(String host, String token) {
        return host + "/api/v1/auth?token=" + token;
    }
}
