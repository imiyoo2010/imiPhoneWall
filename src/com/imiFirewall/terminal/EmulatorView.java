package com.imiFirewall.terminal;


import com.imiFirewall.Function;
import android.os.Bundle;  
import com.imiFirewall.R;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;


class EmulatorView extends View implements OnGestureListener {

  public static final String TAG = "EmulatorView";
  private TranscriptScreen mTranscriptScreen;
  private static final int TRANSCRIPT_ROWS = 10000;
  private int mCharacterWidth;
  private int mCharacterHeight;
  private TextRenderer mTextRenderer;
  private int mTextSize;
  private int mForeground;
  private int mBackground;
  private Paint mCursorPaint;
  private Paint mBackgroundPaint;
  private TerminalEmulator mEmulator;
  private boolean mKnownSize;
  private int mRows;
  private int mColumns;
  private int mVisibleColumns;
  private int mTopRow;
  private int mLeftColumn;
  private FileInputStream mTermIn;
  private FileOutputStream mTermOut;
  private FileDescriptor mTermFd;
  private TermKeyListener mKeyListener;

  private ByteQueue mByteQueue;
  private byte[] mReceiveBuffer=null;

  private static final int UPDATE = 1;
  private Thread mPollingThread;

  private GestureDetector mGestureDetector;

  private float mScrollRemainder;

