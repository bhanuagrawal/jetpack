package agrawal.bhanu.jetpack.launcher.model;

import java.io.Serializable;

public class AppsAndFolder implements Serializable{
    public static final String APP = "app";
    public static final String FOLDER = "folder";

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppsAndFolder(String type) {
        this.type = type;
    }
}
