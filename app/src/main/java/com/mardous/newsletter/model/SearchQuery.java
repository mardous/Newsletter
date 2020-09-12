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
import com.mardous.newsletter.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class SearchQuery extends Query {

    private String keyword;
    private String sortBy;

    public SearchQuery(@NonNull Context context) {
        super(context, null);
        this.setSortBy(PrefUtil.getInstance(context).getSearchResultSortOrder());
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
