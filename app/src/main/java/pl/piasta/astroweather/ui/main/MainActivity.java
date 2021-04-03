package pl.piasta.astroweather.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import pl.piasta.astroweather.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel model;
    private BroadcastReceiver broadcastReceiver;

    private TextView time;
    private TextView date;
    private TextView coordinates;
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
        coordinates = findViewById(R.id.coordinates);
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
    public void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    model.updateClock();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
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
                tab.setText(((AstroFragmentStateAdapter) pa.getAdapter()).getItemName(position))
        ).attach();
    }

    private void setupListeners() {
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.animation_refresh);
        refreshButton.setOnClickListener(button -> button.startAnimation(rotation));
    }

    private void observeModel() {
        model.getDate().observe(this, date::setText);
        model.getTime().observe(this, time::setText);
    }
}