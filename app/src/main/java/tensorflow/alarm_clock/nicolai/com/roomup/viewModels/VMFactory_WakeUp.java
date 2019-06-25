package tensorflow.alarm_clock.nicolai.com.roomup.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class VMFactory_WakeUp implements ViewModelProvider.Factory {

    private Application application;
    private int id;

    public VMFactory_WakeUp(Application application, int id){
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModel_WakeUp(application, id);
    }
}
