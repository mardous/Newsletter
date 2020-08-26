package ve.msucre.noticias.gui.adapter.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import ve.msucre.noticias.R;

import java.util.List;

/**
 * @author Christians Martínez Alvarado
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
