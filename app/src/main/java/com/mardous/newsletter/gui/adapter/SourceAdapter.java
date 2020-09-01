package com.mardous.newsletter.gui.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.model.Source;
import com.mardous.newsletter.gui.activities.SourceDetailActivity;
import com.mardous.newsletter.gui.adapter.base.BaseAdapter;
import com.mardous.newsletter.gui.adapter.base.BaseViewHolder;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.List;

/**
 * @author Christians Martínez Alvarado
 */
public class SourceAdapter extends BaseAdapter<SourceAdapter.ViewHolder, Source> implements FastScrollRecyclerView.SectionedAdapter {

    private List<String> exclusions;

    public SourceAdapter(@NonNull AppCompatActivity activity, List<Source> sources) {
        super(activity, sources, R.layout.item_source);
        exclusions = PrefUtil.getInstance(activity).getExcludedDomains();
    }

    @Override
    protected ViewHolder createViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Source source = dataSet.get(position);

        if (holder.title != null) {
            holder.title.setText(source.getName());
        }
        if (holder.text != null) {
            holder.text.setText(source.getDescription());
        }
        if (holder.category != null) {
            holder.category.setText(Utils.getCategoryTitleFromName(activity.getResources(), source.getCategory()));
        }

        if (isSourceExcluded(source)) {
            setExcludedDecoration(holder);
        }
    }

    private void setExcludedDecoration(@NonNull ViewHolder holder) {
        final float alpha = 0.5f;
        if (holder.title != null) {
            holder.title.setAlpha(alpha);
        }
        if (holder.text != null) {
            holder.text.setAlpha(alpha);
        }
        if (holder.category != null) {
            holder.category.setAlpha(alpha);
        }
        if (holder.extraText != null) {
            holder.extraText.setText(R.string.excluded);
        }
    }

    private boolean isSourceExcluded(@NonNull Source source) {
        return Utils.isSourceExcluded(exclusions, source);
    }

    public void setExclusions(List<String> exclusions) {
        this.exclusions = exclusions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        String name = dataSet.get(position).getName();
        return TextUtils.isEmpty(name) ? "" : String.valueOf(name.charAt(0));
    }

    public class ViewHolder extends BaseViewHolder<Source> implements View.OnClickListener {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            final Intent intent = new Intent(activity, SourceDetailActivity.class);
            intent.putExtra(SourceDetailActivity.EXTRA_SOURCE, getCurrent());
            if (!isSourceExcluded(getCurrent())) {
                activity.startActivity(intent);
            }
        }

        @NonNull
        @Override
        protected Source getCurrent() {
            return dataSet.get(getAdapterPosition());
        }
    }
}
