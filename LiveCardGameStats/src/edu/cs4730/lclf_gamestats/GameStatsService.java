package edu.cs4730.lclf_gamestats;

import java.util.Random;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;


/*
 * This is google's example as shown on the live-cards web page
 * https://developers.google.com/glass/develop/gdk/live-cards
 * There several fixes I had to make as well.
 * 
 */


public class GameStatsService extends Service {

	private static final String LIVE_CARD_TAG = "GameStats";


	private LiveCard mLiveCard;
	private RemoteViews mLiveCardView;

	private int homeScore, awayScore;
	private Random mPointsGenerator;

	private final Handler mHandler = new Handler();
	private final UpdateLiveCardRunnable mUpdateLiveCardRunnable =
			new UpdateLiveCardRunnable();
	private static final long DELAY_MILLIS = 30000;


	@Override
	public void onCreate() {
		super.onCreate();

		mPointsGenerator = new Random();
	}


	/*
	 * (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 * 
	 * used by the menuActivity, so it can call the readscoreaload() method above.
	 * 
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mLiveCard == null) {

			// Get an instance of a live card
			mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

			// Inflate a layout into a remote view
			mLiveCardView = new RemoteViews(getPackageName(),
					R.layout.main_layout);

			// Set up initial RemoteViews values
			homeScore = 0;
			awayScore = 0;
			mLiveCardView.setTextViewText(R.id.home_team_name_text_view,
					"UW Cowboys");
			mLiveCardView.setTextViewText(R.id.away_team_name_text_view,
					"CSU Rams");
			mLiveCardView.setTextViewText(R.id.footer_text,
					"2");

			// Set up the live card's action with a pending intent
			// to show a menu when tapped
			Intent menuIntent = new Intent(this, MenuActivity.class);
			menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
					Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mLiveCard.setAction(PendingIntent.getActivity(
					this, 0, menuIntent, 0));

			// Publish the live card
			mLiveCard.publish(PublishMode.REVEAL);

			// Queue the update text runnable
			mHandler.post(mUpdateLiveCardRunnable);
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (mLiveCard != null && mLiveCard.isPublished()) {
			//Stop the handler from queuing more Runnable jobs
			mUpdateLiveCardRunnable.setStop(true);

			mLiveCard.unpublish();
			mLiveCard = null;
		}
		super.onDestroy();
	}

	/**
	 * Runnable that updates live card contents
	 */
	private class UpdateLiveCardRunnable implements Runnable{

		private boolean mIsStopped = false;

		/*
		 * Updates the card with a fake score every 30 seconds as a demonstration.
		 * You also probably want to display something useful in your live card.
		 *
		 * If you are executing a long running task to get data to update a
		 * live card(e.g, making a web call), do this in another thread or
		 * AsyncTask.
		 */
		public void run(){
			if(!isStopped()){
				// Generate fake points.
				homeScore += mPointsGenerator.nextInt(3);
				awayScore += mPointsGenerator.nextInt(3);

				// Update the remote view with the new scores.
				mLiveCardView.setTextViewText(R.id.home_score_text_view,
						String.valueOf(homeScore));
				mLiveCardView.setTextViewText(R.id.away_score_text_view,
						String.valueOf(awayScore));

				// Always call setViews() to update the live card's RemoteViews.
				mLiveCard.setViews(mLiveCardView);

				// Queue another score update in 30 seconds.
				mHandler.postDelayed(mUpdateLiveCardRunnable, DELAY_MILLIS);
			}
		}

		public boolean isStopped() {
			return mIsStopped;
		}

		public void setStop(boolean isStopped) {
			this.mIsStopped = isStopped;
		}
	}
}