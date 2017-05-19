package com.contract.exception;

import com.contract.constant.ExceptionsConstant;

/**
 * Created by anggomez1 on 5/19/17.
 */
public class DataBaseResultNotFoundException extends Exception {
    private static String msg = ExceptionsConstant.DATABASE_RESULT_NOT_FOUND_EXCEPTION;

    public DataBaseResultNotFoundException(){
        super(msg);
    }
}
