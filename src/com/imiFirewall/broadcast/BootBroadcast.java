package com.imiFirewall.broadcast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;
import com.imiFirewall.R;
import com.imiFirewall.imiApi;
import com.imiFirewall.imiSql;
import com.imiFirewall.R.string;
import com.imiFirewall.service.BootService;
import com.imiFirewall.service.NetService;

public class BootBroadcast extends BroadcastReceiver {

	private imiSql myPhoneDbHelper;
	private ITelephony iTelephony;
	static final String BOOT_ACTION="android.intent.action.BOOT_COMPLETED";
	static final String SMS_ACTION="android.provider.Telephony.SMS_RECEIVED";
	static final String CALL_ACTION="android.intent.action.PHONE_STATE";
	static final String OUT_ACTION="android.intent.action.NEW_OUTGOING_CALL";
	final private String TAG = "imiFirewall_Receiver";
	
	@Override
	public void onReceive(Context context, Intent intent){
	 final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				
		if (intent.getAction().equals(BOOT_ACTION))
		{
			 //开机自启动程序
			 Log.d(TAG, "system boot completed");
		        
			 if (prefs.getBoolean("Network",true)==true) //防火墙开关
			 {
					if (!imiApi.applySavedIptablesRules(context, false)) 
					{
						// Error enabling firewall on boot
						Toast.makeText(context, R.string.toast_error_enabling, Toast.LENGTH_SHORT).show();
						imiApi.setEnabled(context, false);
					}
			}
			 //清空流量统计的数据	
		//	Intent intentclean =new Intent(context,BootService.class);
		//    context.startService(intentclean);
	    //    Intent intentnet =new Intent(context,NetService.class);
	    //    context.startService(intentnet);
		}
		
	   
		          			
	}//OnReceiver_END
}
