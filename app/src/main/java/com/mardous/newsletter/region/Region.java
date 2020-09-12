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
import com.mardous.newsletter.BuildConfig;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public enum Region {
    AR(DisplayName.AR, Language.ES),
    BR(DisplayName.BR, Language.PT),
    CA(DisplayName.CA, Language.EN),
    CO(DisplayName.CO, Language.ES),
    CU(DisplayName.CU, Language.ES),
    MX(DisplayName.MX, Language.ES),
    US(DisplayName.US, Language.EN),
    VE(DisplayName.VE, Language.ES);

    public final DisplayName displayName;
    public final Language language;

    Region(DisplayName displayName, Language language) {
        this.displayName = displayName;
        this.language = language;
    }

    @NonNull
    public String getDisplayName(@NonNull Resources res) {
        return displayName.getDisplayName(res);
    }

    @Nullable
    public static Region parse(@Nullable String code) {
        if (code != null) {
            try {
                return valueOf(code);
            } catch (IllegalArgumentException ignored) {}
        }
        return getDefault();
    }

    @Nullable
    public static Region getDefault() {
        for (Region region : values()) {
            if (BuildConfig.DefaultRegion.equals(region.name())) {
                return region;
            }
        }
        return null;
    }
}
