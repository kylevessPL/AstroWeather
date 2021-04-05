package pl.piasta.astroweather.ui.main;

import java.util.concurrent.TimeUnit;

public enum UpdateInterval {

    MINUTES_15(15, TimeUnit.MINUTES),
    MINUTES_30(30, TimeUnit.MINUTES),
    HOURS_1(1, TimeUnit.HOURS),
    HOURS_2(2, TimeUnit.HOURS),
    HOURS_5(5, TimeUnit.HOURS),
    HOURS_12(12, TimeUnit.HOURS),
    HOURS_24(24, TimeUnit.HOURS);

    private final int interval;
    private final TimeUnit unit;

    UpdateInterval(int interval, TimeUnit unit) {
        this.interval = interval;
        this.unit = unit;
    }

    public int getInterval() {
        return interval;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}
