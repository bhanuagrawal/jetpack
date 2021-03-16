package agrawal.bhanu.jetpack.launcher.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;


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
