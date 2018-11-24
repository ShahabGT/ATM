package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.ActivitiesArrayModel;
import ir.shahabazimi.atm.Models.HistoryArrayModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GetHistoryController {

    private DataApi.getHistoryListener getHistoryListener;

    public GetHistoryController(DataApi.getHistoryListener getHistoryListener) {
        this.getHistoryListener = getHistoryListener;
    }

    public void start(String username,String password){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<HistoryArrayModel> call = dataApi.getHistory(username,password);
        call.enqueue(new Callback<HistoryArrayModel>() {
            @Override
            public void onResponse(Call<HistoryArrayModel> call, Response<HistoryArrayModel> response) {

                if(response.isSuccessful())
                    getHistoryListener.getMessage(true,response.body());
                else
                    getHistoryListener.getMessage(false,new HistoryArrayModel());

            }

            @Override
            public void onFailure(Call<HistoryArrayModel> call, Throwable t) {

                getHistoryListener.getMessage(false, new HistoryArrayModel());
            }
        });
    }


}
