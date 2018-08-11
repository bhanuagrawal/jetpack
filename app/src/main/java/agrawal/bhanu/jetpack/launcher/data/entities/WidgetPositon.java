package agrawal.bhanu.jetpack.launcher.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity
public class WidgetPositon {

    @PrimaryKey
    @NonNull
    private int position;

    @NonNull
    public int getPosition() {
        return position;
    }

    public void setPosition(@NonNull int position) {
        this.position = position;
    }

    public WidgetPositon(@NonNull int position) {
        this.position = position;
    }
}
