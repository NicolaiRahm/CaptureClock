package tensorflow.alarm_clock.nicolai.com.roomup.receiver_service;

import android.os.Build;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.AlarmRoomDatabase;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.AlarmAlertWakeLock;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.AsyncHandler;
import tensorflow.alarm_clock.nicolai.com.roomup.utils.SetAlarmUtil;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        final PendingResult result = goAsync();
        final PowerManager.WakeLock wl = AlarmAlertWakeLock.createPartialWakeLock(context);
        wl.acquire();

        AsyncHandler.post(() -> {
            try {
                if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // All N devices have split storage areas, but we may need to
                    // move the existing preferences to the new device protected
                    // storage area, which is where the data lives from now on.

                    final Context deviceContext = context.createDeviceProtectedStorageContext();
                    //true if the move was successful or if the database didn't exist in the source context, otherwise false.
                    //--> wird nur ausgeführt wenn auf >= 24 geupdated wird
                    deviceContext.moveDatabaseFrom(context, AlarmRoomDatabase.DATABASE_NAME);

                    //AlarmRoomDatabase.class uses context.createDeviceProtectedStorageContext() for >= Version 24
                    SetAlarmUtil.setAfterBoot(context);
                } else if(Intent.ACTION_BOOT_COMPLETED.equals(action) && Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                    SetAlarmUtil.setAfterBoot(context);
                }
            } finally {
                result.finish();
                wl.release();
            }
        });
    }
}
