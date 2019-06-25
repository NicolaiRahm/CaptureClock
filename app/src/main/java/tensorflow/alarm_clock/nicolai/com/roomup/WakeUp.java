package tensorflow.alarm_clock.nicolai.com.roomup;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.receiver_service.ForegroundService_AlarmSound;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.RoomUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SoundUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.TimeUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.databinding.ActivityWakeUpBinding;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.VMFactory_WakeUp;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_WakeUp;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class WakeUp extends AppCompatActivity implements SensorEventListener {

    private ActivityWakeUpBinding mBinding;
    private ViewModel_WakeUp viewModel;
    private Alarm_POJO mAlarm;
    private int id;
    private String roomObject;
    private String roomAndObject;

    private Timer sinceTimer, bounceTimer;
    private long sincePaused;
    private int sinceCount;
    private volatile float mVolume;
    private float volumeStart;

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float mAccel, mAccelCurrent, mAccelLast, equalizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Overdraw with Them background by activity
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        window.setBackgroundDrawable(null);

        //Rotated?!
        if(savedInstanceState != null){
            sinceCount = savedInstanceState.getInt("since_count");
            sincePaused = savedInstanceState.getLong("since_paused");
            mVolume = savedInstanceState.getFloat("volume");
            id = savedInstanceState.getInt("ID");
            roomObject = savedInstanceState.getString("roomObject");
            roomAndObject = savedInstanceState.getString("roomAndObject");
        }else{
            String[] rendomRoomAndObject = RoomUtil.randomRoomWithObject(this);
            roomObject = rendomRoomAndObject[0];
            roomAndObject = rendomRoomAndObject[1];

            if(getIntent().hasExtra("ID")) id = getIntent().getExtras().getInt("ID");
        }

        viewModel = ViewModelProviders.of(this, new VMFactory_WakeUp(getApplication(), id)).get(ViewModel_WakeUp.class);
        viewModel.setObject(roomObject);
        mAlarm = viewModel.getmAlarm();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wake_up);
        mBinding.setTime(TimeUtil.timeView(this, mAlarm.getHour(), mAlarm.getMinute()));
        mBinding.setRoom(roomAndObject);

        //Turn off ringtone button if TEST
        if(getIntent().getAction() != null && getIntent().getAction().equals("test")) mBinding.wuFabOff.show();

        //Set up mediaplayer -> start playing in service and bind volume
        setUpMediaPlayer();

        //Since binding with timer in onResume

        setUpAccelerometer();

        //Observe objectCaptured
        viewModel.isObjectCaptured().observe(this, objectCaptured -> {

            Intent intent = new Intent(this, ForegroundService_AlarmSound.class);
            intent.setAction(ForegroundService_AlarmSound.ACTION_STOP);
            startService(intent);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, 2000);
        });
    }

    public void setUpAccelerometer(){
        //Initialise accelerometer
        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        equalizer = accelerometer.getMaximumRange() > 120 ? accelerometer.getMaximumRange() * 20 : accelerometer.getMaximumRange() * 30;
    }

    //Bind volume and start ForegrounService
    public void setUpMediaPlayer(){

        //Volume
        volumeStart = (float) mAlarm.getVolume();
        mVolume = mVolume == 0 ? volumeStart : mVolume;

        //Bind volume
        mBinding.setVolume(getString(R.string.volume_percentage, String.format("%.2f", (mVolume/volumeStart)*100)));

        //Start ForeGroundService
        Intent intent = new Intent(this, ForegroundService_AlarmSound.class);
        intent.setAction(ForegroundService_AlarmSound.ACTION_START);
        intent.putExtra("volume", mVolume);
        intent.putExtra("sound", mAlarm.getSound());

        startService(intent);
    }

    //Update volume info
    private synchronized void updateVolume(boolean bounce){
        Intent intent = new Intent(this, ForegroundService_AlarmSound.class);
        intent.setAction(ForegroundService_AlarmSound.ACTION_CHANGE_VOLUME);


        float volumePercent = mVolume / volumeStart;

        if(bounce){
            if(volumePercent + 0.004678F <= 1.0F){
                volumePercent += 0.004678F;
                mVolume = volumeStart * volumePercent;

                //Send changes !!!! MediaPlayer volume between 1.0f and 0
                intent.putExtra("volume", (volumePercent* volumeStart)/SoundUtil.maxVolume(this));
                startService(intent);

                String volumePer = String.format("%.2f", volumePercent*100);
                mBinding.setVolume(getString(R.string.volume_percentage, volumePer));
            }else if(volumePercent < 1.0){
                mVolume = volumeStart;

                String volumePer = String.format("%.2f", 100.00);
                mBinding.setVolume(getString(R.string.volume_percentage, volumePer));
            }
        }else{

            //Lower volume by acceleration sensor
            if(volumePercent - (Math.abs(mAccel) / equalizer) >= 0.0F){
                volumePercent -= (Math.abs(mAccel) / equalizer);
            }else{
                volumePercent = 0.0F;
            }

            //Dont't register very smal changes when volume is at 100%
            if(volumePercent < 0.996F ){
                mVolume = volumePercent * volumeStart;

                //Send changes
                intent.putExtra("volume", (volumePercent* volumeStart)/SoundUtil.maxVolume(this));
                startService(intent);

                String volumePer = String.format("%.2f", volumePercent*100);
                mBinding.setVolume(getString(R.string.volume_percentage, volumePer));
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float[] mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 1){
                updateVolume(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }


    /*
    *OnClick methods
    */
    public void fabOnClick(View view){
        Intent intent = new Intent(this, ForegroundService_AlarmSound.class);
        intent.setAction(ForegroundService_AlarmSound.ACTION_STOP);
        startService(intent);
    }

    public void cameraOnClick(View view){
        /*if (savedInstanceState != null) {
            return;
        }*/

        FragmentManager fragmentManager = getSupportFragmentManager();
        Camera2BasicFragment fragment = (Camera2BasicFragment) fragmentManager.findFragmentByTag("CameraFragment");

        if(fragment == null){
            Camera2BasicFragment cameraFragment = new Camera2BasicFragment();
            fragmentManager.beginTransaction().add(R.id.wu_fragment_container, cameraFragment, "CameraFragment").commit();

            findViewById(R.id.wu_group).setVisibility(View.INVISIBLE);
            findViewById(R.id.wu_changeObject).setVisibility(View.VISIBLE);

            //Stop since_in count
            sinceTimer.cancel();
        }


        //TODO replace
        /*transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();*/

        //startActivity(new Intent(this, ImageDisplay.class));
    }

    public void changeObject(View view){
        String[] rendomRoomAndObject = RoomUtil.randomRoomWithObject(this);
        roomObject = rendomRoomAndObject[0];
        roomAndObject = rendomRoomAndObject[1];

        viewModel.setObject(roomObject);
        mBinding.setRoom(roomAndObject);
    }


    /*
    *Lifecycle methods
    */
    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        //Start sinceTimer where it left
        if(sincePaused != 0){
            sinceCount += (Calendar.getInstance().getTimeInMillis() - sincePaused) / 1000;
        }

        sinceTimer = new Timer();
        sinceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sinceCount++;
                mBinding.setSince(TimeUtil.since_in(getApplicationContext(), sinceCount));
            }
        }, 0, 1000);

        bounceTimer = new Timer();
        bounceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateVolume(true);
            }
        }, 0, 250);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("since_count", sinceCount);
        outState.putLong("since_paused", sincePaused);
        outState.putFloat("volume", mVolume);
        outState.putInt("ID", id);
        outState.putString("roomObject", roomObject);
        outState.putString("roomAndObject", roomAndObject);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //1
        sensorMan.unregisterListener(this);
        //2
        sincePaused = Calendar.getInstance().getTimeInMillis();
        sinceTimer.cancel();
        sinceTimer = null;
        //3
        bounceTimer.cancel();
        bounceTimer = null;
    }
}
