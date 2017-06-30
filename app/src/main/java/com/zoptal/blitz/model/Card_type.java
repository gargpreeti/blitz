package com.zoptal.blitz.model;

/**
 * Created by zotal.102 on 28/03/17.
 */
public class Card_type {


    private String card_name;
    private Boolean status;

    public Card_type(String card_name, Boolean status) {
        // TODO Auto-generated constructor stub
        this.card_name=card_name;
        this.status=status;
    }


    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



}
