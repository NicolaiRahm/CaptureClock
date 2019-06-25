package tensorflow.alarm_clock.nicolai.com.roomup.viewModels;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.AlarmRepository;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SetAlarmUtil;

import java.util.List;

public class ViewModel_MainAlarm extends AndroidViewModel {

    private Context context;
    private AlarmRepository mRepository;
    private LiveData<List<Alarm_POJO>> alarmList;
    private MutableLiveData<Alarm_POJO> mutableAlarm = new MutableLiveData<>();
    private Alarm_POJO notLiveAlarm;
    private MutableLiveData<Boolean> chooseRingtone = new MutableLiveData<>();

    public ViewModel_MainAlarm(@NonNull Application application) {
        super(application);

        context = application;
        mRepository = new AlarmRepository(application);
        alarmList = mRepository.getAllAlarms();
        setChooseRingtone(false);
    }

    //Get list
    public LiveData<List<Alarm_POJO>> getAllAlarms(){
        return alarmList;
    }

    //Delete by id
    public void deleteById(int id){
        Alarm_POJO deletedAlarm = mRepository.getById(id);
        if(deletedAlarm.isOn()){
            deletedAlarm.changeOn();
            SetAlarmUtil.setOn_OR_Off(context, deletedAlarm);
        }

        mRepository.deleteById(id);
    }

    //@Update
    public void update(){
        //Is at least one day chosen?
        if(notLiveAlarm.getDays().atLeastOne()){
            SetAlarmUtil.setOn_OR_Off(context, notLiveAlarm);
        }else if (notLiveAlarm.isOn()){
            notLiveAlarm.changeOn();
            SetAlarmUtil.setOn_OR_Off(context, notLiveAlarm);
        }

        mRepository.update(notLiveAlarm);
    }

    //@Insert
    public void insert(Alarm_POJO newAlarm){
        int idInsert = (int) mRepository.insert(newAlarm);
        SetAlarmUtil.setOn_OR_Off(context, mRepository.getById(idInsert));
    }


    //Id of clicked item
    public void setId_EditAlarm(int id){
        mutableAlarm.setValue(mRepository.getById(id));
        notLiveAlarm = mutableAlarm.getValue();
    }
    //Get clicked item
    public LiveData<Alarm_POJO> getLiveById(){
        return mutableAlarm;
    }
    //Not live -> no infinite loop
    public Alarm_POJO getById(){
        return mutableAlarm.getValue();
    }

    //Change alarm
    public void changeDay(int day){
        switch (day){
            case 0: notLiveAlarm.getDays().changeMonday(); break;
            case 1: notLiveAlarm.getDays().changeTuesday(); break;
            case 2: notLiveAlarm.getDays().changeWednesday(); break;
            case 3: notLiveAlarm.getDays().changeThursday(); break;
            case 4: notLiveAlarm.getDays().changeFriday(); break;
            case 5: notLiveAlarm.getDays().changeSaturday(); break;
            case 6: notLiveAlarm.getDays().changeSunday(); break;
        }

        mutableAlarm.setValue(notLiveAlarm);
    }

    public void changeWeekly(CompoundButton buttonView, boolean isChecked){
        if(isChecked != notLiveAlarm.isWeekly()){
            notLiveAlarm.changeWeekly();
        }
    }

    public void setVolume(SeekBar seekBar, int progress, boolean fromUser){
        notLiveAlarm.setVolume(progress);
    }

    public void setName(Editable s){
        notLiveAlarm.setName(s.toString());
    }

    public Alarm_POJO changeOn(int id){
        Alarm_POJO alarm = mRepository.getById(id);
        alarm.changeOn();

        if(alarm.getDays().atLeastOne()){
            SetAlarmUtil.setOn_OR_Off(context, alarm);
        }else {
            Alarm_POJO alarmWithDays = SetAlarmUtil.selectNearestDay(alarm);
            SetAlarmUtil.setOn_OR_Off(context, alarmWithDays);
        }

        mRepository.update(alarm);

        return alarm;
    }

    public void setSound(String sound){
        notLiveAlarm.setSound(sound);
        mutableAlarm.setValue(notLiveAlarm);
    }

    public void setTime(int hour, int minute){
        notLiveAlarm.changeTime(hour, minute);
    }


    //Choose ringtone

    public LiveData<Boolean> isChooseRingtone() {
        return chooseRingtone;
    }

    public void setChooseRingtone(boolean chooseRingtone) {
        this.chooseRingtone.setValue(chooseRingtone);
    }
}
