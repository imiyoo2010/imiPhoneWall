package com.imiFirewall.activity;


import java.math.BigDecimal;
import java.math.RoundingMode;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.chart.PointStyle;

import com.imiFirewall.GridViewAdapter;
import com.imiFirewall.R;
import com.imiFirewall.imiDataset;
import com.imiFirewall.imiNetdevice;
import com.imiFirewall.R.drawable;
import com.imiFirewall.R.id;
import com.imiFirewall.R.layout;
import com.imiFirewall.R.string;
import com.imiFirewall.util.imiUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.app.TabActivity;


public class ActivityNetcount extends TabActivity{
	
	private TabHost tabHost;
	private Runnable runnable;
	private Handler handler;
	
   private LinearLayout mobilelinearlayout;
   private LinearLayout wifilinearlayout;
   
   private GridViewAdapter mobileGridViewAdapter;
   private GridViewAdapter wifiGridViewAdapter;
   
   private GridView mobileGridView;
   private GridView wifiGridView;
   
   private long init_data_mobile[];
   private long init_data_wifi[];
   private long last_data_mobile[];
   private long last_data_wifi[];
   
   private imiDataset all_ds_mobile;
   private imiDataset all_ds_wifi;
	
   private String device_mobile[]; //2G/3G流量统计的目录
   private String device_wifi[];  //wifi流量统计的目录
   
   double pic_mobile_last=0;
   double pic_wifi_last=0;
   
	public ActivityNetcount(){
		
		long al0[]=null;
		init_data_mobile=al0;
		
		long al1[]=null;
		init_data_wifi  =al1;
		
		long al2[]=null;
		last_data_mobile=al2;
		
		long al3[]=null;
		last_data_wifi  =al3;
		
		String as[]=new String[2];
		device_mobile=as;
		
		String as1[]=new String[2];
		device_wifi =as1;
		
		imiDataset dataset= new imiDataset();
		all_ds_mobile=dataset;
		
		imiDataset dataset1= new imiDataset();
		all_ds_wifi=dataset1;
		
		Handler handler1 = new Handler();
		handler =handler1;
		RenderData renderdata = new RenderData();
		runnable = renderdata;
	}
	@Override
	public void onCreate(Bundle bundle){
		
		int i=1;	
		super.onCreate(bundle);		
		Bundle bundle1 = getIntent().getExtras();
		init_data_mobile =(long []) bundle1.getLongArray("init_data_mobile");
		init_data_wifi   =(long [])bundle1.getLongArray("init_data_wifi");
		last_data_mobile =(long [])bundle1.getLongArray("last_data_mobile");
        last_data_wifi   =(long [])bundle1.getLongArray("last_data_wifi");
        device_mobile    =(String [])bundle1.getStringArray("device_mobile");
        device_wifi      =(String [])bundle1.getStringArray("device_wifi");
        
        int k=bundle1.getInt("tab_type");
        int i1=0;
		tabHost=getTabHost();	
		LayoutInflater layoutinflater =LayoutInflater.from(this);
		layoutinflater.inflate(R.layout.mobile, ((android.view.ViewGroup)(tabHost.getTabContentView())));	    
	    layoutinflater.inflate(R.layout.wifi, ((android.view.ViewGroup)(tabHost.getTabContentView())));
	    
	    android.widget.TabHost.TabSpec tabspec=tabHost.newTabSpec("mobile");
	    tabspec.setContent(R.id.mobileTab);
	    tabspec.setIndicator((CharSequence)(getString(R.string.tab_mobile)), (android.graphics.drawable.Drawable)(getResources().getDrawable(R.drawable.mobile)));
	    tabHost.addTab(tabspec);
	   
	    android.widget.TabHost.TabSpec tabspec1 =tabHost.newTabSpec("wifi");
	    tabspec1.setContent(R.id.wifiTab);  
	    tabspec1.setIndicator(((CharSequence)(getString(R.string.tab_wifi))),((android.graphics.drawable.Drawable)(getResources().getDrawable(R.drawable.wifi))));
	    tabHost.addTab(tabspec1);
	    
	    if(k==i)
	    {
	    	i1=0;//tab开关
	    }
	    else
	    {
	       i1=1;
	    }
	    GridView gridview1;
	    GridView gridview2;
	    
	    GridViewAdapter gridviewadapter1;
	    GridViewAdapter gridviewadapter2;
	  
	    LinearLayout linearlayout1;
	    LinearLayout linearlayout2;
	    
	    tabHost.setCurrentTab(i1);
	    gridview1 =(GridView) findViewById(R.id.mobileGridView);
	    mobileGridView=((GridView)(gridview1));
	    gridview2 =(GridView) findViewById(R.id.wifiGridView);
	    wifiGridView=((GridView)(gridview2));
	    
	    gridviewadapter1 = new GridViewAdapter(this);
	    mobileGridViewAdapter=((GridViewAdapter)(gridviewadapter1));
	    
	    gridviewadapter2 = new GridViewAdapter(this);
	    wifiGridViewAdapter =((GridViewAdapter)(gridviewadapter2));
	    
	    linearlayout1=(LinearLayout)findViewById(R.id.mobileLinearLayout);
	    mobilelinearlayout=linearlayout1;
	    linearlayout2=(LinearLayout)findViewById(R.id.wifiLinearLayout);
	    wifilinearlayout=linearlayout2;
	    	        
	    Handler handler1;
	    Runnable runnable1;
	    	    
	    handler1=handler;
	    runnable1=runnable;
	    
	    handler1.post(runnable1);
	}
		
