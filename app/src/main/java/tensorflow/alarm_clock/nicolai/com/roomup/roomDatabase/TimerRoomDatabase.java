package tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase;

import android.content.Context;
import android.os.Build;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.StateConverter;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Timer_POJO;

@Database(entities = {Timer_POJO.class}, version = 7, exportSchema = false)
@TypeConverters({StateConverter.class})
public abstract class TimerRoomDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "timer_database";
    public abstract Timer_Dao timerDao();

    private static volatile tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.TimerRoomDatabase INSTANCE;

    public static tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.TimerRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.TimerRoomDatabase.class) {
                if (INSTANCE == null) {
                    //Use different context for direct boot
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        INSTANCE = Room.databaseBuilder(context.createDeviceProtectedStorageContext(), tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.TimerRoomDatabase.class, DATABASE_NAME)
                                .fallbackToDestructiveMigration()
                                .build();
                    } else {
                        INSTANCE = Room.databaseBuilder(context, tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase.TimerRoomDatabase.class, DATABASE_NAME)
                                .fallbackToDestructiveMigration()
                                .build();
                    }

                }
            }
        }

        return INSTANCE;
    }
}
