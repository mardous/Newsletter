package ve.msucre.noticias.region;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ve.msucre.noticias.BuildConfig;

/**
 * @author Christians Martínez Alvarado
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
