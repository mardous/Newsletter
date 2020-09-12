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
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mardous.newsletter.R;
import com.mardous.newsletter.gui.dialogs.AddDomainDialog;
import com.mardous.newsletter.util.pref.PrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class ExcludeDomainPreferenceDialog extends DialogFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private List<String> excludedDomains;

    public static ExcludeDomainPreferenceDialog create() {
        return new ExcludeDomainPreferenceDialog();
    }

    public ExcludeDomainPreferenceDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        PrefUtil.getInstance(getActivity()).registerOnSharedPreferenceChangedListener(this);

        refreshExcludedDomains();
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.preference_excluded_domains_title)
                .items(excludedDomains)
                .itemsCallback((materialDialog, itemView, position, text) -> {
                    AddDomainDialog.create(text.toString(), AddDomainDialog.MODE_EDIT).show(getFragmentManager(), "EDIT_DOMAIN");
                })
                .positiveText(android.R.string.ok)
                .negativeText(R.string.action_add)
                .neutralText(R.string.action_clear)
                .autoDismiss(false)
                .onPositive((materialDialog, which) -> dismiss())
                .onNegative((materialDialog, which) -> {
                    AddDomainDialog.create(null, AddDomainDialog.MODE_ADD).show(getChildFragmentManager(), "ADD_DOMAIN");
                })
                .onNeutral((materialDialog, which) -> {
                    PrefUtil.getInstance(getActivity()).setExcludedDomains(new ArrayList<>());
                })
                .build();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        PrefUtil.getInstance(getActivity()).unregisterOnSharedPreferenceChangedListener(this);
        super.onDismiss(dialog);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (key.equals(PrefUtil.EXCLUDED_DOMAINS)) {
            refreshExcludedDomains();
        }
    }

    private void refreshExcludedDomains() {
        excludedDomains = PrefUtil.getInstance(getActivity()).getExcludedDomains();

        MaterialDialog dialog = (MaterialDialog) getDialog();
        if (dialog != null) {
            String[] pathArray = new String[excludedDomains.size()];
            pathArray = excludedDomains.toArray(pathArray);
            dialog.setItems(pathArray);
        }
    }
}
