package com.qakap.muvi.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.qakap.muvi.R;
import com.qakap.muvi.models.genre.Genre;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {


    private List<Genre> allGenres;
    private Context context;

    public GenreAdapter(Context context, List<Genre> allGenres) {
        this.context = context;
        this.allGenres = allGenres;
    }


    @NonNull
    @Override
    public GenreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.MyViewHolder holder, int position) {
        holder.genreTitle.setText(allGenres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return allGenres.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.genreTitle)
        TextView genreTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
