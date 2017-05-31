package com.contract.entities.custom;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by anggomez1 on 5/31/17.
 */
public class Service {
    private String name;
    private String type;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date dataTime;
    private String provider;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
