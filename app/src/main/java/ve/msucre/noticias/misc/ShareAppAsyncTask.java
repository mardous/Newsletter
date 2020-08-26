package ve.msucre.noticias.misc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.afollestad.materialdialogs.MaterialDialog;
import ve.msucre.noticias.R;
import ve.msucre.noticias.util.Intents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Christians Martínez Alvarado
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
