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
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.mardous.newsletter.App;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.model.Article;
import com.mardous.newsletter.api.model.Source;

import java.io.File;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class Intents {
    @NonNull
    public static Intent createBrowserIntent(@NonNull Article article) {
        return createBrowserIntent(article.getUrl());
    }

    @NonNull
    public static Intent createBrowserIntent(@NonNull Source source) {
        return createBrowserIntent(source.getUrl());
    }

    @NonNull
    public static Intent createBrowserIntent(@NonNull String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @NonNull
    public static Intent createEmailIntent(@NonNull Context context, @Nullable String text) {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:mardous.contact@gmail.com"));
        i.putExtra(Intent.EXTRA_EMAIL, "mardous.contact@gmail.com");
        i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        if (text != null) {
            i.putExtra(Intent.EXTRA_TEXT, text);
        }
        return i;
    }

    @NonNull
    public static Intent openShareIntent(@NonNull Resources res, @NonNull Article article) {
        final String source = article.getSource().getName();
        final String body = String.format("%s\n%s\n\n%s", article.getTitle(), article.getUrl(), res.getString(R.string.share_from_the_news_app));

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, source);
        i.putExtra(Intent.EXTRA_TEXT, body);
        return i;
    }

    @NonNull
    public static Intent createShareAppIntent(@NonNull Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, App.getInstance().getFileProviderAuthority(), file);
        return new Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_STREAM, uri)
                .setType("application/vnd.android.package-archive")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
