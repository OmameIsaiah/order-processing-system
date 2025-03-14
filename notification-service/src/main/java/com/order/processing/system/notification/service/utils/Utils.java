package com.order.processing.system.notification.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import static com.order.processing.system.notification.service.utils.UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter;


public class Utils {
    public static String OTP_EMAIL_SUBJECT = "Email Verification";
    public static String OTP_EMAIL_TEMPLATE = "OTPNotification.ftl";
    public static String EMAIL_TEMPLATES_DIR = "/templates/notification";
    public static final String TOPIC_NOTIFICATION = "email-notification";
    public static final String EMAIL_SIGNUP_OTP = "signup-otp";

    public static Gson getGson() {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, getUnixEpochDateTypeAdapter())
                .create();
        return gson;
    }
}
