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

package com.mardous.newsletter.region;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.mardous.newsletter.R;

/**
 * @author Christians Martínez Alvarado (mardous)
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
