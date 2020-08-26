package ve.msucre.noticias.gui.fragments.base;

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
import ve.msucre.noticias.R;
import ve.msucre.noticias.gui.ErrorViewPresenter;

/**
 * @author Christians Martínez Alvarado
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
