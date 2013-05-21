package com.imiFirewall.activity;

import java.util.ArrayList;
import java.util.List;

import com.imiFirewall.R;
import com.imiFirewall.TabManagerAdapter;
import com.imiFirewall.imiSql;
import com.imiFirewall.Interface.PersonSelectListener;
import com.imiFirewall.common.Commons;
import com.imiFirewall.util.imiPersonSelectDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;



public class ActivitySpamList extends TabActivity 
       implements TabHost.OnTabChangeListener,AdapterView.OnItemClickListener, PersonSelectListener
{
	private TabHost  tabHost;	
	private static final String[] TAB;
	private imiSql mydb=null;
	private ListView listView = null;
	private ArrayList mDataArray;
	private TabManagerAdapter mAdapter=null;
	
	private EditText mText1;
	private EditText mText2;
	
	final CharSequence [] items = { "Red" , "Green" , "Blue" };
	
	static{
		String[] tabString = new String[2];
		tabString[0]       ="BLACKLIST";
		tabString[1]       ="WHITELIST";
		TAB=tabString;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		tabHost = getTabHost();
	
		LayoutInflater layoutinflater =LayoutInflater.from(this);
		FrameLayout framelayout = tabHost.getTabContentView();
		layoutinflater.inflate(R.layout.spamlisttab, framelayout, true);
		
	    
	    android.widget.TabHost.TabSpec tabspec=tabHost.newTabSpec(TAB[0]);
	    tabspec.setContent(R.id.spam_black_list);
	    tabspec.setIndicator((CharSequence)(getString(R.string.tab_spam_black)), (android.graphics.drawable.Drawable)(getResources().getDrawable(R.drawable.mobile)));
	    tabHost.addTab(tabspec);
	   
	    android.widget.TabHost.TabSpec tabspec1 =tabHost.newTabSpec(TAB[1]);
	    tabspec1.setContent(R.id.spam_white_list);  
	    tabspec1.setIndicator(((CharSequence)(getString(R.string.tab_spam_white))),((android.graphics.drawable.Drawable)(getResources().getDrawable(R.drawable.wifi))));
	    tabHost.addTab(tabspec1);
		tabHost.setOnTabChangedListener(this);
	    
		  mydb = new imiSql(this);
	      mydb.open();
		
		initBlackListView();
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		if(tabId.equals(TAB[0]))
		{
			initBlackListView();
		}
		if(tabId.equals(TAB[1]))
		{
			initWhiteListView();
		}
	}

	private void initWhiteListView() {
		// TODO Auto-generated method stub
		//初始化白名单视图
		listView=(ListView) findViewById(R.id.white_listview);
	    
	    mydb.open();
	    mDataArray =new ArrayList();
	    mydb.getData("UserList", mDataArray, "type", 1);	 //type==1为白名单  
	    mAdapter = new TabManagerAdapter(this,mDataArray);
	    listView.setAdapter(mAdapter);
	    listView.setOnItemClickListener(this);
	}

	private void initBlackListView() {
		// TODO Auto-generated method stub
		//初始化黑名单视图
		listView = (ListView) findViewById(R.id.black_listview);
		
		mydb.open();
		mDataArray =new ArrayList();
		mydb.getData("UserList", mDataArray, "type", 0);   //type==0为黑名单
		mAdapter = new TabManagerAdapter(this,mDataArray);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}
	
	private void insertNewData(int type)
	{
		String str1 = mText1.getText().toString();
		String str2 = mText2.getText().toString();
		
		insertNewData(type,str1,str2);
		
	}
	
	private void insertNewData(int type,String name,String phone)
	{
		Commons.PDATA pData = new Commons.PDATA();
		pData.mStringValue_1 = name;
		pData.mStringValue_2 = phone;
		pData.mIntValue_1    = type;
		
		mydb.AddData("UserList", pData);
		mDataArray.add(pData);
		mAdapter.notifyDataSetChanged();
		
		return;
		
		
	}

	protected Dialog onCreateDialog(int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("test");
		builder.setItems(items, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	    return builder.create();
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		createMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	private void createMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, R.string.menu_add_contact).setIcon(R.drawable.menu_add_icon);
		menu.add(0, 1, 0, R.string.menu_add_calllog).setIcon(R.drawable.menu_add_icon);
		menu.add(0, 2, 0, R.string.menu_add_email).setIcon(R.drawable.menu_add_icon);
		menu.add(0, 3, 0, R.string.menu_add_custom).setIcon(R.drawable.menu_add_icon);
			
	}

     @Override
    
    /*   增加程序对菜单项的单击响应 
     * 
     */
    public boolean onOptionsItemSelected(MenuItem item){
    
      String str="";
    	 
      switch(item.getItemId()){
      case 0:
    	  
    	  imiPersonSelectDialog.SelectFromType SelectFromContact = imiPersonSelectDialog.SelectFromType.FromContacts;
    	  new imiPersonSelectDialog(this,this,SelectFromContact).setDisplay();
    	  
    	  break;
      case 1:
    	  
    	  imiPersonSelectDialog.SelectFromType SelectFromCallLog = imiPersonSelectDialog.SelectFromType.FromCallLog;
          new imiPersonSelectDialog(this, this,SelectFromCallLog).setDisplay();
          
    	  break;
      case 2:
    	  
    	  imiPersonSelectDialog.SelectFromType SelectFromMessage = imiPersonSelectDialog.SelectFromType.FromMessage;
          new imiPersonSelectDialog(this, this,SelectFromMessage).setDisplay();
          
          break;
      case 3:
    	  
    	  showInputDlg(str, str, 0);
    	  
    	  break;
    	  
      }
      return super.onOptionsItemSelected(item);
    }
     
	private void showInputDlg(String str1, String str2, int i) {
		// TODO Auto-generated method stub
		View myview  = LayoutInflater.from(this).inflate(R.layout.phone_input, null);
		mText1  =(EditText)myview.findViewById(R.id.phone_input_name);
		mText1.setText(str1);
		mText2  =(EditText)myview.findViewById(R.id.phone_input_phone);
		mText2.setText(str2);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.phone_input_dialog_title));
		builder.setView(myview);
		builder.setPositiveButton(getString(R.string.button_ok), new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
		});
		builder.setNegativeButton(getString(R.string.button_cancel), new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		builder.create();
		builder.show();
		
		return;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	//通过OnSelect传递Dialog和ListActivity的数据
	@Override
	public void OnSelect(List dataList) {
		// TODO Auto-generated method stub
		
		//判断Dialog中的数据与mDataArray是否有重复
		mDataArray.removeAll(mDataArray);
		int tabIndex=this.tabHost.getCurrentTab();
		mydb.open();
		
		mydb.getData("UserList", mDataArray,"type", tabIndex);
		for(int m=0;m<dataList.size();m++)
		{
		 String mName =((Commons.PersonTypeData)dataList.get(m)).mName;
		 String mPhone=((Commons.PersonTypeData)dataList.get(m)).mPhoneArray[0];
		 insertNewData(tabIndex,mName,mPhone);		 
		}
	}
}