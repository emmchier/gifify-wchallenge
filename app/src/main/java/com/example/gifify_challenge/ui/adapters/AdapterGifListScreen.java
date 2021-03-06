package com.example.gifify_challenge.ui.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gifify_challenge.R;
import com.example.gifify_challenge.core.entities.GifEntity;
import com.example.gifify_challenge.databinding.CellGifBinding;
import java.util.ArrayList;
import java.util.List;

/*
 * Adapter for gif list
 */
public class AdapterGifListScreen extends RecyclerView.Adapter {

    private List<GifEntity> gifList;
    private GifListener gifListener;

    public AdapterGifListScreen(GifListener gifListener) {
        this.gifList = new ArrayList<>();
        this.gifListener = gifListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        CellGifBinding gifCell = CellGifBinding.bind(inflater.inflate(R.layout.cell_gif, parent, false));
        GifListViewholder  viewholder = new GifListViewholder(gifCell);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GifEntity gif = gifList.get(position);
        GifListViewholder gifListViewholder = (GifListViewholder) holder;
        gifListViewholder.setCellData(gif);
    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }

    public void loadGifs(List<GifEntity> gifs) {
        if (gifs != null) {
            this.gifList.addAll(gifs);
            notifyDataSetChanged();
        }
    }

    public class GifListViewholder extends RecyclerView.ViewHolder {

        private CellGifBinding binding;

        public GifListViewholder(CellGifBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    gifListener.addToFavourite(gifList.get(getAdapterPosition()));
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gifListener.shareGif(gifList.get(getAdapterPosition()));
                }
            });
        }

        @SuppressLint("CheckResult")
        public void setCellData(GifEntity gif) {
            Glide.with(itemView)
                    .load(gif.getImages().getDownsized().getUrl())
                    .placeholder(R.drawable.ic_gif)
                    .error(R.drawable.ic_empty)
                    .into(binding.imageViewGif);
        }
    }

    public interface GifListener {
        void addToFavourite(GifEntity gif);
        void shareGif(GifEntity gif);
    }
}
