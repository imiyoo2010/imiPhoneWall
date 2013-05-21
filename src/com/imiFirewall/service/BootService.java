package com.imiFirewall.service;

import android.app.*;
import android.content.Intent;
import android.database.Cursor;
import android.os.*;
import android.util.Log;
import java.util.HashMap;
import java.util.Date;

import com.imiFirewall.imiSql;
import com.imiFirewall.util.imiUtil;



// Referenced classes of package com.aob.android.mnm:
//            DataBase, InitActivity

public class BootService extends Service
{

    private imiSql mydb;
    private Handler handler;
    private HashMap map;
    private Runnable runnable;
    private String where[][];
    
    final private String TAG="imiFirewall_clean";
    
    public BootService()
    {
        Handler handler1 = new Handler();
        handler = handler1;
        BootRun  bootrun = new BootRun();
        runnable = bootrun;
    }
    @Override
    public void onCreate(){
    	
    	 imiSql database = new imiSql(this);
         mydb = database;
         mydb.open();
        
         String sql ="select * from t_base";
         
         Cursor c_cur=mydb.select(false, "t_base", null,null,null,null,null,null,null);
         
       if(c_cur.getCount()>1)
       {
         
	     HashMap hashmaprm = new HashMap();
	     hashmaprm.put("_value", "0");
	     String s = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
	     hashmaprm.put("_date", s);
	     String as[][] = new String[1][4];
	     String[] s1 = new String[4];
	     s1[0] = "_key";
	     s1[1] = "=";
	     s1[2] = "rx_mobile";	
	     as[0]=s1;
	     try {
			mydb.update("t_base", hashmaprm, as);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		 HashMap hashmaptm = new HashMap();
	     hashmaptm.put("_value", "0");
	     String s2 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
	     hashmaptm.put("_date", s2);
	     String as1[][] = new String[1][4];
	     String[] s3 = new String[4];
	     s3[0] = "_key";
	     s3[1] = "=";
	     s3[2] = "tx_mobile";
	     as1[0]=s3;
	     try {
			mydb.update("t_base", hashmaptm, as1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap hashmaprw = new HashMap();
	    hashmaprw.put("_value", "0");
	     String s4 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
	     hashmaprw.put("_date", s4);
	     String as2[][] = new String[1][4];
	     String[] s5 = new String[4];
	     s5[0] = "_key";
	     s5[1] = "=";
	     s5[2] = "rx_wifi";
	     as2[0]=s5;
	     try {
			mydb.update("t_base", hashmaprw, as2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 HashMap hashmaptw = new HashMap();
	     hashmaptw.put("_value", "0");
	     String s6 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
	     hashmaptw.put("_date", s6);
	     String as3[][] = new String[1][4];
	     String[] s7 = new String[4];
	     s3[0] = "_key";
	     s3[1] = "=";
	     s3[2] = "tx_wifi";
	     as3[0]=s7;
	     try {
			mydb.update("t_base", hashmaptw, as3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
        Handler handler1 = handler;
        Runnable runnable1 = runnable;
        handler1.postDelayed(runnable1, 6000L); //延时销毁服务
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
		super.onDestroy();
	}
	
	private class BootRun implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			   onDestroy();
		}
		
	}

}