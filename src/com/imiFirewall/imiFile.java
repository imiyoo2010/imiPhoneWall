package com.imiFirewall;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.content.Context;




public class imiFile
{  
 
 public String imiReadFile(Context context,String fileName)
 {
       FileInputStream fIn = null;
       InputStreamReader isr = null;       
       char[] inputBuffer = new char[255];
       StringBuilder data = new StringBuilder();       
       try
       {
        fIn =context.openFileInput(fileName);      
           isr = new InputStreamReader(fIn);
        
           while(isr.read(inputBuffer)>=0)
           {
        	   data.append(inputBuffer);
           }
       }
      catch (Exception e) 
          {      
               e.printStackTrace();
    
          }
       finally
       {
              try {
                     isr.close();
                     fIn.close();
                   } 
              catch (IOException e) 
              {
                    e.printStackTrace();
              }
           }
       return data.toString();
   }
 

 public void imiWriteFile(Context context, String fileName,String data)
 {
       FileOutputStream fOut = null;
       OutputStreamWriter osw = null;
       
       try{
           fOut =context.openFileOutput(fileName,0);
           osw = new OutputStreamWriter(fOut);
           osw.write(data);
           osw.flush();       
           }
           catch (Exception e) 
           {      
           e.printStackTrace();
 
           }
           finally 
           {
              try {
                     osw.close();
                     fOut.close();
                   } 
              catch (IOException e)
               {
                    e.printStackTrace();
                 }
           }
  }
}