package com.example.jaikh.trubian;

/**
 * Created by girish on 11/6/2016.
 */

public class Feed {

    private String title;
    private String branch;
    private String desc;
    private String image;

    public Feed(){


    }

    public Feed(String title, String branch, String desc, String image) {
        this.title = title;
        this.branch = branch;
        this.desc = desc;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
