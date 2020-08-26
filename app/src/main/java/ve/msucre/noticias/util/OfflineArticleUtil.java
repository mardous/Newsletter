package ve.msucre.noticias.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ve.msucre.noticias.App;
import ve.msucre.noticias.api.model.Article;
import ve.msucre.noticias.model.Category;
import ve.msucre.noticias.util.pref.PrefUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado
 */
public class OfflineArticleUtil {

    private static final String LAST_UPDATE_TIME = "last_update_time";

    private SharedPreferences preferences;

    private static OfflineArticleUtil sInstance;

    public static OfflineArticleUtil getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new OfflineArticleUtil(context);
        }
        return sInstance;
    }

    private OfflineArticleUtil(@NonNull Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("offline_articles", Context.MODE_PRIVATE);
    }

    public boolean isUpdateRecommended() {
        Hour hour = PrefUtil.getInstance(App.getInstance()).getNewsUpdateInterval();
        if (hour != null) {
            long lastUpdateTimeMillis = preferences.getLong(LAST_UPDATE_TIME, 0);
            long currentTimeMillis = System.currentTimeMillis();
            return (currentTimeMillis - lastUpdateTimeMillis) > hour.hourInMillis;
        }
        return false;
    }

    public List<Article> getOfflineArticles(@NonNull Category category) {
        String json = preferences.getString(category.name, null);
        if (json != null) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Article>>() {
            }.getType();

            try {
                return gson.fromJson(json, collectionType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public void setOfflineArticles(@NonNull Category category, List<Article> articles) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Article>>() {
        }.getType();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(category.name, gson.toJson(articles, collectionType)).apply();
        editor.putLong(LAST_UPDATE_TIME, System.currentTimeMillis()).apply();
        editor.apply();
    }
}
