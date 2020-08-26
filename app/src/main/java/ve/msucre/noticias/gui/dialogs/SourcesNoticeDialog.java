package ve.msucre.noticias.gui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import com.afollestad.materialdialogs.MaterialDialog;
import ve.msucre.noticias.R;

/**
 * @author Christians Martínez Alvarado
 */
public class SourcesNoticeDialog extends DialogFragment implements DialogInterface.OnShowListener {

    private static final String SOURCES_NOTICE_SHOWN = "sources_notice_shown";

    public static SourcesNoticeDialog newInstance() {
        return new SourcesNoticeDialog();
    }

    public SourcesNoticeDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.sources_notice_title)
                .content(R.string.sources_notice_message)
                .positiveText(android.R.string.ok)
                .showListener(this)
                .build();
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putBoolean(SOURCES_NOTICE_SHOWN, true).apply();
    }

    public static boolean wasShown(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SOURCES_NOTICE_SHOWN, false);
    }
}
