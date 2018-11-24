package ir.shahabazimi.atm.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "activities")
public class Activities {

    @SerializedName("act_id")
    private int id;

    @NonNull
    @PrimaryKey
    @SerializedName("act_name")
    private String name;

    @SerializedName("act_name_fa")
    private String nameFa;

    @SerializedName("act_pic")
    private String pic;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getNameFa() {
        return nameFa;
    }

    public void setNameFa(String nameFa) {
        this.nameFa = nameFa;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
