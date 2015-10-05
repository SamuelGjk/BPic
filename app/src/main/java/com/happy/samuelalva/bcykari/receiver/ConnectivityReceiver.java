/*
 * Copyright 2015 SamuelGjk <samuel.alva@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            Utility.showToast(context, R.string.mobile_data);
    }

    public boolean readNetworkState(Context context) {
        if (context == null)
            return false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null || cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected()) {
            return false;
        } else {
            isWIFI = (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
            return true;
        }
    }
}
