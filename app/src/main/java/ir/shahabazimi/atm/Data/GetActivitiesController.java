package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.ActivitiesArrayModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GetActivitiesController {

    private DataApi.getActivitiesListener getActivitiesListener;

    public GetActivitiesController(DataApi.getActivitiesListener getActivitiesListener) {
        this.getActivitiesListener = getActivitiesListener;
    }

    public void start(){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<ActivitiesArrayModel> call = dataApi.getActivities();
        call.enqueue(new Callback<ActivitiesArrayModel>() {
            @Override
            public void onResponse(Call<ActivitiesArrayModel> call, Response<ActivitiesArrayModel> response) {

                if(response.isSuccessful())
                    getActivitiesListener.getMessage(true,response.body());
                else
                    getActivitiesListener.getMessage(false,response.body());

            }

            @Override
            public void onFailure(Call<ActivitiesArrayModel> call, Throwable t) {

                getActivitiesListener.getMessage(false, new ActivitiesArrayModel());
            }
        });
    }


}
