package com.contract.exception;

import com.contract.constant.ExceptionsConstant;

/**
 * Created by anggomez1 on 5/19/17.
 */
public class DataBaseNoRecordsUpdatedException extends Exception {
    private static String msg = ExceptionsConstant.DATABASE_NO_RECORDS_UPDATE_EXCEPTION;

    public DataBaseNoRecordsUpdatedException(){
        super(msg);
    }
}
