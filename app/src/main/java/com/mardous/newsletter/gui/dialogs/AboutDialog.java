package com.mardous.newsletter.gui.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.mardous.newsletter.R;
import com.mardous.newsletter.util.Intents;
import de.psdev.licensesdialog.LicensesDialog;

/**
 * @author Christians Martínez Alvarado
 */
public class AboutDialog extends DialogFragment implements View.OnClickListener {

    private static final String NEWS_API_URL = "https://www.newsapi.org/";
    private static final String GITHUB_REPOSITORY_URL = "https://www.github.com/mardous/Newsletter";

    @BindView(R.id.licenses)
    View licenses;
    @BindView(R.id.licenses_icon)
    ImageView licensesIcon;
    @BindView(R.id.email)
    View email;
    @BindView(R.id.email_icon)
    ImageView emailIcon;
    @BindView(R.id.fork_on_github)
    View forkOnGitHub;
    @BindView(R.id.fork_on_github_icon)
    ImageView forkOnGitHubIcon;
    @BindView(R.id.newsapi)
    View newsApi;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null);
        ButterKnife.bind(this, customView);

        ColorFilter colorFilter = new PorterDuffColorFilter(ThemeStore.textColorSecondary(getActivity()), PorterDuff.Mode.SRC_IN);

        licenses.setOnClickListener(this);
        licensesIcon.setColorFilter(colorFilter);
        email.setOnClickListener(this);
        emailIcon.setColorFilter(colorFilter);
        forkOnGitHub.setOnClickListener(this);
        forkOnGitHubIcon.setColorFilter(colorFilter);
        newsApi.setOnClickListener(this);

        return new MaterialDialog.Builder(getActivity())
                .customView(customView, true)
                .positiveText(android.R.string.ok)
                .build();
    }

    @Override
    public void onClick(View view) {
        if (view == licenses) {
            new LicensesDialog.Builder(getActivity())
                    .setTitle(R.string.open_source_licenses)
                    .setNotices(R.raw.notices)
                    .setNoticesCssStyle(getString(R.string.license_dialog_style)
                            .replace("{bg-color}", ThemeSingleton.get().darkTheme ? "424242" : "ffffff")
                            .replace("{text-color}", ThemeSingleton.get().darkTheme ? "ffffff" : "000000")
                            .replace("{license-bg-color}", ThemeSingleton.get().darkTheme ? "535353" : "eeeeee"))
                    .setDividerColor(ThemeStore.accentColor(getActivity()))
                    .setIncludeOwnLicense(true)
                    .build()
                    .show();
        } else if (view == email) {
            startActivity(Intent.createChooser(Intents.createEmailIntent(getActivity(), null), getString(R.string.email)));
        } else if (view == forkOnGitHub) {
            openUrl(GITHUB_REPOSITORY_URL);
        } else if (view == newsApi) {
            openUrl(NEWS_API_URL);
        }
    }

    private void openUrl(@NonNull String url) {
        startActivity(Intents.createBrowserIntent(url));
    }
}
