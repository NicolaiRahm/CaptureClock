package tensorflow.alarm_clock.nicolai.com.roomup.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import tensorflow.alarm_clock.nicolai.com.roomup.R;
import tensorflow.alarm_clock.nicolai.com.roomup.databinding.NewTimerLayoutBinding;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.TimeUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_MainTimer;

public class NewTimerDF extends DialogFragment {
    private NewTimerLayoutBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final ViewModel_MainTimer viewModel = ViewModelProviders.of(getActivity()).get(ViewModel_MainTimer.class);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.new_timer_layout, null, false);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(viewModel);

        int[] durationFormat = TimeUtil.formatMillis_H_M_S(viewModel.getNewEditTimer().getDuration());

        //set up number picker
        mBinding.hourPicker.setMinValue(0);
        mBinding.hourPicker.setMaxValue(23);
        //mBinding.hourPicker.setWrapSelectorWheel(false);
        mBinding.hourPicker.setValue(durationFormat[0]);

        mBinding.minutePicker.setMinValue(0);
        mBinding.minutePicker.setMaxValue(59);
        //mBinding.hourPicker.setWrapSelectorWheel(false);
        mBinding.minutePicker.setValue(durationFormat[1]);

        mBinding.secondPicker.setMinValue(0);
        mBinding.secondPicker.setMaxValue(59);
        //mBinding.hourPicker.setWrapSelectorWheel(false);
        mBinding.secondPicker.setValue(durationFormat[2]);

        return new AlertDialog.Builder(getActivity()).setView(mBinding.getRoot())
                .setPositiveButton(R.string.save_timer, (dialog, which) -> {
                    viewModel.setDuration(getPickerValues());
                    if(viewModel.isNewTimer()){
                        viewModel.insert();
                    }else{
                        viewModel.update();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .create();
    }

    private long getPickerValues(){
        return (mBinding.hourPicker.getValue() * 3600 + mBinding.minutePicker.getValue() * 60 + mBinding.secondPicker.getValue()) * 1000;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
