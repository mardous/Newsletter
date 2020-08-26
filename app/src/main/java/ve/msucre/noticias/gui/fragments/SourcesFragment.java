package ve.msucre.noticias.gui.fragments;

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
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import ve.msucre.noticias.R;
import ve.msucre.noticias.api.ApiClient;
import ve.msucre.noticias.api.model.Source;
import ve.msucre.noticias.api.model.Sources;
import ve.msucre.noticias.gui.adapter.SourceAdapter;
import ve.msucre.noticias.gui.dialogs.SourcesNoticeDialog;
import ve.msucre.noticias.gui.fragments.base.AbsErrorViewFragment;
import ve.msucre.noticias.api.request.RequestBaseCallback;
import ve.msucre.noticias.model.Query;
import ve.msucre.noticias.util.Utils;
import ve.msucre.noticias.util.pref.PrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado
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