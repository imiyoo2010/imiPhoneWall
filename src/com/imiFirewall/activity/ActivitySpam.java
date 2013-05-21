package com.imiFirewall.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imiFirewall.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ActivitySpam extends ListActivity {

  public final static String AD_TITLE="title";
  public final static String AD_DESC ="desc";
  public final static String AD_IMG="img";
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // I know, I know, this should go into strings.xml and accessed using
    // getString(R.string....)
   
    List<Map<String, Object>> value= new ArrayList<Map<String,Object>>();
    
    Map<String,Object> item1=new HashMap<String,Object>();
  
    item1.put(AD_TITLE, "免扰设置");
    item1.put(AD_DESC,"用户通过设置相关的功能，可以减少吸费电话和垃圾短信的骚扰");
    item1.put(AD_IMG, R.drawable.cmd);
    value.add(item1);
    
    
    Map<String,Object> item2=new HashMap<String,Object>();
    
    item2.put(AD_TITLE, "黑白名单");
    item2.put(AD_DESC,"通过设置黑白名单，对特定的号码进行有效的拦截。");
    item2.put(AD_IMG, R.drawable.cmd);
    value.add(item2);
    
        
    
    setListAdapter(new SimpleAdapter(this, value, R.layout.advancelistitem,
        new String[] {AD_IMG,AD_TITLE, AD_DESC }, new int[] {R.id.advance_cmd_img,R.id.advance_cmd_title, R.id.advance_cmd_desc }));
  }
 
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    switch (position) {
    case 0:
         Intent intent0 = new Intent(this, ActivitySpamOptions.class);
         startActivity(intent0);
         break;
         
    case 1:
        Intent intent1 = new Intent(this, ActivitySpamList.class);
        startActivity(intent1);
        break;
         
    }
  }
}