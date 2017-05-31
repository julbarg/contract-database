package com.contract.entities.provider;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by julbarra on 5/31/17.
 */
public class Experience {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
