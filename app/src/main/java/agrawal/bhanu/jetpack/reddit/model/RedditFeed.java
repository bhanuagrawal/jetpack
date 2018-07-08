package agrawal.bhanu.jetpack.reddit.model;

import java.io.Serializable;

public class RedditFeed implements Serializable{

    private MetaData data;

    public MetaData getMetaData() {
        return data;
    }

    public void setMetaData(MetaData metaData) {
        this.data = metaData;
    }

    private String kind;

    public String getKind ()
    {
        return kind;
    }

    public void setKind (String kind)
    {
        this.kind = kind;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", kind = "+kind+"]";
    }
}
