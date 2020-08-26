package ve.msucre.noticias.model;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ve.msucre.noticias.BuildConfig;
import ve.msucre.noticias.util.Utils;
import ve.msucre.noticias.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado
 */
@SuppressWarnings("WeakerAccess")
public class Query {

    private String category;
    private String language;
    private String country;
    private String excludeDomains;
    private String apiKey;
    private int resultsLimit;

    public Query(@NonNull Context context, @Nullable Category category) {
        this.setCategory(category != null ? category.name : null);
        this.setLanguage(Utils.getLanguage());
        this.setCountry(Utils.getCountry());
        this.setExcludeDomains(Utils.getExcludedDomainsString(context));
        this.setApiKey(BuildConfig.NewsAPIKey);
        this.setResultsLimit(PrefUtil.getInstance(context).getResultsLimit());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExcludeDomains() {
        return excludeDomains;
    }

    public void setExcludeDomains(String excludeDomains) {
        this.excludeDomains = excludeDomains;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getResultsLimit() {
        return resultsLimit;
    }

    public void setResultsLimit(int resultsLimit) {
        this.resultsLimit = resultsLimit;
    }
}
