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

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mardous.newsletter.R;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class BaseViewHolder<D> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    @Nullable
    @BindView(R.id.title)
    public TextView title;
    @Nullable
    @BindView(R.id.text)
    public TextView text;
    @Nullable
    @BindView(R.id.headline)
    public TextView headline;
    @Nullable
    @BindView(R.id.extra_text)
    public TextView extraText;
    @Nullable
    @BindView(R.id.category)
    public TextView category;
    @Nullable
    @BindView(R.id.image)
    public ImageView image;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @NonNull
    protected abstract D getCurrent();

    @Override
    public void onClick(View view) {}

    @Override
    public boolean onLongClick(View view) {
        // maybe implemented in future
        return false;
    }
}
