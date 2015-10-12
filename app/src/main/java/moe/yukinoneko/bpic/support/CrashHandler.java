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

package moe.yukinoneko.bpic.support;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by SamuelGjk on 2015/9/29.
 * Thanks to Peter Cai
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static String CRASH_DIR = Environment.getExternalStorageDirectory().getPath() + "/BPic/Crash";
    private static String CRASH_LOG = "crash.log";

    private static String ANDROID = Build.VERSION.RELEASE;
    private static String MODEL = Build.MODEL;
    private static String MANUFACTURER = Build.MANUFACTURER;
    private static String VERSION = "Unknown";

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static void addCrashHandler(Context context) {
        Thread.currentThread().setUncaughtExceptionHandler(new CrashHandler(context));
    }

    private CrashHandler(Context context) {
        try {
            mDefaultHandler = Thread.currentThread().getUncaughtExceptionHandler();
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            VERSION = info.versionName + info.versionCode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if ("Your Fault".equals(throwable.getMessage())) {
            mDefaultHandler.uncaughtException(thread, throwable);
            return;
        }

        File f = new File(CRASH_DIR, CRASH_LOG);
        if (!f.exists()) {
            try {
                new File(CRASH_DIR).mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                return;
            }
        }

        PrintWriter p;
        try {
            p = new PrintWriter(f);
        } catch (FileNotFoundException e) {
            return;
        }
        p.write("Android Version: " + ANDROID + "\n");
        p.write("Device Model: " + MODEL + "\n");
        p.write("Device Manufacturer: " + MANUFACTURER + "\n");
        p.write("App Version: " + VERSION + "\n");
        p.write("*********************\n");
        throwable.printStackTrace(p);
        p.close();

        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        }
    }
}
