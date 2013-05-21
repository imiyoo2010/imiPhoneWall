package com.imiFirewall;

import java.util.ArrayList;

import com.imiFirewall.common.Commons;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;



public class PersonSelectAdapter extends BaseAdapter{

	
	private ArrayList mCbArray;
	private Context mContext;
	
	private ArrayList mDataArray;
	private int mDlgWidth;
	
	
	public PersonSelectAdapter(Context context, ArrayList mData, ArrayList mCb, int width)
	{
		mContext = context;
		mDataArray =mData;
		mCbArray =mCb;
		mDlgWidth = width;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String str1 = "";
		DoubleLineWithCheckBox  doubleLine=null;
		if((((Commons.PersonTypeData)mDataArray.get(position)).mPhoneArray!=null)&&(((Commons.PersonTypeData)mDataArray.get(position)).mPhoneArray.length>0))
		{
			if(((Commons.PersonTypeData)mDataArray.get(position)).mPhoneArray.length == 1)
			{
				str1 = ((Commons.PersonTypeData)mDataArray.get(position)).mPhoneArray[0];
			}			
		
			String str2 = ((Commons.PersonTypeData)mDataArray.get(position)).mName;
			if(((Commons.PersonTypeData)mDataArray.get(position)).mRev != null)
			{
				String str5 = ((Commons.PersonTypeData)mDataArray.get(position)).mRev;
				str2=String.valueOf(str2 + "\t" + str5);
			}
			doubleLine = new DoubleLineWithCheckBox(mContext,str2,str1,mDlgWidth);
			CheckBox cb = doubleLine.getCheckBox();
			mCbArray.add(cb);
			
		}
		return doubleLine;
	}
	
	
}
