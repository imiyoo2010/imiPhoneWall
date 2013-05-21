package com.imiFirewall;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter
{

    private String args[][];
    private Context context;
    
    public GridViewAdapter(Context context1)
    {
        String as[][] = new String[1][2];
        String s[] = new String[2];
        s[0] = "";
        String s1 = Integer.toString(-1);
        s[1] = s1;
        as[0] = s;
        args = as;
        context = context1;
    }

    public int getCount()
    {
        return args.length;
    }

    public Object getItem(int i)
    {
        return Integer.valueOf(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        Context context1 = context;
        TextView textview = new TextView(context1);
        textview.setGravity(17);
        String s = args[i][0];
        textview.setText(s);
        int j = Integer.parseInt(args[i][1]);
        textview.setTextColor(j);
        return textview;
    }

    public boolean isEnabled(int i)
    {
        return true;
    }

    public void setValue(String as[][])
    {
        args = as;
    }
}
