package ve.msucre.noticias.gui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.common.prefs.supportv7.ATEColorPreference;
import com.kabouzeid.appthemehelper.common.prefs.supportv7.ATEPreferenceFragmentCompat;
import com.kabouzeid.appthemehelper.util.ColorUtil;
import ve.msucre.noticias.R;
import ve.msucre.noticias.gui.activities.base.AbsThemeActivity;
import ve.msucre.noticias.preference.ExcludeDomainPreference;
import ve.msucre.noticias.preference.ExcludeDomainPreferenceDialog;
import ve.msucre.noticias.preference.RegionPreference;
import ve.msucre.noticias.preference.RegionPreferenceDialog;
import ve.msucre.noticias.provider.SearchHistory;
import ve.msucre.noticias.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado
 */
public class SettingsActivity extends AbsThemeActivity implements ColorChooserDialog.ColorCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        } else {
            SettingsFragment fragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment != null) {
                fragment.invalidateOptions();
            }
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        if (dialog.getTitle() == R.string.preference_accent_color_title) {
            ThemeStore.editTheme(this)
                    .accentColor(selectedColor)
                    .commit();
        }
        recreate();
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public static class SettingsFragment extends ATEPreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
        }

        @Nullable
        @Override
        public DialogFragment onCreatePreferenceDialog(Preference preference) {
            if (preference instanceof ExcludeDomainPreference) {
                return ExcludeDomainPreferenceDialog.create();
            } else if (preference instanceof RegionPreference) {
                return RegionPreferenceDialog.newInstance();
            }
            return super.onCreatePreferenceDialog(preference);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            invalidateOptions();
        }

        public void invalidateOptions() {
            final Preference darkThemePref = findPreference(PrefUtil.DARK_THEME);
            darkThemePref.setOnPreferenceChangeListener((preference, newValue) -> {
                ThemeStore.markChanged(getActivity());
                getActivity().recreate();
                return true;
            });

            final ATEColorPreference accentColorPref = findPreference(PrefUtil.ACCENT_COLOR);
            final int accentColor = ThemeStore.accentColor(getActivity());
            accentColorPref.setColor(accentColor, ColorUtil.darkenColor(accentColor));
            accentColorPref.setOnPreferenceClickListener(preference -> {
                new ColorChooserDialog.Builder(getActivity(), R.string.preference_accent_color_title)
                        .accentMode(true)
                        .allowUserColorInput(true)
                        .allowUserColorInputAlpha(false)
                        .preselect(accentColor)
                        .show(getActivity());
                return true;
            });

            final Preference sortSearchResultByPref = findPreference(PrefUtil.SORT_SEARCH_RESULT_BY);
            setSummary(sortSearchResultByPref);
            sortSearchResultByPref.setOnPreferenceChangeListener((preference, newValue) -> {
                setSummary(sortSearchResultByPref, newValue);
                return true;
            });

            findPreference("delete_search_history").setOnPreferenceClickListener(preference -> {
                SearchHistory.getInstance(getActivity()).clearHistory();
                Toast.makeText(getActivity(), R.string.search_history_deleted, Toast.LENGTH_SHORT).show();
                return false;
            });
        }

        private static void setSummary(@NonNull Preference preference) {
            setSummary(preference, PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), ""));
        }

        private static void setSummary(Preference preference, @NonNull Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else {
                preference.setSummary(stringValue);
            }
        }
    }
}
