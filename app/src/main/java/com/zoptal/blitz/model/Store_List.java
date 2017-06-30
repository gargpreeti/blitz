package com.zoptal.blitz.model;

/**
 * Created by zotal.102 on 28/03/17.
 */
public class Store_List {


    private String id;
    private String store_name;
    private String store_address;
    private String store_phone;
    private String store_hours_from;
    private String store_hours_to;
    private String latitude;
    private String longitude;
    private boolean status;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }


    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_phone() {
        return store_phone;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public String getStore_hours_from() {
        return store_hours_from;
    }

    public void setStore_hours_from(String store_hours_from) {
        this.store_hours_from = store_hours_from;
    }


    public String getStore_hours_to() {
        return store_hours_to;
    }

    public void setStore_hours_to(String store_hours_to) {
        this.store_hours_to = store_hours_to;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
