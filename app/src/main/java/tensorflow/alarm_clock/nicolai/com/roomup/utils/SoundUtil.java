package tensorflow.alarm_clock.nicolai.com.roomup.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.SeekBar;

import static android.content.Context.AUDIO_SERVICE;

public class SoundUtil {

    public static int getDefaultVolume(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        return (audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) / 2) + 1 ;
    }

    public static int getMaxVolume(SeekBar seekBar){
        AudioManager audioManager = (AudioManager) seekBar.getContext().getSystemService(AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    public static int maxVolume(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    public static String getDefaultUri(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        return uri.toString();
    }

    public static String getTitelForUri(Context context, String uri){
        return RingtoneManager.getRingtone(context, Uri.parse(uri)).getTitle(context);
    }

    public static void changeVolume(Context context, int volume){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
    }
}
