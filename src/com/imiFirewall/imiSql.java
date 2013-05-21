package com.imiFirewall;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.imiFirewall.common.Commons;
import com.imiFirewall.util.imiUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class imiSql{
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private Context mCtx;    //与当前类关联起来
    
	
	public static final String KEY_ID     = "_id";  //使用SimpleCursorAdapter Cursor关联时注意这里,前面有_
	public static final String KEY_PHONE  = "phone";
	public static final String KEY_NAME   = "name";
	public static final String KEY_TYPE   = "type";
	public static final String KEY_DATE   = "date";
	
	private static final String DATABSE_CREATE_PASSWORD="create table PasswordBox (_id integer primary key autoincrement,name text,"
		+"password text);";
	private static final String DATABASE_CREATE_OPTIONS="create table Options (_id integer primary key autoincrement,optionsName text,"
		+"optionsInt integer, optionsString text)";
	private static final String DATABASE_CREATE_USERLIST="create table UserList (_id integer primary key autoincrement,name text,"
		+"phone text,type integer,message integer,call integer)";	
	private static final String DATABASE_CREATE_USERLOG="create table UserLog (_id integer primary key autoincrement,name text,"
		+"phone text,type integer,date long,content text,logThread integer,logState integer)";
	
	
    public static final  String DATABASE_NAME             = "imiFirewall";
    
	public static final  String DATABASE_TABLE_PASSWORD   ="PasswordBox";
	public static final  String DATABASE_TABLE_USERLIST   ="UserList";
	public static final  String DATABASE_TABLE_OPTIONS    ="Options";
	public static final  String DATABASE_TABLE_USERLOG    ="UserLog";
	
    
    private static final int DATABASE_VERSION = 1;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			

			
			StringBuilder stringbuilder = new StringBuilder();
			stringbuilder.append("CREATE TABLE IF NOT EXISTS ");
	        stringbuilder.append("t_base");
	        stringbuilder.append("(");
	        stringbuilder.append("_key");
	        stringbuilder.append(" TEXT PRIMARY KEY, ");
	        stringbuilder.append("_value");
	        stringbuilder.append(" TEXT NOT NULL, ");
	        stringbuilder.append("_date");
	        stringbuilder.append(" TEXT NOT NULL)");
	        String s0 = stringbuilder.toString();
	   
	        StringBuilder stringbuilder1 = new StringBuilder();
	        stringbuilder1.append("CREATE TABLE IF NOT EXISTS ");
	        stringbuilder1.append("t_data");
	        stringbuilder1.append("(");
	        stringbuilder1.append("_id");
	        stringbuilder1.append(" TEXT PRIMARY KEY, ");
	        stringbuilder1.append("_rx_mobile");
	        stringbuilder1.append(" NUMERIC NOT NULL, ");
	        stringbuilder1.append("_tx_mobile");
	        stringbuilder1.append(" NUMERIC NOT NULL, ");
	        stringbuilder1.append("_rx_wifi");
	        stringbuilder1.append(" NUMERIC NOT NULL, ");
	        stringbuilder1.append("_tx_wifi");
	        stringbuilder1.append(" NUMERIC NOT NULL, ");
	        stringbuilder1.append("_date");
	        stringbuilder1.append(" TEXT NOT NULL, ");
	        stringbuilder1.append("_year");
	        stringbuilder1.append(" TEXT NOT NULL, ");
	        stringbuilder1.append("_month");
	        stringbuilder1.append(" TEXT NOT NULL, ");
	        stringbuilder1.append("_day");
	        stringbuilder1.append(" TEXT NOT NULL, ");
	        stringbuilder1.append("_hour");
	        stringbuilder1.append(" TEXT NOT NULL, ");
	        stringbuilder1.append("_week");
	        stringbuilder1.append(" TEXT NOT NULL)");
	        String s1 = stringbuilder1.toString();
			
	        
	       db.execSQL(DATABASE_CREATE_OPTIONS);
           db.execSQL(DATABASE_CREATE_USERLIST);
	       db.execSQL(DATABSE_CREATE_PASSWORD);
           db.execSQL(DATABASE_CREATE_USERLOG);
	        
		    //netcount
		   db.execSQL(s0);
   	       db.execSQL(s1);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
	public imiSql(Context ctx) {
		this.mCtx = ctx;
	}
	public imiSql open() throws SQLException{
		
		  mDbHelper = new DatabaseHelper(mCtx);
		  if(mDb==null)
		  {
			mDb      =  mDbHelper.getWritableDatabase();
		  }else
		  {
			close();
		    mDb      =  mDbHelper.getWritableDatabase();
		  }
			  
		  return this;
		
	}
	/*
	 * 根据短信的号码查询其会话ID(ThreadID)
	 */
	
	public int getMessageThreadId(String number)
	{
		Cursor cursor;
		int threadId = 0;
		
		String[] select_colums = new String[2];
		select_colums[0]="_id";
		select_colums[1]="logThread";
		
		String[] select_args   = new String[2];
		select_args[0]         = String.valueOf(2000);
		select_args[1]         = number;
		
		cursor=mDb.query(true, "userlog", select_colums, "type=? AND phone LIKE ?", select_args, "logThread", null, "ID DESC", null);
		if(cursor.getCount()>0)
		{
			cursor.moveToFirst();
			threadId=cursor.getInt(1);
		}
		return threadId;
	}
	
	public int getOptionsIntData(String name)
	{
		int optionsInt=-1;
		String str1="optionsName LIKE '"+name+"'";		
		Cursor cursor1=mDb.query("options", null, str1, null, null, null, null);
		
		if(cursor1.getCount()>0)
		{
			cursor1.moveToFirst();
			optionsInt = cursor1.getInt(2);
		}else
		{
			initOptionsData();
			String str2="optionsName LIKE '"+name+"'";		
			Cursor cursor2=mDb.query("options", null, str2, null, null, null, null);
			if(cursor2.getCount()>0)
			{
				cursor2.moveToFirst();
				optionsInt = cursor2.getInt(2);
			}				
		}
			return optionsInt;
	}
		
	public void initOptionsData()
	{
		    insertOptionsData("StartType", 1);
		   // insertOptionsData("CallBackTone", 0);
		    insertOptionsData("CallSpamType", 4);
		    insertOptionsData("MessageSpamFilter", 1);
		    insertOptionsData("MessageSpamType", 1);
		    insertOptionsData("Password", "");
		  //  insertOptionsData("GPRSUsage", 0);
		  // insertOptionsData("GPRSTotal", 10000);
		  //  insertOptionsData("BalanceDate", 1);
	}
	
	public long insertOptionsData(String key, int value)
	{
		    ContentValues cv1 = new ContentValues();
		    cv1.put("OptionsName", key);
		    cv1.put("OptionsInt", Integer.valueOf(value));
		    return mDb.insert("options", null, cv1);
    }

   public long insertOptionsData(String key, String value)
    {
		    ContentValues cv2 = new ContentValues();
		    cv2.put("OptionsName", key);
		    cv2.put("OptionsString", value);
		    return mDb.insert("options", null, cv2);
    }
	public boolean isNumberInUserList(String incomingNumber,int type)
	{
		String str1="type=" + type +" AND " + "phone" + "LIKE '%" + incomingNumber + "'";
		int i=0;
		i=mDb.query("userlist", null, str1, null, null, null, null).getCount();
		if(i >0)
		{
			return true;
		}else{
			return false;
		}
			
	}
	
	public void drop(String s) throws Exception{
		  StringBuilder stringbuilder =(new StringBuilder("DROP TABLE IF EXISTS ")).append(s);
		  String s1=stringbuilder.toString();
		  mDb.execSQL(s1);
		  return;
	}
	
	public void reset_table(String s) throws Exception{
		
		StringBuilder stringbuilder1 =(new StringBuilder("Delete From ")).append(s);
		String s2=stringbuilder1.toString();
		mDb.execSQL(s2);
		
		return ;
	}
	
	public Cursor select(boolean flag, String s, String as[], String s1, String as1[], String s2, 
            String s3, String s4, String s5){
		 return mDb.query(flag, s, as, s1 , as1, s2, s3, s4, s5);
	}
	
	public Cursor read(String s){
		
      return mDb.rawQuery(s, null);
  }
	
    public long day(String s)
    {    //一天的流量
     long l;
     Date date;
     l = 0L;
     date = new Date();
     Cursor cursor;
     
     StringBuilder stringbuilder = new StringBuilder();
     stringbuilder.append("select sum(");
     stringbuilder.append(s);
     stringbuilder.append(") from ");
     stringbuilder.append("t_data");
     stringbuilder.append(" where ");
     stringbuilder.append("_date");
     stringbuilder.append("='");
     Object obj = imiUtil.date2String(date, "yyMMdd");
     stringbuilder.append(((String) (obj)));
     stringbuilder.append("'");
     String s1 = stringbuilder.toString();
     cursor = read(s1);
     
     while(cursor.moveToNext()){
         int i=0;
         l=cursor.getLong(i);
     }
        cursor.close();
        return l;
   }
    public long week(String s)
    {  //一周的流量
        long l;
        l = 0L;

        String as[];
        StringBuilder stringbuilder;
        int i;
        int k;

        as = imiUtil.date2Week(new Date(), "yyMMdd");
        stringbuilder = new StringBuilder();
        i = as.length;
        k = 0;

        while(k<i)
       {
        String s4 = as[k];
        stringbuilder.append("'");
        stringbuilder.append(s4);
        stringbuilder.append("',");
        k++;
       }

        Cursor cursor;
        String s1 = stringbuilder.toString();
        int i1 = s1.length() - 1;
        String s2 = s1.substring(0, i1);
        StringBuilder stringbuilder1 = new StringBuilder();
        stringbuilder1.append("select sum(");
        stringbuilder1.append(s);
        stringbuilder1.append(") from ");
        stringbuilder1.append("t_data");
        stringbuilder1.append(" where ");
        stringbuilder1.append("_date");
        stringbuilder1.append(" in (");
        stringbuilder1.append(s2);
        stringbuilder1.append(")");

        String s3 = stringbuilder1.toString();
        cursor = read(s3);

        while(cursor.moveToNext())
       {
         int j = 0;
         l = cursor.getLong(j);
       }

        cursor.close();
        return l;
  
    }
    public long month(String s)
    {    //一个月的流量
        long l;
        Date date;
        l = 0L;
        date = new Date();
        Cursor cursor;
        
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("select sum(");
        stringbuilder.append(s);
        stringbuilder.append(") from ");
        stringbuilder.append("t_data");
        stringbuilder.append(" where ");
        stringbuilder.append("_year");
        stringbuilder.append("='");
        Object obj = imiUtil.date2String(date, "yy");
        stringbuilder.append(((String) (obj)));
        stringbuilder.append("' and ");
        stringbuilder.append("_month");
        stringbuilder.append("='");
        obj =imiUtil.date2String(date, "MM");
        stringbuilder.append((String)(obj));
        stringbuilder.append("'");
        String s1 = stringbuilder.toString();
        cursor = read(s1);
        
        while(cursor.moveToNext()){
            int i=0;
            l=cursor.getLong(i);
        }
           cursor.close();
           return l;
      }
    public long total(String s)
    {    //总共的流量
        long l;
        l = 0L;
        Cursor cursor;
        
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("select sum(");
        stringbuilder.append(s);
        stringbuilder.append(") from ");
        stringbuilder.append("t_data");
       
        String s1 = stringbuilder.toString();
        cursor = read(s1);
        
        while(cursor.moveToNext()){
            int i=0;
            l=cursor.getLong(i);
        }
           cursor.close();
           return l;
     }
	
	public long insert(String s,Map map){
		ContentValues contentvalues;
		contentvalues = new ContentValues();
		
		 Set set = map.keySet();	        
	     Iterator iterator=set.iterator();
	        
	     while(iterator.hasNext()){
			String s1;
			Object obj1;
			s1=(String)iterator.next();
			obj1=map.get(s1);
			boolean flag1=obj1 instanceof Boolean;
			if(flag1)
            {
           	  obj1 = (Boolean)obj1;
              contentvalues.put(s1, ((Boolean) (obj1)));
            }else
            {
           	     boolean flag2 = obj1 instanceof Byte;
                 if(flag2)
                 {
                     obj1 = (Byte)obj1;
                     contentvalues.put(s1, ((Byte) (obj1)));
                 } else
                 {
                     boolean flag3 = obj1 instanceof Float;
                     if(flag3)
                     {
                         obj1 = (Float)obj1;
                         contentvalues.put(s1, ((Float) (obj1)));
                     } else
                     {
                         boolean flag4 = obj1 instanceof Double;
                         if(flag4)
                         {
                             obj1 = (Double)obj1;
                             contentvalues.put(s1, ((Double) (obj1)));
                         } else
                         {
                             boolean flag5 = obj1 instanceof Short;
                             if(flag5)
                             {
                                 obj1 = (Short)obj1;
                                 contentvalues.put(s1, ((Short) (obj1)));
                             } else
                             {
                                 boolean flag6 = obj1 instanceof Long;
                                 if(flag6)
                                 {
                                     obj1 = (Long)obj1;
                                     contentvalues.put(s1, ((Long) (obj1)));
                                 } else
                                 {
                                     boolean flag7 = obj1 instanceof Integer;
                                     if(flag7)
                                     {
                                         obj1 = (Integer)obj1;
                                         contentvalues.put(s1, ((Integer) (obj1)));
                                     } else
                                     {
                                         boolean flag8 = obj1 instanceof String;
                                         if(flag8)
                                         {
                                             obj1 = (String)obj1;
                                             contentvalues.put(s1, ((String) (obj1)));
                                         } else
                                         {
                                             byte abyte0[] = (byte[])obj1;
                                             contentvalues.put(s1, abyte0);
                                         }
                                     }
                                 }
                             }
                         }
                     }
                 }
             }
		}
			 long l=mDb.insert(s, null, contentvalues);
		     return l;
	}
	public int update(String s,Map map ,String as[][]) throws Exception {
		
		    ContentValues contentvalues;
	        contentvalues = new ContentValues();
	        Set set = map.keySet();	        
	        Iterator iterator=set.iterator();
	        
	        while(iterator.hasNext()){
	             String   key = (String) iterator.next();
	             Object value =  map.get(key);
	             boolean flag1 = value instanceof Boolean;
	             if(flag1)
	             {	      
	               value = (Boolean)value;
	               contentvalues.put(key, ((Boolean) (value)));
	             }else
	             {
	                   boolean flag2 = value instanceof Byte;
	                   if(flag2)
	                   {
	                       value = (Byte)value;
	                       contentvalues.put(key, ((Byte) (value)));
	                    } else
	                    {
	                        boolean flag3 = value instanceof Float;
	                        if(flag3)
	                        {
	                           value = (Float)value;
	                           contentvalues.put(key, ((Float) (value)));
	                        } else
	                        {
	                             boolean flag4 = value instanceof Double;
	                             if(flag4)
	                             {
	                                  value = (Double)value;
	                                   contentvalues.put(key, ((Double) (value)));
	                             } else
	                             {
	                                   boolean flag5 = value instanceof Short;
	                                    if(flag5)
	                                    {
	                                         value = (Short)value;
	                                         contentvalues.put(key, ((Short) (value)));
	                                     } else
	                                    {
	                                           boolean flag6 = value instanceof Long;
	                                           if(flag6)
	                                           {
	                                                value = (Long)value;
	                                                contentvalues.put(key, ((Long) (value)));
	                                            } else
	                                            {
	                                                   boolean flag7 = value instanceof Integer;
	                                                   if(flag7)
	                                                   {
	                                                        value = (Integer)value;
	                                                         contentvalues.put(key, ((Integer) (value)));
	                                                    } else
	                                                    {
	                                                              boolean flag8 = value instanceof String;
	                                                              if(flag8)
	                                                              {
	                                                                     value = (String)value;
	                                                                     contentvalues.put(key, ((String) (value)));
	                                                               } else
	                                                               {
	                                                                    byte abyte0[] = (byte[])value;
	                                                                     contentvalues.put(key, abyte0);
	                                                                }
	                                                      }
	                                                }
	                                           }
	                                      }
	                                   }
	                                }
	                         }//END IF
	        }
	        
	        StringBuilder stringbuilder= new StringBuilder();
	        String as1[] = (String[])null;
	        int row;
	        if(as==null)
	        {
	            ContentValues contentvalues1 = contentvalues;
	            row=mDb.update(s, contentvalues1, null, null);
	        }else
	        {
	        	int i=as.length;
	            int k=0;
	            int j=0;
	            as1 = new String[as.length];
	            while(k<i)
	            {
	                String as3[] = as[k];          //获取二维数组
	                String s6 = as3[0];	              
	                stringbuilder.append(s6);
	                
	                String s7 = as3[1];
	                stringbuilder.append(s7);
	                
	                String s8 = "?";
	                stringbuilder.append(s8);

	                String s9 = " ";
	                stringbuilder.append(s9);
	                
	                as1[j] =as3[2];
	                k++;
	                j++;
	            }
	            String s1 = stringbuilder.toString();
	            row=mDb.update(s, contentvalues, s1, as1);//"key=?" as1=wifi_rx
	                       
	        }
	 
	        return row;
	}
	
	public String[] getBase(String s){
        String as[];
        as = (String[])null;
        Cursor cursor;
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("select ");
        stringbuilder.append("_value");
        stringbuilder.append(", ");
        stringbuilder.append("_date");
        stringbuilder.append(" from ");
        stringbuilder.append("t_base");
        stringbuilder.append(" where ");
        stringbuilder.append("_key");
        stringbuilder.append("='");
        stringbuilder.append(s);
        stringbuilder.append("'");
        String s1 = stringbuilder.toString();
        cursor = read(s1);
        while(cursor.moveToNext())
        {
            int i=2;
            as=new String[i];
            String s2= cursor.getString(0);
            as[0]=s2;
            i=1;
            String s3=cursor.getString(1);
            as[i]=s3;       
        }

        cursor.close();
        return as;
    }
	 public long[] getBytes(String s) throws Exception{
	        String s1 = ", ";	       
	        long []databyte=new long[4];
	        Cursor cursor;
	        StringBuilder stringbuilder = new StringBuilder();
	        stringbuilder.append("select ");
	        stringbuilder.append("_rx_mobile");
	        stringbuilder.append(", ");
	        stringbuilder.append("_tx_mobile");
	        stringbuilder.append(", ");
	        stringbuilder.append("_rx_wifi");
	        stringbuilder.append(", ");
	        stringbuilder.append("_tx_wifi");
	        stringbuilder.append(" from ");
	        stringbuilder.append("t_data");
	        stringbuilder.append(" where ");
	        stringbuilder.append("_id");
	        stringbuilder.append("='");
	        stringbuilder.append(s);
	        stringbuilder.append("'");
	        String s2 = stringbuilder.toString();
	        cursor = read(s2);
	        boolean flag=cursor.moveToNext();  //执行SQL语句后的Cursor指针为BOF,需要执行 moveToNext获取第一条记录
	        if(flag)
	        {
	          
	           long l0 =cursor.getLong(0);
	           databyte[0]=l0;

	           long l1 =cursor.getLong(1);
	           databyte[1]=l1;

	           long l2=cursor.getLong(2);
	           databyte[2]=l2;
	            
	           long l3=cursor.getLong(3);
	           databyte[3]=l3;
	        }else
	        {
	        	databyte=null;
	        }
	        
	        cursor.close();
	        return ((long []) (databyte));
	    }
	
	public void close(){   //关闭数据库的操作
		mDb.close();
	}
    /*数据表的插入操作*/
	public long AddData(String tableName,Commons.PDATA pData){
		ContentValues contentvalue = new ContentValues();	
		if(tableName==DATABASE_TABLE_USERLOG){                                //防骚扰日志记录表
			
			contentvalue.put(KEY_NAME, pData.mStringValue_1);                 //联系人名字
			contentvalue.put(KEY_PHONE,pData.mStringValue_2);                 //联系人电话
			contentvalue.put(KEY_DATE, Integer.valueOf(pData.mIntValue_1));   //时间
			contentvalue.put("content", "");
			contentvalue.put("logstate", 0);
			mDb.insert(DATABASE_TABLE_USERLOG, null, contentvalue);
		}
		if(tableName==DATABASE_TABLE_USERLIST){
		   contentvalue.put(KEY_NAME, pData.mStringValue_1);
		   contentvalue.put(KEY_PHONE,pData.mStringValue_2);
		   contentvalue.put(KEY_TYPE, Integer.valueOf(pData.mIntValue_1));
		   mDb.insert(DATABASE_TABLE_USERLIST, null, contentvalue);
	    }
		return 1;
		
	}

	
    //数据库查询操作
	public Cursor Query(String tableName, String[] col) {
		// TODO Auto-generated method stub
		Cursor mCursor = mDb.query(tableName, col, null, null, null, null, null);
		return mCursor;
	}
	
	//获取数据库中的数据
	
    public long getData(String tableName,ArrayList data,String column,int value)
    {
    	Cursor cursor=null;
    	
    	int i = column.length();
    	if(i>0)
    	{
    		String selectArgs=String.valueOf(column) + "=" + value;
    		cursor=mDb.query(tableName, null, selectArgs, null, null, null, null);
    		if(cursor.getCount()>0)
    		{
    			cursor.moveToFirst();
    			while(cursor.moveToNext())
    			{
    				Commons.PDATA pData = new Commons.PDATA();
    				pData.mId  = cursor.getInt(0);
    				setDataDetail(tableName,cursor, pData);
    				data.add(pData);
    			}
    		}
    	}
    	
    	
    	return 0;
    }
    
    
    private long setDataDetail(String tableName,Cursor cursor, Commons.PDATA pData)
    {
    	
    	if(tableName=="UserList")
    	{
    		String name = cursor.getString(1);
    		pData.mStringValue_1 = name;
    		
    		String phone = cursor.getString(2);
    		pData.mStringValue_2 = phone;
    		
    		int type  = cursor.getInt(3);
    		pData.mIntValue_1 = type;
    		
    		int message= cursor.getInt(4);
    		pData.mIntValue_2=message;
    		
    		int call = cursor.getInt(5);
    		pData.mIntValue_3=call;
    		
    	}
    	
    	
    	
    	return 1L;
    }
    
	//修改属性表的值
	public void updateOptionsData(String optionsName, int data)
	{
		String[] modify_option = new String[1];
		modify_option[0] = optionsName;
		
		ContentValues cv = new ContentValues();
	    cv.put("optionsInt", Integer.valueOf(data));
	    
	    mDb.update("options", cv, "optionsName=?", modify_option);
	}
	
	public void updateOptionsData(String optionsName, String data)
	{
		String[] modify_option = new String[1];
		modify_option[0] = optionsName;
		
		ContentValues cv = new ContentValues();
	    cv.put("OptionsString", data);
	    
	    mDb.update("options", cv, "optionsName=?", modify_option);
	}
}