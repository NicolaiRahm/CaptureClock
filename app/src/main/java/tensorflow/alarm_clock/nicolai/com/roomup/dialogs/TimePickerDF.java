package tensorflow.alarm_clock.nicolai.com.roomup.dialogs;

/**
 * Created by Nicolai on 09.03.2018.
 */

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.snackbar.Snackbar;
import tensorflow.alarm_clock.nicolai.com.roomup.R;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SetAlarmUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SoundUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.TimeUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_MainAlarm;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Days;

import java.util.Calendar;

public class TimePickerDF extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private int hourBegin;
    private int minuteBegin;
    private boolean newAlarm;

    public static TimePickerDF newInstance(int hour, int minute, boolean newAlarm) {
        TimePickerDF fragment = new TimePickerDF();
        Bundle args = new Bundle();
        args.putInt("hour", hour);
        args.putInt("minute", minute);
        args.putBoolean("newAlarm", newAlarm);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hourBegin = getArguments().getInt("hour");
            minuteBegin = getArguments().getInt("minute");
            newAlarm = getArguments().getBoolean("newAlarm");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), R.style.TimeDialogTheme,this, hourBegin, minuteBegin,
                DateFormat.is24HourFormat(getActivity()));

    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hour, int minute){
        ViewModel_MainAlarm viewModel = ViewModelProviders.of(getActivity()).get(ViewModel_MainAlarm.class);

        String in = "";
        if(newAlarm){

            Alarm_POJO newAlarm = new Alarm_POJO("", true, hour, minute,
                    new Days(), false, SoundUtil.getDefaultVolume(getContext()), SoundUtil.getDefaultUri());
            //Chose the next day and insert
            newAlarm = SetAlarmUtil.selectNearestDay(newAlarm);
            viewModel.insert(newAlarm);

            long secondsToGo = SetAlarmUtil.secondsToGo(newAlarm);
            in = getString(R.string.ringsIn, TimeUtil.since_in(getContext(), secondsToGo));
        }else{
            viewModel.setTime(hour, minute);
            viewModel.update();

            Alarm_POJO updatedAlarm = viewModel.getById();
            if(updatedAlarm.isOn()){
                long secondsToGo = SetAlarmUtil.secondsToGo(updatedAlarm);
                in = getString(R.string.ringsIn, TimeUtil.since_in(getContext(), secondsToGo));
            }
        }

        if(!in.isEmpty()){
            //Toast.makeText(MainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator_main), in, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}