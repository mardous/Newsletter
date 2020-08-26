package ve.msucre.noticias.gui.activities;

import android.os.Bundle;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import ve.msucre.noticias.R;

/**
 * @author Christians Martínez Alvarado
 */
public class AppIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonCtaVisible(true);
        setButtonNextVisible(false);
        setButtonBackVisible(false);

        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);
        setButtonCtaLabel(R.string.get_started);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.app_name)
                .description(R.string.intro_welcome)
                .image(R.drawable.icon_web)
                .background(R.color.md_red_500)
                .backgroundDark(R.color.md_red_700)
                .layout(R.layout.fragment_simple_slide_small_image)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.home_label)
                .description(R.string.intro_description_home)
                .image(R.drawable.ic_home_white_24dp)
                .background(R.color.md_blue_grey_700)
                .backgroundDark(R.color.md_blue_grey_900)
                .layout(R.layout.fragment_simple_slide_small_image)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.sources_label)
                .description(R.string.intro_description_sources)
                .image(R.drawable.ic_public_white_24dp)
                .background(R.color.md_indigo_500)
                .backgroundDark(R.color.md_indigo_700)
                .layout(R.layout.fragment_simple_slide_small_image)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.search_label)
                .description(R.string.intro_description_search)
                .image(R.drawable.ic_search_white_24dp)
                .background(R.color.md_pink_500)
                .backgroundDark(R.color.md_pink_900)
                .layout(R.layout.fragment_simple_slide_small_image)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_finishing_title)
                .description(R.string.intro_finishing_description)
                .image(R.drawable.ic_check_circle_white_24dp)
                .background(R.color.md_orange_500)
                .backgroundDark(R.color.md_orange_700)
                .layout(R.layout.fragment_simple_slide_small_image)
                .build());
    }
}
