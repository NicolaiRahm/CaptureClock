package tensorflow.alarm_clock.nicolai.com.roomup;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import tensorflow.alarm_clock.nicolai.com.roomup.databinding.ActivityShareRateBinding;

public class ShareRate extends AppCompatActivity {

    private ActivityShareRateBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_share_rate);

        /*setSupportActionBar(mBinding.toolbarShare);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.share_and_rate));*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void share(View view){
        String url = "https://goo.gl/fqqZy4";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message,  url));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_chooser)));
    }

    public void rate(View view){
        /*
         * Start with rating the app
         * Determine if the Play Store is installed on the device
         *
         * */

        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    public void feedback(View view){
        Intent i = new Intent(Intent.ACTION_SENDTO);
        //i.setDataAndType(Uri.parse("mailto:nicolairahm@gmail.com"), "message/rfc822");
        //i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:nicolairahm@gmail.com"));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"nicolairahm@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, getPackageName()));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.give_feedback)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
