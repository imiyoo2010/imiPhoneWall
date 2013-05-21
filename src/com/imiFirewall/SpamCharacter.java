package com.imiFirewall;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SpamCharacter
{
	final String FILTERFLAG ="item";
	Context mContext;
	
	
	public ArrayList mSpamArray;
	String mTempString;
	
	public SpamCharacter(Context context)
	{
		mContext = context;
	}
	public boolean isSpamMessage(String body)
	{
		return true;
	}
}