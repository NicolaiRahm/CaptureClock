package tensorflow.alarm_clock.nicolai.com.roomup;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import tensorflow.alarm_clock.nicolai.com.roomup.databinding.ActivityMainBinding;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_MainAlarm;
import tensorflow.alarm_clock.nicolai.com.roomup.adapter.SwipeAdapter_Main;
import tensorflow.alarm_clock.nicolai.com.roomup.dialogs.TimePickerDF;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_MainTimer;

public class MainActivity extends AppCompatActivity {

    private ViewModel_MainAlarm viewModel;
    private ViewModel_MainTimer vmTimer;
    private ActivityMainBinding mBinding;

    public static final int RINGTONE_CHOSEN_AlARM = 5;
    public static final int RINGTONE_CHOSEN_TIMER = 11;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case RINGTONE_CHOSEN_AlARM:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    viewModel.setSound(uri.toString());
                }
                break;

            case RINGTONE_CHOSEN_TIMER:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    vmTimer.setSound(uri.toString());
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Remove Overdraw with Them background by activity
        getWindow().setBackgroundDrawable(null);

        //ViewModel
        viewModel = ViewModelProviders.of(this).get(ViewModel_MainAlarm.class);
        vmTimer = ViewModelProviders.of(this).get(ViewModel_MainTimer.class);

        //Initialise SwipeAdapter
        SwipeAdapter_Main swipeAdapter = new SwipeAdapter_Main(getSupportFragmentManager(), getApplicationContext());
        mBinding.viewPager.setOffscreenPageLimit(3);

        //Toolbar
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set up ViewPager and TabLayout
        mBinding.viewPager.setAdapter(swipeAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mBinding.setFabImg(mBinding.viewPager.getCurrentItem() == 0 ? R.drawable.ic_add_alarm_white_24dp : R.drawable.ic_list_white_24dp);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Choose ringtone
        viewModel.isChooseRingtone().observe(this, isChoose -> {
            if (isChoose){
                viewModel.setChooseRingtone(false);
                ringtoneIntent(RINGTONE_CHOSEN_AlARM);
            }
        });

        vmTimer.isChooseRingtone().observe(this, isChosen -> {
            if (isChosen){
                vmTimer.setChooseRingtone(false);
                ringtoneIntent(RINGTONE_CHOSEN_TIMER);
            }
        });

        mBinding.fabNewAlarm.setOnLongClickListener(v -> {
            if(mBinding.viewPager.getCurrentItem() == 0){
                //Start TimePicker DialogFragment
                TimePickerDF newFragment = TimePickerDF.newInstance(7, 30, true);
                newFragment.show(getSupportFragmentManager(), "TimePicker");
            }else{
                Intent i = new Intent(MainActivity.this, WakeUp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setAction("test");
                startActivity(i);
            }

            return true;
        });
    }

    private void ringtoneIntent(int requestCode){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.select_ringtone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
        this.startActivityForResult(intent, requestCode);
    }

    public void fabOnClick(View view){
        if(mBinding.viewPager.getCurrentItem() == 0){
            //Start TimePicker DialogFragment
            TimePickerDF newFragment = TimePickerDF.newInstance(7, 30, true);
            newFragment.show(getSupportFragmentManager(), "TimePicker");
        }else{
            vmTimer.changeMotion();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share_rate://Settings
                startActivity(new Intent(this, ShareRate.class));
                return true;
            case R.id.action_settings://Settings

                return true;
            case R.id.action_help://Settings

                return true;
            case R.id.action_legal://Settings
                startActivity(new Intent(this, Legal.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
