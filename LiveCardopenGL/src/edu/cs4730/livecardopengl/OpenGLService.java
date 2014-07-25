package edu.cs4730.livecardopengl;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/*
 * This a simple example of a livecard using opengl 2.0
 * It is based on Google example code online (with a number of fixes, so it actually works!)
 * https://developers.google.com/glass/develop/gdk/live-cards#using_opengl
 * 
 */

public class OpenGLService extends Service {
    private static final String LIVE_CARD_TAG = "opengl";

    private LiveCard mLiveCard;

	

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);
            mLiveCard.setRenderer(new CubeRenderer());
            mLiveCard.setAction(
                    PendingIntent.getActivity(this, 0, new Intent(this, MenuActivity.class), 0));
            mLiveCard.publish(PublishMode.REVEAL);
        } else {
            mLiveCard.navigate();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }

}
