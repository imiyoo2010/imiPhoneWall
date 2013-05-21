package com.imiFirewall.activity;
// /system/bin/sh -c cmd arg0 arg1.....
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.imiFirewall.R;
import com.imiFirewall.imiApi;
import com.imiFirewall.R.id;
import com.imiFirewall.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ActivityShell extends Activity{
	 
	  Button btn_exe;	  
	  TextView tv_output;	 
	  private AutoCompleteTextView actinput; 
	  
	  public static final int WHITE=0xffffffff;
	  public static final int BLACK=0xff000000;
	  
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.ad_command);
	        actinput =(AutoCompleteTextView)findViewById(R.id.command);	  	 
	        final ArrayList<HashMap<String,String>> datalist= new ArrayList<HashMap<String,String>>();
	       
	     	        
	        HashMap<String,String> map0 =new HashMap<String,String>();
	        map0.put("command","cat /proc/version");
	        map0.put("exinfo","查看操作系统版本");
	        map0.put("dataindex","0");
	        datalist.add(map0);
	        	   	        
	        HashMap<String,String> map1 =new HashMap<String,String>();
	        map1.put("command","cat /proc/cpuinfo");
	        map1.put("exinfo","查看手机CPU信息");
	        map1.put("dataindex","1");
	        datalist.add(map1);
	        
	        HashMap<String,String> map2 =new HashMap<String,String>();
	        map2.put("command","cat /proc/meminfo");
	        map2.put("exinfo","查看当前目录文件");
	        map2.put("dataindex","2");
	        datalist.add(map2);
	        
	        HashMap<String,String> map3 =new HashMap<String,String>();
	        map3.put("command","df");
	        map3.put("exinfo","查看手机硬盘信息");
	        map3.put("dataindex","3");
	        datalist.add(map3);
	        
	        HashMap<String,String> map4 =new HashMap<String,String>();
	        map4.put("command","ls");
	        map4.put("exinfo","查看当前目录文件");
	        map4.put("dataindex","4");
	        datalist.add(map4);
	        
	        HashMap<String,String> map5 =new HashMap<String,String>();
	        map5.put("command","netstat");
	        map5.put("exinfo","查看开放的端口");
	        map5.put("dataindex","5");
	        datalist.add(map5);
	        
	        HashMap<String,String> map6 =new HashMap<String,String>();
	        map6.put("command","pwd");
	        map6.put("exinfo","查看当前系统路径");
	        map6.put("dataindex","6");
	        datalist.add(map6);
	        
	        HashMap<String,String> map7 =new HashMap<String,String>();
	        map7.put("command","ps");
	        map7.put("exinfo","查看当前进程信息");
	        map7.put("dataindex","7");
	        datalist.add(map7);
	        
	        SimpleAdapter adapter = new SimpleAdapter(this,datalist,R.layout.dropdown,
	                                    new String[]{"command","exinfo"},new int[]{R.id.droptext1,R.id.droptext2});		
	        
	        actinput.setAdapter(adapter); 
	        actinput.setThreshold(1);
	   	        
	        actinput.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub									   
					     int cmdindex=actinput.getText().toString().indexOf("command");
					     char firstchar=actinput.getText().toString().charAt(cmdindex+8);
					     switch(firstchar)
					     {
					     case 'a':
					    	    break;
					     case 'c':
					    	     if(arg2==0)
					    	    	 actinput.setText(datalist.get(0).get("command"));
					    	     if(arg2==1)
					    	    	 actinput.setText(datalist.get(1).get("command"));
					    	     if(arg2==2)
					    	    	 actinput.setText(datalist.get(2).get("command"));
					    	     break;
					     case 'd':
					    	     if(arg2==0)
				    	    	     actinput.setText(datalist.get(3).get("command"));
					    	    break;
					       
					     case 'l':
					    	    if(arg2==0)
			    	    	         actinput.setText(datalist.get(4).get("command"));
					    	    break;
					     case 'n':
					    	    if(arg2==0)
			    	    	         actinput.setText(datalist.get(5).get("command"));
					    	     break;
					    	     
					     case 'p':
					    	     if(arg2==0)
			    	    	         actinput.setText(datalist.get(6).get("command"));
					    	     if(arg2==1)
				    	    	     actinput.setText(datalist.get(7).get("command"));
					    	    break;
					     default:
					    	 
					     }				     
					
				}
	        	
	        });
	                
	        btn_exe =(Button)findViewById(R.id.execute);
	        btn_exe.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub								
					String et_input_value = actinput.getText().toString();					
					String cmdoutput="";					
					ArrayList my_cmd_list=new ArrayList();
				    if(et_input_value!=null)
				    {
                        String mycmd="'"+et_input_value.trim()+"'"; 
				    	 
				    	my_cmd_list.add(0,"/system/bin/sh"); //shell path
				    	my_cmd_list.add(1,"-c");
				    	my_cmd_list.add(2,mycmd);
				    	
				    	int size=my_cmd_list.size();
				    	String[] my_cmd=new String[size];
				    	for(int i=0;i<size;i++)
				    		my_cmd[i]=(String)my_cmd_list.get(i);
				    	try {
							cmdoutput=imiApi.RunCustomCmd(my_cmd, "/");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					tv_output =(TextView)findViewById(R.id.output);
		   		    tv_output.setText(cmdoutput);
		   		    actinput.setText("");
					}else
					{
						tv_output =(TextView)findViewById(R.id.output);
			   		    tv_output.setText("The Content Input is not proper!");
					}
				}
	        
	        });
	  }
}