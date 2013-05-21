package com.imiFirewall.activity.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.imiFirewall.PROEntity;
import com.imiFirewall.R;
import com.imiFirewall.common.Commons;
import com.imiFirewall.common.ProgressUtils;
import com.imiFirewall.util.imiProcess;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class ActivityProcess extends Activity
{
	
	public Handler handler;
	public ListView mList;
	public ProcessAdapter adapter;
	public ActivityManager mActivityMgr;
	public int mPosition;
	public ProgressUtils mProgress;
	public ProgressThread mProgressThread;

	
	public ActivityProcess(){
		handler = new ProcessHanlder();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_layout);
		
		if(savedInstanceState!=null)
		{
			int i = savedInstanceState.getInt("last_position", 0);
			mPosition=i;
		}
		
		mList = (ListView) findViewById(R.id.process_listview);
		mList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				mPosition=arg2;
				createOperationDialog().show();
			}
			
		});
		
		mActivityMgr = (ActivityManager)getSystemService("activity");
		
		ViewStub stub = (ViewStub) findViewById(R.id.pro_viewstubid);
		mProgress = new ProgressUtils(this,stub);
				
	}
  
	private Dialog createClearConfirmDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("test");
		
		return builder.create();
	}
	
	private Dialog createOperationDialog()
	{
		AlertDialog.Builder op_dialog = new AlertDialog.Builder(this);
		
		String[] op_string = new String[3];
		op_string[0]= getString(R.string.process_op1);   //打开进程
		op_string[1]= getString(R.string.process_op2);   //关闭进程 
		op_string[2]= getString(R.string.process_op3);   //详细信息 
		
		op_dialog.setTitle(getString(R.string.process_dialog_title));
		
		DialogInterface.OnClickListener opitemlistener = new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int pos;
				pos=mPosition;
				switch(which)
				{
				case 0:
					launchProcess(pos);
					break;
				case 1:
					closeProcess(pos);
				    break;
				case 2:
					showProcessDetail(pos);
					break;
				}
				
			}
					
		};
		
		op_dialog.setItems(op_string,opitemlistener);
					
		return op_dialog.create();
	}
	
	private void launchProcess(int pos) //打开进程
	{
		String app=((PROEntity)mList.getItemAtPosition(pos)).getProcessName();		
		Intent launch_intent=getPackageManager().getLaunchIntentForPackage(app);
		if(launch_intent!=null)
		{
			startActivity(launch_intent);
		}
	}
	
	private void closeProcess(int pos) //结束进程 
	{
		String app=((PROEntity)mList.getItemAtPosition(pos)).getProcessName();
		ActivityManager am = this.mActivityMgr;
		imiProcess.killByLevel7(am,app);		
		((ProcessAdapter)mList.getAdapter()).removeContent(pos);
	}
	
	
	
	private void showProcessDetail(int pos) //显示进程详细信息
	{
	   String app=((PROEntity)mList.getItemAtPosition(pos)).getProcessName();
	   Intent detail =  Commons.getPackageDetailIntent(app); 	
	   startActivity(detail);
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		mProgress.showSearchPanel();
		mProgressThread = new ProgressThread(handler);
		mProgressThread.start();
		
	}
		
	public PROEntity BuildProcessEntity(ActivityManager.RunningAppProcessInfo runningprocessInfo) throws NameNotFoundException
	{
		if(runningprocessInfo!=null)
		{			
	     ApplicationInfo applicationinfo=null;
	     PackageManager packagemanager = getPackageManager();
		
	     PROEntity process = new PROEntity();
	     
		 applicationinfo= packagemanager.getPackageInfo(runningprocessInfo.processName, 1).applicationInfo;
			    
		  if(applicationinfo!=null)
		  {
		   process.setTitle(applicationinfo.loadLabel(packagemanager).toString());
		   process.setPid(runningprocessInfo.pid);
		   process.setProcessName(runningprocessInfo.processName);
		   process.setId(runningprocessInfo.pid);
		   process.setMemory(123);
		   process.setSystem(false);
		  }
		   return process;
	    }
		
		 return (PROEntity)null;
	}
	
	class ProgressThread extends Thread
	{
		private Handler mHandler;
		
		public ProgressThread(Handler handler){
			mHandler=handler;
		}
		
		private void initAdapter()
		{
			List pList = mActivityMgr.getRunningAppProcesses();
			
			ArrayList<PROEntity> mProcessArray =new ArrayList<PROEntity>();
			
			Iterator it = pList.iterator();
			
			while(it.hasNext())
			{
				PROEntity processentity=null;
				try {
					ActivityManager.RunningAppProcessInfo runningappprocessinfo = (ActivityManager.RunningAppProcessInfo)it.next();
					processentity = BuildProcessEntity(runningappprocessinfo);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(processentity!=null)
				{
					mProcessArray.add(processentity);
					Message msg = mHandler.obtainMessage();			
					Bundle bundle = new Bundle();		
					  
					String processname = processentity.getProcessName();
			        bundle.putString("content", processname);
			        msg.setData(bundle);
			     	msg.what=0;					
				    Handler handler1 = mHandler;
			     	handler1.sendMessage(msg);                  //异步通讯
				}
			   
			 
		    }
		
			adapter = new ProcessAdapter(ActivityProcess.this,mProcessArray);
			Message msg1 = mHandler.obtainMessage();
			msg1.what=2;		
			Handler handler2 = mHandler;
			handler2.sendMessage(msg1);  
			
		}
		
		public void run()
		{
			initAdapter();
		}	
	}
	
	
	class ProcessHanlder extends Handler                 //利用Handler进行异步通讯处理
	{
		public void handleMessage(Message msg)
		{
			if(msg.what==0)
			{
				Bundle bundle = msg.getData();
				if(bundle.containsKey("content"))
				{
				   //完成 进度条的相关操作
					String s1 = bundle.getString("content");
					mProgress.changeSearchPanelTitle(s1);
				}
			}
			if(msg.what==2)
			{
				//完成列表控件的显示操作
				
				 ProcessAdapter processadapter = adapter;								
			     mList.setAdapter(processadapter);
			     mProgress.hideSearchPanel();
			     
			}
		}
	}
	
	
	class ProcessAdapter extends BaseAdapter{
				
		private List mContent;
		private LayoutInflater mInflater;
		private PackageManager mPackageMgr;
				
		private ProcessAdapter(Context context, List applist){
			
			mContent   =  applist;
			mInflater  = (LayoutInflater)LayoutInflater.from(context);
			mPackageMgr= (PackageManager) context.getPackageManager();
			
		}		
		
		private class ViewHolder{
			TextView appName;
			ImageView appIcon;
			TextView appSize;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mContent.size();
		}

		@Override
		public PROEntity getItem(int position) {
			// TODO Auto-generated method stub
			return (PROEntity)mContent.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder itemView;
			
			if(convertView==null)
			{
				convertView = mInflater.inflate(R.layout.process_list_item, null);
				itemView = new ViewHolder();
				
				itemView.appIcon=(ImageView)convertView.findViewById(R.id.process_app_icon);
				itemView.appName=(TextView)convertView.findViewById(R.id.process_app_name);
				itemView.appSize=(TextView)convertView.findViewById(R.id.process_app_size);
				
				convertView.setTag(itemView);
			}else{
				itemView= (ViewHolder)convertView.getTag();
			}
			
			PROEntity proentity =getItem(position);
			String processname = proentity.getProcessName();
			Drawable drawable=null;
			
			try {
				drawable =mPackageMgr.getApplicationIcon(processname);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			itemView.appIcon.setImageDrawable(drawable);
			
			String  processtitle = proentity.getTitle();			
			itemView.appName.setText(processtitle);
			
			long processsize =proentity.getMemory();
			itemView.appSize.setText(String.valueOf(processsize));
			
			
			return convertView;
		}
		
		public void removeContent(int position){
			mContent.remove(position);
			notifyDataSetChanged();
		}
	}
}