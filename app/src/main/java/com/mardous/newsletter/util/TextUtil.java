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
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.model.Article;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class TextUtil {

    @NonNull
    public static Spanned getFormattedText(@NonNull String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
        }
        return Html.fromHtml(source);
    }

    @NonNull
    public static Spanned getTitleFormattedText(@NonNull String text) {
        return getFormattedText("<b>" + text + "</b>");
    }

    @NonNull
    public static Spanned getTextWithTitle(@NonNull Context context, String title, String text) {
        return getFormattedText(context.getString(R.string.title_and_text_format, title, text));
    }

    @NonNull
    public static Spanned getSourceAndAuthor(@NonNull Context context, Article article) {
        return getTextWithTitle(context, article.getSource().getName(), article.getAuthor());
    }

    @NonNull
    public static Spanned getDateAndTime(@NonNull Context context, Article article) {
        return getTextWithTitle(context, getDateFormatText(article.getPublishedAt()), getDateToTimeFormatText(article.getPublishedAt()));
    }

    @Nullable
    public static String getDateToTimeFormatText(String publishedAt) {
        Locale locale = new Locale(Utils.getLanguage(), Utils.getCountry());
        PrettyTime p = new PrettyTime(locale);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale);
            Date date = sdf.parse(publishedAt);
            return p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static String getDateFormatText(@NonNull String publishedAt) {
        Locale locale = new Locale(Utils.getLanguage(), Utils.getCountry());
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", locale);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale).parse(publishedAt);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return publishedAt;
    }
}