	public void finish()
	{    
		init_data_mobile = null;
        init_data_wifi = null;
        last_data_mobile = null;
        last_data_wifi = null;
        device_mobile = null;
        device_wifi = null;
        all_ds_mobile.clear();
        all_ds_mobile = null;
        all_ds_wifi.clear();
        all_ds_wifi = null;
        mobilelinearlayout.removeAllViews();
        wifilinearlayout.removeAllViews();
        Handler handler1 = handler;
        Runnable runnable1 = runnable;
        handler1.removeCallbacks(runnable1);
        super.finish();
		
	}
	
	private class RenderData implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			XYMultipleSeriesRenderer xymultipleseriesrenderer;
			XYMultipleSeriesDataset xymultipleseriesdataset;
			XYSeriesRenderer xyseriesrenderer;
			XYSeries xyseries;
			
			
			long l0=imiNetdevice.rx_bytes(device_mobile[0]);
			long l1=imiNetdevice.tx_bytes(device_mobile[0]);
			long l2 = last_data_mobile[0];
	            
	        long l5=0L;
	        if(l0>=l2)
	        {	               
	                l5 = l0 - l2;	              	          
	        }
	       long l3 = last_data_mobile[1];	           
	       long l6=0L;
	       if(l1>=l3)
	       {	              
	             l6 = l1 - l3;	                             	             
	       }
	            //mobile GridView数据处理
	            String as[][] = new String[20][2];
	            String s[] = new String[2];
	            s[0] = "";
	            String s1 = Integer.toString(65535);
	            s[1] = s1;
	            as[0] = s;
	            String s2[] = new String[2];
	            String s3 = getString(R.string.upload_title);
	            s2[0] = s3;
	            String s4 = Integer.toString(0xff00ff00);
	            s2[1] = s4;
	            as[1] = s2;
	            String s5[] = new String[2];
	            String s6 = getString(R.string.download_title);
	            s5[0] = s6;
	            String s7 = Integer.toString(0xff00ff00);
	            s5[1] = s7;
	            as[2] = s5;
	            String s8[] = new String[2];
	            String s9 = getString(R.string.all_title);
	            s8[0] = s9;
	            String s10 = Integer.toString(0xff00ff00);
	            s8[1] = s10;
	            as[3] = s8;
	            String s11[] = new String[2];
	            String s12 = getString(R.string.day_title);
	            s11[0] = s12;
	            String s13 = Integer.toString(0xff00ff00);
	            s11[1] = s13;
	            as[4] = s11;
	            String s14[] = new String[2];
	            String s15 = imiUtil.formatByte(init_data_mobile[0] + l6);
	            s14[0] = s15;
	            String s16 = Integer.toString(0xff00ff00);
	            s14[1] = s16;
	            as[5] = s14;
	            String s17[] = new String[2];
	            String s18 = imiUtil.formatByte(init_data_mobile[1] + l5);
	            s17[0] = s18;
	            String s19 = Integer.toString(0xff00ff00);
	            s17[1] = s19;
	            as[6] = s17;
	            String s20[] = new String[2];
	            String s21 = imiUtil.formatByte(init_data_mobile[2] + l6 + l5);
	            s20[0] = s21;
	            String s22 = Integer.toString(0xff00ff00);
	            s20[1] = s22;
	            as[7] = s20;
	            String s23[] = new String[2];
	            String s24 = getString(R.string.week_title);
	            s23[0] = s24;
	            String s25 = Integer.toString(0xff00ff00);
	            s23[1] = s25;
	            as[8] = s23;
	            String s26[] = new String[2];
	            String s27 = imiUtil.formatByte(init_data_mobile[3] + l6);
	            s26[0] = s27;
	            String s28 = Integer.toString(0xff00ff00);
	            s26[1] = s28;
	            as[9] = s26;
	            String s29[] = new String[2];
	            String s30 = imiUtil.formatByte(init_data_mobile[4] + l5);
	            s29[0] = s30;
	            String s31 = Integer.toString(0xff00ff00);
	            s29[1] = s31;
	            as[10] = s29;
	            String s32[] = new String[2];
	            String s33 = imiUtil.formatByte(init_data_mobile[5] + l6 + l5);
	            s32[0] = s33;
	            String s34 = Integer.toString(0xff00ff00);
	            s32[1] = s34;
	            as[11] = s32;
	            String s35[] = new String[2];
	            String s36 = getString(R.string.month_title);
	            s35[0] = s36;
	            String s37 = Integer.toString(0xff00ff00);
	            s35[1] = s37;
	            as[12] = s35;
	            String s38[] = new String[2];
	            String s39 = imiUtil.formatByte(init_data_mobile[6] + l6);
	            s38[0] = s39;
	            String s40 = Integer.toString(0xff00ff00);
	            s38[1] = s40;
	            as[13] = s38;
	            String s41[] = new String[2];
	            String s42 = imiUtil.formatByte(init_data_mobile[7] + l5);
	            s41[0] = s42;
	            String s43 = Integer.toString(0xff00ff00);
	            s41[1] = s43;
	            as[14] = s41;
	            String s44[] = new String[2];
	            String s45 = imiUtil.formatByte(init_data_mobile[8] + l6 + l5);
	            s44[0] = s45;
	            String s46 = Integer.toString(0xff00ff00);
	            s44[1] = s46;
	            as[15] = s44;
	            String s47[] = new String[2];
	            String s48 = getString(R.string.total_title);
	            s47[0] = s48;
	            String s49 = Integer.toString(0xff00ff00);
	            s47[1] = s49;
	            as[16] = s47;
	            String s50[] = new String[2];
	            String s51 = imiUtil.formatByte(init_data_mobile[9] + l6);
	            s50[0] = s51;
	            String s52 = Integer.toString(0xff00ff00);
	            s50[1] = s52;
	            as[17] = s50;
	            String s53[] = new String[2];
	            String s54 = imiUtil.formatByte(init_data_mobile[10] + l5);
	            s53[0] = s54;
	            String s55 = Integer.toString(0xff00ff00);
	            s53[1] = s55;
	            as[18] = s53;
	            String s56[] = new String[2];
	            String s57 = imiUtil.formatByte(init_data_mobile[11] + l6 + l5);
	            s56[0] = s57;
	            String s58 = Integer.toString(0xff00ff00);
	            s56[1] = s58;
	            as[19] = s56;
	            
