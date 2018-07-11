package agrawal.bhanu.jetpack.launcher.model;

import java.util.ArrayList;

public class AppsInfo {

    private int row_count;
    private int column_count;
    private int apps_per_page;
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

    public AppsInfo(int row_count, int column_count, int apps_per_page, ArrayList<AppDTO> apps, ArrayList<AppDTO> defaultApps) {
        this.row_count = row_count;
        this.column_count = column_count;
        this.apps_per_page = apps_per_page;
        this.apps = apps;
        this.defaultApps = defaultApps;
    }


    public AppsInfo() {
        this.row_count = row_count;
        this.column_count = column_count;
        this.apps_per_page = apps_per_page;
        this.apps = apps;
    }

    public int getRow_count() {
        return row_count;
    }

    public void setRow_count(int row_count) {
        this.row_count = row_count;
    }

    public int getColumn_count() {
        return column_count;
    }

    public void setColumn_count(int column_count) {
        this.column_count = column_count;
    }

    public int getApps_per_page() {
        return apps_per_page;
    }

    public void setApps_per_page(int apps_per_page) {
        this.apps_per_page = apps_per_page;
    }

    public ArrayList<AppDTO> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AppDTO> apps) {
        this.apps = apps;
    }
}
