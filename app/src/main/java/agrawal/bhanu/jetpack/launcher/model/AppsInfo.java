package agrawal.bhanu.jetpack.launcher.model;

import java.util.ArrayList;

public class AppsInfo {

    private int row_count;
    private int column_count;
    private int apps_per_page;
    private ArrayList<AppDTO> apps;

    public AppsInfo(int row_count, int column_count, int apps_per_page, ArrayList<AppDTO> apps) {
        this.row_count = row_count;
        this.column_count = column_count;
        this.apps_per_page = apps_per_page;
        this.apps = apps;
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
