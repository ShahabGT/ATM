package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.JsonResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginUserController {

    private DataApi.loginUserListener loginUserListener;

    public LoginUserController(DataApi.loginUserListener loginUserListener) {
        this.loginUserListener = loginUserListener;
    }

    public void start(String username, String password, String token, String imei){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<JsonResponseModel> call = dataApi.loginUser(username, password, token, imei);
        call.enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                if(response.isSuccessful())
                    loginUserListener.getMessage(true,response.body());
                else
                    loginUserListener.getMessage(false,response.body());

            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                JsonResponseModel jsonResponseModel = new JsonResponseModel();
                jsonResponseModel.setResponse(t.getCause().getMessage());
                loginUserListener.getMessage(false, jsonResponseModel);
            }
        });
    }


}
