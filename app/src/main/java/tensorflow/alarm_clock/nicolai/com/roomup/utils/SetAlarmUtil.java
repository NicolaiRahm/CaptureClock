package tensorflow.alarm_clock.nicolai.com.roomup.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import tensorflow.alarm_clock.nicolai.com.roomup.MainActivity;
import tensorflow.alarm_clock.nicolai.com.roomup.receiver_service.Alarm_Receiver;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.DaysConverter;
import tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.AlarmRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SetAlarmUtil {

    public static void setOn_OR_Off(Context context, Alarm_POJO alarm){
        if(alarm.isOn()){
            setAlarm(context, alarm);
        }else{
            unsetAlarm(context, alarm);
        }
    }

    private static void setAlarm(Context context, Alarm_POJO alarm){
        Intent alarmIntent = new Intent(context, Alarm_Receiver.class);
        alarmIntent.putExtra("ID", alarm.getId());
        alarmIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long ringsAt = ringsAt(alarm);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //Den AlarmManager setzen. Pending_intent wird bis zu der Zeit zurückgehalten.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent i2 = new Intent(context, MainActivity.class);
            PendingIntent pi2 = PendingIntent.getActivity(context, alarm.getId(), i2, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(ringsAt, pi2);
            alarmManager.setAlarmClock(ac, pendingIntent); //Holt handy aus doze kurz vor alarm
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, ringsAt, pendingIntent);
        }
    }

    private static void unsetAlarm(Context context, Alarm_POJO alarm){
        Intent alarmIntent = new Intent(context, Alarm_Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //cancel the alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static Alarm_POJO selectNearestDay(Alarm_POJO alarm){

        Calendar calendar = Calendar.getInstance();

        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int current_minute = calendar.get(Calendar.MINUTE);

        //day als Tage des Jahres
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //calendar.set(Calendar.DAY_OF_MONTH, day);//damit Kalendartag wenn erhöht zurück gesetzt wird
        if ((alarm.getHour() < current_hour) || (alarm.getHour() == current_hour && alarm.getMinute() < current_minute)) {
            day = day + 1;
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.GERMAN);
        weekDay = dayFormat.format(calendar.getTime());

        switch (weekDay){
            case "Montag":  alarm.getDays().changeMonday(); break;
            case "Dienstag": alarm.getDays().changeTuesday(); break;
            case "Mittwoch": alarm.getDays().changeWednesday(); break;
            case "Donnerstag": alarm.getDays().changeThursday(); break;
            case "Freitag": alarm.getDays().changeFriday(); break;
            case "Samstag": alarm.getDays().changeSaturday(); break;
            case "Sonntag": alarm.getDays().changeSunday(); break;
        }

        return alarm;
    }

    private static long ringsAt(Alarm_POJO alarm){
        Calendar calendar = Calendar.getInstance();
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int current_minute = calendar.get(Calendar.MINUTE);
        int weekDayInt = calendar.get(Calendar.DAY_OF_WEEK);//Sonnatg = 1

        //Kalender mit eingegebener Stunde und Minute füllen
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        int distance = 0;
        //Wenn der nächste Termin nich mehr heute wäre
        if ((alarm.getHour() < current_hour) || (alarm.getHour() == current_hour && alarm.getMinute() <= current_minute)) {
            weekDayInt++;
            distance++;
        }

        //Wenn Sonntag 1-2 -> 6  also weekdayInt = 8
        if(weekDayInt == 1){
            weekDayInt = 8;
        }

        //Solange der nächste Tag nicht geklickt ist distance erhöhen
        while (DaysConverter.fromDays(alarm.getDays()).charAt(weekDayInt - 2) == 'n'){
            if(weekDayInt == 8){
                weekDayInt = 2;
            }else{
                weekDayInt++;
            }

            distance++;
        }


        //Gestellten Wecker mit Stunden, Minuten, Sekund mit dem nächsten Tag verrechnen
        return calendar.getTimeInMillis() + distance * 24*60*60*1000;
    }
    public static long secondsToGo(Alarm_POJO alarm){
        return (ringsAt(alarm) - Calendar.getInstance().getTimeInMillis()) / 1000;
    }

    //After alarm clock has gone off
    public static Alarm_POJO newSetUp(Context context, Alarm_POJO mAlarm){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.GERMAN);
        String thisDay = dayFormat.format(calendar.getTime());

        if(mAlarm.isWeekly()){
            setAlarm(context, mAlarm);
        }else{
            mAlarm.getDays().unCheck(thisDay);
            if(mAlarm.getDays().atLeastOne()) setAlarm(context, mAlarm); else mAlarm.changeOn();
        }

        return mAlarm;
    }

    public static void setAfterBoot(Context context){

        AlarmRepository mRepository = new AlarmRepository(context);
        List<Alarm_POJO> list = mRepository.getBootList();

        for(Alarm_POJO alarm : list){
            if(alarm.isOn()) setAlarm(context, alarm);
        }
    }
}
