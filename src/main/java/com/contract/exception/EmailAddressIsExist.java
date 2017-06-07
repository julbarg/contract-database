package com.contract.exception;
import static com.contract.constant.ExceptionsConstant.*;

/**
 * Created by anggomez1 on 6/6/17.
 */
public class EmailAddressIsExist extends Exception{

    public EmailAddressIsExist(){
        super(EMAIL_ADDRESS_IS_EXIST);
    }
}
