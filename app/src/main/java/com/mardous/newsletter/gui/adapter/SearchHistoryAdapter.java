package com.mardous.newsletter.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.mardous.newsletter.R;
import com.mardous.newsletter.gui.adapter.base.BaseAdapter;
import com.mardous.newsletter.gui.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author Christians Martínez Alvarado
 */
public class SearchHistoryAdapter extends BaseAdapter<SearchHistoryAdapter.ViewHolder, String> {

    @Nullable
    private OnHistoryItemClickListener listener;

    public SearchHistoryAdapter(@NonNull AppCompatActivity activity, @NonNull List<String> dataSet, @Nullable OnHistoryItemClickListener listener) {
        super(activity, dataSet, R.layout.item_search_history);
        this.listener = listener;
    }

    @Override
    protected ViewHolder createViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.title != null) {
            holder.title.setText(dataSet.get(position));
        }
    }

    @Override
    protected void applyBottomMargin(ViewGroup.MarginLayoutParams layoutParams, int viewType) {
        int listRightMargin = activity.getResources().getDimensionPixelSize(R.dimen.list_item_bottom_margin);
        if (viewType == TYPE_LAST) {
            layoutParams.rightMargin = listRightMargin;
        }
    }

    public interface OnHistoryItemClickListener {
        void onHistoryItemClick(String data);

        void onHistoryItemLongClick(String data);
    }

    public class ViewHolder extends BaseViewHolder<String> {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onHistoryItemClick(getCurrent());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (listener != null) {
                listener.onHistoryItemLongClick(getCurrent());
                return true;
            }
            return false;
        }

        @NonNull
        @Override
        protected String getCurrent() {
            return dataSet.get(getAdapterPosition());
        }
    }
}
