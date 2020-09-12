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

package com.mardous.newsletter.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.kabouzeid.appthemehelper.common.prefs.supportv7.ATEDialogPreference;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class RegionPreference extends ATEDialogPreference {
    public RegionPreference(Context context) {
        super(context);
    }

    public RegionPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RegionPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
