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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.util.TintHelper;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.ApiClient;
import com.mardous.newsletter.api.model.Article;
import com.mardous.newsletter.api.model.News;
import com.mardous.newsletter.api.request.RequestBaseCallback;
import com.mardous.newsletter.gui.adapter.ArticleAdapter;
import com.mardous.newsletter.gui.adapter.SearchHistoryAdapter;
import com.mardous.newsletter.gui.fragments.base.AbsErrorViewFragment;
import com.mardous.newsletter.model.SearchQuery;
import com.mardous.newsletter.provider.SearchHistory;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class SearchFragment extends AbsErrorViewFragment implements SwipeRefreshLayout.OnRefreshListener, TextWatcher,
        SearchHistoryAdapter.OnHistoryItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    FastScrollRecyclerView recyclerView;
    @BindView(R.id.search_history_recycler_view)
    RecyclerView searchHistoryRecyclerView;

    private ArticleAdapter searchAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;

    private ApiClient apiClient;
    private SearchQuery searchQuery;
    private Call<News> call;

    private boolean searchOnTextChanged;

    @NonNull
    public static SearchFragment create() {
        return new SearchFragment();
    }

    public SearchFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar();
        setUpSearchEditText();
        setUpSwipeRefreshLayout();
        setUpRecyclerView();
        setUpSearchHistoryHeader();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PrefUtil.getInstance(getActivity()).registerOnSharedPreferenceChangedListener(this);

        apiClient = ApiClient.create();
        searchQuery = new SearchQuery(getActivity());
        searchOnTextChanged = PrefUtil.getInstance(getActivity()).searchOnTextChanged();
    }

    @Override
    public void onDestroyView() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.stopNestedScroll();
        }
        searchEditText.removeTextChangedListener(this);
        super.onDestroyView();
        PrefUtil.getInstance(getActivity()).unregisterOnSharedPreferenceChangedListener(this);
        unbinder.unbind();
    }

    @NonNull
    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    private void setUpToolbar() {
        getMainActivity().setSupportActionBar(toolbar);
        getActivity().setTitle(null);
    }

    private void setUpSearchEditText() {
        searchEditText.addTextChangedListener(this);
        searchEditText.setOnKeyListener(mOnKeyListener);
        TintHelper.setTintAuto(searchEditText, ThemeStore.accentColor(getActivity()), false);
    }

    private void setUpSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ThemeStore.accentColor(getActivity()));
    }

    private void setUpRecyclerView() {
        searchAdapter = new ArticleAdapter(getMainActivity(), new ArrayList<>(), Glide.with(this));

        Utils.applyFastScrollRecyclerViewColor(getActivity(), recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchAdapter);
    }

    private void setUpSearchHistoryHeader() {
        searchHistoryAdapter = new SearchHistoryAdapter(getMainActivity(),
                SearchHistory.getInstance(getActivity()).getRecentSearches(), this);
        searchHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        searchHistoryRecyclerView.setAdapter(searchHistoryAdapter);
        updateSearchHistory(null);
    }

    private void updateSearchHistory(String searched) {
        SearchHistory.getInstance(getActivity()).addSearchString(searched);
        if (searchHistoryAdapter != null) {
            searchHistoryAdapter.swapDataSet(SearchHistory.getInstance(getActivity()).getRecentSearches());
        }
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (call != null) {
            call.cancel();
        }

        swipeRefreshLayout.setRefreshing(true);

        call = searchQuery.getExcludeDomains().isEmpty() ?
                call = apiClient.getApiInterface().getNewsSearch(searchQuery.getKeyword(),
                    searchQuery.getLanguage(), searchQuery.getSortBy(),
                    searchQuery.getResultsLimit(), searchQuery.getApiKey()) :
                apiClient.getApiInterface().getNewsSearch(searchQuery.getKeyword(),
                    searchQuery.getLanguage(), searchQuery.getExcludeDomains(),
                    searchQuery.getSortBy(), searchQuery.getResultsLimit(),
                    searchQuery.getApiKey());

        call.enqueue(new RequestBaseCallback<News>() {
            @Override
            protected void onFinished(News result, Code code) {
                if (getActivity() == null) return;

                swipeRefreshLayout.setRefreshing(false);
                switch (code) {
                    case SUCCESS:
                        List<Article> articles = result.getArticle();
                        if (!articles.isEmpty()) {
                            swapAdapterDataSet(articles);
                            updateSearchHistory(searchQuery.getKeyword());
                        } else {
                            showErrorView(R.drawable.no_result, R.string.no_result_title, R.string.no_matches_found);
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

    @Override
    public void onHistoryItemClick(String data) {
        searchEditText.setText(data);
        submitSearch(false);
    }

    @Override
    public void onHistoryItemLongClick(String data) {
        SearchHistory.getInstance(getActivity()).removeItem(data);
        Toast.makeText(getActivity(), R.string.search_history_item_removed, Toast.LENGTH_SHORT).show();
        updateSearchHistory(null);
    }

    @NonNull
    @Override
    protected ViewGroup getErrorView() {
        return getView().findViewById(R.id.recycler_view_container);
    }

    @Override
    protected boolean shouldShowErrorView() {
        return searchAdapter == null || searchAdapter.getItemCount() == 0;
    }

    @Override
    protected void createOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            if (searchEditText != null) {
                searchEditText.getText().clear();
                searchEditText.clearFocus();
            }
            if (call != null) {
                call.cancel();
                call = null;
            }
            swipeRefreshLayout.setRefreshing(false);
            swapAdapterDataSet(new ArrayList<>());
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
        searchQuery.setKeyword(charSequence.toString());
        if (searchOnTextChanged && canSubmit()) {
            submitSearch(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, @NonNull String key) {
        if (key.equals(PrefUtil.SEARCH_ON_TEXT_CHANGED)) {
            searchOnTextChanged = preferences.getBoolean(key, false);
        }
    }

    @Override
    public void onRefresh() {
        if (canSubmit()) {
            startLoading();
        }
    }

    private View.OnKeyListener mOnKeyListener = (v, keyCode, event) -> {
        if (event.hasNoModifiers()) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    v.cancelLongPress();
                    if (canSubmit()) {
                        submitSearch(false);
                        return true;
                    }
                    Toast.makeText(getActivity(), R.string.type_more_than_two_letters, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return false;
    };

    private void swapAdapterDataSet(List<Article> articles) {
        if (searchAdapter != null) {
            searchAdapter.swapDataSet(articles);
        }
    }

    private boolean canSubmit() {
        return searchQuery.getKeyword().length() > 2;
    }

    private void submitSearch(boolean whileTyping) {
        if (!whileTyping) hideSoftKeyboard();
        if (!swipeRefreshLayout.isRefreshing() || whileTyping) {
            startLoading();
        }
    }

    private void hideSoftKeyboard() {
        Utils.hideSoftInputMethod(getActivity());
        if (searchEditText != null) {
            searchEditText.clearFocus();
        }
    }
}
