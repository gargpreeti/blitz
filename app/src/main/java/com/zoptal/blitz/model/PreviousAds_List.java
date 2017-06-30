package com.zoptal.blitz.model;

/**
 * Created by zotal.102 on 28/03/17.
 */
public class PreviousAds_List {


    private String ad_id;
    private String category;
    private String sales_heading;
    private String sales_description;

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSales_description() {
        return sales_description;
    }

    public void setSales_description(String sales_description) {
        this.sales_description = sales_description;
    }

    public String getSales_heading() {
        return sales_heading;
    }

    public void setSales_heading(String sales_heading) {
        this.sales_heading = sales_heading;
    }
}
