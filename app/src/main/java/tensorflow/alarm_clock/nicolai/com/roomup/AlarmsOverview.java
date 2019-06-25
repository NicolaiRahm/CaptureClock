package tensorflow.alarm_clock.nicolai.com.roomup;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import tensorflow.alarm_clock.nicolai.com.roomup.dialogs.TimePickerDF;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SetAlarmUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.TimeUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_MainAlarm;
import tensorflow.alarm_clock.nicolai.com.roomup.adapter.RecyclerItemTouchHelper;
import tensorflow.alarm_clock.nicolai.com.roomup.adapter.RecyclerView_AlarmsAdapter;
import tensorflow.alarm_clock.nicolai.com.roomup.dialogs.EditAlarmDF;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;

import java.util.ArrayList;

public class AlarmsOverview extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private View view;
    private RecyclerView_AlarmsAdapter mAdapter;
    private ViewModel_MainAlarm viewModel;

    public AlarmsOverview() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarms_overview, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(ViewModel_MainAlarm.class);

        //LayoutManager for RecyclerView
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        //RecyclerView ListAdapter
        mAdapter = new RecyclerView_AlarmsAdapter(new RecyclerView_AlarmsAdapter.EditAlarmClock() {
            @Override
            public void itemClicked(int position) {
                viewModel.setId_EditAlarm(mAdapter.getID(position));
                EditAlarmDF editAlarmDF = new EditAlarmDF();
                editAlarmDF.show(getFragmentManager(), "EditAlarm");
            }

            @Override
            public void timeClicked(int position) {
                viewModel.setId_EditAlarm(mAdapter.getID(position));

                Alarm_POJO clickedAlarm = viewModel.getById();
                TimePickerDF newFragment = TimePickerDF.newInstance(clickedAlarm.getHour(), clickedAlarm.getMinute(), false);
                newFragment.show(getFragmentManager(), "TimePicker");
            }

            @Override
            public void onOffButton(final int position) {
                Alarm_POJO alarm = viewModel.changeOn(mAdapter.getID(position));

                if(alarm.isOn()) {
                    long secondsToGo = SetAlarmUtil.secondsToGo(alarm);
                    String in = getString(R.string.ringsIn, TimeUtil.since_in(getContext(), secondsToGo));

                    //Toast.makeText(MainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator_main), in, Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    snackbar.show();
                }
            }
        });
        mAdapter.submitList(new ArrayList<Alarm_POJO>());

        //Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.ao_recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        //For SwipeDelete to function
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, AlarmsOverview.this);
        ItemTouchHelper swipeHelper = new ItemTouchHelper(itemTouchHelperCallback);
        swipeHelper.attachToRecyclerView(recyclerView);

        //Get the data from the ViewModel
        viewModel.getAllAlarms().observe(this, alarms -> {
            getActivity().runOnUiThread(() -> {
                mAdapter.submitList(alarms);
            });
        });

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        final int id = mAdapter.getID(position);
        viewModel.deleteById(id);
    }
}
