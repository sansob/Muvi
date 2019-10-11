package com.qakap.muvi.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qakap.muvi.R;
import com.qakap.muvi.adapter.HomeAdapter;
import com.qakap.muvi.config.Config;
import com.qakap.muvi.config.RetroConfig;
import com.qakap.muvi.helper.ApiService;
import com.qakap.muvi.helper.ScrollerHelper;
import com.qakap.muvi.models.Movie;
import com.qakap.muvi.models.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {

    GridLayoutManager linearLayoutManager;

    HomeAdapter homeAdapter;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private ApiService apiService;

    @BindView(R.id.recyclerMovie)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View views = inflater.inflate(R.layout.home, container, false);
        ButterKnife.bind(this, views);
        return views;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeAdapter = new HomeAdapter(getActivity());


        linearLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(homeAdapter);

        recyclerView.addOnScrollListener(new ScrollerHelper(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        apiService = RetroConfig.getClient().create(ApiService.class);

        loadFirstPage();

    }

    private void loadFirstPage() {

        SpotsDialog spotsDialog = (SpotsDialog) new SpotsDialog.Builder().setContext(getContext()).build();
        spotsDialog.setMessage("loading");
        spotsDialog.show();

        callMovie().enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                spotsDialog.dismiss();
                if (recyclerView != null) {
                    List<Result> results = fetchResults(response);
                    homeAdapter.addAll(results);

                    if (currentPage <= TOTAL_PAGES) homeAdapter.addLoadingFooter();
                    else isLastPage = true;
                } else {
                    spotsDialog.dismiss();
                    Log.e("eror", "error");
                }

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                spotsDialog.dismiss();
                t.printStackTrace();
            }
        });

    }

    private List<Result> fetchResults(Response<Movie> response) {
        Movie movie = response.body();
        assert movie != null;
        return movie.getResults();
    }

    private void loadNextPage() {

        callMovie().enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                homeAdapter.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                homeAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) homeAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private Call<Movie> callMovie() {
        return apiService.GetNowPlaying(
                Config.BASE_API,
                Config.BASE_LANG,
                currentPage
        );
    }
}
