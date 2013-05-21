package com.imiFirewall.util;

import android.app.ActivityManager;
import android.util.Log;

public class imiProcess{

   public static void killByLevel7(ActivityManager am, String packageName)
   {
         Log.d("kill process","kill process by API Level7");
         am.restartPackage(packageName);        
   }
}