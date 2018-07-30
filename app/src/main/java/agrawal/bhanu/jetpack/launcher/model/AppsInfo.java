package agrawal.bhanu.jetpack.launcher.model;

import java.util.ArrayList;

public class AppsInfo {

    private ArrayList<AppDTO> apps;
    private AppDTO messagingApp;
    private AppDTO internetApp;
    private AppDTO googleApp;
    private AppDTO callApp;
    private AppDTO contactsApps;
    private ArrayList<AppDTO> defaultApps;

    public ArrayList<AppDTO> getDefaultApps() {
        return defaultApps;
    }

    public void setDefaultApps(ArrayList<AppDTO> defaultApps) {
        this.defaultApps = defaultApps;
    }

    public AppDTO getMessagingApp() {
        return messagingApp;
    }

    public void setMessagingApp(AppDTO messagingApp) {
        this.messagingApp = messagingApp;
    }

    public AppDTO getInternetApp() {
        return internetApp;
    }

    public void setInternetApp(AppDTO internetApp) {
        this.internetApp = internetApp;
    }

    public AppDTO getGoogleApp() {
        return googleApp;
    }

    public void setGoogleApp(AppDTO googleApp) {
        this.googleApp = googleApp;
    }

    public AppDTO getCallApp() {
        return callApp;
    }

    public void setCallApp(AppDTO callApp) {
        this.callApp = callApp;
    }

    public AppDTO getContactsApps() {
        return contactsApps;
    }

    public void setContactsApps(AppDTO contactsApps) {
        this.contactsApps = contactsApps;
    }

    public AppsInfo(ArrayList<AppDTO> apps, ArrayList<AppDTO> defaultApps) {
        this.apps = apps;
        this.defaultApps = defaultApps;
    }

    public AppsInfo() {
    }


    public ArrayList<AppDTO> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AppDTO> apps) {
        this.apps = apps;
    }
}
