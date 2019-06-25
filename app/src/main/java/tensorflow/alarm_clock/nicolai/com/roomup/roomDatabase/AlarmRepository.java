package tensorflow.alarm_clock.nicolai.com.roomup.roomDatabase;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import tensorflow.alarm_clock.nicolai.com.roomup.objects.Alarm_POJO;

import java.util.List;

public class AlarmRepository {
    private Alarm_Dao mAlarmDao;
    private LiveData<List<Alarm_POJO>> mAllAlarms;

    public AlarmRepository(Context context) {
        AlarmRoomDatabase db = AlarmRoomDatabase.getDatabase(context);
        mAlarmDao = db.alarmDao();
        mAllAlarms = mAlarmDao.getAllAlarms();
    }

    public LiveData<List<Alarm_POJO>> getAllAlarms() {
        return mAllAlarms;
    }

    //Boot list
    public List<Alarm_POJO> getBootList () {
        bootListAsyncTask asyncTask = new bootListAsyncTask(mAlarmDao);
        try {
            return asyncTask.execute().get();
        }catch (java.util.concurrent.ExecutionException e){
            return null;
        }catch (java.lang.InterruptedException e){
            return null;
        }
    }

    private static class bootListAsyncTask extends AsyncTask<Void, Void, List<Alarm_POJO>> {

        private Alarm_Dao mAsyncTaskDao;

        bootListAsyncTask(Alarm_Dao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Alarm_POJO> doInBackground(final Void... params) {
            return mAsyncTaskDao.getBootList();
        }
    }


    //Insert new alarm clock
    public long insert (Alarm_POJO alarm) {
        insertAsyncTask asyncTask = new insertAsyncTask(mAlarmDao);
        try {
            return asyncTask.execute(alarm).get();
        }catch (java.util.concurrent.ExecutionException e){
            return 0;
        }catch (java.lang.InterruptedException e){
            return 0;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Alarm_POJO, Void, Long> {

        private Alarm_Dao mAsyncTaskDao;

        insertAsyncTask(Alarm_Dao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final Alarm_POJO... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }


    //delete by id
    public void deleteById(int id){
        new deleteByIdAsyncTask(mAlarmDao).execute(id);
    }

    private static class deleteByIdAsyncTask extends AsyncTask<Integer, Void, Void> {

        private Alarm_Dao mAsyncTaskDao;

        deleteByIdAsyncTask(Alarm_Dao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteById(params[0]);
            return null;
        }
    }


    //Get by id
    public Alarm_POJO getById(int id){
        getByIdAsyncTask alarm =  new getByIdAsyncTask(mAlarmDao);
        try {
            return alarm.execute(id).get();
        }catch (java.util.concurrent.ExecutionException e){
            return null;
        }catch (java.lang.InterruptedException e){
            return null;
        }
    }

    private static class getByIdAsyncTask extends AsyncTask<Integer, Void, Alarm_POJO> {

        private Alarm_Dao mAsyncTaskDao;

        getByIdAsyncTask(Alarm_Dao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Alarm_POJO doInBackground(final Integer... params) {
            return mAsyncTaskDao.getAlarmById(params[0]);
        }
    }


    //Update by id
    public void update(Alarm_POJO updatedAlarm){
        new updateAsyncTask(mAlarmDao).execute(updatedAlarm);
    }

    private static class updateAsyncTask extends AsyncTask<Alarm_POJO, Void, Void> {

        private Alarm_Dao mAsyncTaskDao;

        updateAsyncTask(Alarm_Dao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Alarm_POJO... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
