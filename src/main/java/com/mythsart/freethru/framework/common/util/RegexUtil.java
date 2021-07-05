package com.mythsart.freethru.framework.common.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexUtil {

    public final static String PHONE_REGEX = "0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8}";

    public boolean isPhoneNumberValid(final String phoneNumber) {
        final Pattern pattern = Pattern.compile(PHONE_REGEX);
        final Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public String removeBlankChar(final String string) {
        if (string == null) {
            return null;
        }
        return string.replace(" ", "").equals("") ? null : string.replace(" ", "");
    }

}
