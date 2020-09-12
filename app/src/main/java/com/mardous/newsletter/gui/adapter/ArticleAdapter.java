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

package com.mardous.newsletter.gui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mardous.newsletter.R;
import com.mardous.newsletter.api.model.Article;
import com.mardous.newsletter.gui.activities.ArticleDetailActivity;
import com.mardous.newsletter.gui.adapter.base.BaseAdapter;
import com.mardous.newsletter.gui.adapter.base.BaseViewHolder;
import com.mardous.newsletter.util.Intents;
import com.mardous.newsletter.util.TextUtil;
import com.mardous.newsletter.util.Utils;
import com.mardous.newsletter.util.pref.PrefUtil;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class ArticleAdapter extends BaseAdapter<ArticleAdapter.ViewHolder, Article> implements FastScrollRecyclerView.SectionedAdapter {

    private RequestManager requestManager;

    public ArticleAdapter(@NonNull AppCompatActivity activity, List<Article> articles, RequestManager requestManager) {
        super(activity, articles, R.layout.item);
        this.requestManager = requestManager;
    }

    @Override
    protected ViewHolder createViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article model = dataSet.get(position);

        if (holder.title != null) {
            holder.title.setText(model.getTitle());
        }

        if (holder.text != null) {
            String desc = model.getDescription();
            if (!TextUtils.isEmpty(desc)) {
                holder.text.setText(desc);
            } else {
                holder.text.setVisibility(View.GONE);
            }
        }

        if (holder.headline != null) {
            if (Utils.isValidAuthorName(model)) {
                holder.headline.setText(TextUtil.getSourceAndAuthor(activity, model));
            } else {
                holder.headline.setText(TextUtil.getTitleFormattedText(model.getSource().getName()));
            }
        }

        if (holder.extraText != null) {
            holder.extraText.setText(TextUtil.getDateAndTime(activity, model));
        }

        if (holder.image != null) {
            requestManager.load(model.getUrlToImage())
                    .placeholder(Utils.getRandomDrawableColor())
                    .error(Utils.getRandomDrawableColor())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(holder.image);
        }
    }

    public class ViewHolder extends BaseViewHolder<Article> implements View.OnClickListener {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (PrefUtil.getInstance(activity).openNewsInApp()) {
                activity.startActivity(new Intent(activity, ArticleDetailActivity.class)
                        .putExtra(ArticleDetailActivity.EXTRA_ARTICLE, getCurrent()));
            } else {
                try {
                    activity.startActivity(Intents.createBrowserIntent(getCurrent()));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(activity, R.string.browser_app_is_not_installed, Toast.LENGTH_SHORT).show();
                }
            }
        }

        @NonNull
        @Override
        protected Article getCurrent() {
            return dataSet.get(getAdapterPosition());
        }
    }
}
