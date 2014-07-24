package edu.cs4730.carddemo;

import java.util.ArrayList;
import java.util.List;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class MainActivity extends Activity {

	private List<Card> mCards;
    private CardScrollView mCardScrollView;
    ExampleCardScrollAdapter adapter;
    int tapped = 0;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		
		//create a list of cards in mCards
		//mCards is used inside the adapter.
        mCards = new ArrayList<Card>();
        Card card;  //for each new card we create.

        //Simple card with text and footer.
        card = new Card(this);
        card.setText("Statis text header.  And Tap me!");
        card.setFootnote("Footer, we haven't be tapped yet.");
        mCards.add(card);

        
        //Card with one big background image.
        card = new Card(this);
        card.setText("This is the UW main campus.");
        card.setFootnote("I can see my office from here.");
        card.setImageLayout(Card.ImageLayout.FULL);
        card.addImage(R.drawable.uwcampus);
        mCards.add(card);

        //picture on the left and text on right.
        card = new Card(this);
        card.setText("The Engineering building");
        card.setFootnote("I can't see my office now.");
        card.setImageLayout(Card.ImageLayout.LEFT);
        card.addImage(R.drawable.uwengineering);
        mCards.add(card);
        
        //mosaic of pictures with left on the right.
        card = new Card(this);
        card.setText("This card has a mosaic of UW logos.");
        card.setFootnote("neat.");
        card.setImageLayout(Card.ImageLayout.LEFT);
        card.addImage(R.drawable.uwflag);
        card.addImage(R.drawable.uwseal);
        card.addImage(R.drawable.uwlogo);
        mCards.add(card);
        
        //now create the cardsrollview and add the adapter
        mCardScrollView = new CardScrollView(this);
         adapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position ==0) {//first card, one that says click me.
					tapped++;
					mCards.get(0).setFootnote("Tapped "+ tapped +" times");
					//redraw the card with new data.  hopefully, except they don't.
//					mCardScrollView.invalidate();
//					view.invalidate();
//					parent.invalidate();
					//well this is likely not the correct way to do it, but it works.
					//and the text updates.  likely eats memory and stuff.
					mCardScrollView.setAdapter(adapter);
				}
				
			}
        	
		});
        mCardScrollView.activate();
        setContentView(mCardScrollView);

		
	}

	/*
	 * This is cardScrollAdapter for the card list.
	 * this copied completely from Google page
	 * https://developers.google.com/glass/develop/gdk/ui-widgets#scrolling_cards_in_activities
	 * 
	 * It was then modified to create an extra custom layout cards as the last entry.
	 * 
	 */
    private class ExampleCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return Card.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position){
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView,
                ViewGroup parent) {
          return  mCards.get(position).getView(convertView, parent);
       }
        
    }

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
