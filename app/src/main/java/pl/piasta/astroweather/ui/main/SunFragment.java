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

public class SunFragment extends BaseFragment {

    private static final String FRAGMENT_NAME = "Słońce";

    private MainViewModel mModel;

    private TextView mSunRiseTime;
    private TextView mSunRiseAzimuth;
    private TextView mSunSetTime;
    private TextView mSunSetAzimuth;
    private TextView mSunDuskTime;
    private TextView mSunDawnTime;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_sun, container, false);
        mSunRiseTime = root.findViewById(R.id.time_rise);
        mSunRiseAzimuth = root.findViewById(R.id.azimuth_rise);
        mSunSetTime = root.findViewById(R.id.time_set);
        mSunSetAzimuth = root.findViewById(R.id.azimuth_set);
        mSunDuskTime = root.findViewById(R.id.time_dusk);
        mSunDawnTime = root.findViewById(R.id.time_dawn);
        mModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
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
        mModel.getSunRiseTime().observe(getViewLifecycleOwner(), mSunRiseTime::setText);
        mModel.getSunRiseAzimuth().observe(getViewLifecycleOwner(), mSunRiseAzimuth::setText);
        mModel.getSunSetTime().observe(getViewLifecycleOwner(), mSunSetTime::setText);
        mModel.getSunSetAzimuth().observe(getViewLifecycleOwner(), mSunSetAzimuth::setText);
        mModel.getSunDuskTime().observe(getViewLifecycleOwner(), mSunDuskTime::setText);
        mModel.getSunDawnTime().observe(getViewLifecycleOwner(), mSunDawnTime::setText);
    }
}
