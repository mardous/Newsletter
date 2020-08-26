package ve.msucre.noticias.preference;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import ve.msucre.noticias.R;
import ve.msucre.noticias.gui.dialogs.AddDomainDialog;
import ve.msucre.noticias.util.pref.PrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado
 */
public class ExcludeDomainPreferenceDialog extends DialogFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private List<String> excludedDomains;

    public static ExcludeDomainPreferenceDialog create() {
        return new ExcludeDomainPreferenceDialog();
    }

    public ExcludeDomainPreferenceDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        PrefUtil.getInstance(getActivity()).registerOnSharedPreferenceChangedListener(this);

        refreshExcludedDomains();
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.preference_excluded_domains_title)
                .items(excludedDomains)
                .itemsCallback((materialDialog, itemView, position, text) -> {
                    AddDomainDialog.create(text.toString(), AddDomainDialog.MODE_EDIT).show(getFragmentManager(), "EDIT_DOMAIN");
                })
                .positiveText(android.R.string.ok)
                .negativeText(R.string.action_add)
                .neutralText(R.string.action_clear)
                .autoDismiss(false)
                .onPositive((materialDialog, which) -> dismiss())
                .onNegative((materialDialog, which) -> {
                    AddDomainDialog.create(null, AddDomainDialog.MODE_ADD).show(getChildFragmentManager(), "ADD_DOMAIN");
                })
                .onNeutral((materialDialog, which) -> {
                    PrefUtil.getInstance(getActivity()).setExcludedDomains(new ArrayList<>());
                })
                .build();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        PrefUtil.getInstance(getActivity()).unregisterOnSharedPreferenceChangedListener(this);
        super.onDismiss(dialog);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (key.equals(PrefUtil.EXCLUDED_DOMAINS)) {
            refreshExcludedDomains();
        }
    }

    private void refreshExcludedDomains() {
        excludedDomains = PrefUtil.getInstance(getActivity()).getExcludedDomains();

        MaterialDialog dialog = (MaterialDialog) getDialog();
        if (dialog != null) {
            String[] pathArray = new String[excludedDomains.size()];
            pathArray = excludedDomains.toArray(pathArray);
            dialog.setItems(pathArray);
        }
    }
}
