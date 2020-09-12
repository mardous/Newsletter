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

package com.mardous.newsletter.gui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.ApiClient;
import com.mardous.newsletter.api.model.Article;
import com.mardous.newsletter.api.model.News;
import com.mardous.newsletter.api.request.RequestBaseCallback;
import com.mardous.newsletter.gui.activities.SettingsActivity;
import com.mardous.newsletter.gui.adapter.ArticleAdapter;
import com.mardous.newsletter.gui.dialogs.AboutDialog;
import com.mardous.newsletter.gui.dialogs.bottomsheet.CategoriesDialog;
import com.mardous.newsletter.gui.fragments.base.AbsErrorViewFragment;
import com.mardous.newsletter.misc.ShareAppAsyncTask;
import com.mardous.newsletter.model.Category;
import com.mardous.newsletter.model.Query;
import com.mardous.newsletter.util.OfflineArticleUtil;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class HomeFragment extends AbsErrorViewFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        SwipeRefreshLayout.OnRefreshListener, CategoriesDialog.Listener {

    private static final int PERMISSION_REQUEST = 100;

    private Unbinder unbinder;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    FastScrollRecyclerView recyclerView;

    private Category category;
    private Call<News> call;

    private ArticleAdapter articleAdapter;
    private List<Article> articles = new ArrayList<>();

    private ApiClient apiClient;
    private Query query;

    public static HomeFragment create() {
        return new HomeFragment();
    }

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar();
        setUpSwipeRefreshLayout();
        setUpRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PrefUtil.getInstance(getActivity()).registerOnSharedPreferenceChangedListener(this);

        apiClient = ApiClient.create();
        setCategory(PrefUtil.getInstance(getActivity()).getLastCategory());
    }

    @Override
    public void onDestroyView() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.stopNestedScroll();
        }
        PrefUtil.getInstance(getActivity()).unregisterOnSharedPreferenceChangedListener(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    private void setUpToolbar() {
        getMainActivity().setSupportActionBar(toolbar);
        getActivity().setTitle(R.string.app_name);
    }

    private void setUpSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ThemeStore.accentColor(getActivity()));
    }

    private void setUpRecyclerView() {
        articleAdapter = new ArticleAdapter(getMainActivity(), new ArrayList<>(), Glide.with(this));
        articleAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                recyclerView.scrollToPosition(0);
            }
        });

        Utils.applyFastScrollRecyclerViewColor(getActivity(), recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(articleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void startLoadingPost() {
        swipeRefreshLayout.post(this::startLoading);
    }

    @Override
    protected void startLoading() {
        super.startLoading();
        swipeRefreshLayout.setRefreshing(true);

        call = apiClient.getApiInterface().getTopHeadlines(query.getCountry(), query.getCategory(),
                query.getResultsLimit(), query.getApiKey());
        call.enqueue(new RequestBaseCallback<News>() {
            @Override
            protected void onFinished(News result, Code code) {
                if (getActivity() == null) return;

                swipeRefreshLayout.setRefreshing(false);
                switch (code) {
                    case SUCCESS:
                        List<Article> articles = getArticlesWithoutExcludedSources(result.getArticle());
                        setArticles(articles);
                        if (!articles.isEmpty()) {
                            OfflineArticleUtil.getInstance(getActivity()).setOfflineArticles(category, articles);
                        }
                        break;
                    case ERROR:
                        if (errorCode != null) {
                            showErrorView(R.drawable.no_result, R.string.no_result_title, errorCode.messageRes);
                        } else {
                            showErrorView(R.drawable.no_result, R.string.no_result_title, R.string.please_try_again);
                        }
                        break;
                    case NETWORK_FAILURE:
                        showErrorView(R.drawable.oops, R.string.network_error_title, R.string.network_error_message);
                        break;
                }
            }
        });
    }

    private void setCategory(Category category) {
        if (call != null) {
            call.cancel();
            call = null;
        }
        this.category = category;
        this.query = new Query(getActivity(), category);
        setArticles(OfflineArticleUtil.getInstance(getActivity()).getOfflineArticles(category));
    }

    private void setArticles(List<Article> result) {
        if (!articles.isEmpty()) {
            articles.clear();
        }
        this.articles = result;
        if (articleAdapter != null) {
            articleAdapter.swapDataSet(articles);
        }
        if (OfflineArticleUtil.getInstance(getActivity()).isUpdateRecommended() || articleAdapter.getItemCount() == 0) {
            startLoading();
        }
    }

    @NonNull
    private List<Article> getArticlesWithoutExcludedSources(@NonNull List<Article> articles) {
        List<Article> list = new ArrayList<>();
        List<String> exclusions = PrefUtil.getInstance(getActivity()).getExcludedDomains();
        for (Article a : articles) {
            if (!Utils.isSourceExcluded(exclusions, a)) list.add(a);
        }
        return list;
    }

    @Override
    protected boolean shouldShowErrorView() {
        return articleAdapter == null || articleAdapter.getItemCount() == 0;
    }

    @Override
    public void onCategoryClicked(Category category) {
        hideErrorView();
        setCategory(category);
        PrefUtil.getInstance(getActivity()).setLastCategory(category);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), WRITE_EXTERNAL_STORAGE)) {
                        Snackbar.make(getView(), R.string.permissions_denied, Snackbar.LENGTH_LONG)
                                .setAction(R.string.action_grant, view -> requestPermissions())
                                .setActionTextColor(ThemeStore.accentColor(getActivity()))
                                .show();
                    } else {
                        Snackbar.make(getView(), R.string.permissions_denied, Snackbar.LENGTH_LONG)
                                .setAction(R.string.action_settings, view -> {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                })
                                .setActionTextColor(ThemeStore.accentColor(getActivity()))
                                .show();
                    }
                    return;
                }
            }
            shareApp();
        }
    }

    private void shareApp() {
        if (ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            new ShareAppAsyncTask(getActivity()).execute();
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
    }

    @Override
    protected void createOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        if (itemId == R.id.action_about) {
            new AboutDialog().show(getFragmentManager(), "ABOUT");
            return true;
        }
        if (itemId == R.id.action_share_app) {
            shareApp();
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        startLoading();
    }

    @NonNull
    @Override
    protected ViewGroup getErrorView() {
        return getView().findViewById(R.id.recycler_view_container);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, @NonNull String key) {
        switch (key) {
            case PrefUtil.REGION:
                query.setCountry(Utils.getCountry());
                query.setLanguage(Utils.getLanguage());
                startLoadingPost();
                break;
            case PrefUtil.RESULTS_LIMIT:
                query.setResultsLimit(PrefUtil.getInstance(getActivity()).getResultsLimit());
                startLoadingPost();
                break;
            case PrefUtil.EXCLUDED_DOMAINS:
                startLoadingPost();
                break;
        }
    }
}