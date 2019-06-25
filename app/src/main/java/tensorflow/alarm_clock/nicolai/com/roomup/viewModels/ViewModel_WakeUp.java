package tensorflow.alarm_clock.nicolai.com.roomup.viewModels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Days;
import tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.AlarmRepository;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SetAlarmUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SoundUtil;

public class ViewModel_WakeUp extends AndroidViewModel {

    private AlarmRepository mRepository;
    private Alarm_POJO mAlarm;
    private MutableLiveData<String> mObject = new MutableLiveData<>();

    private MutableLiveData<Boolean> objectCaptured = new MutableLiveData<>();

    public ViewModel_WakeUp(@NonNull Application application, int id) {
        super(application);

        if(id == 0){
            //TODO only test

            mAlarm = new Alarm_POJO("", true, 7, 30,
                    new Days(), true, 4, SoundUtil.getDefaultUri());

        }else{
            mRepository = new AlarmRepository(application);
            mAlarm = mRepository.getById(id);

            //kill this day and turn on / off
            newSetUp(application);
        }

        //setObjectCaptured(false);
    }

    public void newSetUp(Context context){
        Alarm_POJO newAlarm = SetAlarmUtil.newSetUp(context, mAlarm);
        mRepository.update(newAlarm);
    }

    public Alarm_POJO getmAlarm(){
        return mAlarm;
    }


    //Get-Set objectCaptured

    public LiveData<Boolean> isObjectCaptured() {
        return objectCaptured;
    }

    public void setObjectCaptured(boolean objectCaptured) {
        this.objectCaptured.postValue(objectCaptured);
    }


    //Get-Set object

    public LiveData<String> getObject() {
        return mObject;
    }

    public void setObject(String mObject) {
        this.mObject.setValue(mObject);
    }
}
