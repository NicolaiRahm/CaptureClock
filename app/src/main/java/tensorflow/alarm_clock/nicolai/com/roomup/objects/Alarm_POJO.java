package tensorflow.alarm_clock.nicolai.com.roomup.objects;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm_table")
public class Alarm_POJO {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private boolean on;
    private int hour;
    private int minute;
    private Days days;
    private boolean weekly;
    private int volume;
    private String sound;

    public Alarm_POJO(int id, String name, boolean on, int hour, int minute, Days days, boolean weekly, int volume, String sound) {
        this.id = id;
        this.name = name;
        this.on = on;
        this.hour = hour;
        this.minute = minute;
        this.days = days;
        this.weekly = weekly;
        this.volume = volume;
        this.sound = sound;
    }

    @Ignore
    //For insert -> no id
    public Alarm_POJO(String name, boolean on, int hour, int minute, Days days, boolean weekly, int volume, String sound) {
        this.name = name;
        this.on = on;
        this.hour = hour;
        this.minute = minute;
        this.days = days;
        this.weekly = weekly;
        this.volume = volume;
        this.sound = sound;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public boolean isOn() {
        return on;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public Days getDays() {
        return days;
    }

    public boolean isWeekly() {
        return weekly;
    }

    public int getVolume() {
        return volume;
    }

    public String getSound() {
        return sound;
    }

    //Change
    public void changeWeekly(){
        weekly = !weekly;
    }

    public void changeOn(){
        on = !on;
    }

    public void setVolume(int volume){
        this.volume = volume;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSound(String sound){
        this.sound = sound;
    }

    public void changeTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    //Equals method for RecyclerView ListAdapter
    @Override
    public boolean equals(@Nullable Object obj) {
        if(Alarm_POJO.class.isInstance(obj)){
            Alarm_POJO alarm = (Alarm_POJO) obj;

            if(!this.name.equals(alarm.getName())){
                return false;
            }
            if(this.hour != alarm.hour){
                return false;
            }
            if(this.minute != alarm.minute){
                return false;
            }
            if(!DaysConverter.fromDays(this.days).equals(DaysConverter.fromDays(alarm.days))){
                return false;
            }

            return this.weekly == alarm.weekly;
        }

        return false;
    }
}
