package tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Timer_POJO;

import java.util.List;

@Dao
public interface Timer_Dao {

    @Insert
    public long insert(Timer_POJO timer);

    @Update
    public void update(Timer_POJO timer);

    @Query("SELECT * FROM timer_table")
    public LiveData<List<Timer_POJO>> getAllTimers();
    @Query("SELECT * FROM timer_table ORDER BY id LIMIT 1")
    public Timer_POJO latestTimer();
    @Query("SELECT * FROM timer_table")
    public List<Timer_POJO> getBootList();
    @Query("SELECT * FROM timer_table WHERE id = :id")
    public Timer_POJO getTimerById(int id);

    @Delete
    public void delete(Timer_POJO alarm);
    @Query("DELETE FROM timer_table WHERE id = :id")
    public void deleteById(int id);
}