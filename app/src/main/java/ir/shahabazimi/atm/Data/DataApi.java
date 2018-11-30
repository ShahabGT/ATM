package ir.shahabazimi.atm.Data;

import ir.shahabazimi.atm.Models.ActivitiesArrayModel;
import ir.shahabazimi.atm.Models.HistoryArrayModel;
import ir.shahabazimi.atm.Models.JsonResponseModel;
import ir.shahabazimi.atm.Models.TablesArrayModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataApi {


    @FormUrlEncoded
    @POST("register.php")
    Call<JsonResponseModel> registerUser(@Field("name") String name,
                                         @Field("username") String username,
                                         @Field("email") String email,
                                         @Field("password") String password,
                                         @Field("token") String token,
                                         @Field("sex") String sex);

    interface registerUserListener{
        void getMessage(boolean successful,JsonResponseModel message);
    }
    //______________________________________________________________________
    @FormUrlEncoded
    @POST("login.php")
    Call<JsonResponseModel> loginUser(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("token") String token);
    interface loginUserListener{
        void getMessage(boolean successful,JsonResponseModel message);
    }
    //______________________________________________________________________

    @GET("getactivities.php")
    Call<ActivitiesArrayModel> getActivities();

    interface getActivitiesListener{
        void getMessage(boolean successful,ActivitiesArrayModel list);
    }
    //______________________________________________________________________
    @FormUrlEncoded
    @POST("getversion.php")
    Call<JsonResponseModel> getVersion(@Field("table") String table);

    interface getVersionListener{
        void getMessage(boolean successful,JsonResponseModel message);
    }
    //______________________________________________________________________
    @FormUrlEncoded
    @POST("newrequest.php")
    Call<JsonResponseModel> resetPass(@Field("email") String email);

    interface resetPassListener{
        void getMessage(boolean successful,JsonResponseModel message);
    }
    //______________________________________________________________________
    @GET("gettables.php")
    Call<TablesArrayModel> getTables();

    interface getTablesListener{
        void getMessage(boolean successful,TablesArrayModel list);
    }
    //______________________________________________________________________
    @FormUrlEncoded
    @POST("setroomdata.php")
    Call<JsonResponseModel> sendRoomData(@Field("username") String username,
                                         @Field("password") String password,
                                         @Field("actname") String actname,
                                         @Field("startdate") String startdate,
                                         @Field("time") String time);

    interface sendRoomDataListener{
        void getMessage(boolean successful,JsonResponseModel message);
    }
    //______________________________________________________________________
    @FormUrlEncoded
    @POST("gethistory.php")
    Call<HistoryArrayModel> getHistory(@Field("username") String username,
                                       @Field("password") String password);

    interface getHistoryListener{
        void getMessage(boolean successful,HistoryArrayModel list);
    }
    //______________________________________________________________________
}
