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

package com.mardous.newsletter.model;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mardous.newsletter.BuildConfig;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
@SuppressWarnings("WeakerAccess")
public class Query {

    private String category;
    private String language;
    private String country;
    private String excludeDomains;
    private String apiKey;
    private int resultsLimit;

    public Query(@NonNull Context context, @Nullable Category category) {
        this.setCategory(category != null ? category.name : null);
        this.setLanguage(Utils.getLanguage());
        this.setCountry(Utils.getCountry());
        this.setExcludeDomains(Utils.getExcludedDomainsString(context));
        this.setApiKey(BuildConfig.NewsAPIKey);
        this.setResultsLimit(PrefUtil.getInstance(context).getResultsLimit());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExcludeDomains() {
        return excludeDomains;
    }

    public void setExcludeDomains(String excludeDomains) {
        this.excludeDomains = excludeDomains;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getResultsLimit() {
        return resultsLimit;
    }

    public void setResultsLimit(int resultsLimit) {
        this.resultsLimit = resultsLimit;
    }
}
