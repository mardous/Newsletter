/*
 * Copyright (C) 2014 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mardous.newsletter.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SearchHistory extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "search_history.db";
    private static final int VERSION = 1;
    private static final int MAX_ITEMS_IN_DB = 25;

    private static SearchHistory sInstance = null;

    private SearchHistory(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static synchronized SearchHistory getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new SearchHistory(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SearchHistoryColumns.NAME + " ("
                + SearchHistoryColumns.SEARCHSTRING + " TEXT NOT NULL,"
                + SearchHistoryColumns.TIMESEARCHED + " LONG NOT NULL);");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SearchHistoryColumns.NAME);
        onCreate(db);
    }

    public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SearchHistoryColumns.NAME);
        onCreate(db);
    }

    public void addSearchString(@Nullable String searchString) {
        if (searchString == null) {
            return;
        }

        searchString = searchString.trim();
        if (searchString.isEmpty()) {
            return;
        }

        final SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        try {
            database.delete(SearchHistoryColumns.NAME,
                    SearchHistoryColumns.SEARCHSTRING + " = ? COLLATE NOCASE", new String[]{searchString});

            final ContentValues values = new ContentValues(2);
            values.put(SearchHistoryColumns.SEARCHSTRING, searchString);
            values.put(SearchHistoryColumns.TIMESEARCHED, System.currentTimeMillis());
            database.insert(SearchHistoryColumns.NAME, null, values);

            try (Cursor oldest = database.query(SearchHistoryColumns.NAME,
                    new String[]{SearchHistoryColumns.TIMESEARCHED}, null, null, null, null,
                    SearchHistoryColumns.TIMESEARCHED + " ASC")) {

                if (oldest != null && oldest.getCount() > MAX_ITEMS_IN_DB) {
                    oldest.moveToPosition(oldest.getCount() - MAX_ITEMS_IN_DB);
                    long timeOfRecordToKeep = oldest.getLong(0);

                    database.delete(SearchHistoryColumns.NAME,
                            SearchHistoryColumns.TIMESEARCHED + " < ?",
                            new String[]{String.valueOf(timeOfRecordToKeep)});

                }
            }
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }

    private Cursor queryRecentSearches(final String limit) {
        final SQLiteDatabase database = getReadableDatabase();
        return database.query(SearchHistoryColumns.NAME,
                new String[]{SearchHistoryColumns.SEARCHSTRING}, null, null, null, null,
                SearchHistoryColumns.TIMESEARCHED + " DESC", limit);
    }

    @NonNull
    public List<String> getRecentSearches() {
        List<String> results = new ArrayList<>(MAX_ITEMS_IN_DB);

        try (Cursor searches = queryRecentSearches(String.valueOf(MAX_ITEMS_IN_DB))) {
            if (searches != null && searches.moveToFirst()) {
                int colIdx = searches.getColumnIndex(SearchHistoryColumns.SEARCHSTRING);

                do {
                    results.add(searches.getString(colIdx));
                } while (searches.moveToNext());
            }
        }

        return results;
    }

    public void removeItem(String data) {
        getWritableDatabase().delete(SearchHistoryColumns.NAME, SearchHistoryColumns.SEARCHSTRING + "=?",
                new String[]{data});
    }

    public void clearHistory() {
        getWritableDatabase().delete(SearchHistoryColumns.NAME, null, null);
    }

    public interface SearchHistoryColumns {
        /* Table name */
        String NAME = "searchhistory";

        /* What was searched */
        String SEARCHSTRING = "searchstring";

        /* Time of search */
        String TIMESEARCHED = "timesearched";
    }
}
