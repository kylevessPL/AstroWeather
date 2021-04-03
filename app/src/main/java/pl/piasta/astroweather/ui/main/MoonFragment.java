package pl.piasta.astroweather.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import pl.piasta.astroweather.R;
import pl.piasta.astroweather.ui.base.BaseFragment;

public class MoonFragment extends BaseFragment {

    private static final String FRAGMENT_NAME = "Księżyc";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_moon, container, false);
    }

    @NonNull
    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }
}
