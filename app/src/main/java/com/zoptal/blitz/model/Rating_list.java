package com.zoptal.blitz.model;

/**
 * Created by zotal.102 on 28/03/17.
 */
public class Rating_list {


    private String rating;
    private String comment;
    private String rating_by;
    private String profile_pic;


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating_by() {
        return rating_by;
    }

    public void setRating_by(String rating_by) {
        this.rating_by = rating_by;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
