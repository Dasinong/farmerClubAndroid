package com.dasinong.farmerclub.components.net;

import com.android.volley.VolleyError;

/**
 * Created by liuningning on 15/6/13.
 */
public interface INetRequest {

    public void onTaskSuccess(int requestCode,Object response);
//    public void onTaskExceptionSuccess(int requestCode,T response);
    public void onTaskFailedSuccess(int requestCode,NetError error);

}
