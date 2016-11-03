package com.gamedesign.seeker;

import android.media.Image;

/**
 * Created by Zixiao on 10/27/2016.
 */

public class Clue {

    private int id;
    private String hint;
    private String image_path;
    private String spot_name;
    private int spot_order;
    private String spot_addr;
    private String spot_latitude;
    private String spot_longitude;

    // Constructor

    public Clue() {
        this.id = -1;
        this.hint = "No hint";
        this.image_path = "";
        this.spot_name = "";
        this.spot_order = -1;
        this.spot_addr = "";
        this.spot_latitude = "";
        this.spot_longitude = "";
    }

    public Clue(String hint, String image_path, String spot_name, int spot_order, String spot_addr, String spot_latitude, String spot_longitude) {
        this.hint = hint;
        this.image_path = image_path;
        this.spot_name = spot_name;
        this.spot_order = spot_order;
        this.spot_addr = spot_addr;
        this.spot_latitude = spot_latitude;
        this.spot_longitude = spot_longitude;
    }

    public Clue(int id, String hint, String image_path, String spot_name, int spot_order, String spot_addr, String spot_latitude, String spot_longitude) {
        this.id = id;
        this.hint = hint;
        this.image_path = image_path;
        this.spot_name = spot_name;
        this.spot_order = spot_order;
        this.spot_addr = spot_addr;
        this.spot_latitude = spot_latitude;
        this.spot_longitude = spot_longitude;
    }

    // Setter, getter

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getSpot_name() {
        return spot_name;
    }

    public void setSpot_name(String spot_name) {
        this.spot_name = spot_name;
    }

    public int getSpot_order() {
        return spot_order;
    }

    public void setSpot_order(int spot_order) {
        this.spot_order = spot_order;
    }

    public String getSpot_addr() {
        return spot_addr;
    }

    public void setSpot_addr(String spot_addr) {
        this.spot_addr = spot_addr;
    }

    public String getSpot_latitude() {
        return spot_latitude;
    }

    public void setSpot_latitude(String spot_latitude) {
        this.spot_latitude = spot_latitude;
    }

    public String getSpot_longitude() {
        return spot_longitude;
    }

    public void setSpot_longitude(String spot_longitude) {
        this.spot_longitude = spot_longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
