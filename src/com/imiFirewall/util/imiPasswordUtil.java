package com.imiFirewall.util;

import android.content.Context;


public class imiPasswordUtil
{
	
	private final Context mContext;
	
	protected imiPasswordUtil(Context context)
	{
		mContext=context;
	}
	
	//返回当前类的实例可以进行相关操作的对象
	public static imiPasswordUtil getInstance(Context context) 
	{
		return new imiPasswordUtil(context);
	}
}