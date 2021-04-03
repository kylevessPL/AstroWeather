package pl.piasta.astroweather.ui.settings;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

    private final Pattern mPattern;

    public DecimalDigitsInputFilter(int integerPartDigits, int fractionalPartDigits) {
        mPattern = Pattern.compile("-?[0-9]{0," + (integerPartDigits) + "}+((\\.[0-9]{0," + (fractionalPartDigits)
                + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String replacement = source.subSequence(start, end).toString();
        String newVal = dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length()).toString();
        Matcher matcher = mPattern.matcher(newVal);
        if (matcher.matches()) {
            return null;
        }
        if (TextUtils.isEmpty(source)) {
            return dest.subSequence(dstart, dend);
        }
        return "";
    }
}
