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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.ApiClient;
import com.mardous.newsletter.api.model.Source;
import com.mardous.newsletter.api.model.Sources;
import com.mardous.newsletter.api.request.RequestBaseCallback;
import com.mardous.newsletter.gui.adapter.SourceAdapter;
import com.mardous.newsletter.gui.dialogs.SourcesNoticeDialog;
import com.mardous.newsletter.gui.fragments.base.AbsErrorViewFragment;
import com.mardous.newsletter.model.Query;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class SourcesFragment extends AbsErrorViewFragment implements SwipeRefreshLayout.OnRefreshListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    FastScrollRecyclerView recyclerView;

    private ApiClient apiClient;
    private Query query;

    private SourceAdapter adapter;

    public static SourcesFragment create() {
        return new SourcesFragment();
    }

    public SourcesFragment() {}

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
        query = new Query(getActivity(), null);

        checkShowNotice();

        startLoading();
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
        getActivity().setTitle(R.string.sources_label);
    }

    private void setUpSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ThemeStore.accentColor(getActivity()));
    }

    private void setUpRecyclerView() {
        adapter = new SourceAdapter(getMainActivity(), new ArrayList<>());

        Utils.applyFastScrollRecyclerViewColor(getActivity(), recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void checkShowNotice() {
        if (!SourcesNoticeDialog.wasShown(getActivity())) {
            SourcesNoticeDialog.newInstance().show(getFragmentManager(), "SOURCES_NOTICE");
        }
    }

    @Override
    protected void startLoading() {
        super.startLoading();
        swipeRefreshLayout.setRefreshing(true);

        apiClient.getApiInterface().getSources(query.getLanguage(), query.getApiKey()).enqueue(new RequestBaseCallback<Sources>() {
            @Override
            protected void onFinished(Sources result, Code code) {
                if (getActivity() == null) return;

                swipeRefreshLayout.setRefreshing(false);
                switch (code) {
                    case SUCCESS:
                        List<Source> sources = result.getSources();
                        if (sources != null && adapter != null) {
                            if (!sources.isEmpty()) {
                                adapter.swapDataSet(sources);
                            } else {
                                showErrorView(R.drawable.no_result, R.string.no_result_title, R.string.no_result_found_for_the_current_region_message);
                            }
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
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (key.equals(PrefUtil.EXCLUDED_DOMAINS)) {
            adapter.setExclusions(PrefUtil.getInstance(getActivity()).getExcludedDomains());
        }
    }

    @Override
    protected boolean shouldShowErrorView() {
        return adapter == null || adapter.getItemCount() == 0;
    }

    @NonNull
    @Override
    protected ViewGroup getErrorView() {
        return getView().findViewById(R.id.recycler_view_container);
    }

    @Override
    public void onRefresh() {
        startLoading();
    }
}