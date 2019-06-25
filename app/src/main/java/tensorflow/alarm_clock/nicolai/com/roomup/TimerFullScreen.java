package tensorflow.alarm_clock.nicolai.com.roomup;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Timer_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.viewModels.ViewModel_MainTimer;

public class TimerFullScreen extends AppCompatActivity {

    private ViewModel_MainTimer viewModel;
    private int id = -10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_full_screen);

        Window window = getWindow();
        //window.setBackgroundDrawable(null);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        viewModel = ViewModelProviders.of(this).get(ViewModel_MainTimer.class);
        id = getIntent().getIntExtra("ID", -10);
        if(id != -10) viewModel.setCurrentTimer(viewModel.getById(id));
        viewModel.setFullScreen(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        id = intent.getIntExtra("ID", -10);
        if(id != -10) viewModel.setCurrentTimer(viewModel.getById(id));
    }
}
