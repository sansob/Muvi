package com.qakap.muvi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.qakap.muvi.DetailActivity;
import com.qakap.muvi.R;
import com.qakap.muvi.models.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.qakap.muvi.config.Config.BASE_IMAGE;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Result> movieList;
    private Context context;

    private boolean isLoading = false;

    public HomeAdapter(Context context) {
        this.context = context;
        movieList = new ArrayList<>();
    }

    public List<Result> getMovies() {
        return movieList;
    }

    public void setMovies(List<Result> movieList) {
        this.movieList = movieList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_movie, parent, false);
        viewHolder = new MuviHolder(v1);
        return viewHolder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Result result = movieList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final MuviHolder muviHolder = (MuviHolder) holder;

                Glide
                        .with(context)
                        .load(BASE_IMAGE + result.getPosterPath())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                muviHolder.movie_progress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                muviHolder.movie_progress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(muviHolder.poster_path);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("movieId", result.getId().toString());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });

                break;

            case LOADING:
                break;
        }

    }
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieList.size() - 1 && isLoading) ? LOADING : ITEM;
    }


    public void add(Result r) {
        movieList.add(r);
        notifyItemInserted(movieList.size() - 1);
    }

    public void addAll(List<Result> moveResults) {
        for (Result result : moveResults) {
            add(result);
        }
    }

    public void remove(Result r) {
        int position = movieList.indexOf(r);
        if (position > -1) {
            movieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoading = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoading = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoading = false;

        int position = movieList.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            movieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return movieList.get(position);
    }

    protected class MuviHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageMovie)
        ImageView poster_path;

        @BindView(R.id.movie_progress)
        ProgressBar movie_progress;

        public MuviHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    protected class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
