package com.mardous.newsletter.gui.fragments.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.kabouzeid.appthemehelper.common.ATHToolbarActivity;
import com.kabouzeid.appthemehelper.util.ToolbarContentTintHelper;
import com.mardous.newsletter.gui.activities.MainActivity;

/**
 * @author Christians Martínez Alvarado
 */
public abstract class AbsMainActivityFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    protected void createOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {}

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        createOptionsMenu(menu, inflater);
        Activity activity = getActivity();
        if (activity == null) return;
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(activity, getToolbar(), menu, ATHToolbarActivity.getToolbarBackgroundColor(getToolbar()));
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Activity activity = getActivity();
        if (activity == null) return;
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(activity, getToolbar());
    }

    @NonNull
    protected abstract Toolbar getToolbar();

    @Nullable
    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
