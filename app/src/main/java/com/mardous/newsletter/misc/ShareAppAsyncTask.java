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

package com.mardous.newsletter.misc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mardous.newsletter.R;
import com.mardous.newsletter.util.Intents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class ShareAppAsyncTask extends DialogAsyncTask<Boolean, Void, Boolean> {

    private final File source;
    private final File destination;

    public ShareAppAsyncTask(@NonNull Context context) {
        super(context);
        final String apkName = context.getString(R.string.app_name).concat(".apk");
        this.source = new File(context.getPackageResourcePath());
        this.destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);
    }

    @Override
    public Dialog createDialog(@NonNull Context context) {
        return new MaterialDialog.Builder(context)
                .title(R.string.action_share_app)
                .content(R.string.share_app_preparing)
                .progress(true, 0)
                .cancelListener(dialog -> cancel(false))
                .dismissListener(dialogInterface -> cancel(false))
                .negativeText(android.R.string.cancel)
                .onNegative((dialog, action) -> cancel(false))
                .show();
    }

    @Override
    protected Boolean doInBackground(Boolean... args) {
        return copyFile(source, destination);
    }

    private boolean copyFile(@NonNull File from, File to) {
        if (!from.isFile() || !from.exists()) {
            return false;
        }

        if (to.exists()) {
            to.delete();
        }

        try {
            to.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        FileChannel source;
        FileChannel destination;

        try {
            source = new FileInputStream(from).getChannel();
            destination = new FileOutputStream(to).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        final Context context = getContext();
        if (context != null && result) {
            context.startActivity(Intent.createChooser(Intents.createShareAppIntent(context, destination),
                    context.getString(R.string.action_share_app)));
        }
    }
}
