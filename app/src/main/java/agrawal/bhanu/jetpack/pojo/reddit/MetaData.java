package agrawal.bhanu.jetpack.pojo.reddit;

import java.io.Serializable;
import java.util.ArrayList;

public class MetaData implements Serializable {

    private int dist;
    private String before;
    private String after;
    private ArrayList<Post> children;

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public ArrayList<Post> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Post> children) {
        this.children = children;
    }
}
