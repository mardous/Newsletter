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

package com.mardous.newsletter.gui.adapter.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.mardous.newsletter.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseAdapter<VH extends BaseViewHolder, M> extends RecyclerView.Adapter<VH> implements FastScrollRecyclerView.SectionedAdapter {

    protected static final int TYPE_OTHER = 0;
    protected static final int TYPE_LAST = 1;

    protected final AppCompatActivity activity;
    protected List<M> dataSet;
    @LayoutRes
    protected int itemLayoutRes;
    protected boolean applyBottomMargin;

    public BaseAdapter(@NonNull AppCompatActivity activity, @NonNull List<M> dataSet, int itemLayoutRes) {
        this(activity, dataSet, itemLayoutRes, true);
    }

    public BaseAdapter(@NonNull AppCompatActivity activity, @NonNull List<M> dataSet, int itemLayoutRes, boolean applyBottomMargin) {
        this.activity = activity;
        this.dataSet = dataSet;
        this.itemLayoutRes = itemLayoutRes;
        this.applyBottomMargin = applyBottomMargin;
        setHasStableIds(true);
    }

    protected abstract VH createViewHolder(View view, int viewType);

    protected View createView(@NonNull ViewGroup parent, int viewType) {
        return LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = createView(parent, viewType);
        if (applyBottomMargin) applyBottomMargin((ViewGroup.MarginLayoutParams) view.getLayoutParams(), viewType);
        return createViewHolder(view, viewType);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return dataSet.get(position).hashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return position == dataSet.size() - 1 ? TYPE_LAST : TYPE_OTHER;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return "";
    }

    @NonNull
    public List<M> getDataSet() {
        return dataSet;
    }

    public void swapDataSet(List<M> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    protected void setAlpha(@NonNull VH holder) {
        final float alpha = 0.5f;
        if (holder.image != null) {
            holder.image.setAlpha(alpha);
        }
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
            holder.extraText.setAlpha(alpha);
        }
    }

    protected void applyBottomMargin(ViewGroup.MarginLayoutParams layoutParams, int viewType) {
        int listBottomMargin = activity.getResources().getDimensionPixelSize(R.dimen.list_item_bottom_margin);
        if (viewType == TYPE_LAST) {
            layoutParams.bottomMargin = listBottomMargin;
        }
    }
}
