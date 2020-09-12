/*
 * Copyright (c) 2020  Christians Martínez Alvarado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mardous.newsletter.gui.fragments.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import com.google.android.material.snackbar.Snackbar;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.mardous.newsletter.R;
import com.mardous.newsletter.gui.ErrorViewPresenter;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class AbsErrorViewFragment extends AbsMainActivityFragment implements ErrorViewPresenter.RetryCallback {

    private ErrorViewPresenter errorView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        errorView = new ErrorViewPresenter(getActivity(), getErrorView(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        errorView.destroy();
    }

    @CallSuper
    @UiThread
    protected void startLoading() {
        errorView.hide();
    }

    @CallSuper
    @UiThread
    protected void showErrorView(@DrawableRes int imageRes, @StringRes int titleRes, @StringRes int messageRes) {
        Activity activity = getActivity();
        if (activity == null) return;

        if (!errorView.image(imageRes)
                .title(titleRes)
                .message(messageRes)
                .preventShow(!shouldShowErrorView())
                .show()) {
            Snackbar.make(getErrorView(), messageRes, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry, view -> startLoading())
                    .setActionTextColor(ThemeStore.accentColor(getActivity()))
                    .show();
        }
    }

    @CallSuper
    @UiThread
    protected void hideErrorView() {
        errorView.hide();
    }

    protected abstract boolean shouldShowErrorView();

    @NonNull
    protected abstract ViewGroup getErrorView();

    @Override
    public void onRetry() {
        startLoading();
    }
}
