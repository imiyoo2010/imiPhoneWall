package com.imiFirewall.activity.personal;


import java.util.HashMap;

import com.imiFirewall.R;
import com.imiFirewall.imiApi;
import com.imiFirewall.imiSql;
import com.imiFirewall.R.drawable;
import com.imiFirewall.R.id;
import com.imiFirewall.R.layout;
import com.imiFirewall.R.string;

import android.app.Activity;    //引用android开发包，记得android需要小写
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;     //引入菜单项
import android.view.MenuItem;

public class ActivityPersonal extends Activity {
    
	
	private EditText mPwd1;
	private EditText mPwd2;
	private String  mypassword;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
            
	        imiSql mydb = new imiSql(this);
	        mydb.open();
	        String[] col={"password"};
	        Cursor cursor=mydb.Query("passwordbox",col);
	        if(cursor.getCount()>0)
	        { 
	        	showLoginInput();
	                	        	
	        }else
	        {
	        	showPwdInput();
	        	HashMap hashmap = new HashMap();
	        	
	        	hashmap.put("name", "imiFirewall");
	        	hashmap.put("password", mypassword);
	        	
	        	mydb.insert("passwordbox", hashmap);
	        	
	        }
	        setContentView(R.layout.callblacklist);

	 }
	 
	 public void finish()
	 {
		 super.finish();
	 }
	 
	 
	 
	 private void showLoginInput()
	 {
		 View login_view = LayoutInflater.from(this).inflate(R.layout.single_add_dialog, null);
		 String query_pwd = ((EditText)login_view.findViewById(R.id.single_edit)).getText().toString();
		
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("请输入登录密码");
		 builder.setView(login_view);
		 
		 builder.setPositiveButton(R.string.pwd_dialog_ok, new OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					//验证密码不一致，直接退出当前Activity
					
				}
				 
			 });
			 
			 
			 builder.setNegativeButton(R.string.pwd_dialog_cancel, new OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					  finish();
				}
				 
			 });
		 
		 
		 
		 builder.create();
		 builder.show();
		
	 }
	 
	 private void showPwdInput()
	 {
		 //加载输入密码的对话框布局文件
		 View pwd_view = LayoutInflater.from(this).inflate(R.layout.password_input_dialog, null);
		 mPwd1 =(EditText) pwd_view.findViewById(R.id.password_edit_once);
		 mPwd1.setText("");
		 mPwd2 =(EditText) pwd_view.findViewById(R.id.password_edit_twice);
		 mPwd2.setText("");
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("为您的私密空间设置密码");
		 builder.setView(pwd_view);
		 
		 
		 builder.setPositiveButton(R.string.pwd_dialog_ok, new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//验证密码不一致，直接退出当前Activity
				String m1 =mPwd1.getText().toString();
				String m2 =mPwd2.getText().toString();
				
				if(m1.equals(m2)){
					mypassword = m1;
					return;
				}
				finish();
				
			}
			 
		 });
		 
		 
		 builder.setNegativeButton(R.string.pwd_dialog_cancel, new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				  finish();
			}
			 
		 });
		 
		 builder.create();
		 builder.show();
	 }
	 
	 
	 
}
