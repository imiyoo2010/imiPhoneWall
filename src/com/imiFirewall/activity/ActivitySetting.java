package com.imiFirewall.activity;


import java.util.Date;
import java.util.HashMap;

import com.imiFirewall.R;
import com.imiFirewall.imiSql;
import com.imiFirewall.R.string;
import com.imiFirewall.R.xml;
import com.imiFirewall.util.imiUtil;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivitySetting extends PreferenceActivity implements OnPreferenceChangeListener,OnPreferenceClickListener{
	
	CheckBoxPreference warnCheckPref;
	EditTextPreference mobile_warn;
	EditTextPreference wifi_warn;
	Preference reset_data;
		
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
		
		
	    super.onCreate(savedInstanceState);
	    // Load the preferences from an XML resource
	    addPreferencesFromResource(R.xml.setting); 
	    
	    
	    mobile_warn =  (EditTextPreference)findPreference("mobile_warn");
		wifi_warn   =  (EditTextPreference)findPreference("wifi_warn");
	    warnCheckPref= (CheckBoxPreference)findPreference("warn");
	    reset_data        =  (Preference)findPreference("reset_data");
	    
	    warnCheckPref.setOnPreferenceChangeListener(this);
	    warnCheckPref.setOnPreferenceClickListener(this);
	    
	    reset_data.setOnPreferenceChangeListener(this);
	    reset_data.setOnPreferenceClickListener(this);
	    
	   
	    	    
	  }
	
	public void reset_data() throws Exception{
		
		//reset t_base
		 imiSql mydb = new imiSql(this);
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
		
		 HashMap hashmapwm = new HashMap();
	     hashmapwm.put("_value", "0");
	     String s8 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
	     hashmapwm.put("_date", s8);
	     String as4[][] = new String[1][4];
	     String[] s9 = new String[4];
	     s9[0] = "_key";
	     s9[1] = "=";
	     s9[2] = "warn_mobile";	
	     as4[0]=s9;
	     try {
			mydb.update("t_base", hashmaprm, as4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 HashMap hashmapww = new HashMap();
	     hashmapww.put("_value", "0");
	     String s10 = imiUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
	     hashmaprm.put("_date", s10);
	     String as5[][] = new String[1][4];
	     String[] s11 = new String[4];
	     s11[0] = "_key";
	     s11[1] = "=";
	     s11[2] = "warn_wifi";	
	     as5[0]=s11;
	     try {
			mydb.update("t_base", hashmaprm, as5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
      }
		 mydb.reset_table("t_data");
	}
	
	 @Override  
	 public boolean onPreferenceChange(Preference preference, Object newValue) { 
	        // TODO Auto-generated method stub  
	     
	        //判断是哪个Preference改变了  
			    		 
		    return true;
	 }
	 
	 
	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub		 
		 if(preference.getKey().equals("reset_data"))
		    {
		    	try {
					reset_data();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Toast.makeText(ActivitySetting.this, R.string.toast_resetdata_text, Toast.LENGTH_LONG).show();
				
		    	return true;
		    	
		    }	
		
		if(warnCheckPref.isChecked())
	    {
	    	mobile_warn.setEnabled(true);
	    	wifi_warn.setEnabled(true);
	    	return true;
	    
	    }
	    
	    if(!warnCheckPref.isChecked())
	    {
	    
	    	mobile_warn.setEnabled(false);
	    	wifi_warn.setEnabled(false);
	    	return true;
	    } 
	   	    
		   return true;
	}
   
}