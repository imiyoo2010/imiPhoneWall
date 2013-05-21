/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.imiFirewall.terminal;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import com.imiFirewall.Function;
import com.imiFirewall.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


public class Terminal extends Activity {

  
  public static final boolean DEBUG = false;

  public static final boolean LOG_CHARACTERS_FLAG = DEBUG;

  public static final boolean LOG_UNKNOWN_ESCAPE_SEQUENCES = DEBUG;


  public static final String TAG = "Terminal";

  private EmulatorView mEmulatorView;

  
  private TermKeyListener mKeyListener;

 
  private static final int EMULATOR_VIEW = R.id.emulatorView;

  private int mFontSize = 9;
  private int mColorId = 2;
  private int mControlKeyId = 0;
  
  private static final String FONTSIZE_KEY = "fontsize";
  private static final String COLOR_KEY = "color";
  private static final String CONTROLKEY_KEY = "controlkey";
  private static final String SHELL_KEY = "shell";
  private static final String INITIALCOMMAND_KEY = "initialcommand";
 
  public static final int WHITE = 0xffffffff;
  public static final int BLACK = 0xff000000;
  public static final int BLUE = 0xff344ebd;

  private static final int DEFAULT_COLOR_SCHEME = 1;
  private InputMethodManager inputMethod;

  private static final int[][] COLOR_SCHEMES =
      { { BLACK, WHITE }, { WHITE, BLACK }, { WHITE, BLUE } };

  private static final int[] CONTROL_KEY_SCHEMES =
      { KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_AT, KeyEvent.KEYCODE_ALT_LEFT,
          KeyEvent.KEYCODE_ALT_RIGHT };
  private static final String[] CONTROL_KEY_NAME = { "Ball", "@", "Left-Alt", "Right-Alt" };
  private int mControlKeyCode;
  private SharedPreferences mPrefs;
  
  private FileDescriptor mTermFd;
  private FileOutputStream mTermOut;
  private String mShell;
  private String mInitialCommand;  
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        
    
    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    readPrefs();
    
    setContentView(R.layout.ad_terminal);    
    mEmulatorView = (EmulatorView) findViewById(EMULATOR_VIEW);
    startListening();
    mEmulatorView.setFocusable(true);
    mEmulatorView.setFocusableInTouchMode(true);
    mEmulatorView.requestFocus();
    
    mKeyListener = new TermKeyListener();    
    mEmulatorView.register(mKeyListener);
    
