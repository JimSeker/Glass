package edu.cs4730.glassinvaders;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.view.WindowUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private GestureDetector mGestureDetector;
	mySurfaceView myGameBoard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //don't think I need this.
		// Requests a voice menu on this activity. As for any other
		// window feature, be sure to request this before
		// setContentView() is called
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        // Ensure screen stays on during game.  and yes, this is will drain the battery.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		myGameBoard = new mySurfaceView(this);
		setContentView(myGameBoard);

		mGestureDetector = createGestureDetector(this);





	}
	/*
	*setup voice commands.  silly to use them here, but it's example of how this work
	*They are implemented very similar to menu items.
	*
	*see https://developers.google.com/glass/develop/gdk/voice 
	*/


    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        // Pass through to super to setup touch menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            switch (item.getItemId()) {
                case R.id.left:
                    // handle voice command left.
                	myGameBoard.moveship = -10;
                    break;
                case R.id.right:
                    // handle voice command right.
                	myGameBoard.moveship = 10;
                    break;
                case R.id.shoot:
                    // handle voice command fire
                	myGameBoard.tofire = true;
                    break;
                case R.id.exit:
                    // handle second-level golden menu item
                	finish();
                    break;
                default:
                    return true;
            }
            return true;
        }
        // Good practice to pass through to super if not handled
        return super.onMenuItemSelected(featureId, item);
    }

    
    
	/*
	 * "touch" event to deal with the slide forward(right), slide backward (left), and tap fire "button"
	 * 
	 */
	private GestureDetector createGestureDetector(Context context) {
		GestureDetector gestureDetector = new GestureDetector(context);
		//Create a base listener for generic gestures
		gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.TAP) {
					// do something on tap
					myGameBoard.tofire = true;

					return true;
				} else if (gesture == Gesture.TWO_TAP) {
					// do something on two finger tap
					//don't care.
					return true;
				} else if (gesture == Gesture.SWIPE_RIGHT) {
					// swipe forward, we are going left on the screen.
					myGameBoard.moveship = 10;

					return true;
				} else if (gesture == Gesture.SWIPE_LEFT) {
					// do something on left (backwards) swipe
					myGameBoard.moveship = -10;
					return true;
				}
				return false;
			}
		});
		gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
			@Override
			public void onFingerCountChanged(int previousCount, int currentCount) {
				// do something on finger count changes
			}
		});
		gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
			@Override
			public boolean onScroll(float displacement, float delta, float velocity) {
				// do something on scrolling, but I'm not.
				return false;
			}
		});
		return gestureDetector;
	}

	/*
	 * Send generic motion events to the gesture detector
	 */
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}

}
