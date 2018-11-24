package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.ActivitiesArrayModel;
import ir.shahabazimi.atm.Models.TablesArrayModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GetTablesController {

    private DataApi.getTablesListener getTablesListener;

    public GetTablesController(DataApi.getTablesListener getTablesListener) {
        this.getTablesListener = getTablesListener;
    }

    public void start(){

        Retrofit retrofit = RetrofitClient.getClient();
        DataApi dataApi = retrofit.create(DataApi.class);

        Call<TablesArrayModel> call = dataApi.getTables();
        call.enqueue(new Callback<TablesArrayModel>() {
            @Override
            public void onResponse(Call<TablesArrayModel> call, Response<TablesArrayModel> response) {

                if(response.isSuccessful())
                    getTablesListener.getMessage(true,response.body());
                else
                    getTablesListener.getMessage(false,response.body());

            }

            @Override
            public void onFailure(Call<TablesArrayModel> call, Throwable t) {

                getTablesListener.getMessage(false, new TablesArrayModel());
            }
        });
    }


}
