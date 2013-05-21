package com.imiFirewall.service;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.*;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.HashMap;
import java.util.Date;

import com.imiFirewall.R;
import com.imiFirewall.imiNetdevice;
import com.imiFirewall.imiSql;
import com.imiFirewall.R.drawable;
import com.imiFirewall.R.string;
import com.imiFirewall.activity.ActivityInit;
import com.imiFirewall.util.imiUtil;



// Referenced classes of package com.aob.android.mnm:
//            DataBase, InitActivity

public class NetService extends Service
{

    private imiSql db;
    private Handler handler;
    private Runnable runnable;
      
    final private String TAG="imiFirewall_count";
    
    public NetService()
    {
    	
        Handler handler1 = new Handler();
        handler = handler1;
        StaticRun  staticrun = new StaticRun();
        runnable = staticrun;
    }
    @Override
    public void onCreate(){
    	
    	imiSql database = new imiSql(this);
        db = database;
        db.open();
    	
    	                          
        Handler handler1 = handler;
        Runnable runnable1 = runnable;
        handler1.postDelayed(runnable1, 5000L);
        super.onCreate();
    }
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onDestroy()
	{
		Handler handler1=handler;
		Runnable runnable1=runnable;
		handler1.removeCallbacks(runnable1);
		db.close();
		super.onDestroy();
	}
	

