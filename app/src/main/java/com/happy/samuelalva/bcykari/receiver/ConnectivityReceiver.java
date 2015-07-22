package com.happy.samuelalva.bcykari.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Utility;

/**
 * Created by Samuel.Alva on 2015/6/17.
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    public static boolean isWIFI;
    public static boolean isConnected;

    @Override
    public void onReceive(Context context, Intent intent) {
        isConnected = readNetworkState(context);
        if (isConnected && !isWIFI)
            Utility.showToast(context, context.getString(R.string.mobile_data));
    }

    public boolean readNetworkState(Context context) {
        if (context == null)
            return false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {

            isWIFI = (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);

            return true;
        } else {
            return false;
        }
    }
}
