package org.com.classmate;


import android.app.Application;

import org.com.classmate.recivers.ConnectivityReceiver;

/**
 * Created by cts on 6/12/17.
 */

public class Classmate extends Application {

    private static Classmate mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Classmate getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
