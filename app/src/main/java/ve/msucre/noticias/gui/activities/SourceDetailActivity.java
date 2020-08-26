package ve.msucre.noticias.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.kabouzeid.appthemehelper.util.ToolbarContentTintHelper;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import ve.msucre.noticias.R;
import ve.msucre.noticias.api.ApiClient;
import ve.msucre.noticias.api.model.Article;
import ve.msucre.noticias.api.model.News;
import ve.msucre.noticias.api.model.Source;
import ve.msucre.noticias.api.request.RequestBaseCallback;
import ve.msucre.noticias.gui.ErrorViewPresenter;
import ve.msucre.noticias.gui.activities.base.AbsThemeActivity;
import ve.msucre.noticias.gui.adapter.ArticleAdapter;
import ve.msucre.noticias.gui.dialogs.AddDomainDialog;
import ve.msucre.noticias.model.Query;
import ve.msucre.noticias.util.Intents;
import ve.msucre.noticias.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado
 */
public class SourceDetailActivity extends AbsThemeActivity implements SwipeRefreshLayout.OnRefreshListener, ErrorViewPresenter.RetryCallback {
    public static final String EXTRA_SOURCE = "extra_source";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.country_icon)
    ImageView countryIcon;
    @BindView(R.id.country_name)
    TextView countryName;
    @BindView(R.id.category_icon)
    ImageView categoryIcon;
    @BindView(R.id.category_title)
    TextView categoryTitle;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    FastScrollRecyclerView recyclerView;

    private ApiClient apiClient;
    private Source source;
    private Query query;

    private ArticleAdapter adapter;

    private ErrorViewPresenter errorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);
        ButterKnife.bind(this);

        source = getIntent().getParcelableExtra(EXTRA_SOURCE);
        if (source == null) {
            finish();
        }

        apiClient = ApiClient.create();
        query = new Query(this, null);

        errorView = new ErrorViewPresenter(this, findViewById(R.id.recycler_view_container), this);

        setUpToolbar();
        setUpSwipeRefreshLayout();
        setUpRecyclerView();
        loadSourceDetail();
        loadNewsFromSource();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    private void setUpSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ThemeStore.accentColor(this));
    }

    private void setUpRecyclerView() {
        adapter = new ArticleAdapter(this, new ArrayList<>(), Glide.with(this));

        Utils.applyFastScrollRecyclerViewColor(this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadSourceDetail() {
        title.setText(source.getName());
        url.setText(source.getUrl());
        description.setText(source.getDescription());
        countryIcon.setImageResource(R.drawable.ic_language_white_24dp);
        countryName.setText(Utils.getISODisplayName(getResources(), source.getCountry()));
        categoryIcon.setImageResource(Utils.getCategoryIconFromName(source.getCategory()));
        categoryTitle.setText(Utils.getCategoryTitleFromName(getResources(), source.getCategory()));
    }

    private void loadNewsFromSource() {
        errorView.hide();
        swipeRefreshLayout.setRefreshing(true);

        apiClient.getApiInterface()
                .getTopHeadlinesFromSource(source.getId(), query.getResultsLimit(), query.getApiKey())
                .enqueue(new RequestBaseCallback<News>() {
            @Override
            protected void onFinished(News result, Code code) {
                swipeRefreshLayout.setRefreshing(false);
                switch (code) {
                    case SUCCESS:
                        List<Article> articles = result.getArticle();
                        if (adapter != null) {
                            if (!articles.isEmpty()) {
                                adapter.swapDataSet(articles);
                            } else {
                                notifyError(R.drawable.no_result, R.string.no_result_title, R.string.the_source_did_not_return_any_data);
                            }
                        }
                        break;
                    case ERROR:
                        if (errorCode != null) {
                            notifyError(R.drawable.no_result, R.string.no_result_title, errorCode.messageRes);
                        } else {
                            notifyError(R.drawable.no_result, R.string.no_result_title, R.string.please_try_again);
                        }
                        break;
                    case NETWORK_FAILURE:
                        notifyError(R.drawable.oops, R.string.network_error_title, R.string.network_error_message);
                        break;
                }
            }
        });
    }

    private void notifyError(@DrawableRes int imageRes, @StringRes int titleRes, @StringRes int messageRes) {
        if (!errorView.image(imageRes)
                .title(titleRes)
                .message(messageRes)
                .preventShow(adapter != null && adapter.getItemCount() > 0)
                .show()) {
            Snackbar.make(swipeRefreshLayout, messageRes, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry, view -> loadNewsFromSource())
                    .setActionTextColor(ThemeStore.accentColor(this))
                    .show();
        }
    }

    @Override
    public void onRefresh() {
        loadNewsFromSource();
    }

    @Override
    public void onRetry() {
        loadNewsFromSource();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_source, menu);
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(this, toolbar, menu, getToolbarBackgroundColor(toolbar));
        return super.onCreateOptionsMenu(menu);
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
        if (itemId == R.id.action_open_web_page) {
            startActivity(Intent.createChooser(Intents.createBrowserIntent(source), item.getTitle()));
            return true;
        }
        if (itemId == R.id.action_add_exclusion) {
            AddDomainDialog.create(source.getUrl(), AddDomainDialog.MODE_ADD).show(getSupportFragmentManager(), "ADD_DOMAIN");
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.stopNestedScroll();
        }
        errorView.destroy();
        super.onDestroy();
    }
}
