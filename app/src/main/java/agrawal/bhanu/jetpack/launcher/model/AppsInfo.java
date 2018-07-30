package agrawal.bhanu.jetpack.launcher.model;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.data.entities.App;

public class AppsInfo {

    private ArrayList<App> apps;
    private App messagingApp;
    private App internetApp;
    private App googleApp;
    private App callApp;
    private App contactsApps;
    private ArrayList<App> defaultApps;

    public ArrayList<App> getDefaultApps() {
        return defaultApps;
    }

    public void setDefaultApps(ArrayList<App> defaultApps) {
        this.defaultApps = defaultApps;
    }

    public App getMessagingApp() {
        return messagingApp;
    }

    public void setMessagingApp(App messagingApp) {
        this.messagingApp = messagingApp;
    }

    public App getInternetApp() {
        return internetApp;
    }

    public void setInternetApp(App internetApp) {
        this.internetApp = internetApp;
    }

    public App getGoogleApp() {
        return googleApp;
    }

    public void setGoogleApp(App googleApp) {
        this.googleApp = googleApp;
    }

    public App getCallApp() {
        return callApp;
    }

    public void setCallApp(App callApp) {
        this.callApp = callApp;
    }

    public App getContactsApps() {
        return contactsApps;
    }

    public void setContactsApps(App contactsApps) {
        this.contactsApps = contactsApps;
    }

    public AppsInfo(ArrayList<App> apps, ArrayList<App> defaultApps) {
        this.apps = apps;
        this.defaultApps = defaultApps;
    }

    public AppsInfo() {
    }


    public ArrayList<App> getApps() {
        return apps;
    }

    public void setApps(ArrayList<App> apps) {
        this.apps = apps;
    }
}
