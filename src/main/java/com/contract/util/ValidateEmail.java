package com.contract.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.contract.constant.RegexConstant.*;

/**
 * Created by anggomez1 on 5/31/17.
 */
public class ValidateEmail {

    private Pattern pattern;
    private Matcher matcher;

    public boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
