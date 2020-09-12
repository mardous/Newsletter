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

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import com.mardous.newsletter.R;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public enum Category {
    GENERAL(R.string.category_general, R.drawable.ic_pages_white_24dp, "general", 0),
    BUSINESS(R.string.category_business, R.drawable.ic_business_white_24dp, "business", 1),
    ENTERTAINMENT(R.string.category_entertainment, R.drawable.ic_local_movies_white_24dp, "entertainment", 2),
    HEALTH(R.string.category_health, R.drawable.ic_local_hospital_white_24dp, "health", 3),
    SCIENCE(R.string.category_science, R.drawable.ic_polymer_white_24dp, "science", 4),
    SPORTS(R.string.category_sports, R.drawable.ic_stars_white_24dp, "sports", 5),
    TECHNOLOGY(R.string.category_technology, R.drawable.ic_laptop_white_24dp, "technology", 6);

    @StringRes
    public int titleRes;
    @DrawableRes
    public int iconRes;
    public String name;
    public int id;

    Category(@StringRes int titleRes, @DrawableRes int iconRes, String name, int id) {
        this.titleRes = titleRes;
        this.iconRes = iconRes;
        this.name = name;
        this.id = id;
    }
}
