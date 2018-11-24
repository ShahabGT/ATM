package ir.shahabazimi.atm.Classes;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MySharedPreference {

    private static MySharedPreference instanse=null;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    public static MySharedPreference getInstance(Context context){
            if(instanse==null){
                instanse =  new MySharedPreference(context);
            }

            return instanse;

    }


    private MySharedPreference(Context context){

        sp = context.getSharedPreferences("ATMPreference",Context.MODE_PRIVATE);
        editor = sp.edit();

    }

    public void clear(){
        editor.clear().apply();
    }

    public void setLanguage(String language){
        editor.putString("language",language).apply();

    }

    public String getLanguage(){

        return sp.getString("language","en");
    }

    public void setIsFirst(boolean isFirst){
        editor.putBoolean("isfirst",isFirst).apply();
    }

    public boolean getIsFirst(){

        return sp.getBoolean("isfirst",true);
    }

    public void setUsername(String username){
        editor.putString("username",username).apply();

    }

    public String getUsername(){

        return sp.getString("username","");
    }
    public void setToken(String Token){
        editor.putString("token",Token).apply();

    }

    public String getToken(){

        return sp.getString("token","");
    }
    public void setPassword(String password){
        editor.putString("password",password).apply();

    }

    public String getPassword(){

        return sp.getString("password","");
    }
    public void setEmail(String email){
        editor.putString("email",email).apply();

    }

    public String getEmail(){

        return sp.getString("email","");
    }
    public void setName(String name){
        editor.putString("name",name).apply();
    }
    public String getName(){
        return sp.getString("name","");
    }
    public void setSex(String sex){
        editor.putString("sex",sex).apply();
    }
    public String getSex(){
        return sp.getString("sex","");
    }

    public void setActiveActivityName(String name){
        editor.putString("activeactivityname",name).apply();
    }
    public void setActiveActivityNameEn(String name){
        editor.putString("activeactivitynameEn",name).apply();
    }

    public String getActiveActivityName(){
        return  sp.getString("activeactivityname","");
    }
    public String getActiveActivityNameEn(){
        return  sp.getString("activeactivitynameEn","");
    }
    public void setActiveActivityPic(String pic){
        editor.putString("activeactivitypic",pic).apply();
    }

    public String getActiveActivityPic(){
        return  sp.getString("activeactivitypic","");
    }

    public void setHaveActiveActivity(boolean HaveActiveActivity){
        editor.putBoolean("haveactiveactivity",HaveActiveActivity).apply();
    }

    public boolean getHaveActiveActivity(){

        return sp.getBoolean("haveactiveactivity",false);
    }
    public void setrecivedHistory(boolean recivedHistory){
        editor.putBoolean("recivedhistory",recivedHistory).apply();
    }

    public boolean getrecivedHistory(){

        return sp.getBoolean("recivedhistory",false);
    }

    public void stopTimer(){
        editor.putLong("timer",0).commit();
    }

    public void setStartTime(long value){

        editor.putLong("starttime",value).commit();
        setStartDate();

    }
    public long getStartTime(){

        return sp.getLong("starttime",0);

    }

    private void setStartDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String formattedDate = df.format(c);
        editor.putString("startdate",formattedDate).apply();
    }

    public String getStartDate(){


        return sp.getString("startdate","");
    }

}
