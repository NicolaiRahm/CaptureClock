package tensorflow.alarm_clock.nicolai.com.roomup;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Legal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
    }

    public void privacyPolicy(View view){
        String url = "https://nicolairahm.wixsite.com/apparently/blank-1";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
