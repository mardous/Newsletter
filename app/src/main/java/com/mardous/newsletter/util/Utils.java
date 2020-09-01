package com.mardous.newsletter.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.util.ATHUtil;
import com.kabouzeid.appthemehelper.util.ColorUtil;
import com.kabouzeid.appthemehelper.util.MaterialValueHelper;
import com.mardous.newsletter.App;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.model.Article;
import com.mardous.newsletter.api.model.Source;
import com.mardous.newsletter.model.Category;
import com.mardous.newsletter.region.DisplayName;
import com.mardous.newsletter.region.Region;
import com.mardous.newsletter.util.pref.PrefUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static void hideSoftInputMethod(@Nullable Activity activity) {
        if (activity != null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    public static void applyFastScrollRecyclerViewColor(@NonNull Context context, @NonNull FastScrollRecyclerView recyclerView) {
        int accentColor = ThemeStore.accentColor(context);
        recyclerView.setPopupBgColor(accentColor);
        recyclerView.setPopupTextColor(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(accentColor)));
        recyclerView.setThumbColor(accentColor);
        recyclerView.setTrackColor(ColorUtil.withAlpha(ATHUtil.resolveColor(context, R.attr.colorControlNormal), 0.12f));
    }

    public static void applyBottomNavigationViewColor(@NonNull Context context, @NonNull BottomNavigationView bottomNavigationView) {
        ColorStateList sl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        ThemeStore.textColorSecondary(context),
                        ThemeStore.accentColor(context)
                }
        );
        bottomNavigationView.setItemIconTintList(sl);
        bottomNavigationView.setItemTextColor(sl);
    }

    @NonNull
    public static String getExcludedDomainsString(@NonNull Context context) {
        List<String> domains = PrefUtil.getInstance(context).getExcludedDomains();
        StringBuilder builder = new StringBuilder();
        int domainsSize = domains.size();
        for (int i = 0; i < domainsSize; i++) {
            builder.append(domains.get(i));
            if (i < domainsSize - 1) {
                builder.append(",");
            }
        }
        Log.d("Utils", builder.toString());
        return builder.toString();
    }

    private static ColorDrawable[] vibrantLightColorList = {
            new ColorDrawable(Color.parseColor("#ffeead")),
            new ColorDrawable(Color.parseColor("#93cfb3")),
            new ColorDrawable(Color.parseColor("#fd7a7a")),
            new ColorDrawable(Color.parseColor("#faca5f")),
            new ColorDrawable(Color.parseColor("#1ba798")),
            new ColorDrawable(Color.parseColor("#6aa9ae")),
            new ColorDrawable(Color.parseColor("#ffbf27")),
            new ColorDrawable(Color.parseColor("#d93947"))
    };

    public static ColorDrawable getRandomDrawableColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    @NonNull
    public static String getISODisplayName(@NonNull Resources res, @Nullable String isoCode) {
        if (isoCode != null) {
            isoCode = isoCode.toUpperCase();
            try {
                return DisplayName.valueOf(isoCode).getDisplayName(res);
            } catch (Exception ignored) {}
            return isoCode;
        }
        return res.getString(R.string.country_unknown);
    }

    @NonNull
    public static String getCategoryTitleFromName(@NonNull Resources res, @Nullable String categoryName) {
        if (categoryName != null) {
            for (Category category : Category.values()) {
                if (category.name.equals(categoryName)) {
                    return res.getString(category.titleRes);
                }
            }
        }
        return res.getString(R.string.category_unknown);
    }

    @DrawableRes
    public static int getCategoryIconFromName(@Nullable String categoryName) {
        if (categoryName != null) {
            for (Category category : Category.values()) {
                if (category.name.equals(categoryName)) {
                    return category.iconRes;
                }
            }
        }
        return R.drawable.ic_help_white_24dp;
    }

    @NonNull
    public static String getCountry() {
        Region region = PrefUtil.getInstance(App.getInstance()).getRegion();
        if (region == null) {
            return Locale.getDefault().getCountry().toLowerCase();
        }
        return region.name().toLowerCase();
    }

    @NonNull
    public static String getLanguage(){
        Region region = PrefUtil.getInstance(App.getInstance()).getRegion();
        if (region == null) {
            return Locale.getDefault().getLanguage().toLowerCase();
        }
        return region.language.name().toLowerCase();
    }

    public static boolean isValidUrl(@Nullable String url) {
        return new UrlValidator().isValid(url);
    }

    public static boolean isValidAuthorName(@NonNull Article article) {
        final String name = article.getAuthor();
        return !TextUtils.isEmpty(name) && !name.equals("(none)");
    }

    public static boolean isSourceExcluded(@NonNull List<String> exclusions, @NonNull Article article) {
        for (String domain : exclusions) {
            if (article.getUrl().startsWith(domain)) return true;
        }
        return false;
    }

    public static boolean isSourceExcluded(@NonNull List<String> exclusions, @NonNull Source source) {
        String sourceUrl = source.getUrl();
        if (!TextUtils.isEmpty(sourceUrl)) {
            for (String domain : exclusions) {
                if (sourceUrl.equals(domain) || sourceUrl.contains(domain) || domain.contains(sourceUrl)) {
                    return true;
                }
            }
        }
        return false;
    }
}