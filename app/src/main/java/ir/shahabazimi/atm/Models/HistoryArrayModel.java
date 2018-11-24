package ir.shahabazimi.atm.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ir.shahabazimi.atm.Database.Activities;
import ir.shahabazimi.atm.Database.History;

public class HistoryArrayModel {

    @SerializedName("result")
    private ArrayList<History> list;

    public HistoryArrayModel(){
    }


    public ArrayList<History> getList() {
        return list;
    }

    public void setList(ArrayList<History> list) {
        this.list = list;
    }
}
