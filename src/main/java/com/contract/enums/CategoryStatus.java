package com.contract.enums;

/**
 * Created by anggomez1 on 5/11/17.
 */
public enum CategoryStatus {

    ACTIVE("active"),
    INACTIVE("inactive");

    String status;

    CategoryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
