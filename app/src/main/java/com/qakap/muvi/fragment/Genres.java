package com.qakap.muvi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qakap.muvi.R;
import com.qakap.muvi.adapter.GenreAdapter;
import com.qakap.muvi.config.Config;
import com.qakap.muvi.config.RetroConfig;
import com.qakap.muvi.helper.ApiService;
import com.qakap.muvi.models.Result;
import com.qakap.muvi.models.genre.Genre;
import com.qakap.muvi.models.genre.GenreResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Genres extends Fragment {

    private ApiService apiService;
    private ArrayList<Genre> getGenres;
    @BindView(R.id.recyclerGenres)
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    GenreAdapter genreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.genres, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        apiService = RetroConfig.getClient().create(ApiService.class);

        getGenres().enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                getGenres = response.body().getGenres();
                genreAdapter = new GenreAdapter(getActivity(), getGenres);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(genreAdapter);
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {

            }
        });
    }

    private Call<GenreResponse> getGenres() {
        return apiService.getGenres(
                "16849afe437df1320762750a5efc0fc0",
                Config.BASE_LANG
        );
    }
}
