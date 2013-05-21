package com.imiFirewall.activity;

import java.util.Arrays;
import java.util.Comparator;
import java.lang.String;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;  //单选框事件
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.imiFirewall.R;
import com.imiFirewall.imiApi;
import com.imiFirewall.R.drawable;
import com.imiFirewall.R.id;
import com.imiFirewall.R.layout;
import com.imiFirewall.R.string;
import com.imiFirewall.imiApi.DroidApp;

public class ActivityNetwork extends Activity implements OnCheckedChangeListener,OnClickListener {
	
	
	private ListView listview;                     //列表框
	private ProgressDialog progress = null;        //进度条
	private ImageView iv;
	private TextView firewall_on;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPreferences();
        setContentView(R.layout.network_layout);
        this.findViewById(R.id.label_mode).setOnClickListener(this);
        final boolean enabled=imiApi.isEnabled(this);
    	if(enabled){
    		//applyOrSaveRules();
    		setTitle(R.string.title_enabled);
    		iv=(ImageView)findViewById(R.id.menu_network_on);
    		iv.setImageDrawable(getResources().getDrawable(R.drawable.menu_unlock));
    		
    		firewall_on =(TextView)findViewById(R.id.menu_text_on);
   		    firewall_on.setText("On");
    	}else{
    		setTitle(R.string.title_disabled);
    		iv=(ImageView)findViewById(R.id.menu_network_on);
    		iv.setImageDrawable(getResources().getDrawable(R.drawable.menu_lock));
    		 
    		firewall_on =(TextView)findViewById(R.id.menu_text_on);
   		    firewall_on.setText("Off");
    	}
    }

    
    @Override
    public void onResume(){
    	super.onResume();
    	if(this.listview==null){
    		this.listview =(ListView) this.findViewById(R.id.listview);
    	}
    	refreshHeader();
    	showOrLoadApplications();
    	/*
    	final DroidApp[] apps = imiApi.getApps(this);
    	ListAdapter adapter = new ArrayAdapter<DroidApp>(this,R.layout.listitem,R.id.itemtext,apps);
    	listview.setAdapter(adapter);
    	*/
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	this.listview.setAdapter(null);
    }
    
    private void checkPreferences() {
    	final SharedPreferences prefs = getSharedPreferences(imiApi.PREFS_NAME, 0);
    	final Editor editor = prefs.edit();
    	boolean changed = false;
    	if (prefs.getString(imiApi.PREF_MODE, "").length() == 0) {
    		editor.putString(imiApi.PREF_MODE, imiApi.MODE_WHITELIST);
    		changed = true;
    	}
    	/* delete the old preference names */
    	if (prefs.contains("AllowedUids")) {
    		editor.remove("AllowedUids");
    		changed = true;
    	}
    	if (prefs.contains("Interfaces")) {
    		editor.remove("Interfaces");
    		changed = true;
    	}
    	if (changed) editor.commit();
    }
    private void refreshHeader() {
    	final SharedPreferences prefs = getSharedPreferences(imiApi.PREFS_NAME, 0);
    	final String mode = prefs.getString(imiApi.PREF_MODE, imiApi.MODE_WHITELIST);
		final TextView labelmode = (TextView) this.findViewById(R.id.label_mode);
		if (mode.equals(imiApi.MODE_WHITELIST)) {
			labelmode.setText("Mode: White list (allow selected)");
		} else {
			labelmode.setText("Mode: Black list (block selected)");
		}
		setTitle(imiApi.isEnabled(this) ? R.string.title_enabled : R.string.title_disabled);
    }
    
    private void selectMode() {
    	new AlertDialog.Builder(this).setItems(new String[]{"White list (allow selected)","Black list (block selected)"}, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				final String mode = (which==0 ? imiApi.MODE_WHITELIST : imiApi.MODE_BLACKLIST);
				final Editor editor = getSharedPreferences(imiApi.PREFS_NAME, 0).edit();
				editor.putString(imiApi.PREF_MODE, mode);
				editor.commit();
				refreshHeader();
			}
    	}).setTitle("Select mode:")
    	.show();
    }

    /**
	 * If the applications are cached, just show them, otherwise load and show
	 */
	private void showOrLoadApplications() {
    	if (imiApi.applications == null) {
    		// The applications are not cached.. so lets display the progress dialog
    		progress = ProgressDialog.show(this, "Working...", "Reading installed applications", true);
        	final Handler handler = new Handler() {
        		public void handleMessage(Message msg) {
        			if (progress != null) progress.dismiss();
        			showApplications();
        		}
        	};
        	new Thread() {
        		public void run() {
        			imiApi.getApps(ActivityNetwork.this);
        			handler.sendEmptyMessage(0);
        		}
        	}.start();
    	} else {
    		// the applications are cached, just show the list
        	showApplications();
    	}
	}
    
	 /**
     * Show the list of applications
     */
    private void showApplications() {
        final DroidApp[] apps = imiApi.getApps(this);
        // Sort applications - selected first, then alphabetically
        Arrays.sort(apps, new Comparator<DroidApp>() {
			@Override
			public int compare(DroidApp o1, DroidApp o2) {
				if ((o1.selected_wifi|o1.selected_3g) == (o2.selected_wifi|o2.selected_3g)) {
					return o1.names[0].compareTo(o2.names[0]);
				}
				if (o1.selected_wifi || o1.selected_3g) return -1;
				return 1;
			}
        });
        final LayoutInflater inflater = getLayoutInflater();
		final ListAdapter adapter = new ArrayAdapter<DroidApp>(this,R.layout.listitem,R.id.itemtext,apps) {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
       			ListEntry entry;
        		if (convertView == null) {
        			// Inflate a new view
        			convertView = inflater.inflate(R.layout.listitem, parent, false);
       				entry = new ListEntry();
       				entry.box_wifi = (CheckBox) convertView.findViewById(R.id.itemcheck_wifi);
       				entry.box_3g = (CheckBox) convertView.findViewById(R.id.itemcheck_3g);
       				entry.text = (TextView) convertView.findViewById(R.id.itemtext);
       				convertView.setTag(entry);
       				entry.box_wifi.setOnCheckedChangeListener(ActivityNetwork.this);
       				entry.box_3g.setOnCheckedChangeListener(ActivityNetwork.this);
        		} else {
        			// Convert an existing view
        			entry = (ListEntry) convertView.getTag();
        		}
        		final DroidApp app = apps[position];
        		entry.text.setText(app.toString());
        		final CheckBox box_wifi = entry.box_wifi;
        		box_wifi.setTag(app);
        		box_wifi.setChecked(app.selected_wifi);
        		final CheckBox box_3g = entry.box_3g;
        		box_3g.setTag(app);
        		box_3g.setChecked(app.selected_3g);
       			return convertView;
        	}
        };
        this.listview.setAdapter(adapter);
    }
    public void disableorenable(){
    	final boolean enabled=!imiApi.isEnabled(this);
    	imiApi.setEnabled(this, enabled);
    	if(enabled){
    		//applyOrSaveRules();
    		setTitle(R.string.title_enabled);
    		iv=(ImageView)findViewById(R.id.menu_network_on);
    		iv.setImageDrawable(getResources().getDrawable(R.drawable.menu_unlock));
    		
    		firewall_on =(TextView)findViewById(R.id.menu_text_on);
   		    firewall_on.setText("On");
    	}else{
    		setTitle(R.string.title_disabled);
    		iv=(ImageView)findViewById(R.id.menu_network_on);
    		iv.setImageDrawable(getResources().getDrawable(R.drawable.menu_lock));
    		 
    		firewall_on =(TextView)findViewById(R.id.menu_text_on);
   		    firewall_on.setText("Off");
    	}
    }
    public void onClickEventBtn_on(View v){
    	   disableorenable();
    }
     
    public void onClickEventBtn_user(View v){
    	if(!imiApi.hasRootAccess(this, true))
    		return ;
    }
    
    public void onClickEventBtn_apply(View v){
    	//线程消息传递的异步处理
    	final Handler handler;
    	final boolean enabled = imiApi.isEnabled(this);
    	progress=ProgressDialog.show(this,"Working...",(enabled?"Applying":"Saving")+"the Firewall rules", true);
    	handler = new Handler() {
    		public void handleMessage(Message msg){
    			if(progress!=null) progress.dismiss();
    			if (enabled) {
    				if (!imiApi.hasRootAccess(ActivityNetwork.this, true)) return;
					if (imiApi.applyIptablesRules(ActivityNetwork.this, true)) {
						Toast.makeText(ActivityNetwork.this, "Rules applied with success", Toast.LENGTH_SHORT).show();
					}
				} else {
					imiApi.saveRules(ActivityNetwork.this);
					Toast.makeText(ActivityNetwork.this, "Rules saved with success", Toast.LENGTH_SHORT).show();
				}
    		}
    	};
    	handler.sendEmptyMessageDelayed(0, 200);
    }
    
    public void onClickEventBtn_purge(View v){
    	final Handler handler;
		progress = ProgressDialog.show(this, "Working...", "Deleting Firewall rules.", true);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (progress != null) progress.dismiss();
				if (!imiApi.hasRootAccess(ActivityNetwork.this, true)) return;
				if (imiApi.purgeIptables(ActivityNetwork.this, true)) {
					Toast.makeText(ActivityNetwork.this, "Rules purged with success", Toast.LENGTH_SHORT).show();
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 200);
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		final DroidApp app = (DroidApp) buttonView.getTag();
		if (app != null) {
			switch (buttonView.getId()) {
				case R.id.itemcheck_wifi: app.selected_wifi = isChecked; break;
				case R.id.itemcheck_3g: app.selected_3g = isChecked; break;
			}
		}
	}
    
    private static class ListEntry {
		private CheckBox box_wifi;
		private CheckBox box_3g;
		private TextView text;
	}
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.label_mode:
			selectMode();
			break;
		}
	}
}