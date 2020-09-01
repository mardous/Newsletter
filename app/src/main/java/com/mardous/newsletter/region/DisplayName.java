package com.mardous.newsletter.region;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.mardous.newsletter.R;

/**
 * @author Christians Martínez Alvarado
 */
public enum DisplayName {
    AR(R.string.country_argentina),
    AU(R.string.country_australia),
    BR(R.string.country_brazil),
    CA(R.string.country_canada),
    CO(R.string.country_colombia),
    CU(R.string.country_cuba),
    ES(R.string.country_spain),
    GB(R.string.country_united_kingdom),
    IN(R.string.country_india),
    IT(R.string.country_italy),
    MX(R.string.country_mexico),
    US(R.string.country_usa),
    VE(R.string.country_venezuela);

    @StringRes
    public final int resource;

    DisplayName(@StringRes int resource) {
        this.resource = resource;
    }

    @NonNull
    public String getDisplayName(@NonNull Resources res) {
        return res.getString(resource);
    }

    @Nullable
    public static DisplayName parse(@Nullable String code) {
        if (code != null) {
            try {
                return DisplayName.valueOf(code);
            } catch (IllegalArgumentException ignored) {}
        }
        return null;
    }
}
