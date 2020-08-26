package ve.msucre.noticias.model;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import ve.msucre.noticias.R;
import ve.msucre.noticias.gui.fragments.HomeFragment;
import ve.msucre.noticias.gui.fragments.SourcesFragment;
import ve.msucre.noticias.gui.fragments.SearchFragment;

/**
 * @author Christians Martínez Alvarado
 */
public enum MainFr {
    HOME(HomeFragment.create(), R.id.action_home),
    SOURCES(SourcesFragment.create(), R.id.action_sources),
    SEARCH(SearchFragment.create(), R.id.action_search);

    public Fragment fragment;
    @IdRes
    public int itemId;

    MainFr(Fragment fragment, @IdRes int itemId) {
        this.fragment = fragment;
        this.itemId = itemId;
    }
}
