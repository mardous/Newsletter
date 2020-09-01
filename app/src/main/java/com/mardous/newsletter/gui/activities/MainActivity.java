package com.mardous.newsletter.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.mardous.newsletter.R;
import com.mardous.newsletter.gui.activities.base.AbsThemeActivity;
import com.mardous.newsletter.gui.dialogs.bottomsheet.CategoriesDialog;
import com.mardous.newsletter.gui.fragments.HomeFragment;
import com.mardous.newsletter.model.MainFr;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado
 */
public class MainActivity extends AbsThemeActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener {

    private static final int APP_INTRO_REQUEST = 100;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpBottomNavigationView();

        if (savedInstanceState == null) {
            MainFr last = PrefUtil.getInstance(this).getLastMainFr();
            setCurrentFragment(last);
            bottomNavigationView.setSelectedItemId(last.itemId);
        }

        checkShowIntro();
    }

    private void setUpBottomNavigationView() {
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemReselectedListener(this);
        Utils.applyBottomNavigationViewColor(this, bottomNavigationView);
    }

    private void setCurrentFragment(@NonNull MainFr fr) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fr.fragment)
                .commit();

        PrefUtil.getInstance(this).setLastMainFr(fr.name());
    }

    private void checkShowIntro() {
        if (!PrefUtil.getInstance(this).introShown()) {
            PrefUtil.getInstance(this).setIntroShown();
            new Handler().postDelayed(() -> startActivityForResult(new Intent(this, AppIntroActivity.class), APP_INTRO_REQUEST), 50);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.action_home:
                setCurrentFragment(MainFr.HOME);
                return true;
            case R.id.action_sources:
                setCurrentFragment(MainFr.SOURCES);
                return true;
            case R.id.action_search:
                setCurrentFragment(MainFr.SEARCH);
                return true;
        }
        return false;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_home) {
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (homeFragment != null) {
                CategoriesDialog.newInstance().setListener(homeFragment).show(getSupportFragmentManager(), "CATEGORIES");
            }
        }
    }
}