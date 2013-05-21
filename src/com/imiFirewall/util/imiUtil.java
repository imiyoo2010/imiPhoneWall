package com.imiFirewall.util;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Build;

public class imiUtil
{

    public imiUtil()
    {
    }

    public static int GetSdkLevel()
    {
    	return Build.VERSION.SDK_INT;
    	
    }
    
    public static String date2String(int i, int j, int k)
    {
        byte byte0 = 10;
        String s = "0";
        String s1 = "-";
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(i);
        Object obj = "-";
        stringbuilder.append(s1);
        if(++j < byte0)
            obj = (new StringBuilder(s)).append(j).toString();
        else
            obj = Integer.valueOf(j);
        stringbuilder.append(obj);
        obj = "-";
        stringbuilder.append(s1);
        if(k < byte0)
            obj = (new StringBuilder(s)).append(k).toString();
        else
            obj = Integer.valueOf(k);
        stringbuilder.append(obj);
        return stringbuilder.toString();
    }

    public static String date2String(Date date, String s)
    {
        Object obj = 0;
        try
        {
            String s1 = stringNull(s);
            obj = (new SimpleDateFormat(s1)).format(date);
        }
        catch(Exception exception) { }
        return ((String) (obj));
    }

    public static String formatByte(long l)
    {
        String s = "0 KB";
        if(l <= 0x100000L){ 
        BigDecimal bigdecimal = new BigDecimal(l);
        BigDecimal bigdecimal1 = new BigDecimal(1024);
        RoundingMode roundingmode = RoundingMode.CEILING;
        String s1 = String.valueOf(bigdecimal.divide(bigdecimal1, 0, roundingmode).toString());
        s = (new StringBuilder(s1)).append(" KB").toString();
        }else{
           if(l >= 0x40000000L){
            BigDecimal bigdecimal2 = new BigDecimal(l);
            BigDecimal bigdecimal3 = new BigDecimal(0x40000000);
            RoundingMode roundingmode1 = RoundingMode.CEILING;
            String s2 = String.valueOf(bigdecimal2.divide(bigdecimal3, 2, roundingmode1).toString());
            s = (new StringBuilder(s2)).append(" G").toString();
            }else{
            BigDecimal bigdecimal4 = new BigDecimal(l);
            BigDecimal bigdecimal5 = new BigDecimal(0x100000);
            RoundingMode roundingmode2 = RoundingMode.CEILING;
            String s3 = String.valueOf(bigdecimal4.divide(bigdecimal5, 2, roundingmode2).toString());
            s = (new StringBuilder(s3)).append(" MB").toString();
            }    
        }
            return s;
    }

    public static boolean isEmpty(String s)
    {
        boolean flag = false;
        if(stringNull(s) == null)
            flag = true;
        return flag;
    }
    public static String read(String s) throws IOException
    {
        String s1;
        RandomAccessFile randomaccessfile;
        s1 = "";
        randomaccessfile = null;
        File file = new File(s);
        randomaccessfile = new RandomAccessFile(file, "r");
        s1 = randomaccessfile.readLine();
        randomaccessfile.close();
        return s1;
    }

    public static Date string2Date(String s, String s1)
    {
        Date date = new Date();
        try
        {
            String s2 = stringNull(s1);
            SimpleDateFormat simpledateformat = new SimpleDateFormat(s2);
            String s3 = stringNull(s);
            date = simpledateformat.parse(s3);
        }
        catch(Exception exception) { }
        return date;
    }

    public  static String[] date2Week(Date date, String s)
    {
    		String as[];
            int j;
            int k;
            as = new String[7];
          
            Calendar cal=Calendar.getInstance();
            cal.setTime(date);
            
            int l=cal.get(Calendar.DAY_OF_WEEK); //获取当前时间对应的星期1-7
            l=l-1;
            String s1=date2String(date,s);
            as[l] = s1;
            k=l;
            j=1;
            while(k>0)
           {
                  int i1=k-1;
                  long l1=(long)0x5265c00* j;
                  long l2=cal.getTimeInMillis()-l1;
                  String s2=date2String(new Date(l2),s);
                  as[i1]=s2;
                   k--;
                   j++;
            }
                 return as;
    }
    public static String stringBlank(String s)
    {
        s = stringNull(s);
        if(s == null)
            s = "";
        return s;
    }

    public static String stringNull(String s)
    {
        if(s != null)
        {
            s = s.trim();
            if(s.equals("") || s.equalsIgnoreCase("null"))
                s = "";
        }
        return s;
    }
    public static int getSDKVer()
    {
    	return Integer.parseInt(Build.VERSION.SDK);
    	
    }
    
}
