package agrawal.bhanu.jetpack.launcher.model;

import java.io.Serializable;

public class AppContainer extends AppsAndFolder implements Serializable{

    private String containerId;
    public AppContainer(String id) {
        super(AppsAndFolder.APP);
        this.containerId = id;
        this.type = AppsAndFolder.APP;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
