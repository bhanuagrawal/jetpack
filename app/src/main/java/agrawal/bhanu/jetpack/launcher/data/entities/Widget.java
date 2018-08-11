package agrawal.bhanu.jetpack.launcher.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys={@ForeignKey(
        entity = WidgetPositon.class,
        parentColumns = "position",
        childColumns = "position",
        onDelete = CASCADE
)}/*, indices = {@Index(value = {"position"},
        unique = true)}*/)
public class Widget {

    public Widget() {
    }

    @PrimaryKey(autoGenerate = true)
    private long widgetId;

    private int position;


    @ColumnInfo(name = "type")
    String type;

    public long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(long widgetId) {
        this.widgetId = widgetId;
    }

    @ColumnInfo(name = "removable")
    private Boolean removable;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRemovable() {
        return removable;
    }

    public void setRemovable(Boolean removable) {
        this.removable = removable;
    }

    public Widget(int position, String type, Boolean removable) {
        this.position = position;
        this.type = type;
        this.removable = removable;
    }
}
