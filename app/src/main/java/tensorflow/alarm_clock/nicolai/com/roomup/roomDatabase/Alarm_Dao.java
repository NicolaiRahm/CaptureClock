package tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;

import java.util.List;

@Dao
public interface Alarm_Dao {

    @Insert
    public long insert(Alarm_POJO alarm);

    @Update
    public void update(Alarm_POJO alarm);

    @Query("SELECT * FROM alarm_table")
    public LiveData<List<Alarm_POJO>> getAllAlarms();
    @Query("SELECT * FROM alarm_table")
    public List<Alarm_POJO> getBootList();
    @Query("SELECT * FROM alarm_table WHERE id = :id")
    public Alarm_POJO getAlarmById(int id);

    @Delete
    public void delete(Alarm_POJO alarm);
    @Query("DELETE FROM alarm_table WHERE id = :id")
    public void deleteById(int id);
    @Query("DELETE FROM alarm_table")
    public void deleteAll();
}
