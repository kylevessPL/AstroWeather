package pl.piasta.astroweather.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import pl.piasta.astroweather.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel model;
    private BroadcastReceiver dateTimeBroadcastReceiver;
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    private TextView time;
    private TextView date;
    private TextView latitude;
    private TextView longitude;
    private ImageButton refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ViewPager2 pa = findViewById(R.id.pager);
        setAstroFragmentViewPager(pa);
        setAstroTabLayout(pa);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        model = new ViewModelProvider(this).get(MainViewModel.class);
        refreshButton = findViewById(R.id.refresh);
        setupListeners();
        observeModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerDateTimeBroadcastReceiver();
        loadPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dateTimeBroadcastReceiver != null) {
            unregisterReceiver(dateTimeBroadcastReceiver);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent settings = new Intent("pl.piasta.astroweather.SETTINGS");
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAstroFragmentViewPager(ViewPager2 pa) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentStateAdapter sa = new AstroFragmentStateAdapter(fm, getLifecycle());
        pa.setAdapter(sa);
    }

    private void setAstroTabLayout(ViewPager2 pa) {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, pa, (tab, position) ->
                tab.setText(((AstroFragmentStateAdapter) Objects.requireNonNull(pa.getAdapter()))
                        .getItemName(position))
        ).attach();
    }

    private void registerDateTimeBroadcastReceiver() {
        dateTimeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    model.updateClock();
                }
            }
        };
        registerReceiver(dateTimeBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    private void setupListeners() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.animation_refresh);
        refreshButton.setOnClickListener(button -> button.startAnimation(rotation));
    }

    private void observeModel() {
        model.getDate().observe(this, date::setText);
        model.getTime().observe(this, time::setText);
    }

    private void loadPreferences() {
        SharedPreferences preferenceManager = PreferenceManager
                .getDefaultSharedPreferences(this);
        String latitude = preferenceManager.getString("latitude", "DEFAULT");
        String longtitide = preferenceManager.getString("longtitude", "DEFAULT");
        setCoordinates(latitude, longtitide);
    }

    private void setCoordinates(String latitude, String longtitude) {
        this.latitude.setText(latitude);
        this.longitude.setText(longtitude);
    }
}