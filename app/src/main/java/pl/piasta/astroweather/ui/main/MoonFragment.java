package pl.piasta.astroweather.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import pl.piasta.astroweather.R;
import pl.piasta.astroweather.ui.base.BaseFragment;

public class MoonFragment extends BaseFragment {

    private static final String FRAGMENT_NAME = "Księżyc";

    private MainViewModel model;
    
    private TextView mMoonRiseTime;
    private TextView mMoonSetTime;
    private TextView mNewMoonDate;
    private TextView mFullMoonDate;
    private TextView mMoonPhaseValue;
    private TextView mMoonLunarMonthDay;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_moon, container, false);
        mMoonRiseTime = root.findViewById(R.id.time_rise);
        mMoonSetTime = root.findViewById(R.id.time_set);
        mNewMoonDate = root.findViewById(R.id.date_new_moon);
        mFullMoonDate = root.findViewById(R.id.date_full_moon);
        mMoonPhaseValue = root.findViewById(R.id.value_moon_phase);
        mMoonLunarMonthDay = root.findViewById(R.id.day_lunar_month);
        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeModel();
    }

    @NonNull
    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }

    private void observeModel() {
        model.getMoonRiseTime().observe(getViewLifecycleOwner(), mMoonRiseTime::setText);
        model.getMoonSetTime().observe(getViewLifecycleOwner(), mMoonSetTime::setText);
        model.getNewMoonDate().observe(getViewLifecycleOwner(), mNewMoonDate::setText);
        model.getFullMoonDate().observe(getViewLifecycleOwner(), mFullMoonDate::setText);
        model.getMoonPhaseValue().observe(getViewLifecycleOwner(), mMoonPhaseValue::setText);
        model.getMoonLunarMonthDay().observe(getViewLifecycleOwner(), mMoonLunarMonthDay::setText);
    }
}
