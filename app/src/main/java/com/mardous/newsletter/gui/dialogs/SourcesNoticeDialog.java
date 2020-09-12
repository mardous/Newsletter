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

package com.mardous.newsletter.gui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mardous.newsletter.R;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class SourcesNoticeDialog extends DialogFragment implements DialogInterface.OnShowListener {

    private static final String SOURCES_NOTICE_SHOWN = "sources_notice_shown";

    public static SourcesNoticeDialog newInstance() {
        return new SourcesNoticeDialog();
    }

    public SourcesNoticeDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.sources_notice_title)
                .content(R.string.sources_notice_message)
                .positiveText(android.R.string.ok)
                .showListener(this)
                .build();
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putBoolean(SOURCES_NOTICE_SHOWN, true).apply();
    }

    public static boolean wasShown(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SOURCES_NOTICE_SHOWN, false);
    }
}