	            GridViewAdapter gridviewadapter = mobileGridViewAdapter;
	            gridviewadapter.setValue(as);
	            GridViewAdapter gridviewadapter1= mobileGridViewAdapter;
	            mobileGridView.setAdapter(gridviewadapter1);
		    	/*mobilegridview*/
	        
	       //图形点的数据
	        double d1=l6+l5;
	        double net_v0=d1-pic_mobile_last;
	        pic_mobile_last=d1;
	        all_ds_mobile.put(net_v0);	        
	        double d2 = net_v0;
            BigDecimal bigdecimal = new BigDecimal(1024);
            RoundingMode roundingmode = RoundingMode.CEILING;
            double d3 = (new BigDecimal(d2)).divide(bigdecimal, 2, roundingmode).doubleValue();
            mobilelinearlayout.removeAllViews();
	        
			xymultipleseriesrenderer = new XYMultipleSeriesRenderer();
			xymultipleseriesdataset =  new XYMultipleSeriesDataset();
			xyseriesrenderer        =  new XYSeriesRenderer();
			LinearLayout linearlayout = mobilelinearlayout;
			
			String s59=String.valueOf(d3);
			String s60=(new StringBuilder(s59)).append("KB/S").toString();
			xymultipleseriesrenderer.setChartTitle(s60);
			xymultipleseriesrenderer.setYLabels(5);

			
			int j=0xff00ff00;
			xyseriesrenderer.setColor(j);
			PointStyle  pointstyle =PointStyle.POINT;
			xyseriesrenderer.setPointStyle(pointstyle);
			
