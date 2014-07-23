package edu.cs4730.hellogglass;

import android.app.Activity;
import android.os.Bundle;

/*
 * This is a very simple app.  it has the basic layout with a textview that say "hello glass..."
 * otherwise, it is just a basic app to get the idea of what everything looks like
 * The voice command is in the AndroidManifest.xml, xml/voice_trigger.xml and values/string.xml file
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

}
