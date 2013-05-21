package com.imiFirewall;

import java.util.ArrayList;

import com.imiFirewall.common.Commons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class TabManagerAdapter extends BaseAdapter{

	private ArrayList mDataArray;
	private Context mContext;
	private LayoutInflater mInflater;
	
	
	public class TabViewHolder {
		TextView ContactName;
		TextView PhoneNumber;
	}
	
	public TabManagerAdapter(Context context , ArrayList data)
	{
		mContext   = context;
		mDataArray = data;
		mInflater  =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mDataArray==null)
		{
			return 0;
		}
		else
	    {
			return mDataArray.size();
	    }
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		
		return mDataArray.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void clear(){
		if(mDataArray!=null)
		  mDataArray.clear();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		TabViewHolder holder;
		if(convertView==null){
			convertView =  mInflater.inflate(R.layout.spam_item, null);
			holder = new TabViewHolder();
			
			holder.ContactName = (TextView) convertView.findViewById(R.id.Spam_Contact_Name);
			holder.PhoneNumber = (TextView) convertView.findViewById(R.id.Spam_Phone_Number);
			
			convertView.setTag(holder);
		}else{
			holder = (TabViewHolder) convertView.getTag();
		}
		
		String str3 =((Commons.PDATA)mDataArray.get(position)).mStringValue_1;
		holder.ContactName.setText(str3);
		String str4 =((Commons.PDATA)mDataArray.get(position)).mStringValue_2;
		holder.PhoneNumber.setText(str4);
		
		return convertView;
	}
	
	public void remove(int position)
	{
		mDataArray.remove(position);
		notifyDataSetChanged();
	}
	
}