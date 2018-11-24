package ir.shahabazimi.atm.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ir.shahabazimi.atm.Database.Activities;

public class ActivitiesArrayModel {

    @SerializedName("result")
    private ArrayList<Activities> list;

    public ActivitiesArrayModel(){
    }


    public ArrayList<Activities> getList() {
        return list;
    }

    public void setList(ArrayList<Activities> list) {
        this.list = list;
    }
}
