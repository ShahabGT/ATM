package ir.shahabazimi.atm.Models;

import com.google.gson.annotations.SerializedName;

public class JsonResponseModel {


    @SerializedName("message")
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
