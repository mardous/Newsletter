package ve.msucre.noticias.gui.adapter.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ve.msucre.noticias.R;

/**
 * @author Christians Martínez Alvarado
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
