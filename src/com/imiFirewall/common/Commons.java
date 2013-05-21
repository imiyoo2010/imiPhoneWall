package com.imiFirewall.common;

import com.imiFirewall.util.imiUtil;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class Commons
{
	private static String APP_PKG_NAME_21="com.android.settings.ApplicationPkgName";
	private static String APP_PKG_NAME_22="pkg";
	
	
	public static final int WHITE_LIST = 0;
	public static final int BLACK_LIST = 1;
	public static final int VIP_LIST = 2;
	
	
	public static final int NEW_VIP_MESSAGE_NOTIFY  = 1000;
	public static final int NEW_SPAM_MESSAGE_NOTIFY = 1001;
	public static final int NEW_VIP_CALL_NOTIFY     = 1002;
	public static final int NEW_REJECT_CALL_NOTIFY  = 1003;
	public static final int SERVICES_RUNNING_NOTIFY = 1004;
	
	public static final String CALLSpamType="CallSpamType";
	public static final int CALL_PROFILE_REJECT_UNKNOW=0;
	public static final int CALL_PROFILE_REJECT_BL=1;
	public static final int CALL_PROFILE_ONLY_WL_=2;
	public static final int CALL_PROFILE_REJECT_ALL=3;
	public static final int CALL_PROFILE_NORMAL=4;   //Set default
	
	public static final String CallBackTone = "CallBackTone";
	public static final int CALL_BUSY = 0;
    public static final int CALL_POWER_OFF = 1;
    public static final int CALL_OUT_OF_SERVICE = 2;
	
    public static final int LOG_SPAM_CALL = 1002;
	public static final int LOG_SPAM_MESSAGE = 2001;
    public static final int LOG_SPAM_REJECT_CALL = 1010;
    public static final int LOG_VIP_CALL = 1000;
	public static final int LOG_VIP_INCOMING_CALL = 1011;
	public static final int LOG_VIP_INCOMING_MESSAGE = 2011;
	public static final int LOG_VIP_MESSAGE = 2000;
	public static final int LOG_VIP_MISSED_CALL = 1013;
	public static final int LOG_VIP_OUTGOING_CALL = 1012;
	public static final int LOG_VIP_OUTGOING_MESSAGE = 2010;
	
	public static final String MessageSpamFilter = "MessageSpamFilter";
    public static final String MessageSpamType = "MessageSpamType";
	
    public static final int MESSAGE_PROFILE_RECEIVING_ALL = 1;
    public static final int MESSAGE_PROFILE_REJECT_UNKNOW = 0;
    
    
    
	public static  Intent getPackageDetailIntent(String app)
	{
		int apiLevel=imiUtil.GetSdkLevel();
		
		String appPkgName=((apiLevel==8) ? APP_PKG_NAME_22:APP_PKG_NAME_21);		
		Intent detail_intent = new Intent();
		detail_intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
		detail_intent.putExtra(appPkgName, app);
		
		return detail_intent;
	}
	
	
	
	public static class PDATA implements Parcelable
	{

		public long mDate;
	    public int mId;
	    public int mIntValue_1;
	    public int mIntValue_2;
	    public int mIntValue_3;
	    public int mIntValue_4;
	    public String mStringValue_1;
	    public String mStringValue_2;
	    public String mStringValue_3;
	    
		public static final Parcelable.Creator<PDATA> CREATOR = new Creator<PDATA>(){

			@Override
			public PDATA createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				Commons.PDATA pData = new Commons.PDATA();

				pData.mIntValue_1   = source.readInt();
				pData.mStringValue_1= source.readString();
				
				
				return pData;
			}

			@Override
			public PDATA[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Commons.PDATA[size];
			}
			
		};
		
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeInt(mIntValue_1);
			dest.writeString(mStringValue_1);
		}
		
	}
	
	public class PersonTypeData{
		
		 public String mName;
		 public String[] mPhoneArray;  //联系人中有多个号码
		 public String mRev;
	}
	
	
	
	
	
	
	
	
	
}