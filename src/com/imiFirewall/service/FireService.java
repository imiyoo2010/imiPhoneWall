package com.imiFirewall.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.telephony.ITelephony;
import com.imiFirewall.Interface.CallEventListener;
import com.imiFirewall.activity.ActivityMain;
import com.imiFirewall.activity.ActivitySplash;
import com.imiFirewall.common.Commons;

import com.imiFirewall.R;
import com.imiFirewall.SpamCharacter;
import com.imiFirewall.imiCallEngine;
import com.imiFirewall.imiSql;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.Contacts;
import android.telephony.TelephonyManager;
import android.util.Log;



public class FireService extends Service implements CallEventListener
{

	private imiCallEngine mCallEngine;
	private boolean mVIPNeedDeleteLog = false;
	private String mVIPIncomingNumber;
	private imiSql db=null;	
	private SpamCharacter mSpamCharacter;
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		if(db==null){
			db = new imiSql(this);
			db.open();
		}
	    
		mCallEngine = new imiCallEngine(this,this);
		mCallEngine.StartListen((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE));
		
		mSpamCharacter = new SpamCharacter(this);
		    
	}
	
	@Override
	public void onStart(Intent intent, int startid){
		super.onStart(intent, startid);
		
		if(db==null){
			db = new imiSql(this);
			db.open();
		}
		
		Bundle extras = intent.getExtras();
		
		if(extras!=null){
			//获取来电号码
			String outGoingNumber = extras.getString("outgoing");
			if(null!= outGoingNumber){
				if(outGoingNumber.length()>0){
					processOutgoingCall(outGoingNumber);
				}
			}
			String newSMS   = extras.getString("incomingSMS");
			String newPhone = extras.getString("incomingPhone");
			long dateTime   = extras.getLong("incomingDate");
			
			if(newSMS!=null&&newPhone!=null){
				if(newSMS.length()>0 && newPhone.length()>0){
					 processIncomingSMS(newPhone,newSMS,dateTime);
				}
			}
		}
	}
	

	@Override
	public void CallEventL(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		Log.i("CallNumber", incomingNumber);
		
		switch (state){
		case TelephonyManager.CALL_STATE_IDLE:{
			   if(mVIPNeedDeleteLog)
			   {
				 deleteLastCallLog(incomingNumber);
				 mVIPNeedDeleteLog = false;				
			    }
		     	break;
		     }
		case TelephonyManager.CALL_STATE_RINGING:{
			
			     processCall(incomingNumber);
			     break;
			  
		     }
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void processIncomingSMS(String smsPhone, String smsMessage,
			long date) {

             Log.i("imi_get_message",smsMessage);
             
             if(!processSMSData(0,smsPhone,smsMessage,date,1)){
            	 //如果短信未被处理，则将其插入至系统收件箱
            	 //insertSMSToSys(smsPhone,smsMessage,date);
             }
		
	}

	/*
	 * 监听呼出电话的处理操作
	 */
	
	public void processOutgoingCall(String outGoingNumber){
		
	}
   
	/*
	 * 监听呼入电话的处理操作
	 */
    public void processCall(String incomingNumber){
		if(db.isNumberInUserList(incomingNumber, Commons.VIP_LIST))
		{
			Log.i("imi","VIP number");
			
			mVIPIncomingNumber = incomingNumber;
			mVIPNeedDeleteLog  = true;
		}else
		{
			boolean bNeedHangup =false;
			
			//判断其它情景模式
			switch (db.getOptionsIntData(Commons.CALLSpamType)){
			case Commons.CALL_PROFILE_REJECT_UNKNOW:{
				 if(getNameFromContacts(incomingNumber).length()<=0){
					 bNeedHangup = true ;
				 }
				 break;
			}

			case Commons.CALL_PROFILE_REJECT_BL:{
				if(db.isNumberInUserList(incomingNumber, Commons.BLACK_LIST)){
					bNeedHangup = true;
				}
				break;
			}
		
			case Commons.CALL_PROFILE_ONLY_WL_:{
				if(!db.isNumberInUserList(incomingNumber, Commons.WHITE_LIST)){
					bNeedHangup = true;
				}
				break;
			}
			case Commons.CALL_PROFILE_REJECT_ALL:{
				bNeedHangup = true;
				break;
			}
			
			default:
				break;
			}
			if (bNeedHangup) {
				mCallEngine.Hangup();

				clearSystemNotification();

				insertRecordToDb(incomingNumber, Commons.LOG_SPAM_CALL,
						Commons.LOG_SPAM_REJECT_CALL);

				setNotifyType(Commons.NEW_REJECT_CALL_NOTIFY);

				//

			}
		}
	}
	public void insertRecordToDb(String number, int type, int logstate) {
		// TODO Auto-generated method stub
		Commons.PDATA tempData = new Commons.PDATA();
		
		tempData.mStringValue_1 = getNameFromContacts(number);
		tempData.mStringValue_2 = number;
		tempData.mIntValue_1    = type;
		tempData.mIntValue_2    = logstate;
		tempData.mDate          = System.currentTimeMillis();
		
		db.AddData(imiSql.DATABASE_TABLE_USERLOG, tempData);
	}

	public void insertCallLogToDb(Cursor cursor, int type)
	{
		Commons.PDATA tempData = new Commons.PDATA();
		
		tempData.mStringValue_2 = cursor.getString(1);
		tempData.mStringValue_1 = (cursor.getString(2)==null)? "":cursor.getString(2);
		tempData.mIntValue_2    = Commons.LOG_VIP_INCOMING_CALL + cursor.getInt(3)
		                          - CallLog.Calls.INCOMING_TYPE;
		tempData.mDate          = Long.parseLong(cursor.getString(4));
		tempData.mIntValue_1    = type;
		
		db.AddData(imiSql.DATABASE_TABLE_USERLOG, tempData);
	}
		
	public void deleteLastCallLog(String number){
		if(number.length()>0){
			mVIPIncomingNumber = number;
		}
		Timer timer = new Timer(true);
		
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("imi", "Delete Log Start");
				Log.i("imi",mVIPIncomingNumber);
				if(deleteLastCallLogEntry(mVIPIncomingNumber))
					Log.i("imi","true");
				else
					Log.i("imi","false");
			}
			
		}, 1000);
	}
	
	public boolean deleteLastCallLogEntry(String number){
		Cursor cursor = getContentResolver().query(
				                             CallLog.Calls.CONTENT_URI,
				                             new String[]{"_id",CallLog.Calls.NUMBER,
				                            		      CallLog.Calls.CACHED_NAME,
				                            		      CallLog.Calls.TYPE,
				                            		      CallLog.Calls.DATE},"number=?",
				                            		      new String[]{number},"_id desc limit 1");
				                            		 
       if(cursor.moveToFirst()){
    	   int id = cursor.getInt(0);
    	   
    	   //在删除之前将记录写入数据库
    	   
    	   
    	   if(cursor.getInt(3)== CallLog.Calls.MISSED_TYPE){
    		   //未接来电，提示用户
    		   
    	   }
    	   
    	   getContentResolver().delete(CallLog.Calls.CONTENT_URI, 
    			   "_id=?", new String[] { id + ""});
    	   return true;
       }
       return false;	
	}
	
	
	public void setNotifyType(int flag){
		NotificationManager manager = (NotificationManager)this
		                               .getSystemService(NOTIFICATION_SERVICE);
		
		Notification imiNoti = new Notification();
		imiNoti.flags = Notification.FLAG_AUTO_CANCEL;
		
		switch(flag){
		case Commons.NEW_VIP_MESSAGE_NOTIFY:{
			Intent notifyIntent = new Intent(this,ActivityMain.class);
			notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			PendingIntent appIntent = PendingIntent.getActivity(FireService.this, 
					0, notifyIntent, 0);
			
			imiNoti.icon       = R.drawable.vipmessage_notify;
			imiNoti.tickerText = getString(R.string.vipmessage_notify);
			imiNoti.defaults   = Notification.DEFAULT_SOUND;
			imiNoti.setLatestEventInfo(FireService.this,
					                   getString(R.string.app_name), 
					                   getString(R.string.vipmessage_notify),
					                   appIntent);		
			
		}
		        break;
		case Commons.SERVICES_RUNNING_NOTIFY:{
			Intent notifyIntent = new Intent(this,ActivitySplash.class);
			notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			PendingIntent appIntent = PendingIntent.getActivity(FireService.this, 
					                  0, notifyIntent, 0);
			
			imiNoti.flags      = Notification.FLAG_NO_CLEAR
			                     | Notification.FLAG_ONGOING_EVENT;
			imiNoti.icon       = R.drawable.icon;
			imiNoti.tickerText = getString(R.string.service_running_text);
			imiNoti.setLatestEventInfo(FireService.this, 
					                   getString(R.string.app_name), 
					                   getString(R.string.service_running_text), appIntent);
		}
		        break;
		default:
			    break;
		 
		}
		
	}
	
	
	public void clearSystemNotification(){
		
		ITelephony iTelephony = null ;
		
		TelephonyManager tManager = (TelephonyManager) this
		                           .getSystemService(Context.TELEPHONY_SERVICE);

        // 初始化iTelephony
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
	           getITelephonyMethod = c.getDeclaredMethod("getITelephony",
			                    (Class[]) null);
	           getITelephonyMethod.setAccessible(true);
       } catch (SecurityException e) {
	   // TODO Auto-generated catch block
	           e.printStackTrace();
       } catch (NoSuchMethodException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
       }
          
       try {
			iTelephony = (ITelephony) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			iTelephony.cancelMissedCallsNotification();// 删除未接来电通知

			StringBuilder where = new StringBuilder("type=");
			where.append(Calls.MISSED_TYPE);
			where.append(" AND new=1");
			ContentValues values = new ContentValues(1);
			values.put(Calls.NEW, "0");
			this.getContentResolver().update(Calls.CONTENT_URI, values,
					where.toString(), null);

		} catch (Exception e) {

		}
       
	}
	/*
	 * 从联系人中读取姓名  根据号码来判断
	 */
	public String getNameFromContacts(String number)
	{
		String contactName ="";
		
		Cursor c = getContentResolver().query(
				       Contacts.People.CONTENT_URI,
				       new String[]{Contacts.People._ID,Contacts.People.NAME,Contacts.People.NUMBER },
				       Contacts.People.NUMBER + "=?", new String[] {number},
				       Contacts.People.DEFAULT_SORT_ORDER);
		if(c!=null){
			if(c.getCount()>0){
				c.moveToFirst();
				contactName = c.getString(1);
			}
		}
		return contactName;
	}
	
	/*
	 * 从联系人中读取姓名  根据标识来判断
	 */
	public String getNameFromContacts(int index) {
		String contactName = "";

		Cursor c = getContentResolver().query(
				Contacts.People.CONTENT_URI,
				new String[] { Contacts.People._ID, Contacts.People.NAME,
						Contacts.People.NUMBER }, Contacts.People._ID + "=?",
				new String[] { String.valueOf(index) },
				Contacts.People.DEFAULT_SORT_ORDER);

		if (null != c) {
			if (c.getCount() > 0) {
				c.moveToFirst();
				contactName = c.getString(1);
			}
		}

		return contactName;
	}
	/**
	 * 处理短信
	 * 
	 * @param contactsIndex
	 *            : 当前电话在电话本的位置
	 * @param phone
	 *            : 短信的电话号码
	 * @param body
	 *            : 电话的内容
	 * @param date
	 *            : 短信的时间
	 * @param type
	 *            : 短信的类型   (用户列表中的type类型，type==1 表示为短信联系记录)
	 * 
	 * @return 是否处理此条短信
	 */
	boolean processSMSData(int contactsIndex, String phone, String body, long date, int type) {

		if (phone.length() > 0) {
			// 确认其为发出的短信

			if (db.isNumberInUserList(
					phone.length() > 9 ? phone.substring(phone.length() - 9,
							phone.length()) : phone, Commons.VIP_LIST)) {
				Commons.PDATA tempData = new Commons.PDATA();
				tempData.mStringValue_1 = contactsIndex > 0 ? getNameFromContacts(contactsIndex)
						: "";
				tempData.mStringValue_2 = phone;
				tempData.mStringValue_3 = body;
				tempData.mIntValue_1 = Commons.LOG_VIP_MESSAGE;
				tempData.mIntValue_2 = (type == 1) ? Commons.LOG_VIP_INCOMING_MESSAGE
						: Commons.LOG_VIP_OUTGOING_MESSAGE;
				tempData.mIntValue_3 = 0;
				tempData.mIntValue_4 = db.getMessageThreadId(phone);
				tempData.mDate = date;

				db.AddData(imiSql.DATABASE_TABLE_USERLOG, tempData);

				if (type == 1) {
					setNotifyType(Commons.NEW_VIP_MESSAGE_NOTIFY);
				}

				return true;
			} else if (type == 1) {
				// 判断其是否为骚扰短信
				if ((db.getOptionsIntData(Commons.MessageSpamType) == Commons.MESSAGE_PROFILE_REJECT_UNKNOW && (type == 1 && contactsIndex == 0))
						|| (db.getOptionsIntData(Commons.MessageSpamFilter) > 0 && mSpamCharacter.isSpamMessage(body))) {
					Commons.PDATA tempData = new Commons.PDATA();
					tempData.mStringValue_1 = contactsIndex > 0 ? getNameFromContacts(contactsIndex)
							: "";
					tempData.mStringValue_2 = phone;
					tempData.mStringValue_3 = body;
					tempData.mIntValue_1 = Commons.LOG_SPAM_MESSAGE;
					tempData.mIntValue_2 = Commons.LOG_SPAM_MESSAGE;
					tempData.mIntValue_3 = 0;
					tempData.mDate = date;

					db.AddData(imiSql.DATABASE_TABLE_USERLOG, tempData);

					if (type == 1) {
						setNotifyType(Commons.NEW_SPAM_MESSAGE_NOTIFY);
					}

					return true;
				}
			}
		}

		return false;
	}
	
}