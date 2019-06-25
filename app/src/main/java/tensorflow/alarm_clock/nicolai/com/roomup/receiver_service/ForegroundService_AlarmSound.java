package tensorflow.alarm_clock.nicolai.com.roomup.receiver_service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import tensorflow.alarm_clock.nicolai.com.roomup.TimerFullScreen;
import tensorflow.alarm_clock.nicolai.com.roomup.WakeUp;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.AlarmAlertWakeLock;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.NotificationUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SoundUtil;

import java.io.IOException;

public class ForegroundService_AlarmSound extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    public static final String ACTION_BROADCAST = "ALRAM_RECEIVER";
    public static final String ACTION_START = "START";
    public static final String ACTION_STOP = "STOP";
    public static final String ACTION_CHANGE_VOLUME = "CHANGE_VOLUME";

    private MediaPlayer mp;
    private float volume;
    private String sound;

    private boolean alreadySetUp, alreadyStarted;

    public ForegroundService_AlarmSound() { }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_BROADCAST:
                    if(!alreadySetUp){
                        alreadySetUp = true;
                        startForegroundService(intent.getIntExtra("ID", 0));
                    }
                    break;
                case ACTION_START:
                    if(!alreadyStarted){
                        alreadyStarted = true;

                        volume = intent.getFloatExtra("volume", 3);
                        SoundUtil.changeVolume(getApplicationContext(), (int) volume);
                        sound = intent.getStringExtra("sound");
                        setUpMediaPlayer();
                    }
                    break;
                case ACTION_STOP:
                    stopForegroundService();
                    break;
                case ACTION_CHANGE_VOLUME: //!!!! MediaPlayer volume between max:1.0f and min:0.0f
                    if(mp != null && mp.isPlaying()){
                        float changedVolume = intent.getFloatExtra("volume", 0.75f);
                        mp.setVolume(changedVolume, changedVolume);
                    }

                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService(int id) {
        startForeground(NotificationUtil.SERVICE_NOTIFICATION, NotificationUtil.alarmNotification(getApplicationContext(), id));

        Intent i = new Intent(getApplicationContext(), WakeUp.class);
        i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        i.putExtra("ID", id);
        getApplication().startActivity(i);
    }

    private void stopForegroundService() {

        if(mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }

        AlarmAlertWakeLock.releaseCpuLock();

        stopForeground(true);
        stopSelf();
    }

    public void setUpMediaPlayer(){

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
        //mp.setVolume(volume / SoundUtil.maxVolume(getApplicationContext()), SoundUtil.maxVolume(getApplicationContext()));
        mp.setLooping(true);
        mp.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mp.setOnErrorListener(this);
        mp.setOnPreparedListener(this);
        try {
            mp.setDataSource(getApplicationContext(), Uri.parse(sound));
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        //TODO vlt. da einsteigen wo aufgehört
        mp.reset();
        mp.release();
        mp = null;
        setUpMediaPlayer();

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mp != null){
            mp.release();
        }

        AlarmAlertWakeLock.releaseCpuLock();
    }
}