    inputMethod = (InputMethodManager)getSystemService("input_method");
    updatePrefs();
  
  }
  
  @Override
  public void onResume() {
      super.onResume();
      readPrefs();
      updatePrefs();
  }
  
  
  @Override
  public void onDestroy() {
      super.onDestroy();
      if (mTermFd != null) {    
          mTermFd = null;
      }
  }
  
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);

      mEmulatorView.updateSize();
  }

  void showInput()
  {
    inputMethod.showSoftInput(mEmulatorView, 0);
  }
  
  private void startListening()
  {
	  int[] pid = new int[1];
	  createSubprocess(pid);
	  final int i = pid[0];
	  
	  
	  
	  final Handler handler = new Handler(){
		
		  @Override
		  public void handleMessage(Message msg){
			  
		  }
	  };
	  
	  Runnable watchForDeath = new Runnable(){
		
		  public void run(){
			  int result = Function.waitFor(i);
			  handler.sendEmptyMessage(result);
		  }
	  };
	  
	  Thread watcher = new Thread(watchForDeath);
	  watcher.start();
	  
	  mTermOut = new FileOutputStream(mTermFd);
	  mEmulatorView.initialize(mTermFd, mTermOut);
	  
      sendInitialCommand();
	   
  }
  
  private void sendInitialCommand()
  {
	  String initialCommand = this.mInitialCommand;
	    if ((initialCommand == null) || (initialCommand.equals(""))){
	    	initialCommand = "export PATH=/data/local/bin:$PATH";
	    }
	    if (initialCommand.length() > 0)
	    {
	      
	      write(initialCommand + '\r');
	    }
  }
  
  private void write(String data)
  {
	  try
	    {
	      mTermOut.write(data.getBytes());
	      mTermOut.flush();
	    }
	    catch(IOException e)
	    {   
	    	e.printStackTrace();
	    }
  }
  private void createSubprocess(int[] pid)
  {
	
	 String str2 = "/system/bin/sh";
	  
	 mTermFd = Function.createSubprocess(str2, "-", null, pid);
	
  }
  private void restart() {
    startActivity(getIntent());
    finish();
  }
  
  
  private void readPrefs() {
      mFontSize = readIntPref(FONTSIZE_KEY, mFontSize, 20);
      mColorId = readIntPref(COLOR_KEY, mColorId, COLOR_SCHEMES.length - 1);
      mControlKeyId = readIntPref(CONTROLKEY_KEY, mControlKeyId,
              CONTROL_KEY_SCHEMES.length - 1);
      {
          String newShell = readStringPref(SHELL_KEY, mShell);
          if ((newShell == null) || ! newShell.equals(mShell)) {
              if (mShell != null) {
                  restart();
              }
              mShell = newShell;
          }
      }
      {
          String newInitialCommand = readStringPref(INITIALCOMMAND_KEY,
                  mInitialCommand);
          if ((newInitialCommand == null)
                  || ! newInitialCommand.equals(mInitialCommand)) {
              if (mInitialCommand != null) {
                  restart();
              }
              mInitialCommand = newInitialCommand;
          }
      }
  }
  
  private void updatePrefs() {
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      mEmulatorView.setTextSize((int) (mFontSize * metrics.density));
      setColors();
      mControlKeyCode = CONTROL_KEY_SCHEMES[mControlKeyId];
  }
  
  private int readIntPref(String key, int defaultValue, int maxValue) {
      int val;
      try {
          val = Integer.parseInt(
              mPrefs.getString(key, Integer.toString(defaultValue)));
      } catch (NumberFormatException e) {
          val = defaultValue;
      }
      val = Math.max(0, Math.min(val, maxValue));
      return val;
  }

  private String readStringPref(String key, String defaultValue) {
      return mPrefs.getString(key, defaultValue);
  }
  
  /**
   * Handle dpad left-right-up-down events. Don't handle dpad-center, that's our control key.
   *
   * @param keyCode
   * @param down
 * @throws IOException 
   */
  private boolean handleDPad(int keyCode, boolean down) {
	  
	  FileOutputStream localfileoutput;
    if (keyCode < KeyEvent.KEYCODE_DPAD_UP || keyCode > KeyEvent.KEYCODE_DPAD_CENTER) {
      // keyCode does not correspond to the dpad.
    	localfileoutput=null;
        return false;
    }

    if (down) {
      if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
        // TODO(damonkohler): If center is our control key, why are we printing \r?
           localfileoutput=mTermOut;
    	   try {
			localfileoutput.write('\r');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      } else {
        char code;
        switch (keyCode) {
          case KeyEvent.KEYCODE_DPAD_UP:
            code = 'A';
            break;
          case KeyEvent.KEYCODE_DPAD_DOWN:
            code = 'B';
            break;
          case KeyEvent.KEYCODE_DPAD_LEFT:
            code = 'D';
            break;
          default:
          case KeyEvent.KEYCODE_DPAD_RIGHT:
            code = 'C';
            break;
        }
        try {
			mTermOut.write((char) 27);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ESC
        if (mEmulatorView.getKeypadApplicationMode()) {
          try {
			mTermOut.write('O');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        } else {
          try {
			mTermOut.write('[');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
         try {
			mTermOut.write(code);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    }
    return true;
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (handleControlKey(keyCode, true)) {
      return true;
    } else if (isSystemKey(keyCode, event)) {
      // Don't intercept the system keys.
      return super.onKeyDown(keyCode, event);
    } else if (handleDPad(keyCode, true)) {
      return true;
    }

    // Translate the keyCode into an ASCII character.
    int letter = mKeyListener.keyDown(keyCode, event);
    if (letter >=0) {
       FileOutputStream fileoutput2=mTermOut;
    	  try {
			mTermOut.write((char)letter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    return true;
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    if (handleControlKey(keyCode, false)) {
      return true;
    } else if (event.isSystem()) {
      // Don't intercept the system keys.
      return super.onKeyUp(keyCode, event);
    } else if (handleDPad(keyCode, false)) {
      return true;
    }
    mKeyListener.keyUp(keyCode);
    return true;
  }


  private boolean handleControlKey(int keyCode, boolean down) {
	    if (keyCode == mControlKeyCode) {
	      mKeyListener.handleControlKey(down);
	      return true;
	    }
	    return false;
	  }
  
  private boolean isSystemKey(int keyCode, KeyEvent event) {
      return event.isSystem();
  }

  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == R.id.menu_preferences) {
           doPreferences();
      } else if (id == R.id.menu_reset) {
          doResetTerminal();
      }else if(id==R.id.menu_keyboard) {
    	  showInput();
      }
    	  
      return super.onOptionsItemSelected(item);
  }
  
  private void doPreferences() {
      startActivity(new Intent(this, TerminalPreferences.class));
  }
  
  private void doResetTerminal() {
      restart();
  }
  
  private void setColors() {
      int[] scheme = COLOR_SCHEMES[mColorId];
      mEmulatorView.setColors(scheme[0], scheme[1]);
  }
  
}
