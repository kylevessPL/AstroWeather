package pl.piasta.astroweather.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    private final MutableLiveData<String> mDate = new MutableLiveData<>();
    private final MutableLiveData<String> mTime = new MutableLiveData<>();

    public MainViewModel() {
        refreshCurrentDateTime();
    }

    public LiveData<String> getDate() {
        return mDate;
    }

    public LiveData<String> getTime() {
        return mTime;
    }

    public void updateClock() {
        executor.execute(this::refreshCurrentDateTime);
    }

    private void refreshCurrentDateTime() {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalDateTime dateTime = LocalDateTime.now();
        mDate.postValue(dateTime.format(dateFormatter));
        mTime.postValue(dateTime.format(timeFormatter));
    }

}
