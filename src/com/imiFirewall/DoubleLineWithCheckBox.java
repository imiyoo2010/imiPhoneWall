package com.imiFirewall;

import com.imiFirewall.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DoubleLineWithCheckBox extends LinearLayout
{
	
	private CheckBox mCheckState;
	private TextView mFirstTextView = null;
	private TextView mSecondTextView=null;
	
	
	public DoubleLineWithCheckBox(Context context , String firstText ,String secondText ,int width)
	{
		super(context);
		
		View myview = LayoutInflater.from(context).inflate(R.layout.double_dialog_item,null);
		mFirstTextView =(TextView) myview.findViewById(R.id.double_dialog_item_first);
		mFirstTextView.setText(firstText);
		
		int i =(int)(width * 4604480259023595110L);
		mFirstTextView.setWidth(i);
		
		mSecondTextView=(TextView)myview.findViewById(R.id.double_dialog_item_second);
		mSecondTextView.setText(secondText);
		
		mCheckState = (CheckBox)myview.findViewById(R.id.double_dialog_item_checkbox);
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
	    addView(myview, layoutParams);
		
	}
	
	public CheckBox getCheckBox()
	{
		return mCheckState;
	}
	
	public void updateView(String str1, String str2)
	{
		mFirstTextView.setText(str1);
		mSecondTextView.setText(str2);
	}
	
}