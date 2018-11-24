package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.JsonResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SendRoomDataController {

    private DataApi.sendRoomDataListener sendRoomDataListener;

    public SendRoomDataController(DataApi.sendRoomDataListener sendRoomDataListener) {
        this.sendRoomDataListener = sendRoomDataListener;
    }

    public void start(String username,String password, String actname, String startdate, String time){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<JsonResponseModel> call = dataApi.sendRoomData(username, password, actname, startdate, time);
        call.enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                if(response.isSuccessful())
                    sendRoomDataListener.getMessage(true,response.body());
                else
                    sendRoomDataListener.getMessage(false,response.body());

            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                JsonResponseModel jsonResponseModel = new JsonResponseModel();
                jsonResponseModel.setResponse(t.getCause().getMessage());
                sendRoomDataListener.getMessage(false, jsonResponseModel);
            }
        });
    }


}
