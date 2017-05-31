package com.contract.constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by anggomez1 on 5/31/17.
 */
public class RegexConstant {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public RegexConstant() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

}
