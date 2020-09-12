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

package com.mardous.newsletter.util.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mardous.newsletter.model.Category;
import com.mardous.newsletter.model.MainFr;
import com.mardous.newsletter.region.Region;
import com.mardous.newsletter.util.Hour;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
@SuppressWarnings("WeakerAccess")
public class PrefUtil {

    private static final String TAG = PrefUtil.class.getSimpleName();

    public static final String DARK_THEME = "dark_theme";
    public static final String ACCENT_COLOR = "accent_color";
    public static final String REGION = "region";
    public static final String NEWS_UPDATE_INTERVAL = "news_update_interval";
    public static final String OPEN_NEWS_IN_APP = "open_news_in_app";
    public static final String RESULTS_LIMIT = "results_limit";
    public static final String ALWAYS_LOAD_FROM_CACHE_IF_POSSIBLE = "always_load_from_cache_if_possible";
    public static final String SEARCH_ON_TEXT_CHANGED = "search_on_text_changed";
    public static final String SORT_SEARCH_RESULT_BY = "sort_search_result_by";
    public static final String EXCLUDED_DOMAINS = "excluded_domains";
    public static final String LAST_MAIN_FRAGMENT_NAME = "last_main_fragment_name";
    public static final String LAST_CATEGORY_ID = "last_category_id";
    public static final String INTRO_SHOWN = "intro_shown";

    private final SharedPreferences preferences;

    private static PrefUtil instance;

    public static PrefUtil getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new PrefUtil(context);
        }
        return instance;
    }

    private PrefUtil(@NonNull Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public void registerOnSharedPreferenceChangedListener(SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangedListener(SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public boolean darkTheme() {
        return preferences.getBoolean(DARK_THEME, false);
    }

    @Nullable
    public Region getRegion() {
        String region = preferences.getString(REGION, "");
        if (!TextUtils.isEmpty(region)) {
            return Region.parse(region);
        }
        return null;
    }

    public void setRegion(@Nullable Region region) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REGION, region != null ? region.name() : "");
        editor.apply();
    }

    @Nullable
    public final Hour getNewsUpdateInterval() {
        String updateInterval = preferences.getString(NEWS_UPDATE_INTERVAL, PrefValues.EVERY_FIVE_HOURS);
        switch (updateInterval) {
            case PrefValues.EVERY_HOUR:
                return Hour.ONE;
            case PrefValues.EVERY_TWO_HOURS:
                return Hour.TWO;
            case PrefValues.EVERY_FIVE_HOURS:
                return Hour.FIVE;
            case PrefValues.EVERY_EIGHT_HOURS:
                return Hour.EIGHT;
            case PrefValues.EVERY_TWELVE_HOURS:
                return Hour.TWELVE;
            case PrefValues.EVERY_TWENTY_FOUR_HOURS:
                return Hour.TWENTY_FOUR;
        }
        return null;
    }

    public boolean openNewsInApp() {
        return preferences.getBoolean(OPEN_NEWS_IN_APP, true);
    }

    public int getResultsLimit() {
        String number = preferences.getString(RESULTS_LIMIT, "");
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error occurred while reading from preferences: " + e.getLocalizedMessage());
        }
        return 25;
    }

    public boolean loadFromCache() {
        return preferences.getBoolean(ALWAYS_LOAD_FROM_CACHE_IF_POSSIBLE, false);
    }

    public boolean searchOnTextChanged() {
        return preferences.getBoolean(SEARCH_ON_TEXT_CHANGED, false);
    }

    public String getSearchResultSortOrder() {
        return preferences.getString(SORT_SEARCH_RESULT_BY, PrefValues.PUBLISHED_AT);
    }

    public List<String> getExcludedDomains() {
        String json = preferences.getString(EXCLUDED_DOMAINS, null);
        if (json != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<String>>() {}.getType();
            try {
                return gson.fromJson(json, collectionType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public void setExcludedDomains(List<String> domains) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<String>>() {
        }.getType();

        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EXCLUDED_DOMAINS, gson.toJson(domains, collectionType));
        editor.apply();
    }

    @NonNull
    public MainFr getLastMainFr() {
        String name = preferences.getString(LAST_MAIN_FRAGMENT_NAME, "");
        for (MainFr fr : MainFr.values()) {
            if (fr.name().equals(name)) return fr;
        }
        return MainFr.HOME;
    }

    public void setLastMainFr(String name) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_MAIN_FRAGMENT_NAME, name);
        editor.apply();
    }

    @NonNull
    public Category getLastCategory() {
        int id = preferences.getInt(LAST_CATEGORY_ID, 0);
        for (Category category : Category.values()) {
            if (category.id == id) return category;
        }
        return Category.GENERAL;
    }

    public void setLastCategory(Category category) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAST_CATEGORY_ID, category.id);
        editor.apply();
    }

    public boolean introShown() {
        return preferences.getBoolean(INTRO_SHOWN, false);
    }

    public void setIntroShown() {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(INTRO_SHOWN, true);
        editor.apply();
    }
}