    private void notification(double d, int i)  //通知流量值到前台
    {
        NotificationManager notificationmanager = (NotificationManager)getSystemService("notification");
        Intent intent = new Intent(this,ActivityInit.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putInt("tab_type", i);
        intent.putExtras(bundle);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification1 = new Notification();
        notification1.icon = R.drawable.icon;
        notification1.defaults = Notification.DEFAULT_SOUND;
        int j = 16;
        notification1.flags = j;
        String s;
        String s1;
        String s2;
        if(i == 0)
            j = R.string.notification_mobile;
        else
            j = R.string.notification_wifi;
        s = getString(j);
        notification1.tickerText = s;            //2G流量 和 wifi流量的提示
        s1 = String.valueOf(getString(R.string.notification_text));
        s2 = (new StringBuilder(s1)).append(d).append(" ").append("MB").toString();
        notification1.setLatestEventInfo(this, s, s2, pendingintent);//标题，内容
        notificationmanager.notify(j, notification1);
        
        notificationmanager.cancel(j);
    }
    	
	private class StaticRun implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			class StaticTread extends Thread{			
				public void run(){
					Date date;
					date=new Date();

					String s=db.getBase("device_mobile")[0];
					long l0 =imiNetdevice.rx_bytes(s);
					long l1 =imiNetdevice.tx_bytes(s);
					
			        String s1=db.getBase("device_wifi")[0];
			        long   l2=imiNetdevice.rx_bytes(s1);
			        long   l3=imiNetdevice.tx_bytes(s1);

			        long l4 = Long.parseLong(db.getBase("rx_mobile")[0]);			
					long l5 = Long.parseLong(db.getBase("tx_mobile")[0]);			
					long l6 = Long.parseLong(db.getBase("rx_wifi")[0]);		
					long l7 = Long.parseLong(db.getBase("tx_wifi")[0]);
								
				    //double d = Double.parseDouble(db.getBase("warn_mobile")[0]);			
					//double d1 = Double.parseDouble(db.getBase("warn_wifi")[0]);	
					long l8;
					if(l0>l4){
						l8=l0-l4;                   
					}else{
						l8=l0;
					}
					long l9;
					if(l1>l5){                     
					  l9=l1-l5;                   
					}else{
					  l9=l1;
					}
					long l10;
					if(l2>l6){
						l10=l2-l6;
					}else{
						l10=l2;
					}
					long l11;
					if(l3>l7){
						l11=l3-l7;
					}else{
						l11=l3;
					}
					 boolean flag;
			         flag = false;
			         if(l8 > 0L)
			         {
			             
			             HashMap hashmap = new HashMap();			             
			             
			             Long long1 = Long.valueOf(l0);
			             
			             hashmap.put("_value", long1);
			             
			             String s2 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
			             hashmap.put("_date", s2);
			            
			             String as1[][] = new String[1][4];
			             String s3[] = new String[4];
			             s3[0] = "_key";
			             s3[1] = "=";
			             s3[2] = "rx_mobile";
			             s3[3] = "";
			             as1[0] = s3;
			          
			    
			        
						try {
							db.update("t_base", hashmap, as1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
			             flag = true;
			         }
			         if(l9 > 0L)
			         {
			             
			             HashMap hashmap1 = new HashMap();
			        			             
			             Long long2 = Long.valueOf(l1);
			             hashmap1.put("_value", long2);
			             
			             String s4 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
			             hashmap1.put("_date", s4);
			            
			             
			             String as2[][] = new String[1][4];
			             String s5[] = new String[4];
			             s5[0] = "_key";
			             s5[1] = "=";
			             s5[2] = "tx_mobile";
			             s5[3] = "";
			             as2[0] = s5;
			             
			             try {
							db.update("t_base", hashmap1, as2);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			             flag = true;
			         }
			         if(l10 > 0L)
			         {
			            
			             HashMap hashmap2 = new HashMap();			        
			             
			             Long long3 = Long.valueOf(l2);
			             hashmap2.put("_value", long3);
			             
			             String s6 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
			             hashmap2.put("_date", s6);
			            
			             String as4[][] = new String[1][4];
			             String s7[] = new String[4];
			             s7[0] = "_key";
			             s7[1] = "=";
			             s7[2] = "rx_wifi";	
			             s7[3] = "";
			             as4[0] = s7;
			     		             
			             try {
							db.update("t_base", hashmap2, as4);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			             flag = true;
			         }
			         if(l11 > 0L)
			         {
			             
			             HashMap hashmap3 = new HashMap();			            
			             
			             Long long4 = Long.valueOf(l3);
			             hashmap3.put("_value", long4);
			            
			             String s8 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
			             hashmap3.put("_date", s8);
			             			           
			             String as6[][] = new String[1][4];
			             String s9[] = new String[4];
			             s9[0] = "_key";
			             s9[1] = "=";
			             s9[2] = "tx_wifi";
			             s9[3] = "";
			             as6[0] = s9;
			          
			             try {
							db.update("t_base", hashmap3, as6);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			             flag = true;
			         }
			         if(flag==true){
			        	 String s11;
			        	 long al[]=null;
			        	 Date date1=date;
			        	 String s10="yyMMddHH";
			        	 s11=imiUtil.date2String(date1, s10);
			        	 String s12=s11;
			           try {
								al= db.getBytes(s12);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
			        	 if(al==null){ /*小时记录*/
			        		 
			                 HashMap hashmap10 = new HashMap();			               
			                 
			                 
			                 String s13 = "_id";
			                 String s14 = s11;
			                 hashmap10.put(s13, s14);
			                 
			                 Long long5 = Long.valueOf(l8);
			                 hashmap10.put("_rx_mobile", long5);
			                 
			                 Long long6 = Long.valueOf(l9);
			                 hashmap10.put("_tx_mobile", long6);
			                 
			                 Long long7 = Long.valueOf(l10);
			                 hashmap10.put("_rx_wifi", long7);
			                 
			                 Long long8 = Long.valueOf(l11);
			                 hashmap10.put("_tx_wifi", long8);
			                 
			                 Date date2 = date;
			                 String s15 = "yyMMdd";
			                 String s16 = imiUtil.date2String(date2, s15);
			                 hashmap10.put("_date", s16);
			                 
			                 Date date3 = date;
			                 String s17 = "yy";
			                 String s18 = imiUtil.date2String(date3, s17);
			                 hashmap10.put("_year", s18);
			                 
			                 Date date4 = date;
			                 String s19 = "MM";
			                 String s20 = imiUtil.date2String(date4, s19);
			                 hashmap10.put("_month", s20);
			                 
			                 Date date5 = date;
			                 String s21 = "dd";
			                 String s22 = imiUtil.date2String(date5, s21);
			                 hashmap10.put("_day", s22);
			                 
			                 Date date6 = date;
			                 String s23 = "HH";
			                 String s24 = imiUtil.date2String(date6, s23);
			                 hashmap10.put("_hour", s24);
			                 
			                 Integer integer = Integer.valueOf(date.getDay());
			                 hashmap10.put("_week", integer);
			                 
							 db.insert("t_data", hashmap10);
			        	 }else
			        	 { 
			        		 
			                 HashMap hashmap11 = new HashMap();
			               		               
			                 Long long9 = Long.valueOf(al[0] + l8);
			                 hashmap11.put("_rx_mobile", long9);
			                 
			                 Long long10 = Long.valueOf(al[1] + l9);
			                 hashmap11.put("_tx_mobile", long10);
			                 
			                 Long long11 = Long.valueOf(al[2] + l10);
			                 hashmap11.put("_rx_wifi", long11);
			                 
			                 Long long12 = Long.valueOf(al[3] + l11);
			                 hashmap11.put("_tx_wifi", long12);
			                 			               
			                 String as14[][] = new String[1][4];
			                 String s66[] = new String[4];
			                 s66[0] = "_id";
			                 s66[1] = "=";
			                 s66[2] = s11;
			                 s66[3] = "";
			                 as14[0] = s66;
			                
			               			                
			                 try {
								db.update("t_data", hashmap11, as14);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			             }
				 }else{/*外层Flag_IF_END*/
				 
					 //处理数据并通知前台
					 StringBuilder stringbuilder=new StringBuilder("mobile_device-rx|tx:");
					 long l12=l0;
					 StringBuilder stringbuilder1 =stringbuilder.append(l12).append("|");
					 long l13=l1;
					 String s25=stringbuilder1.append(l13).toString();
					 Log.d(TAG,s25);					 
					 StringBuilder stringbuilder2 =new StringBuilder("mobile_last-tx|tx");
					 long l14=l4;
					 StringBuilder stringbuilder3 = stringbuilder2.append(l14).append("|");
	                 long l15 = l5;
	                 String s26 = stringbuilder3.append(l15).toString();
	                 Log.d(TAG, s26);
	                 StringBuilder stringbuilder4 = new StringBuilder("mobile_result-rx|tx:");
	                 long l16 = l8;
	                 StringBuilder stringbuilder5 = stringbuilder4.append(l16).append("|");
	                 long l17 = l9;
	                 String s27 = stringbuilder5.append(l17).toString();
	                 Log.d(TAG, s27);
	                 StringBuilder stringbuilder6 = new StringBuilder("wifi_device-rx|tx:");
	                 long l18 = l2;
	                 StringBuilder stringbuilder7 = stringbuilder6.append(l18).append("|");
	                 long l19 = l3;
	                 String s28 = stringbuilder7.append(l19).toString();
	                 Log.d(TAG, s28);
	                 StringBuilder stringbuilder8 = new StringBuilder("wifi_last-rx|tx:");
	                 long l20 = l6;
	                 StringBuilder stringbuilder9 = stringbuilder8.append(l20).append("|");
	                 long l21 = l7;
	                 String s29 = stringbuilder9.append(l21).toString();
	                 Log.d(TAG, s29);
	                 StringBuilder stringbuilder10 = new StringBuilder("wifi_result-rx|tx:");
	                 long l22 = l10;
	                 StringBuilder stringbuilder11 = stringbuilder10.append(l22).append("|");
	                 long l23 = l11;
	                 String s30 = stringbuilder11.append(l23).toString();
	                 Log.d(TAG, s30);
	                //流量预警判断逻辑
				 }
			         
			         final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NetService.this);			   
		      		 Boolean nc_enbale = prefs.getBoolean("warn",false);	      		         
		             if(nc_enbale)
		                {
		                	long wifi_pref=prefs.getLong("wifi_warn",0);
		                	long mobile_pref=prefs.getLong("mobile_warn",0);
		                	
		                   	wifi_pref=wifi_pref * 1024 * 1024;
		                	mobile_pref=mobile_pref * 1024 * 1024;
		                	
		                	long mobiletotal=db.total("_rx_mobile")+db.total("_tx_mobile");	                
		                    long wifitotal = db.total("_rx_wifi")+db.total("_tx_wifi");
		                    
		                    /*String   wifi_sql="select _value from t_base where _key='warn_wifi'";
		                    String mobile_sql="select _value from t_base where _key='warn_mobile'";
		                    
		                    Cursor w_cur=db.read(wifi_sql);
		                    Cursor m_cur=db.read(mobile_sql);
		                    
		                    w_cur.moveToNext();
		                    long   wifiwarn=w_cur.getLong(0);
		                    
		                    m_cur.moveToNext();
		                    long mobilewarn=m_cur.getLong(0);*/
		                          
		                    if(mobile_pref!=0) //不等于默认值
		                    { 	
		                      if(mobiletotal>=mobile_pref)
		                      {
		                        notification(mobiletotal,0);  //通知Mobile	
		                      }
		                    }
		                    
		                    if(wifi_pref!=0)
		                    {
		                      if(wifitotal>=wifi_pref)
		                      {
		                   	     notification(wifitotal,1);    //通知 Wifi              	       	
		                      }
		                    }
		                	
		                }
		      }//TreadRun
			}//Tread
			(new StaticTread()).start();
			 Handler  handler1 = handler;
			 Runnable runnable1 = runnable;
			 handler1.postDelayed(runnable1, 5000L);			
		}//RunnableRun

  }
}