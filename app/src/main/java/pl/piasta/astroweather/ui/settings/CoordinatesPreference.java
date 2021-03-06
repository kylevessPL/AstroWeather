package pl.piasta.astroweather.ui.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import pl.piasta.astroweather.R;
import pl.piasta.astroweather.ui.util.DecimalDigitsInputFilter;
import pl.piasta.astroweather.ui.util.RangeInputFilter;

public class CoordinatesPreference extends EditTextPreference {

    public CoordinatesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CoordinatesPreference,
                0, 0);
        try {
            CoordinateType coordinateType = CoordinateType.values()[
                    attributes.getInt(R.styleable.CoordinatesPreference_type, 0)];
            super.setOnBindEditTextListener(new CoordinatesOnBindEditTextListener(coordinateType));
        } finally {
            attributes.recycle();
        }
        super.setOnPreferenceChangeListener(new CoordinatesOnPreferenceChangeListener());
    }

    @Override
    public void setText(String text) {
        Double number = Double.parseDouble(text);
        NumberFormat numberFormat = DecimalFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(6);
        super.setText(numberFormat.format(number));
    }

    private static class CoordinatesOnBindEditTextListener implements OnBindEditTextListener {

        private final CoordinateType mCoordinateType;

        public CoordinatesOnBindEditTextListener(CoordinateType coordinateType) {
            mCoordinateType = coordinateType;
        }

        @Override
        public void onBindEditText(@NonNull EditText editText) {
            editText.setInputType(
                    InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED
            );
            editText.setFilters(new InputFilter[]{
                    new DecimalDigitsInputFilter(
                            mCoordinateType.getIntegerPartDigits(),
                            mCoordinateType.getFractionalPartDigits()),
                    new RangeInputFilter(
                            mCoordinateType.getMinValue(),
                            mCoordinateType.getMaxValue())
            });
        }
    }

    private static class CoordinatesOnPreferenceChangeListener implements OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return !(((String) newValue).isEmpty() ||
                    ((String) newValue).matches("^[.\\-]{1,2}"));
        }
    }
}
