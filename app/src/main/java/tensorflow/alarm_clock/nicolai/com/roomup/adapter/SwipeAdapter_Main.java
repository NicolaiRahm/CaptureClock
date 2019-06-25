package tensorflow.alarm_clock.nicolai.com.roomup.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import tensorflow.alarm_clock.nicolai.com.roomup.AlarmsOverview;
import tensorflow.alarm_clock.nicolai.com.roomup.R;
import tensorflow.alarm_clock.nicolai.com.roomup.Timer;


/**
 * Created by Nicolai on 25.02.2019.
 */

public class SwipeAdapter_Main extends FragmentPagerAdapter {

    public Context context;

    public SwipeAdapter_Main(FragmentManager fm, Context c) {
        super(fm);
        context = c;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position) {
            case 0:
                return  context.getText(R.string.fragment_overview_title);
            case 1:
                return  context.getText(R.string.fragment_timer_title);
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AlarmsOverview();
            case 1:
                return new Timer();
            default:
                return null;
        }
    }
}