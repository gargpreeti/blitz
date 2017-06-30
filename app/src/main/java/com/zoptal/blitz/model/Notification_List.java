package com.zoptal.blitz.model;

/**
 * Created by zotal.102 on 28/03/17.
 */
public class Notification_List {


    private String notification_id;
    private String sender_id;
    private String sender_status;
    private String store_name;
    private String message;
    private String type;
    private String ad_id;
    private String date;
    private String status;
    private String tym;
    private String dt;
    private String cmpnyname;

    public String getCmpnyname() {
        return cmpnyname;
    }

    public void setCmpnyname(String cmpnyname) {
        this.cmpnyname = cmpnyname;
    }

    public String getTym() {
        return tym;
    }

    public void setTym(String tym) {
        this.tym = tym;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_status() {
        return sender_status;
    }

    public void setSender_status(String sender_status) {
        this.sender_status = sender_status;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