			xymultipleseriesrenderer.addSeriesRenderer(xyseriesrenderer);
			xymultipleseriesrenderer.setYAxisMax(10);
			xymultipleseriesrenderer.setYAxisMin(0);
			
			xyseries =new XYSeries("");
			int km=0;
			int i1;
			int j1;
			i1=0;
			j1=all_ds_mobile.getN();
			 
			 while(i1 < j1) 
			 {
			       imiDataset dataset1 = all_ds_mobile;
			       int i3 = km;
			       double d17 = dataset1.get(i3);           
	               BigDecimal bigdecimal3 = new BigDecimal(1024);    	              
	               RoundingMode roundingmode2 = RoundingMode.CEILING;	              
	               double d18 = (new BigDecimal(d17)).divide(bigdecimal3, 2, roundingmode2).doubleValue();			                	              
	               double d20 = km;		              
	               double d21 = d18;	               			           
	               xyseries.add(d20, d21);			              
	               km++;			  
	               i1 = km;
			  }
			xymultipleseriesdataset.addSeries(xyseries);			
			org.achartengine.GraphicalView graphicalview1 = ChartFactory.getTimeChartView(ActivityNetcount.this, xymultipleseriesdataset, xymultipleseriesrenderer, "");
			linearlayout.addView(graphicalview1);
			/*wifi_Tab_Start*/
			
			XYMultipleSeriesRenderer xymultipleseriesrenderer1;
			XYMultipleSeriesDataset xymultipleseriesdataset1;
			XYSeriesRenderer xyseriesrenderer1;
			XYSeries xyseries1;
			
	            long l7 = imiNetdevice.rx_bytes(device_wifi[0]);
	            long l8 = imiNetdevice.tx_bytes(device_wifi[0]);
	            long l9 = last_data_wifi[0];   //之前记录的总流量
	            long l10=0;
	            if(l7 >= l9)
	            {
	                l10  =l7-l9;	       
	            }
	            
