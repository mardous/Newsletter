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

package com.mardous.newsletter.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mardous.newsletter.App;
import com.mardous.newsletter.api.model.Article;
import com.mardous.newsletter.model.Category;
import com.mardous.newsletter.util.pref.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class OfflineArticleUtil {

    private static final String LAST_UPDATE_TIME = "last_update_time";

    private SharedPreferences preferences;

    private static OfflineArticleUtil sInstance;

    public static OfflineArticleUtil getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new OfflineArticleUtil(context);
        }
        return sInstance;
    }

    private OfflineArticleUtil(@NonNull Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("offline_articles", Context.MODE_PRIVATE);
    }

    public boolean isUpdateRecommended() {
        Hour hour = PrefUtil.getInstance(App.getInstance()).getNewsUpdateInterval();
        if (hour != null) {
            long lastUpdateTimeMillis = preferences.getLong(LAST_UPDATE_TIME, 0);
            long currentTimeMillis = System.currentTimeMillis();
            return (currentTimeMillis - lastUpdateTimeMillis) > hour.hourInMillis;
        }
        return false;
    }

    public List<Article> getOfflineArticles(@NonNull Category category) {
        String json = preferences.getString(category.name, null);
        if (json != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Article>>() {
            }.getType();

            try {
                return gson.fromJson(json, collectionType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public void setOfflineArticles(@NonNull Category category, List<Article> articles) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Article>>() {
        }.getType();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(category.name, gson.toJson(articles, collectionType)).apply();
        editor.putLong(LAST_UPDATE_TIME, System.currentTimeMillis()).apply();
        editor.apply();
    }
}
