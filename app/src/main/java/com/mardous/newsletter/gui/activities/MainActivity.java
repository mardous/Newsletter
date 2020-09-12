/*
 * Copyright (c) 2020  Christians Martínez Alvarado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mardous.newsletter.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
 * @author Christians Martínez Alvarado (mardous)
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
            new Handler(Looper.getMainLooper()).postDelayed(() -> startActivityForResult(new Intent(this, AppIntroActivity.class), APP_INTRO_REQUEST), 50);
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