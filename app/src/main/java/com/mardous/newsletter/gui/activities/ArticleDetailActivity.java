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

package com.mardous.newsletter.gui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.snackbar.Snackbar;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.util.ATHUtil;
import com.kabouzeid.appthemehelper.util.ToolbarContentTintHelper;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.model.Article;
import com.mardous.newsletter.gui.activities.base.AbsThemeActivity;
import com.mardous.newsletter.util.Intents;
import com.mardous.newsletter.util.TextUtil;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class ArticleDetailActivity extends AbsThemeActivity {

    public static final String EXTRA_ARTICLE = "extra_article";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    MaterialProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    private Article article;
    private String errorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        article = getIntent().getParcelableExtra(EXTRA_ARTICLE);
        if (article == null) {
            finish();
        }

        setUpToolbar();
        setUpProgressBar();
        loadNewsDetail();
        loadNewsUrl();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    private void setUpProgressBar() {
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(ThemeStore.accentColor(this)));
    }

    private void loadNewsDetail() {
        getSupportActionBar().setTitle(article.getTitle());

        if (Utils.isValidAuthorName(article)) {
            getSupportActionBar().setSubtitle(TextUtil.getSourceAndAuthor(this, article));
        } else {
            getSupportActionBar().setSubtitle(article.getSource().getName());
        }
    }

    private void loadNewsUrl() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.getSettings().setAppCacheEnabled(PrefUtil.getInstance(this).loadFromCache());
        webView.getSettings().setAppCachePath(new File(getCacheDir(), "/web_cache/").getAbsolutePath());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                setLoading(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setLoading(false);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // super.onReceivedError(view, errorCode, description, failingUrl);
                setLoading(false);
                showErrorPage();
                showErrorSnackbar();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // super.onReceivedError(view, request, error);
                setLoading(false);
                showErrorPage();
                showErrorSnackbar();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boolean mustBeDark = PrefUtil.getInstance(this).darkTheme();
            webView.setForceDarkAllowed(mustBeDark);
            webView.getSettings().setForceDark(mustBeDark ? WebSettings.FORCE_DARK_ON : WebSettings.FORCE_DARK_OFF);
        } else {
            webView.setBackgroundColor(ATHUtil.resolveColor(this, R.attr.webBackgroundColor));
        }

        webView.loadUrl(article.getUrl());
    }

    private void setLoading(boolean loading) {
        if (progressBar != null)
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void showErrorPage() {
        if (errorData == null) {
            try {
                StringBuilder buf = new StringBuilder();
                InputStream json = getAssets().open("errorpage.html");
                BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
                String str;
                while ((str = in.readLine()) != null)
                    buf.append(str);
                in.close();

                errorData = buf.toString()
                        .replace("{title}", getString(R.string.cannot_load_news))
                        .replace("{text}", getString(R.string.cannot_load_news_message))
                        .replace("{tp}", colorToHex(ThemeStore.textColorPrimary(this)))
                        .replace("{ts}", colorToHex(ThemeStore.textColorSecondary(this)));
            } catch (Exception e) {
                errorData = "<h1>Unable to load</h1><p>" + e.getLocalizedMessage() + "</p>";
            }
        }
        webView.loadData(errorData, "text/html", "UTF-8");
    }

    private void showErrorSnackbar() {
        final View view = getWindow().getDecorView();
        if (view != null) {
            Snackbar.make(view, R.string.press_the_button_to_retry, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, view1 -> {
                        if (webView != null) webView.loadUrl(article.getUrl());
                    })
                    .setActionTextColor(ThemeStore.accentColor(this))
                    .show();
        }
    }

    @NonNull
    private static String colorToHex(int color) {
        return Integer.toHexString(color).substring(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(this, toolbar, menu, getToolbarBackgroundColor(toolbar));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(this, toolbar);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (itemId == R.id.action_share) {
            startActivity(Intent.createChooser(Intents.openShareIntent(getResources(), article), getString(R.string.share_with)));
            return true;
        }
        if (itemId == R.id.action_open_with_browser) {
            startActivity(Intent.createChooser(Intents.createBrowserIntent(article), item.getTitle()));
            return true;
        }
        return false;
    }
}
