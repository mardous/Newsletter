package ve.msucre.noticias.model;

import android.content.Context;
import androidx.annotation.NonNull;
import ve.msucre.noticias.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado
 */
public class SearchQuery extends Query {

    private String keyword;
    private String sortBy;

    public SearchQuery(@NonNull Context context) {
        super(context, null);
        this.setSortBy(PrefUtil.getInstance(context).getSearchResultSortOrder());
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