	            long l12 = last_data_wifi[1];
	            long l13=0;
	            if(l8 >= l12)
	            {
	                l13  =l8-l12;               
	            }
	            String as2[][] = new String[20][2];
	            String s65[] = new String[2];
	            s65[0] = "";
	            String s66 = Integer.toString(65535);
	            s65[1] = s66;
	            as2[0] = s65;
	            String s67[] = new String[2];
	            String s68 = getString(R.string.upload_title);
	            s67[0] = s68;
	            String s69 = Integer.toString(0xff00ff00);
	            s67[1] = s69;
	            as2[1] = s67;
	            String s70[] = new String[2];
	            String s71 = getString(R.string.download_title);
	            s70[0] = s71;
	            String s72 = Integer.toString(0xff00ff00);
	            s70[1] = s72;
	            as2[2] = s70;
	            String s73[] = new String[2];
	            String s74 = getString(R.string.all_title);
	            s73[0] = s74;
	            String s75 = Integer.toString(0xff00ff00);
	            s73[1] = s75;
	            as2[3] = s73;
	            String s76[] = new String[2];
	            String s77 = getString(R.string.day_title);
	            s76[0] = s77;
	            String s78 = Integer.toString(0xff00ff00);
	            s76[1] = s78;
	            as2[4] = s76;
	            String s79[] = new String[2];
	            String s80 = imiUtil.formatByte(init_data_wifi[0] + l13);
	            s79[0] = s80;
	            String s81 = Integer.toString(0xff00ff00);
	            s79[1] = s81;
	            as2[5] = s79;
	            String s82[] = new String[2];
	            String s83 = imiUtil.formatByte(init_data_wifi[1] + l10);
	            s82[0] = s83;
	            String s84 = Integer.toString(0xff00ff00);
	            s82[1] = s84;
	            as2[6] = s82;
	            String s85[] = new String[2];
	            String s86 = imiUtil.formatByte(init_data_wifi[2] + l13 + l10);
	            s85[0] = s86;
	            String s87 = Integer.toString(0xff00ff00);
	            s85[1] = s87;
	            as2[7] = s85;
	            String s88[] = new String[2];
	            String s89 = getString(R.string.week_title);
	            s88[0] = s89;
	            String s90 = Integer.toString(0xff00ff00);
	            s88[1] = s90;
	            as2[8] = s88;
	            String s91[] = new String[2];
	            String s92 = imiUtil.formatByte(init_data_wifi[3] + l13);
	            s91[0] = s92;
	            String s93 = Integer.toString(0xff00ff00);
	            s91[1] = s93;
	            as2[9] = s91;
	            String s94[] = new String[2];
	            String s95 = imiUtil.formatByte(init_data_wifi[4] + l10);
	            s94[0] = s95;
	            String s96 = Integer.toString(0xff00ff00);
	            s94[1] = s96;
	            as2[10] = s94;
	            String s97[] = new String[2];
	            String s98 = imiUtil.formatByte(init_data_wifi[5] + l13 + l10);
	            s97[0] = s98;
	            String s99 = Integer.toString(0xff00ff00);
	            s97[1] = s99;
	            as2[11] = s97;
	            String s100[] = new String[2];
	            String s101 = getString(R.string.month_title);
	            s100[0] = s101;
	            String s102 = Integer.toString(0xff00ff00);
	            s100[1] = s102;
	            as2[12] = s100;
	            String s103[] = new String[2];
	            String s104 = imiUtil.formatByte(init_data_wifi[6] + l13);
	            s103[0] = s104;
	            String s105 = Integer.toString(0xff00ff00);
	            s103[1] = s105;
	            as2[13] = s103;
	            String s106[] = new String[2];
	            String s107 = imiUtil.formatByte(init_data_wifi[7] + l10);
	            s106[0] = s107;
	            String s108 = Integer.toString(0xff00ff00);
	            s106[1] = s108;
	            as2[14] = s106;
	            String s109[] = new String[2];
	            String s110 = imiUtil.formatByte(init_data_wifi[8] + l13 + l10);
	            s109[0] = s110;
	            String s111 = Integer.toString(0xff00ff00);
	            s109[1] = s111;
	            as2[15] = s109;
	            String s112[] = new String[2];
	            String s113 = getString(R.string.total_title);
	            s112[0] = s113;
	            String s114 = Integer.toString(0xff00ff00);
	            s112[1] = s114;
	            as2[16] = s112;
	            String s115[] = new String[2];
	            String s116 = imiUtil.formatByte(init_data_wifi[9] + l13);
	            s115[0] = s116;
	            String s117 = Integer.toString(0xff00ff00);
	            s115[1] = s117;
	            as2[17] = s115;
	            String s118[] = new String[2];
	            String s119 = imiUtil.formatByte(init_data_wifi[10] + l10);
	            s118[0] = s119;
	            String s120 = Integer.toString(0xff00ff00);
	            s118[1] = s120;
	            as2[18] = s118;
	            String s121[] = new String[2];
	            String s122 = imiUtil.formatByte(init_data_wifi[11] + l13 + l10);
	            s121[0] = s122;
	            String s123 = Integer.toString(0xff00ff00);
	            s121[1] = s123;
	            as2[19] = s121;
	            
