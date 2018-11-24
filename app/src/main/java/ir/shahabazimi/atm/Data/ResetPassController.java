package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.JsonResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ResetPassController {

    private DataApi.resetPassListener resetPassListener;

    public ResetPassController(DataApi.resetPassListener resetPassListener) {
        this.resetPassListener = resetPassListener;
    }

    public void start(String email){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<JsonResponseModel> call = dataApi.resetPass(email);
        call.enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                if(response.isSuccessful())
                    resetPassListener.getMessage(true,response.body());
                else
                    resetPassListener.getMessage(false,response.body());

            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                resetPassListener.getMessage(false, new JsonResponseModel());
            }
        });
    }


}
