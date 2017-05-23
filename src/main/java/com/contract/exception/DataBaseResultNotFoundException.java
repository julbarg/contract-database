package com.contract.exception;

import static com.contract.constant.ExceptionsConstant.*;

/**
 * Created by anggomez1 on 5/19/17.
 */
public class DataBaseResultNotFoundException extends Exception {

    public DataBaseResultNotFoundException(){
        super(DATABASE_RESULT_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
