package com.mardous.newsletter.model;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import com.mardous.newsletter.R;
import com.mardous.newsletter.gui.fragments.HomeFragment;
import com.mardous.newsletter.gui.fragments.SearchFragment;
import com.mardous.newsletter.gui.fragments.SourcesFragment;

/**
 * @author Christians Martínez Alvarado
 */
public enum MainFr {
    HOME(HomeFragment.create(), R.id.action_home),
    SOURCES(SourcesFragment.create(), R.id.action_sources),
    SEARCH(SearchFragment.create(), R.id.action_search);

    public Fragment fragment;
    @IdRes
    public int itemId;

    MainFr(Fragment fragment, @IdRes int itemId) {
        this.fragment = fragment;
        this.itemId = itemId;
    }
}
