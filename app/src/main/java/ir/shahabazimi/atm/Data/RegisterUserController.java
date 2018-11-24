package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.JsonResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterUserController {

    private DataApi.registerUserListener registerUserListener;

    public RegisterUserController(DataApi.registerUserListener registerUserListener) {
        this.registerUserListener = registerUserListener;
    }

    public void start(String name, String username, String email, String password, String token, String imei, String sex){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<JsonResponseModel> call = dataApi.registerUser(name, username, email, password, token, imei, sex);
        call.enqueue(new Callback<JsonResponseModel>() {
            @Override
            public void onResponse(Call<JsonResponseModel> call, Response<JsonResponseModel> response) {

                if(response.isSuccessful())
                    registerUserListener.getMessage(true,response.body());
                else
                    registerUserListener.getMessage(false,response.body());

            }

            @Override
            public void onFailure(Call<JsonResponseModel> call, Throwable t) {
                JsonResponseModel jsonResponseModel = new JsonResponseModel();
                jsonResponseModel.setResponse(t.getCause().getMessage());
                registerUserListener.getMessage(false, jsonResponseModel);
            }
        });
    }


}
