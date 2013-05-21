package com.imiFirewall.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.imiFirewall.PersonSelectAdapter;
import com.imiFirewall.R;
import com.imiFirewall.TabManagerAdapter;
import com.imiFirewall.Interface.ButtonGroupListener;
import com.imiFirewall.Interface.PersonSelectListener;
import com.imiFirewall.common.Commons;
import com.imiFirewall.common.Commons.PersonTypeData;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;




public class imiPersonSelectDialog extends Dialog
       implements AdapterView.OnItemClickListener ,ButtonGroupListener
{
	
	
	private ListView mListView;
	private ButtonGroup mBtnGroup;
	private PersonSelectListener mListener;
	private PersonSelectAdapter mSelectorAdapter;
	private ArrayList<Commons.PersonTypeData> mDataArray;
	private ArrayList<CheckBox> mCbArray;
	private SelectFromType mType;
	final int[] mNameArray;

	
	
	public imiPersonSelectDialog(Context context,PersonSelectListener pListen,SelectFromType type)
	{
		super(context);
		
		int[] arrayButtonStringId = {R.string.button_ok,R.string.button_cancel,R.string.button_markall,R.string.button_unmarkall};		
		mNameArray=arrayButtonStringId;
		mListener = pListen;
		mDataArray = new ArrayList<Commons.PersonTypeData>();
		mCbArray=new ArrayList<CheckBox>();
		mType = type;	
	}
	/*
	 * 获取手机所有联系人信息
	 */
	
	private void getContacts(ArrayList mDataArrayContact)
	{
		
		/*
		 * 首先获取所有联系人的统计信息 ContactsContract.Contacts
		 * 然后根据获取的ID,从ContactsContract.CommonDataKinds根据_id获取
		 * 联系人其它相关信息，譬如电话，Email等
		 */
		ContentResolver resolver = getContext().getContentResolver();
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		Cursor cursor = resolver.query(uri, null, null, null, null);
		
		Commons commons = new Commons();
		Commons.PersonTypeData personType =null ;
		personType = commons.new PersonTypeData();
		
	    if(cursor.getCount()>0 && (cursor.moveToFirst()))
	    {
	     
	     do{
	   	   String str_id = cursor.getString(cursor.getColumnIndex("_id"));
	       personType.mName = cursor.getString(cursor.getColumnIndex("display_name"));
	       int m = cursor.getInt(cursor.getColumnIndex("has_phone_number"));
	     
	        if(m>0) //当联系人有多个号码的时候将其它的内容也存入PhoneArray
	        {
	    	   personType.mPhoneArray = new String[m];
	    	   ContentResolver resolver1 = getContext().getContentResolver();
	    	   Uri uri1 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	    	  
	    	   String selectArg ="contact_id = "+str_id;
	    	   Cursor cursor1 = resolver1.query(uri1, null, selectArg, null, null);
	    	     	
	    	   int n=0;
	    	   cursor1.moveToFirst();	    	   
	    	   do{
	    		  String phone1 = cursor1.getString(cursor1.getColumnIndex("data1"));
	    		  personType.mPhoneArray[n]=phone1;
	    		  n=n+1;
	    	   }while(cursor1.moveToNext());
	         }
	        
	         mDataArrayContact.add(personType);
	         
	        }while(cursor.moveToNext());
	        
	     		   
		  }    
	          return ;
		
	}
	
	/*
	 * 获取通话记录中的联系人信息，以便导入到黑白名单
	 */
	private void getCallLog(ArrayList mDataArrayCallLog) {
		// TODO Auto-generated method stub
		ContentResolver resolver = getContext().getContentResolver();
		Uri uri = CallLog.Calls.CONTENT_URI;
		
		String[] info = new String[4];
		info[0]= "number";
		info[1]= "name";
		info[2]= "type";
		info[3]="date";
		
		Cursor cursor = resolver.query(uri, info, null, null, "date DESC");
		if(cursor.getCount()>0&&cursor.moveToFirst())
		{
			while(cursor.moveToNext())
			{
			  String number = cursor.getString(0);
			  String name   = cursor.getString(1);
			  int type    = cursor.getInt(2);
			  SimpleDateFormat simpleDate = new SimpleDateFormat("MM-dd hh:mm:ss");
			  long d = Long.parseLong(cursor.getString(3));
			  Date  t = new Date(d);
			  String date = simpleDate.format(t);
			
			  Commons.PersonTypeData personType= null;
			  personType.mName = name;
			  personType.mRev  = date;
			
			  String[] str_phone = new String[1];
			  personType.mPhoneArray= str_phone;
			  personType.mPhoneArray[0]= number;
			
			  mDataArrayCallLog.add(personType);
				
			}
		}
		
	}
	
	/*
	 * 从短信记录中获取联系人信息，以便导入黑白名单
	 */
	
	private void getMessageLog(ArrayList mDataArrayMessageLog) {
		// TODO Auto-generated method stub
		Uri uri1 = Uri.parse("content://mms-sms/conversations").buildUpon().appendQueryParameter("simple", "true").build();
		Uri uri2 = Uri.parse("content://mms-sms/canonical-address");
		
		String[] MessageEntity = new String[9];
		MessageEntity[0]="_id";
		MessageEntity[1]="date";
		MessageEntity[2]="message_count";
		MessageEntity[3]="recipient_ids";
		MessageEntity[4]="snippet";
		MessageEntity[5]="snippet_cs";
		MessageEntity[6]="read";
		MessageEntity[7]="error";
		MessageEntity[8]="has_attachment";
		
		
		Cursor cursor = getContext().getContentResolver().query(uri1, MessageEntity, null, null, null);
		if(cursor.getCount()>0&&cursor.moveToFirst())
		{
			
				String str1 = cursor.getString(3);
				String str2 = cursor.getString(4);
				
				Commons.PersonTypeData personType=null;
				personType.mRev = str2;
				
				
				String[] selectArgs = new String[2];
				selectArgs[0]="_id";
				selectArgs[1]="address";
				
				String[] selectValue = new String[1];
				selectValue[0]=str1;
				
				Cursor cursor1=getContext().getContentResolver().query(uri2, selectArgs, "_id=?", selectValue, null);
				if(cursor1.getCount()>0)
				{
					cursor1.moveToFirst();
				 do{	
					String[] phonearray = new String[1];
					personType.mPhoneArray=phonearray;
					String str4= cursor.getString(1);
					personType.mPhoneArray[0]=str4;
					
					
					String str7 = getContactByAddr(getContext(),personType.mPhoneArray[0]);
					if(str7==null)
					{
				    personType.mName=getContext().getString(R.string.unknow_name_prompt);
					}
					else
					{
					personType.mName=str7;
					}
					if(str4!=null)
					{
						mDataArrayMessageLog.add(personType);
					}
				 }while(cursor.moveToNext());
				}
		}
				return ;
	}

	public String getContactByAddr(Context context,String address)
	{
		String str="display_name";
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
		
		ContentResolver resolver = context.getContentResolver();
		
		String[] selectArgs = new String[1];
		selectArgs[0]=str;
		
		Cursor cursor = resolver.query(uri, selectArgs, null, null, null);
		cursor.moveToFirst();
		
		String result = cursor.getString(cursor.getColumnIndex(str));
		return result;
	}
	
	public enum SelectFromType
	{
		FromContacts,FromCallLog,FromMessage,Manully;
		static
		{
			SelectFromType[] type = new SelectFromType[4];
			type[0] = FromContacts;	
			type[1]=FromCallLog;
			type[2]=FromMessage;
			type[3]=Manully;
		}
	}


	public void setDisplay() {
		// TODO Auto-generated method stub
		int i=30;
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(1);
		mListView = new ListView(getContext());
		mListView.setOnItemClickListener(this);
		mListView.setCacheColorHint(0);		
		int j =mType.ordinal();   //返回枚举字段的序号值
		
		switch (j)
		{
		case 0:
			
			String str1 = getContext().getString(R.string.app_name);
			setTitle(str1);
			getContacts(mDataArray);	
			break;
		case 1:
			
			String str2 = getContext().getString(R.string.app_name);
			setTitle(str2);
			getCallLog(mDataArray);
			break;
		case 2:
			
			String str3 = getContext().getString(R.string.app_name);
			setTitle(str3);
			getMessageLog(mDataArray);
			break;
		}
		
		
		int k = mDataArray.size();
		Display display =getWindow().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		
		mSelectorAdapter = new PersonSelectAdapter(getContext(),mDataArray,mCbArray,width);
		mListView.setAdapter(mSelectorAdapter);
		int layout_width =display.getWidth()-i;
		int layout_height=display.getHeight()-150;
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(layout_width,layout_height);
		layout.addView(mListView,layoutParams);
		
		int i2 = display.getWidth()-i;
		
		mBtnGroup = new ButtonGroup(getContext(),this,i2,mNameArray);
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(i2,-1);
		layout.addView(mBtnGroup,layoutParams1);
		
		setContentView(layout);
		show();
		return ;
			
	}

	class ButtonGroup extends LinearLayout
	{
		Button[] mButtonGroup;
		int[] mNameArray;
		boolean markAll;
		ButtonGroupListener btnListener;
		
		public ButtonGroup(Context context,ButtonGroupListener listener ,int a1,int[] mButtonArrayId)
		{
			super(context);
			Button[] button = new Button[3];
			mButtonGroup = button;
			markAll=false;
			mNameArray=mButtonArrayId;
			btnListener=listener;
			
			for(int i=0;i<mButtonGroup.length;i++)
			{
				Button btnwithListener = new Button(context);
				mButtonGroup[i]=btnwithListener;
				
				int m = a1/mButtonGroup.length;
				mButtonGroup[i].setWidth(m);
				mButtonGroup[i].setText(getContext().getString(mNameArray[i]));
				
				LinearLayout.LayoutParams layoutParam =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				addView(mButtonGroup[i],layoutParam);
				mButtonGroup[i].setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//利用视图的值与按钮对比来判断那个按钮被点击
						int i=0;
						while(i<3)
						{
							if(v==mButtonGroup[0])
							{
								btnListener.OnButtonClickEventL(i);
								break;
							}
							if(v==mButtonGroup[1])
							{
								btnListener.OnButtonClickEventL(i);
								break;
							}
							if(v==mButtonGroup[2])
							{
								markAll = !markAll;
								if(markAll)
								{
									mButtonGroup[2].setText(getContext().getString(mNameArray[3]));
									btnListener.OnButtonClickEventL(2);
								}
								else
								{
									mButtonGroup[2].setText(getContext().getString(mNameArray[2]));
									btnListener.OnButtonClickEventL(3);
								}
							}
							i++;
						}
						
					}
					
				});
				
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		int a =mDataArray.size();
		int b =mCbArray.size();
		
		mCbArray.get(position).setChecked(!mCbArray.get(position).isChecked());
	}
	@Override
	public void OnButtonClickEventL(int btnIndex) {
		// TODO Auto-generated method stub
		switch(btnIndex)
		{
		case 0:
			//已选的单选按钮
			ArrayList<Commons.PersonTypeData> selected =new ArrayList<Commons.PersonTypeData>();
			for(int i=0;i<this.mCbArray.size();i++)
			{
				if(mCbArray.get(i).isChecked()){
					selected.add(mDataArray.get(i));
				}
			}
			mListener.OnSelect(selected);
			this.dismiss();
			break;
		case 1:
			this.dismiss();
			break;
		case 2:
			for(int i=0;i<this.mCbArray.size();i++)
			{
				 mCbArray.get(i).setChecked(true);
				
			}
			break;
		case 3:
			for(int i=0;i<this.mCbArray.size();i++)
			{
				mCbArray.get(i).setChecked(false);
			}
			break;
		default:
			break;
				
		
		}
	}
	
	/*
	 * 判断电话是否已存在数据组中
	 * @param phone
	 * @param dataArray
	 * @return
	 */
	boolean isExisted(String phone,ArrayList<Commons.PersonTypeData> dataArray)
	{
		for(int i=0;i<dataArray.size();i++){
			for(int j=0;j<dataArray.get(i).mPhoneArray.length;j++){
				String phoneRule = phone.length()>9?phone.substring(phone.length()-9,phone.length()):phone;
				String dataRule  = dataArray.get(i).mPhoneArray[j].length()>9?dataArray.get(i).mPhoneArray[j].substring(dataArray.get(i).mPhoneArray[j].length()-9,
						           dataArray.get(i).mPhoneArray[j].length()):dataArray.get(i).mPhoneArray[j];
						if(phoneRule.equals(dataRule)){
							return true;
						}
			}
		}
		return false;
	}
}