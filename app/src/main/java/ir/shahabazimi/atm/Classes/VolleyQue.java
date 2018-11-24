package ir.shahabazimi.atm.Classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleyQue {

    private static  VolleyQue mInstance;
    private RequestQueue requestQueue;
    private  Context mCtx;

    private VolleyQue(Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }
    private RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized  VolleyQue getmInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance = new VolleyQue(context);
        }
        return mInstance;
    }
    public<T> void addToRequestque (Request<T> request)
    {
        requestQueue.add(request);
    }



}
