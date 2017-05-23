package com.contract.exception;

import  static com.contract.constant.ExceptionsConstant.*;

/**
 * Created by anggomez1 on 5/19/17.
 */
public class DataBaseNoRecordsUpdatedException extends Exception {


    public DataBaseNoRecordsUpdatedException(){
        super(DATABASE_NO_RECORDS_UPDATE_EXCEPTION_MESSAGE);
    }
}