	            GridViewAdapter gridviewadapter2 = wifiGridViewAdapter;
	            String as3[][]=as2;
	            gridviewadapter2.setValue(as3);	        
	            GridViewAdapter gridviewadapter3= wifiGridViewAdapter;
	            wifiGridView.setAdapter(gridviewadapter3);
		    	/*wifigridview*/
			    
	          //图形点的数据
		        double d9=l13+l10;
		        double net_v1=d9-pic_wifi_last;
		        pic_wifi_last=d9;
		        all_ds_wifi.put(net_v1);
		        double d10 = net_v1;
	            BigDecimal bigdecimal1 = new BigDecimal(1024);
	            RoundingMode roundingmode1 = RoundingMode.CEILING;
	            double d11 = (new BigDecimal(d10)).divide(bigdecimal1, 2, roundingmode1).doubleValue();
	            wifilinearlayout.removeAllViews();
		        
				xymultipleseriesrenderer1 = new XYMultipleSeriesRenderer();
				xymultipleseriesdataset1 =  new XYMultipleSeriesDataset();
				xyseriesrenderer1        =  new XYSeriesRenderer();
				LinearLayout linearlayout1 = wifilinearlayout;
				
				String s124=String.valueOf(d11);
				String s125=(new StringBuilder(s124)).append("KB/S").toString();
				xymultipleseriesrenderer1.setChartTitle(s125);
				xymultipleseriesrenderer1.setYLabels(5);
				
				int k1=0xff00ff00;
				xyseriesrenderer.setColor(k1);     //图形点的颜色
				PointStyle  pointstyle1 =PointStyle.POINT;
				xyseriesrenderer1.setPointStyle(pointstyle1);
				
				xymultipleseriesrenderer1.addSeriesRenderer(xyseriesrenderer1);
				xymultipleseriesrenderer1.setYAxisMax(30);
				xymultipleseriesrenderer1.setYAxisMin(0);
				
				xyseries1 =new XYSeries("");
				int kw=0;
				int i2;
				int j2;		
				i2=0;
				j2=all_ds_wifi.getN();
				 
				 while(i2 < j2) 
				 {
				       imiDataset dataset2 = all_ds_wifi;
				       int j3 = kw;
				       double d34 = dataset2.get(j3);		                         
		               BigDecimal bigdecimal4 = new BigDecimal(1024);    	              
		               RoundingMode roundingmode4 = RoundingMode.CEILING;	              
		               double d19 = (new BigDecimal(d34)).divide(bigdecimal4, 2, roundingmode4).doubleValue();            	              
		               double d36 = kw;		              
		               double d37 = d19;
		               			           
		              xyseries1.add(d36, d37);			              
		              kw++;			  
		              i2 = kw;
				  }
							
				xymultipleseriesdataset1.addSeries(xyseries1);
				
				org.achartengine.GraphicalView graphicalview2 = ChartFactory.getTimeChartView(ActivityNetcount.this, xymultipleseriesdataset1, xymultipleseriesrenderer1, "");
				linearlayout1.addView(graphicalview2);
			
			        Handler handler1 = handler;
                    Runnable runnable1 = runnable;
                    handler1.postDelayed(runnable1, 1000L);
                    return;
			
		}
		
	}
}