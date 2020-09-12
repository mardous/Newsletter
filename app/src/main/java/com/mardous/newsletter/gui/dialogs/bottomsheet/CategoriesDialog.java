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

package com.mardous.newsletter.gui.dialogs.bottomsheet;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.mardous.newsletter.R;
import com.mardous.newsletter.gui.adapter.base.BaseViewHolder;
import com.mardous.newsletter.model.Category;
import com.mardous.newsletter.util.pref.PrefUtil;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class CategoriesDialog extends BottomSheetDialogFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private Listener mListener;

    public static CategoriesDialog newInstance() {
        return new CategoriesDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CategoryAdapter(getActivity(), this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public CategoriesDialog setListener(Listener mListener) {
        this.mListener = mListener;
        return this;
    }

    public interface Listener {
        void onCategoryClicked(Category category);
    }

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        private CategoriesDialog categoriesDialog;
        private Category lastCategory;
        private int accentColor;
        private int secondaryTextColor;

        CategoryAdapter(@NonNull Context context, @NonNull CategoriesDialog categoriesDialog) {
            this.categoriesDialog = categoriesDialog;
            this.lastCategory = PrefUtil.getInstance(context).getLastCategory();
            this.accentColor = ThemeStore.accentColor(context);
            this.secondaryTextColor = ThemeStore.textColorSecondary(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(categoriesDialog.getActivity()).inflate(R.layout.item_icon_with_title, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Category category = Category.values()[position];

            boolean currentItem = lastCategory == category;
            if (holder.image != null) {
                holder.image.setImageResource(category.iconRes);
                if (currentItem) {
                    holder.image.setColorFilter(new PorterDuffColorFilter(accentColor, PorterDuff.Mode.SRC_IN));
                } else {
                    holder.image.setColorFilter(new PorterDuffColorFilter(secondaryTextColor, PorterDuff.Mode.SRC_IN));
                }
            }
            if (holder.title != null) {
                holder.title.setText(category.titleRes);
                if (currentItem) {
                    holder.title.setTextColor(accentColor);
                } else {
                    holder.title.setTextColor(secondaryTextColor);
                }
            }
        }

        @Override
        public int getItemCount() {
            return Category.values().length;
        }

        @Override
        public long getItemId(int position) {
            return Category.values()[position].id;
        }

        private class ViewHolder extends BaseViewHolder<Category> {
            ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            @NonNull
            @Override
            protected Category getCurrent() {
                return Category.values()[getAdapterPosition()];
            }

            @Override
            public void onClick(View view) {
                CategoriesDialog.Listener listener = categoriesDialog.mListener;
                if (listener != null) {
                    listener.onCategoryClicked(getCurrent());
                    categoriesDialog.dismiss();
                }
            }
        }
    }
}
