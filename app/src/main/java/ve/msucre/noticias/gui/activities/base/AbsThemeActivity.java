package ve.msucre.noticias.gui.activities.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.kabouzeid.appthemehelper.common.ATHToolbarActivity;
import com.kabouzeid.appthemehelper.util.MaterialDialogsUtil;
import ve.msucre.noticias.R;
import ve.msucre.noticias.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado
 */
public abstract class AbsThemeActivity extends ATHToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(getThemeRes());
        super.onCreate(savedInstanceState);
        MaterialDialogsUtil.updateMaterialDialogsThemeSingleton(this);
    }

    private int getThemeRes() {
        return PrefUtil.getInstance(this).darkTheme() ? R.style.AppTheme : R.style.AppTheme_Light;
    }
}
