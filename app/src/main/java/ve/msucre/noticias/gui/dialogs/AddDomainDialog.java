package ve.msucre.noticias.gui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import ve.msucre.noticias.R;
import ve.msucre.noticias.util.Utils;
import ve.msucre.noticias.util.pref.PrefUtil;

import java.util.List;

/**
 * @author Christians Martínez Alvarado
 */
public class AddDomainDialog extends DialogFragment {

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_MODE = "extra_mode";

    private List<String> domains;

    public static AddDomainDialog create(@Nullable String url, int mode) {
        final AddDomainDialog dialog = new AddDomainDialog();
        final Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        args.putInt(EXTRA_MODE, mode);
        dialog.setArguments(args);
        return dialog;
    }

    public AddDomainDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        final int mode = args != null ? args.getInt(EXTRA_MODE) : MODE_ADD;
        final String url = args != null ? args.getString(EXTRA_URL) : null;

        domains = PrefUtil.getInstance(getActivity()).getExcludedDomains();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(mode == MODE_ADD ? R.string.exclude_domain_title : R.string.edit_domain_title)
                .input(getString(R.string.domain_url), url, false, (dialog1, input) -> {
                    String inputUrl = input.toString().trim();
                    if (Utils.isValidUrl(inputUrl)) {
                        if (mode == MODE_EDIT) {
                            domains.remove(url);
                        }
                        if (!domains.contains(inputUrl)) {
                            domains.add(inputUrl);
                            PrefUtil.getInstance(getActivity()).setExcludedDomains(domains);
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.it_does_not_appear_to_be_a_valid_url, Toast.LENGTH_SHORT).show();
                    }
                })
                .positiveText(mode == MODE_ADD ? R.string.action_add : android.R.string.ok)
                .negativeText(android.R.string.cancel);

        if (mode == MODE_EDIT) {
            builder.neutralText(R.string.action_remove)
                    .onNeutral((dialog, which) -> {
                        domains.remove(url);
                        PrefUtil.getInstance(getActivity()).setExcludedDomains(domains);
                    });
        }

        return builder.build();
    }
}
