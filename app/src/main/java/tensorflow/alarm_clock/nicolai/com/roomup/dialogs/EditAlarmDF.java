package tensorflow.alarm_clock.nicolai.com.roomup.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.snackbar.Snackbar;
import tensorflow.alarm_clock.nicolai.com.roomup.R;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SetAlarmUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.TimeUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_MainAlarm;
import tensorflow.alarm_clock.nicolai.com.roomup.databinding.EditAlarmLayoutBinding;

public class EditAlarmDF extends DialogFragment {
    private EditAlarmLayoutBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final ViewModel_MainAlarm viewModel = ViewModelProviders.of(getActivity()).get(ViewModel_MainAlarm.class);

        //Bind to layout
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.edit_alarm_layout, null, false);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(viewModel);

        return new AlertDialog.Builder(getActivity()).setView(mBinding.getRoot())
                .setPositiveButton(R.string.set_alarm, (dialog, which) -> {
                    if(viewModel.getById().isOn() && viewModel.getById().getDays().atLeastOne()){
                        long secondsToGo = SetAlarmUtil.secondsToGo(viewModel.getById());
                        String in = getString(R.string.ringsIn, TimeUtil.since_in(getContext(), secondsToGo));

                        //Toast.makeText(MainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator_main), in, Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        snackbar.show();
                    }

                    viewModel.update();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}