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

package com.mardous.newsletter.model;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import com.mardous.newsletter.R;
import com.mardous.newsletter.gui.fragments.HomeFragment;
import com.mardous.newsletter.gui.fragments.SearchFragment;
import com.mardous.newsletter.gui.fragments.SourcesFragment;

/**
 * @author Christians Martínez Alvarado (mardous)
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
