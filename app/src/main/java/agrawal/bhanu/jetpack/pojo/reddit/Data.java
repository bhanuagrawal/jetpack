package agrawal.bhanu.jetpack.pojo.reddit;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import java.io.Serializable;

public class Data implements Serializable{

    private String title;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    private String selftext;
}
