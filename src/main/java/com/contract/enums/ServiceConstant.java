package com.contract.enums;

/**
 * Created by anggomez1 on 6/9/17.
 */
public enum ServiceConstant {

    OPEN("open"),
    CLOSED("closed"),
    IN_PROGRESS("in progress");

    String status;

    ServiceConstant(String status){this.status = status; }

    public String getStatus() {return status;}
}
