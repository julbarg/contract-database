package com.contract.exception;
import static com.contract.constant.ExceptionsConstant.*;

/**
 * Created by anggomez1 on 6/5/17.
 */
public class EmailAddressFormatIsWrongException extends Exception{

    public EmailAddressFormatIsWrongException(){
        super(EMAIL_ADDRESS_FORMAT_IS_WRONG);
    }
}
