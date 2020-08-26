package ve.msucre.noticias;

import android.app.Application;
import androidx.annotation.NonNull;
import com.kabouzeid.appthemehelper.ThemeStore;

/**
 * @author Christians Martínez Alvarado
 */
public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        if (!ThemeStore.isConfigured(this, 1)) {
            ThemeStore.editTheme(this)
                    .accentColorRes(R.color.md_red_500)
                    .commit();
        }
    }

    @NonNull
    public static App getInstance() {
        return app;
    }

    @NonNull
    public String getFileProviderAuthority() {
        return String.format("%s.provider", getPackageName());
    }
}
