package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.JsonResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GetVersionController {

    private DataApi.getVersionListener getVersionListener;

    public GetVersionController(DataApi.getVersionListener getVersionListener) {
        this.getVersionListener = getVersionListener;
    }

    public void start(String table){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<JsonResponseModel> call = dataApi.getVersion(table);
        call.enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                if(response.isSuccessful())
                    getVersionListener.getMessage(true,response.body());
                else
                    getVersionListener.getMessage(false,response.body());

            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                getVersionListener.getMessage(false, new JsonResponseModel());
            }
        });
    }


}
