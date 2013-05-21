package com.imiFirewall;

import java.io.FileDescriptor;

public class Function {

 public static FileDescriptor createSubprocess(String cmd, String arg0, String arg1) {

    return createSubprocess(cmd, arg0, arg1, null);
 }

 public static native FileDescriptor createSubprocess(String cmd, String arg0, String arg1,int[] processId);
 
 public static native void setPtyWindowSize(FileDescriptor fd, int row, int col, int xpixel,int ypixel);
 
 public static native int waitFor(int processId);
 
 public static native String version();
 
  static {
    System.loadLibrary("imiFunction");
  }
}