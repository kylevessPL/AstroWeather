package pl.piasta.astroweather.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroCalculator.Location;
import com.astrocalculator.AstroCalculator.MoonInfo;
import com.astrocalculator.AstroCalculator.SunInfo;
import com.astrocalculator.AstroDateTime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainViewModel extends ViewModel {

    private final ExecutorService singleExecutor;
    private final ScheduledExecutorService scheduledExecutor;

    private final MutableLiveData<String> mDate;
    private final MutableLiveData<String> mTime;
    private final MutableLiveData<String> mLastUpdateCheck;
    private final MutableLiveData<String> mSunRiseTime;
    private final MutableLiveData<String> mSunRiseAzimuth;
    private final MutableLiveData<String> mSunSetTime;
    private final MutableLiveData<String> mSunSetAzimuth;
    private final MutableLiveData<String> mSunDuskTime;
    private final MutableLiveData<String> mSunDawnTime;
    private final MutableLiveData<String> mMoonRiseTime;
    private final MutableLiveData<String> mMoonSetTime;
    private final MutableLiveData<String> mNewMoonDate;
    private final MutableLiveData<String> mFullMoonDate;
    private final MutableLiveData<String> mMoonPhaseValue;
    private final MutableLiveData<String> mMoonLunarMonthDay;

    public MainViewModel() {
        this.singleExecutor = Executors.newSingleThreadExecutor();
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mDate = new MutableLiveData<>();
        this.mTime = new MutableLiveData<>();
        this.mLastUpdateCheck = new MutableLiveData<>();
        this.mSunRiseTime = new MutableLiveData<>();
        this.mSunRiseAzimuth = new MutableLiveData<>();
        this.mSunSetTime = new MutableLiveData<>();
        this.mSunSetAzimuth = new MutableLiveData<>();
        this.mSunDuskTime = new MutableLiveData<>();
        this.mSunDawnTime = new MutableLiveData<>();
        this.mMoonRiseTime = new MutableLiveData<>();
        this.mMoonSetTime = new MutableLiveData<>();
        this.mNewMoonDate = new MutableLiveData<>();
        this.mFullMoonDate = new MutableLiveData<>();
        this.mMoonPhaseValue = new MutableLiveData<>();
        this.mMoonLunarMonthDay = new MutableLiveData<>();
        setCurrentDateTime();
    }

    public LiveData<String> getDate() {
        return mDate;
    }

    public LiveData<String> getTime() {
        return mTime;
    }

    public MutableLiveData<String> getLastUpdateCheck() {
        return mLastUpdateCheck;
    }

    public MutableLiveData<String> getSunRiseTime() {
        return mSunRiseTime;
    }

    public MutableLiveData<String> getSunRiseAzimuth() {
        return mSunRiseAzimuth;
    }

    public MutableLiveData<String> getSunSetTime() {
        return mSunSetTime;
    }

    public MutableLiveData<String> getSunSetAzimuth() {
        return mSunSetAzimuth;
    }

    public MutableLiveData<String> getSunDuskTime() {
        return mSunDuskTime;
    }

    public MutableLiveData<String> getSunDawnTime() {
        return mSunDawnTime;
    }

    public MutableLiveData<String> getMoonRiseTime() {
        return mMoonRiseTime;
    }

    public MutableLiveData<String> getMoonSetTime() {
        return mMoonSetTime;
    }

    public MutableLiveData<String> getNewMoonDate() {
        return mNewMoonDate;
    }

    public MutableLiveData<String> getFullMoonDate() {
        return mFullMoonDate;
    }

    public MutableLiveData<String> getMoonPhaseValue() {
        return mMoonPhaseValue;
    }

    public MutableLiveData<String> getMoonLunarMonthDay() {
        return mMoonLunarMonthDay;
    }

    public void updateClock() {
        singleExecutor.execute(() -> {
            String date = getCurrentDateString();
            String time = getCurrentTimeString();
            mDate.postValue(date);
            mTime.postValue(time);
        });
    }

    public void updateData(Double latitude, Double longtitude) {
        singleExecutor.execute(() -> calculateAstro(latitude, longtitude));
    }

    public void setupDataUpdate(UpdateInterval updateInterval,
                                Double latitude, Double longtitude) {
        scheduledExecutor.scheduleWithFixedDelay(() -> {
            calculateAstro(latitude, longtitude);
            setLastUpdateCheckTime();
        }, 0, updateInterval.getInterval(), updateInterval.getUnit());
    }

    public void tearDownDataUpdate() {
        scheduledExecutor.shutdownNow();
    }

    public void updateLastUpdateCheckTime() {
        singleExecutor.execute(this::setLastUpdateCheckTime);
    }

    private void setCurrentDateTime() {
        String dateString = getCurrentDateString();
        String timeString = getCurrentTimeString();
        mDate.setValue(dateString);
        mTime.setValue(timeString);
    }

    private String getCurrentTimeString() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalTime time = LocalTime.now();
        return time.format(formatter);
    }

    private String getCurrentDateString() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        LocalDate date = LocalDate.now();
        return date.format(formatter);
    }

    private void calculateAstro(Double latitude, Double longtitude) {
        AstroDateTime dateTime = createAstroDateTime();
        Location location = new Location(latitude, longtitude);
        AstroCalculator calculator = new AstroCalculator(dateTime, location);
        setSunInfo(calculator);
        setMoonInfo(calculator);
    }

    private void setLastUpdateCheckTime() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalTime time = LocalTime.now();
        mLastUpdateCheck.postValue(time.format(formatter));
    }

    private void setSunInfo(AstroCalculator calculator) {
        SunInfo sunInfo = calculator.getSunInfo();
        mSunRiseAzimuth.postValue(getDoubleAsString(sunInfo.getAzimuthRise()));
        mSunSetAzimuth.postValue(getDoubleAsString(sunInfo.getAzimuthSet()));
        mSunRiseTime.postValue(getAstroDateTimeAsTimeString(sunInfo.getSunrise()));
        mSunSetTime.postValue(getAstroDateTimeAsTimeString(sunInfo.getSunset()));
        mSunDuskTime.postValue(getAstroDateTimeAsTimeString(sunInfo.getTwilightEvening()));
        mSunDawnTime.postValue(getAstroDateTimeAsTimeString(sunInfo.getTwilightMorning()));
    }

    private void setMoonInfo(AstroCalculator calculator) {
        MoonInfo moonInfo = calculator.getMoonInfo();
        mMoonRiseTime.postValue(getAstroDateTimeAsTimeString(moonInfo.getMoonrise()));
        mMoonSetTime.postValue(getAstroDateTimeAsTimeString(moonInfo.getMoonset()));
        mNewMoonDate.postValue(getAstroDateTimeAsDateString(moonInfo.getNextNewMoon()));
        mFullMoonDate.postValue(getAstroDateTimeAsDateString(moonInfo.getNextFullMoon()));
        mMoonPhaseValue.postValue(getMoonIlluminationAsPercentValue(moonInfo.getIllumination()));
        mMoonLunarMonthDay.postValue(String.valueOf(moonInfo.getAge()));
    }

    private AstroDateTime createAstroDateTime() {
        TemporalAccessor temporal = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).parse(mDate.getValue());
        LocalDate date = LocalDate.of(
                Year.from(temporal).getValue(),
                Month.from(temporal).getValue(),
                MonthDay.from(temporal).getDayOfMonth());
        LocalTime time = LocalTime.parse(mTime.getValue());
        return new AstroDateTime(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                time.getHour(), time.getMinute(), time.getSecond(),
                1, true);
    }

    private String getDoubleAsString(Double value) {
        NumberFormat numberFormat = DecimalFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(6);
        return numberFormat.format(value);
    }

    private String getAstroDateTimeAsTimeString(AstroDateTime dateTime) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalTime time = LocalTime.of(dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
        return time.format(formatter);
    }

    private String getAstroDateTimeAsDateString(AstroDateTime dateTime) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
        return date.format(formatter);
    }

    private String getMoonIlluminationAsPercentValue(double value) {
        return (int) value / 100 + "%";
    }
}
