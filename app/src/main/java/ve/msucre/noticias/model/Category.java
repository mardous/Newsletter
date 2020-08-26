package ve.msucre.noticias.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import ve.msucre.noticias.R;

/**
 * @author Christians Martínez Alvarado
 */
public enum Category {
    GENERAL(R.string.category_general, R.drawable.ic_pages_white_24dp, "general", 0),
    BUSINESS(R.string.category_business, R.drawable.ic_business_white_24dp, "business", 1),
    ENTERTAINMENT(R.string.category_entertainment, R.drawable.ic_local_movies_white_24dp, "entertainment", 2),
    HEALTH(R.string.category_health, R.drawable.ic_local_hospital_white_24dp, "health", 3),
    SCIENCE(R.string.category_science, R.drawable.ic_polymer_white_24dp, "science", 4),
    SPORTS(R.string.category_sports, R.drawable.ic_stars_white_24dp, "sports", 5),
    TECHNOLOGY(R.string.category_technology, R.drawable.ic_laptop_white_24dp, "technology", 6);

    @StringRes
    public int titleRes;
    @DrawableRes
    public int iconRes;
    public String name;
    public int id;

    Category(@StringRes int titleRes, @DrawableRes int iconRes, String name, int id) {
        this.titleRes = titleRes;
        this.iconRes = iconRes;
        this.name = name;
        this.id = id;
    }
}
