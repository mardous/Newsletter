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

package com.mardous.newsletter;

import android.app.Application;
import androidx.annotation.NonNull;
import com.kabouzeid.appthemehelper.ThemeStore;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        if (!ThemeStore.isConfigured(this, 1)) {
            ThemeStore.editTheme(this)
                    .accentColorRes(R.color.md_red_500)
                    .commit();
        }
    }

    @NonNull
    public static App getInstance() {
        return app;
    }

    @NonNull
    public String getFileProviderAuthority() {
        return String.format("%s.provider", getPackageName());
    }
}
