package com.mardous.newsletter.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import com.mardous.newsletter.R;

/**
 * @author Christians Martínez Alvarado
 */
public class ErrorViewPresenter {

    private ViewGroup container;

    private View errorView;
    private ImageView errorImage;
    private TextView errorTitle;
    private TextView errorMessage;
    private Button errorButton;

    private boolean preventShow;

    @UiThread
    public ErrorViewPresenter(@NonNull Context context, @NonNull ViewGroup container, @Nullable RetryCallback callback) {
        this.container = container;

        LayoutInflater.from(context).inflate(R.layout.error, container);
        this.errorView = container.findViewById(R.id.error_view);
        this.errorImage = errorView.findViewById(R.id.error_image);
        this.errorTitle = errorView.findViewById(R.id.error_title);
        this.errorMessage = errorView.findViewById(R.id.error_message);
        this.errorButton = errorView.findViewById(R.id.retry_btn);

        if (callback != null) {
            errorButton.setOnClickListener(view -> callback.onRetry());
        } else {
            errorButton.setVisibility(View.GONE);
        }
    }

    @UiThread
    public ErrorViewPresenter image(@DrawableRes int imageRes) {
        if (errorImage != null) {
            if (imageRes != 0) {
                errorImage.setImageResource(imageRes);
            } else {
                errorImage.setVisibility(View.GONE);
            }
        }
        return this;
    }

    @UiThread
    public ErrorViewPresenter title(@StringRes int titleRes) {
        if (errorTitle != null) {
            if (titleRes != 0) {
                errorTitle.setText(titleRes);
            } else {
                errorTitle.setVisibility(View.GONE);
            }
        }
        return this;
    }

    @UiThread
    public ErrorViewPresenter message(@StringRes int messageRes) {
        if (errorMessage != null) {
            if (messageRes != 0) {
                errorMessage.setText(messageRes);
            } else {
                errorMessage.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public ErrorViewPresenter preventShow(boolean preventShow) {
        this.preventShow = preventShow;
        return this;
    }

    @UiThread
    public boolean show() {
        if (!preventShow) {
            if (errorView != null) {
                errorView.setVisibility(View.VISIBLE);
            }
        }
        return !preventShow;
    }

    @UiThread
    public void hide() {
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
    }

    @UiThread
    public void destroy() {
        if (errorView != null) {
            container.removeView(errorView);
            errorView = null;
            errorImage = null;
            errorTitle = null;
            errorMessage = null;
            errorButton = null;
        }
    }

    public interface RetryCallback {
        void onRetry();
    }
}
