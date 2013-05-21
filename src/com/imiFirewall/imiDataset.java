package com.imiFirewall;

import android.util.Log;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

public class imiDataset
{

    private double first=0;
    private double max=0;
    private int n;
    private ArrayList value;
	
    public imiDataset()
    {

        n = 60;
        ArrayList arraylist = new ArrayList();
        value = arraylist;
    }

    public imiDataset(int i)
    {
    
        n = i;
        ArrayList arraylist = new ArrayList();
        value = arraylist;
    }
    public void clear()
    {
        value.clear();
        value = null;
    }

    public double get(int i)
    {
        int d=1;
        double d1=0;
        double d4=0;
        int j =value.size()-d;
        
        if(i>j)
        {
        	 return d4;
        }else
        {
           if(i!=j)
           {
        	   ArrayList arraylist2 = value;
               d4 = ((Double)arraylist2.get(i)).doubleValue();
               return d4;
           }else
           {
               d1=first;
               if(j == 0)
               {
                  d4 = d1;
               } else
               {
                  ArrayList arraylist1 = value;
                  double d2 = ((Double)arraylist1.get(j)).doubleValue();
                  d4 =d2;
               }
                  return d4;
            }
        }
    }

    public int getN()
    {
        return n;
    }

    public int max()
    {
        double d = max;
        return (new BigDecimal(d)).intValue();
    }

    public void put(double d)
    {
    	int i=0;
        int j = value.size();
        int k = n;
        if(j == k)
        {
            double d1 = ((Double)value.get(i)).doubleValue();
            first = d1;
            value.remove(i);
        }
        ArrayList arraylist = value;
        Double double1 = new Double(d);
        arraylist.add(0,double1);
    }
}
