package com.order.processing.system.account.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.order.processing.system.account.service.enums.UserType;
import org.apache.commons.validator.routines.EmailValidator;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.order.processing.system.account.service.config.UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter;

public class Utils {
    public static String OTP_EMAIL_SUBJECT = "Email Verification";
    public static String OTP_EMAIL_TEMPLATE = "OTPNotification.ftl";
    public static String EMAIL_TEMPLATES_DIR = "/templates/notification";
    private static String OTP_EXPIRE_TIME = "600";
    public static String DATE_CREATED = "date_created";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "2024-06-30 10:30:00";
    public static final String DEFAULT_START_DATE = "2024-06-15 10:30:00";
    public static final String DEFAULT_END_DATE = "2024-10-31 11:59:59";
    public static final String ONE_MINUTE_CRON_JOB = "0 * * * * *";

    public static final String TOPIC_NOTIFICATION = "email-notification";
    public static final String EMAIL_SIGNUP_OTP = "signup-otp";
    public static final String SMS_TOPIC = "sms-topic";
    public static final String TOKEN_PREFIX = "Bearer";

    public static Gson getGson() {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, getUnixEpochDateTypeAdapter())
                .create();
        return gson;
    }

    static EmailValidator validator = EmailValidator.getInstance();

    public static Boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        if (!pat.matcher(email).matches()) {
            return false;
        } else {
            return validator.isValid(email);
        }
    }

    public static Boolean isValidUserType(UserType userType) {
        if (Objects.isNull(userType)) {
            return false;
        }
        for (UserType myEnum : UserType.values()) {
            if (myEnum.label.equals(userType.label)) {
                return true;
            }
        }
        return false;
    }

    public static String generate4DigitOTPAndExpireTime() {
        int min = 9999;
        int max = 1000;
        int randomValue = (int) (Math.random() * (max - min + 1) + min);
        String otp = String.valueOf(randomValue);
        long baseTime = (System.currentTimeMillis() / 1000) + Integer.parseInt(OTP_EXPIRE_TIME);
        String otpLimitTime = "" + baseTime + "";
        return otp + "_" + otpLimitTime;
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                .format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static LocalDateTime convertDateStringToLocalDateTime(String date) {
        if (Objects.isNull(date)) {
            return null;
        }
        try {
            if (date.length() <= 10) {
                date += " 00:00:00";
            }
            return LocalDateTime
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC+1"))
                    .toLocalDateTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
