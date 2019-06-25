package tensorflow.alarm_clock.nicolai.com.roomup.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import tensorflow.alarm_clock.nicolai.com.roomup.R;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Days;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.TimeUtil;

import java.lang.ref.WeakReference;

public class RecyclerView_AlarmsAdapter extends ListAdapter<Alarm_POJO, RecyclerView_AlarmsAdapter.WeckerHolder> {

    private final RecyclerView_AlarmsAdapter.EditAlarmClock mEditAlarmClock;

    //Interface for clicks
    public interface EditAlarmClock{
        void itemClicked(int position);
        void timeClicked(int position);
        void onOffButton(int position);
    }


    //################## Holder

    public static class WeckerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView time, daysView, name, point;
        private Switch onPause;
        private ImageView repeatImage;
        protected ConstraintLayout constraintBackgroun;
        private WeakReference<EditAlarmClock> mWeakRefernce;

        public WeckerHolder(View v, RecyclerView_AlarmsAdapter.EditAlarmClock listener) {
            super(v);
            mWeakRefernce = new WeakReference<>(listener);

            time = v.findViewById(R.id.time);
            point = v.findViewById(R.id.point);
            name = v.findViewById(R.id.name);
            onPause = v.findViewById(R.id.running_switch);
            repeatImage = v.findViewById(R.id.imageView3);
            repeatImage.setImageAlpha(80);
            daysView = v.findViewById(R.id.endsIn);
            constraintBackgroun = v.findViewById(R.id.view_foreground);

            //OnClickListeners
            v.setOnClickListener(this);
            time.setOnClickListener(this);
            onPause.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final int vID = v.getId();

            //OnOff Button
            if(vID == onPause.getId()) {
                mWeakRefernce.get().onOffButton(getAdapterPosition());

            }else if(vID == time.getId()){
                mWeakRefernce.get().timeClicked(getAdapterPosition());

                //Whole row was clicked
            }else {
                mWeakRefernce.get().itemClicked(getAdapterPosition());
            }
        }

        public void bindWecker(final Alarm_POJO currentAlarm) {
            time.setText(TimeUtil.timeView(itemView.getContext(), currentAlarm.getHour(), currentAlarm.getMinute()));
            point.setVisibility(currentAlarm.getName().isEmpty() ? View.INVISIBLE : View.VISIBLE);
            name.setText(currentAlarm.getName());
            onPause.setChecked(currentAlarm.isOn());

            if(currentAlarm.isWeekly()){ repeatImage.setVisibility(View.VISIBLE); } else {repeatImage.setVisibility(View.GONE);}
            showDays(currentAlarm.getDays());
        }

        //Visualisierung der Tage wenn da kein Sendername steht
        private void showDays(Days days){
            String Mo = "" + itemView.getResources().getString(R.string.enum_monday).charAt(0);
            String Di = " " + itemView.getResources().getString(R.string.enum_tuesday).charAt(0);//"<font color='#EE0000'>red</font>";
            String Mi = " " + itemView.getResources().getString(R.string.enum_wednesday).charAt(0);
            String Do = " " + itemView.getResources().getString(R.string.enum_thursday).charAt(0);
            String Fr = " " + itemView.getResources().getString(R.string.enum_friday).charAt(0);
            String Sa = " " + itemView.getResources().getString(R.string.enum_saturday).charAt(0);
            String So = " " + itemView.getResources().getString(R.string.enum_sunday).charAt(0);

            if(days.isMonday()){
                Mo = "<font color='#fcfdfd'>" + Mo + "</font>";
            }

            if(days.isTuesday()){
                Di = "<font color='#fcfdfd'>" + Di + "</font>";
            }

            if(days.isWednesday()){
                Mi = "<font color='#fcfdfd'>" + Mi + "</font>";
            }

            if(days.isThursday()){
                Do = "<font color='#fcfdfd'>" + Do + "</font>";
            }

            if(days.isFriday()){
                Fr = "<font color='#fcfdfd'>" + Fr + "</font>";
            }

            if(days.isSaturday()){
                Sa = "<font color='#fcfdfd'>" + Sa + "</font>";
            }

            if(days.isSunday()){
                So = "<font color='#fcfdfd'>" + So + "</font>";
            }

            daysView.setText(Html.fromHtml(Mo + Di + Mi + Do + Fr + Sa + So));
        }
    }

    @Override
    public RecyclerView_AlarmsAdapter.WeckerHolder onCreateViewHolder(ViewGroup parent, int viewType) { //RecyclerAdapter.
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);

        return new RecyclerView_AlarmsAdapter.WeckerHolder(inflatedView, mEditAlarmClock);
    }

    //#################


    public RecyclerView_AlarmsAdapter(RecyclerView_AlarmsAdapter.EditAlarmClock editAlarmClock) {
        super(DIFF_CALLBACK);

        mEditAlarmClock = editAlarmClock;
    }

    public static final DiffUtil.ItemCallback<Alarm_POJO> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Alarm_POJO>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Alarm_POJO oldWecker, @NonNull Alarm_POJO newWecker) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldWecker.getId() == newWecker.getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull Alarm_POJO oldWecker, @NonNull Alarm_POJO newWecker) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldWecker.equals(newWecker);
                }
            };

    @Override
    public void onBindViewHolder(@NonNull WeckerHolder holder, int position) {

        Alarm_POJO currentWecker = getItem(position);
        holder.bindWecker(currentWecker);
    }



    //Costum methods
    public int getID(int position){
        return getItem(position).getId();
    }

    //Damit list element gleich und animation flüssiger läuft
    /*public void setOnOff(int position, boolean onOff){
        getItem(position).setOn_off(onOff);
    }*/
}