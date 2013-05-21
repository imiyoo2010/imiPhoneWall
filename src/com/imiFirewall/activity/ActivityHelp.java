package com.imiFirewall.activity;

import com.imiFirewall.R;
import com.imiFirewall.imiApi;
import com.imiFirewall.R.drawable;
import com.imiFirewall.R.layout;
import com.imiFirewall.R.string;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

public class ActivityHelp extends AlertDialog{

	public ActivityHelp(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		final View view = getLayoutInflater().inflate(R.layout.help_dialog, null);
		setButton(context.getText(R.string.close), (OnClickListener)null);
		setIcon(R.drawable.help_icon);
		setTitle("imiFireWall v" + imiApi.VERSION);
		setView(view);
	}
   
}