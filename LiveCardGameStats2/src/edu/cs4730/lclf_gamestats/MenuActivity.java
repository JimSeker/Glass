package edu.cs4730.lclf_gamestats;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuActivity extends Activity {

	private final Handler mHandler = new Handler();

	private GameStatsService.GameBinder mGameStatsService;
    private boolean mAttachedToWindow;
    private boolean mOptionsMenuOpen;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof GameStatsService.GameBinder) {
				mGameStatsService = (GameStatsService.GameBinder) service;
				openOptionsMenu();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// Do nothing.
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_layout);

        bindService(new Intent(this, GameStatsService.class), mConnection, 0);
	}
	
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        openOptionsMenu();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
    }
    
    @Override
    public void openOptionsMenu() {
        if (!mOptionsMenuOpen && mAttachedToWindow && mGameStatsService != null) {
            super.openOptionsMenu();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.stop:
                // Stop the service at the end of the message queue for proper options menu
                // animation. This is only needed when starting a new Activity or stopping a Service
                // that published a LiveCard.
                post(new Runnable() {

                    @Override
                    public void run() {
                        stopService(new Intent(MenuActivity.this, GameStatsService.class));
                    }
                });
                return true;
            case R.id.read:
            	mGameStatsService.readScoreAloud();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
    	super.onOptionsMenuClosed(menu);
    	mOptionsMenuOpen = false;
    	unbindService(mConnection);  //release IPC 
        
    	// Nothing else to do, closing the Activity.
        finish();
    }

    /**
     * Posts a {@link Runnable} at the end of the message loop, overridable for testing.
     */
    protected void post(Runnable runnable) {
        mHandler.post(runnable);
    }

}
