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
 * @author Christians Martínez Alvarado
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
