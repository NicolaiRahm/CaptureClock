package tensorflow.alarm_clock.nicolai.com.roomup.utils;

import android.content.Context;
import tensorflow.alarm_clock.nicolai.com.roomup.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {

    //Still seconds or alreday minutes
    public static String since_in(Context context, long time){
        if(time < 60){
            return context.getString(R.string.since_in_seconds, time);
        }else if(time < 3600){
            int seconds = (int) time % 60;
            return context.getString(R.string.since_in_minutes, time / 60, smaller10(seconds));
        }else{
            //round up -> plus one minute
            time = time + 60;
            int minutes = (int) time % 3600 / 60;
            return context.getString(R.string.since_in_hours, time / 3600, smaller10(minutes));
        }
    }

    //Minute 01 in stead of 1
    public static String timeView(Context context, int hour, int minute){
        return context.getString(R.string.time, hour, smaller10(minute));
    }

    // 1:05 instead of 1:5
    private static String smaller10(int value){
        return value < 10 ? "0" + String.valueOf(value) : String.valueOf(value);
    }

    private static String timeHasPassed(boolean timeHasPassed, String time){
        return timeHasPassed ? String.format("- %s", time) : time;
    }

    //Timer
    public static String timerTimeView(Context context, long millisToGo){

        //To add a "-" if time has passed
        boolean timeHasPassed = millisToGo < 0;

        int seconds = (int) Math.abs(millisToGo) / 1000;
        if(seconds < 60){
            return timeHasPassed(timeHasPassed, smaller10(seconds));
        }

        int minuets = seconds / 60;
        if(minuets < 60){
            return timeHasPassed(timeHasPassed, context.getString(R.string.time, minuets, smaller10(seconds % 60)));
        }

        int hours = minuets / 60;
        return timeHasPassed(timeHasPassed, context.getString(R.string.timerLongTime, hours, smaller10(minuets % 60), smaller10(seconds % 3600 % 60)));
    }

    //ProgressCircle
    public static int timerProgress(long duration, long millisLeft){
        if(millisLeft > 0){
            return (int) (duration / 1000 - millisLeft / 1000);
        }

        return (int) duration / 1000;
    }

    public static int[] formatMillis_H_M_S(long millis){
        int[] array = new int[3];
        long seconds = Math.abs(millis) / 1000;

        array[2] = (int) seconds % 3600 % 60; // seconds
        array[1] = (int) seconds % 3600 / 60; //minutes
        array[0] = (int) seconds / 3600; //hours

        return array;
    }

    public static String endsAt(Context context, long endsLong){
        if(endsLong == 0) return "";

        DateFormat df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM);

        if(endsLong > Calendar.getInstance().getTimeInMillis()){
            return context.getString(R.string.ends, df.format(endsLong));
        }else {
            return context.getString(R.string.ended, df.format(endsLong));
        }
    }
}
