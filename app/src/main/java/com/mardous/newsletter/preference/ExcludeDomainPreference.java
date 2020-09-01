package com.mardous.newsletter.preference;

import android.content.Context;
import android.util.AttributeSet;
import com.kabouzeid.appthemehelper.common.prefs.supportv7.ATEDialogPreference;

/**
 * @author Christians Martínez Alvarado
 */
public class ExcludeDomainPreference extends ATEDialogPreference {

    public ExcludeDomainPreference(Context context) {
        super(context);
    }

    public ExcludeDomainPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExcludeDomainPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExcludeDomainPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
