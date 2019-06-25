package tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase;

import android.content.Context;
import android.os.Build;
import androidx.room.*;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.DaysConverter;

@Database(entities = {Alarm_POJO.class}, version = 5, exportSchema = false)
@TypeConverters({DaysConverter.class})
public abstract class AlarmRoomDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "alarm_database";
    public abstract Alarm_Dao alarmDao();

    private static volatile AlarmRoomDatabase INSTANCE;

    public static AlarmRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AlarmRoomDatabase.class) {
                if (INSTANCE == null) {
                    //Use different context for direct boot
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        INSTANCE = Room.databaseBuilder(context.createDeviceProtectedStorageContext(), AlarmRoomDatabase.class, DATABASE_NAME)
                                .fallbackToDestructiveMigration()
                                .build();
                    } else {
                        INSTANCE = Room.databaseBuilder(context, AlarmRoomDatabase.class, DATABASE_NAME)
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
            }
        }

        return INSTANCE;
    }
}
