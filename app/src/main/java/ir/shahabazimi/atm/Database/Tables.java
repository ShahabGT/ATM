package ir.shahabazimi.atm.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tables")
public class Tables {

    @NonNull
    @PrimaryKey()
    @SerializedName("tbl_id")
    private int id;
    @SerializedName("tbl_name")
    private String Name;
    @SerializedName("tbl_version")
    private int version;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
