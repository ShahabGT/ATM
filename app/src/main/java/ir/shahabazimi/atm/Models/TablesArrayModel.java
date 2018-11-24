package ir.shahabazimi.atm.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ir.shahabazimi.atm.Database.Tables;

public class TablesArrayModel {
    @SerializedName("result")
    private ArrayList<Tables> list;

    public TablesArrayModel(){
    }


    public ArrayList<Tables> getList() {
        return list;
    }

    public void setList(ArrayList<Tables> list) {
        this.list = list;
    }
}
