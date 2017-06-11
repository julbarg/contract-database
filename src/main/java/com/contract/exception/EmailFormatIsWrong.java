package com.contract.exception;
import static com.contract.constant.ExceptionsConstant.*;

/**
 * Created by anggomez1 on 6/5/17.
 */
public class EmailFormatIsWrong  extends Exception{

    public EmailFormatIsWrong(){
        super(EMAIL_FORMAT_IS_WRONG);
    }
}
