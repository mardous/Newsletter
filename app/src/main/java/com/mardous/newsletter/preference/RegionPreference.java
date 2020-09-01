package com.mardous.newsletter.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.kabouzeid.appthemehelper.common.prefs.supportv7.ATEDialogPreference;

/**
 * @author Christians Martínez Alvarado
 */
public class RegionPreference extends ATEDialogPreference {
    public RegionPreference(Context context) {
        super(context);
    }

    public RegionPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RegionPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
