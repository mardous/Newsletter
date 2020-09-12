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

package com.mardous.newsletter.preference;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mardous.newsletter.R;
import com.mardous.newsletter.region.Region;
import com.mardous.newsletter.util.pref.PrefUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class RegionPreferenceDialog extends DialogFragment {

    @NonNull
    public static RegionPreferenceDialog newInstance() {
        return new RegionPreferenceDialog();
    }

    public RegionPreferenceDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Region current = PrefUtil.getInstance(getActivity()).getRegion();
        List<String> items = new ArrayList<>();
        for (Region region : Region.values()) {
            items.add(region.getDisplayName(getResources()));
        }
        Collections.sort(items, String::compareToIgnoreCase);
        items.add(0, getString(R.string.auto_detected));
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.preference_region_title)
                .items(items)
                .itemsCallbackSingleChoice(current == null ? 0 : items.indexOf(current.getDisplayName(getResources())), (dialog, itemView, which, text) -> {
                    if (which == 0) {
                        PrefUtil.getInstance(getActivity()).setRegion(null);
                    } else {
                        PrefUtil.getInstance(getActivity()).setRegion(Region.values()[which - 1]);
                    }
                    return false;
                })
                .show();
    }
}
