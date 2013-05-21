package com.imiFirewall.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Date;
import java.util.HashMap;

import com.imiFirewall.R;
import com.imiFirewall.imiNetdevice;
import com.imiFirewall.imiSql;
import com.imiFirewall.R.layout;
import com.imiFirewall.R.string;
import com.imiFirewall.service.NetService;
import com.imiFirewall.util.imiUtil;
  
  
public class ActivityInit extends Activity{
  
    private imiSql db;
    private String device_mobile[];
    private String device_wifi[];
    private Handler initHandler;
    private long init_data_mobile[];
    private long init_data_wifi[];
    private long last_data_mobile[];
    private long last_data_wifi[];   
    public ProgressDialog progressDialog;
    
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.init);
        imiSql database = new imiSql(this);//需要在onCreate函数里面
        db = database;
        db.open();
        String s = getString(R.string.progress_loading);
        progressDialog = ProgressDialog.show(this, "", s);
        (new InitThread()).start();
    }
      
     public void finish()
     {
         init_data_mobile = null;
         init_data_wifi = null;
         last_data_mobile = null;
         last_data_wifi = null;
         device_mobile = null;
         device_wifi = null;
         db.close();
         super.finish();
     }
    public ActivityInit()
    {
        long al0[]= new long[12];
        init_data_mobile = al0;
        long al1[]=new long[12];
        init_data_wifi = al1;
        long al2[]=new long[2];
        last_data_mobile = al2;
        long al3[]=new long[2];
        last_data_wifi = al3;
        String as[] = new String[2];
        device_mobile = as;
        String as1[] = new String[2];
        device_wifi = as1;     
        InitHandler handler1 = new InitHandler();
        initHandler = handler1;
    }
    
   class InitHandler extends Handler{
    	public void handleMessage(Message message){
    		String s ="tab_type";
    		if(message.what==1){
    			int i=-1;                
                int j;          
                try{
                j = getIntent().getExtras().getInt("tab_type");
                }catch(Exception e){
                	
                }
                Intent intent = new Intent();
                intent.setClass(ActivityInit.this,ActivityNetcount.class);
                Bundle bundle = new Bundle();
                
              
                bundle.putLongArray("init_data_mobile", init_data_mobile);                          
                bundle.putLongArray("init_data_wifi", init_data_wifi);
                bundle.putLongArray("last_data_mobile", last_data_mobile);
                bundle.putLongArray("last_data_wifi", last_data_wifi);
                bundle.putStringArray("device_mobile", device_mobile);
                bundle.putStringArray("device_wifi", device_wifi);
                bundle.putInt(s, i);	          
                intent.putExtras(bundle);            
                Intent intent1 = new Intent(ActivityInit.this,NetService.class);
                startService(intent1);
                startActivity(intent);
                finish();
    		}
    	}
    }
   
    private class InitThread extends Thread{
    	
    	public void run(){
    		int mymessage=1; //发送异步消息
    		
             String s7 = imiNetdevice.mobile(Build.DEVICE);
             String s8 = imiNetdevice.wifi(Build.DEVICE);
             String s9;      	
			 s9 =imiUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss");
			
			if(db.getBase("device_mobile")==null)
			{
				
				HashMap hashmap_dm0=new HashMap();				
				hashmap_dm0.put("_key", "device_mobile");
				hashmap_dm0.put("_value",s7);
				hashmap_dm0.put("_date", s9);
             
                db.insert("t_base", hashmap_dm0);
				
			}
			
			if(db.getBase("device_wifi")==null)
			{
				
				HashMap hashmap_wf0=new HashMap();				
				hashmap_wf0.put("_key", "device_wifi");
				hashmap_wf0.put("_value",s8);
				hashmap_wf0.put("_date", s9);
 
                db.insert("t_base", hashmap_wf0);
				
			}
			
            if(db.getBase("rx_mobile") == null)
            {
                
                HashMap hashmap1 = new HashMap();               
                hashmap1.put("_key", "rx_mobile");            
                Long long1 = Long.valueOf(imiNetdevice.rx_bytes(s7));
                hashmap1.put("_value", long1);
                hashmap1.put("_date", s9);
          
                db.insert("t_base", hashmap1);
            }
            if(db.getBase("tx_mobile") == null)
            {
                
                HashMap hashmap2 = new HashMap();          
                hashmap2.put("_key", "tx_mobile");
                Long long2 = Long.valueOf(imiNetdevice.tx_bytes(s7));
                hashmap2.put("_value", long2);
                hashmap2.put("_date", s9);
   
                db.insert("t_base", hashmap2);
            }
            if(db.getBase("rx_wifi") == null)
            {
   
                HashMap hashmap3 = new HashMap();
                hashmap3.put("_key", "rx_wifi");   
                Long long3 = Long.valueOf(imiNetdevice.rx_bytes(s8));
                hashmap3.put("_value", long3);
                hashmap3.put("_date", s9);

                db.insert("t_base", hashmap3);
            }
            if(db.getBase("tx_wifi") == null)
            {
                
                HashMap hashmap4 = new HashMap();
                hashmap4.put("_key", "tx_wifi");
                Long long4 = Long.valueOf(imiNetdevice.tx_bytes(s8));
                hashmap4.put("_value", long4);
                hashmap4.put("_date", s9);
              
               db.insert("t_base", hashmap4);
            }
            if(db.getBase("warn_wifi")==null)
            {
            	HashMap hashmap5 =new HashMap();           	
            	hashmap5.put("_key","warn_wifi");            	            	
            	hashmap5.put("_value", Integer.parseInt("0"));
            	hashmap5.put("_date", s9);
            	
            	db.insert("t_base", hashmap5);
            }
            if(db.getBase("warn_mobile")==null)
            {
            	HashMap hashmap6 =new HashMap();            	
            	hashmap6.put("_key", "warn_mobile");           	
            	hashmap6.put("_value", Integer.parseInt("0"));
            	hashmap6.put("_date", s9);
            	
            	db.insert("t_base", hashmap6);
            	
            }
                        
			  
			try {
				device_mobile = db.getBase("device_mobile");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
          
  
			try {
				device_wifi = db.getBase("device_wifi");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            
            /*首页列表框数据处理--Start*/
        
            long l0 = db.day("_tx_mobile");
            init_data_mobile[0] = l0;
           
            long l1 = db.day("_rx_mobile");
            init_data_mobile[1] = l1;
            
           
            long l2 = db.day("_rx_mobile+_tx_mobile");
            init_data_mobile[2] = l2;
            
      
            long l3 = db.week("_tx_mobile");
            init_data_mobile[3] = l3;
                     
            long l4 = db.week("_rx_mobile");
            init_data_mobile[4] = l4;
            
  
            long l5 = db.week("_rx_mobile+_tx_mobile");
            init_data_mobile[5] = l5;
            

            long l6 = db.month("_tx_mobile");
            init_data_mobile[6] = l6;
            

            long l7 = db.month("_rx_mobile");
            init_data_mobile[7] = l7;
            
        
            long l8 = db.month("_rx_mobile+_tx_mobile");
            init_data_mobile[8] = l8;
            
            
            long l9 = db.total("_tx_mobile");
            init_data_mobile[9] = l9;
            
   
            long l10 = db.total("_rx_mobile");
            init_data_mobile[10] = l10;
            
       
            long l11 = db.total("_rx_mobile+_tx_mobile");
            init_data_mobile[11] = l11;
            
            
      
            long l12 = db.day("_tx_wifi");
            init_data_wifi[0] = l12;
      
            long l13 = db.day("_rx_wifi");
            init_data_wifi[1] = l13;
            
        
            long l14 = db.day("_rx_wifi+_tx_wifi");
            init_data_wifi[2] = l14;
            
     
            long l15 = db.week("_tx_wifi");
            init_data_wifi[3] = l15;
            
 
            long l16 = db.week("_rx_wifi");
            init_data_wifi[4] = l16;
            
     
            long l17 = db.week("_rx_wifi+_tx_wifi");
            init_data_wifi[5] = l17;
            
   
            long l18 = db.month("_tx_wifi");
            init_data_wifi[6] = l18;
            
     
            long l19 = db.month("_rx_wifi");
            init_data_wifi[7] = l19;
            
     
            long l20 = db.month("_rx_wifi+_tx_wifi");
            init_data_wifi[8] = l20;
            
        
            long l21 = db.total("_tx_wifi");
            init_data_wifi[9] = l21;
            
  
            long l22 = db.total("_rx_wifi");
            init_data_wifi[10] = l22;
            
     
            long l23 = db.total("_rx_wifi+_tx_wifi");
            init_data_wifi[11] = l23;
            
            
    
            long l24 = Long.parseLong(db.getBase("rx_mobile")[0]);
            last_data_mobile[0] = l24;
        
            long l25 = Long.parseLong(db.getBase("tx_mobile")[0]);
            last_data_mobile[1] = l25;
            
    
            long l26 = Long.parseLong(db.getBase("rx_wifi")[0]);
            last_data_wifi[0] = l26;
    
            long l27 = Long.parseLong(db.getBase("tx_wifi")[0]);
            last_data_wifi[1] = l27;
            /*首页列表框数据处理--END*/
            
            
            
            Handler handler = initHandler;
            Message message = initHandler.obtainMessage(mymessage);
            handler.sendMessage(message);
            progressDialog.dismiss();
    	}
    }
 }
    