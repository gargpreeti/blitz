package com.zoptal.blitz.model;

/**
 * Created by zotal.102 on 28/03/17.
 */
public class Chart_list {


    private String range;
    private String amount_per_sale;
    private String commission_per_sale;
    private String extra_bonus;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getAmount_per_sale() {
        return amount_per_sale;
    }

    public void setAmount_per_sale(String amount_per_sale) {
        this.amount_per_sale = amount_per_sale;
    }

    public String getCommission_per_sale() {
        return commission_per_sale;
    }

    public void setCommission_per_sale(String commission_per_sale) {
        this.commission_per_sale = commission_per_sale;
    }

    public String getExtra_bonus() {
        return extra_bonus;
    }

    public void setExtra_bonus(String extra_bonus) {
        this.extra_bonus = extra_bonus;
    }
}
