package edu.cs4730.sensordemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
/*
 * This is a simple demo of a couple of the hardware sensors
 * light sensor
 * acceleration and magnetic sensor
 * rotation vector
 * 
 * everything is displayed (mostly raw data) to the screen 
 * 
 */
public class MainActivity extends Activity implements SensorEventListener {

	// Sensor manager
	private SensorManager mSensorManager = null;

	// hardware sensors
	private Sensor mSensorLight = null;
	private Sensor mRotVectSensor = null;
	private Sensor mAccel = null;
	private Sensor mMagentic = null;

	//for the Rotation Vector Sensor
	private float[] orientationVals=new float[3];
	private float[] mRotationMatrix=new float[16];
	private float[] mRotationMatrixFromVector=new float[16];
	
	//for the acceleration and magnetic sensors to figure with.
	private float[] accelValues = new float[3];
	private float[] compassValues = new float[3];
	private boolean am_ready = false;

	TextView tv_light, tv_rot, tv_accmag;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_main);
		

		//get the sensorManager
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

		//light sensor
		mSensorLight= mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		//Rotation "sensor"
		mRotVectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		

		//Accelerometer sensor
		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		//magnetic field sensor
		mMagentic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		tv_light = (TextView) findViewById(R.id.Light);
		tv_rot = (TextView) findViewById(R.id.Rotation);
		tv_accmag = (TextView) findViewById(R.id.AccMag);
		
	}


	@Override
	protected void onResume() {
		mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mRotVectSensor, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mMagentic, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}
	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this, mSensorLight);
		mSensorManager.unregisterListener(this, mRotVectSensor);
		mSensorManager.unregisterListener(this, mAccel);
		mSensorManager.unregisterListener(this, mMagentic);
		super.onPause();
	}

	

	@Override
	public void onSensorChanged(SensorEvent event) {
		//all of these on different based on type.
		Sensor sensor = event.sensor;
		int type = sensor.getType();
		//long timestamp = event.timestamp;
		//float[] values = event.values;
		//int accuracy = event.accuracy;

		switch (type) {
		case Sensor.TYPE_LIGHT:
			tv_light.setText("LIGHT: " + event.values[0]);
			break;
		case Sensor.TYPE_ROTATION_VECTOR:
			SensorManager.getRotationMatrixFromVector(mRotationMatrixFromVector,event.values);
			SensorManager.remapCoordinateSystem(mRotationMatrixFromVector,SensorManager.AXIS_X, SensorManager.AXIS_Z, mRotationMatrix);
			SensorManager.getOrientation(mRotationMatrix, orientationVals);
			orientationVals[0]=(float)Math.toDegrees(orientationVals[0]);
			orientationVals[1]=(float)Math.toDegrees(orientationVals[1]);
			orientationVals[2]=(float)Math.toDegrees(orientationVals[2]);
			String msg = String.format(
			"Rotation: azimuth (Z): %7.3f\npitch (X): %7.3f roll (Y): %7.3f",
			orientationVals[0], //Yaw/azimuth in degree [-180 to 180], //heading
			orientationVals[1],   //pitch
			orientationVals[2]);  //roll
			tv_rot.setText(msg);
			break;
		case Sensor.TYPE_ACCELEROMETER:
			for(int i=0; i<3; i++) {
				accelValues[i] = event.values[i];
			}
			if(compassValues[0] != 0)
				am_ready = true;
			finish_AM_calacutions();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for(int i=0; i<3; i++) {
				compassValues[i] = event.values[i];
			}
			if(accelValues[2] != 0)
				am_ready = true;
			finish_AM_calacutions();
			break;
		}



	}

	void finish_AM_calacutions() {
		float[] inR = new float[9];
		float[] inclineMatrix = new float[9];
		float[] prefValues = new float[3];
		float mAzimuth;
		double mPitch;
		double mInclination;
		//we need both the accelerometer and magnetic field values before we can proceed
		if (am_ready) { //yes
			if(SensorManager.getRotationMatrix(
					inR, inclineMatrix, accelValues, compassValues)) {
					// got a good rotation matrix
					SensorManager.getOrientation(inR, prefValues);
					mInclination = SensorManager.getInclination(inclineMatrix);
					
					mAzimuth = (float) Math.toDegrees(prefValues[0]);
					if(mAzimuth < 0)
						mAzimuth += 360.0f;
					//mPitch = 180.0 + Math.toDegrees(prefValues[1]); //so it goes from 0 to 360, instead of -180 to 180
					mPitch = Math.toDegrees(prefValues[1]);
					String msg = String.format(
					"ACC_MAG: azimuth (Z): %7.3f \npitch (X): %7.3f roll (Y): %7.3f\nInclination: %7.3f",
					mAzimuth, //heading
					mPitch,
					Math.toDegrees(prefValues[2]),
					Math.toDegrees(mInclination));
					tv_accmag.setText(msg);
			}

		}
		
	}



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

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