  private final Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == UPDATE) {
    	  update();
      }
    }
  };

  public EmulatorView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
  }

  public EmulatorView(Context context, AttributeSet attrs,int defStyle){
      super(context, attrs, defStyle);
      TypedArray a =
              context.obtainStyledAttributes(R.styleable.EmulatorView);
      initializeScrollbars(a);
      a.recycle();
      commonConstructor(context);
  }

  private void commonConstructor(Context context) {
      mTextRenderer = null;
      mCursorPaint = new Paint();
      mCursorPaint.setARGB(255,128,128,128);
      mBackgroundPaint = new Paint();
      mTopRow = 0;
      mLeftColumn = 0;
      mGestureDetector = new GestureDetector(context, this, null);
      mGestureDetector.setIsLongpressEnabled(false);
      setVerticalScrollBarEnabled(true);
  }
  
  public void setColors(int foreground, int background) {
    mForeground = foreground;
    mBackground = background;   
    updateText();
  }

  /**
   * Sets the text size, which in turn sets the number of rows and columns
   *
   * @param fontSize
   *          the new font size, in pixels.
   */
  public void setTextSize(int fontSize) {
    mTextSize = fontSize;
  }

  public String getTranscriptText() {
    return mEmulator.getTranscriptText();
  }

  public void register(TermKeyListener paramTermKeyListener)
  {
    this.mKeyListener = paramTermKeyListener;
  }
  
  public void resetTerminal() {
    mEmulator.reset();
    invalidate();
  }

  
  private void update() {
      int bytesAvailable = mByteQueue.getBytesAvailable();
      int bytesToRead = Math.min(bytesAvailable, mReceiveBuffer.length);
      try {
          int bytesRead = mByteQueue.read(mReceiveBuffer, 0, bytesToRead);
          append(mReceiveBuffer, 0, bytesRead);
      } catch (InterruptedException e) {
      }
  }
  
  @Override
  public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
	         return new BaseInputConnection(this, false){
	        	 @Override
	             public boolean beginBatchEdit() {
	                 return true;
	             }

	             @Override
	             public boolean clearMetaKeyStates(int states) {
	                 return true;
	             }

	             @Override
	             public boolean commitCompletion(CompletionInfo text) {
	                 return true;
	             }

	             @Override
	             public boolean commitText(CharSequence text, int newCursorPosition) {
	                 sendText(text);
	                 return true;
	             }

	             @Override
	             public boolean deleteSurroundingText(int leftLength, int rightLength) {
	                 return true;
	             }

	             @Override
	             public boolean endBatchEdit() {
	                 return true;
	             }

	             @Override
	             public boolean finishComposingText() {
	                 return true;
	             }

	             @Override
	             public int getCursorCapsMode(int reqModes) {
	                 return 0;
	             }

	             @Override
	             public ExtractedText getExtractedText(ExtractedTextRequest request,
	                     int flags) {
	                 return null;
	             }

	             @Override
	             public CharSequence getTextAfterCursor(int n, int flags) {
	                 return null;
	             }

	             @Override
	             public CharSequence getTextBeforeCursor(int n, int flags) {
	                 return null;
	             }

	             @Override
	             public boolean performEditorAction(int actionCode) {
	                 if(actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {
	                     // The "return" key has been pressed on the IME.
	                     sendText("\n");
	                     return true;
	                 }
	                 return false;
	             }

	             @Override
	             public boolean performContextMenuAction(int id) {
	                 return true;
	             }

	             @Override
	             public boolean performPrivateCommand(String action, Bundle data) {
	                 return true;
	             }

	             @Override
	             public boolean sendKeyEvent(KeyEvent event) {
	                 if (event.getAction() == KeyEvent.ACTION_DOWN) {
	                     switch(event.getKeyCode()) {
	                     case KeyEvent.KEYCODE_DEL:
	                         sendChar(127);
	                         break;
	                     }
	                 }
	                 return true;
	             }

	             @Override
	             public boolean setComposingText(CharSequence text, int newCursorPosition) {
	                 return true;
	             }

	             @Override
	             public boolean setSelection(int start, int end) {
	                 return true;
	             }

	             private void sendChar(int c) {
	                 try {
	                     mapAndSend(c);
	                 } catch (IOException ex) {

	                 }
	             }
	             private void sendText(CharSequence text) {
	                 int n = text.length();
	                 try {
	                     for(int i = 0; i < n; i++) {
	                         char c = text.charAt(i);
	                         mapAndSend(c);
	                     }
	                 } catch (IOException e) {
	                 }
	             }

	             private void mapAndSend(int c) throws IOException {
	                 mTermOut.write(mKeyListener.mapControlChar(c));
	             }
	         };
           
  }
  
  public boolean getKeypadApplicationMode() {
    return mEmulator.getKeypadApplicationMode();
  }

  @Override
  protected int computeVerticalScrollRange() {
    return mTranscriptScreen.getActiveRows();
  }

  @Override
  protected int computeVerticalScrollExtent() {
    return mRows;
  }

  @Override
  protected int computeVerticalScrollOffset() {
    return mTranscriptScreen.getActiveRows() + mTopRow - mRows;
  }

  /**
   * Accept a sequence of bytes (typically from the pseudo-tty) and process them.
   *
   * @param receiveBuffer
   *          a byte array containing bytes to be processed
   * @param base
   *          the index of the first byte in the buffer to process
   * @param length
   *          the number of bytes to process
   */
  public void append(byte[] receiveBuffer, int base, int length) {
    mEmulator.append(receiveBuffer, base, length);
    ensureCursorVisible();
    invalidate();
  }

  /**
   * Page the terminal view (scroll it up or down by delta screenfulls.)
   *
   * @param delta
   *          the number of screens to scroll. Positive means scroll down, negative means scroll up.
   */
  public void page(int delta) {
    mTopRow =
        Math.min(0, Math.max(-(mTranscriptScreen.getActiveTranscriptRows()), mTopRow + mRows
            * delta));
    invalidate();
  }

  /**
   * Page the terminal view horizontally.
   *
   * @param deltaColumns
   *          the number of columns to scroll. Positive scrolls to the right.
   */
  public void pageHorizontal(int deltaColumns) {
    mLeftColumn = Math.max(0, Math.min(mLeftColumn + deltaColumns, mColumns - mVisibleColumns));
    invalidate();
  }

  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    return true;
  }

  @Override
  public void onLongPress(MotionEvent e) {
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    distanceY += mScrollRemainder;
    int deltaRows = (int) (distanceY / mCharacterHeight);
    mScrollRemainder = distanceY - deltaRows * mCharacterHeight;
    mTopRow =
        Math.min(0, Math.max(-(mTranscriptScreen.getActiveTranscriptRows()), mTopRow + deltaRows));
    invalidate();

    return true;
  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    mScrollRemainder = 0.0f;
    onScroll(e1, e2, 2 * velocityX, -2 * velocityY);
    return true;
  }

  @Override
  public void onShowPress(MotionEvent e) {
  }

  @Override
  public boolean onDown(MotionEvent e) {
    mScrollRemainder = 0.0f;
    return true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    return mGestureDetector.onTouchEvent(ev);
  }
  
  private void updateText() {
    if (mTextSize > 0) {
      mTextRenderer = new PaintRenderer(mTextSize, mForeground, mBackground);
    } else {
      mTextRenderer = new Bitmap4x8FontRenderer(getResources(), mForeground, mBackground);
    }
    mBackgroundPaint.setColor(mBackground);
    mCharacterWidth = mTextRenderer.getCharacterWidth();
    mCharacterHeight = mTextRenderer.getCharacterHeight();
    if (mKnownSize) {
        updateSize(getWidth(), getHeight());
    }
  }

  public void initialize(FileDescriptor fd,FileOutputStream fs)
  {
	  mTermOut = fs;
	  mTermFd  = fd;
	  mTextSize = 10;
	  mForeground = Terminal.BLACK;
      mBackground = Terminal.BLUE;
	  updateText();
	  mTermIn = new FileInputStream(mTermFd);
	  mReceiveBuffer = new byte[4 * 1024];
      mByteQueue = new ByteQueue(4 * 1024);
  
  }
  
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
      updateSize(w, h);
      if (!mKnownSize) {
          mKnownSize = true;

          // Set up a thread to read input from the
          // pseudo-teletype:

          mPollingThread = new Thread(new Runnable() {

              public void run() {
                  try {
                      while(true) {
                          int read = mTermIn.read(mBuffer);
                          mByteQueue.write(mBuffer, 0, read);
                          mHandler.sendMessage(mHandler.obtainMessage(UPDATE));
                      }
                  } catch (IOException e) {
                  } catch (InterruptedException e) {
                  }
              }
              private byte[] mBuffer = new byte[4096];
          });
          mPollingThread.setName("Input reader");
          mPollingThread.start();
      }
  }
  
  private void updateSize(int w, int h) {
    mColumns = w / mCharacterWidth;
    mRows = h / mCharacterHeight;

    // If we're attached to a process, inform it of our new size.
    if (mTermFd != null) {
      Function.setPtyWindowSize(mTermFd, mRows, mColumns, w, h);
    }

    if (mTranscriptScreen != null) {
      mEmulator.updateSize(mColumns, mRows);
    } else {
      mTranscriptScreen = new TranscriptScreen(mColumns, TRANSCRIPT_ROWS, mRows, mForeground, mBackground);
      mEmulator = new TerminalEmulator(mTranscriptScreen, mColumns, mRows, mTermOut);
    }

    // Reset our paging:
    mTopRow = 0;
    mLeftColumn = 0;

    invalidate();
  }

  void updateSize() {
      if (mKnownSize) {
          updateSize(getWidth(), getHeight());
      }
  }
 
  @Override
  protected void onDraw(Canvas canvas) {
    int w = getWidth();
    int h = getHeight();
    canvas.drawRect(0, 0, w, h, mBackgroundPaint);
    mVisibleColumns = w / mCharacterWidth;
    float x = -mLeftColumn * mCharacterWidth;
    float y = mCharacterHeight;
    int endLine = mTopRow + mRows;
    int cx = mEmulator.getCursorCol();
    int cy = mEmulator.getCursorRow();
    for (int i = mTopRow; i < endLine; i++) {
      int cursorX = -1;
      if (i == cy) {
        cursorX = cx;
      }
      mTranscriptScreen.drawText(i, canvas, x, y, mTextRenderer, cursorX);
      y += mCharacterHeight;
    }
  }

  private void ensureCursorVisible() {
    mTopRow = 0;
    if (mVisibleColumns > 0) {
      int cx = mEmulator.getCursorCol();
      int visibleCursorX = mEmulator.getCursorCol() - mLeftColumn;
      if (visibleCursorX < 0) {
        mLeftColumn = cx;
      } else if (visibleCursorX >= mVisibleColumns) {
        mLeftColumn = (cx - mVisibleColumns) + 1;
      }
    }
  }
}
