package com.qakap.muvi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qakap.muvi.adapter.GenreAdapter;
import com.qakap.muvi.adapter.ReviewAdapter;
import com.qakap.muvi.config.RetroConfig;
import com.qakap.muvi.helper.ApiService;
import com.qakap.muvi.models.Result;
import com.qakap.muvi.models.genre.Genre;
import com.qakap.muvi.models.genre.GenreResponse;
import com.qakap.muvi.models.review.Review;
import com.qakap.muvi.models.review.ReviewResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private ApiService apiService;
    private ArrayList<Review> getReview;
    @BindView(R.id.recyclerReviews)
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        apiService = RetroConfig.getClient().create(ApiService.class);

        LoadReview();
    }

    private void LoadReview() {
        SpotsDialog spotsDialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).build();
        spotsDialog.setMessage("loading");
        spotsDialog.show();

        callMovieReviews().enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                spotsDialog.dismiss();
                getReview = response.body().getResults();
                reviewAdapter = new ReviewAdapter(ReviewActivity.this, getReview);
                recyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                spotsDialog.dismiss();
            }
        });
    }

    private String getMovieId() {
        final Intent intent = getIntent();
        String extraID = intent.getStringExtra("movieId");
        return extraID;
    }


    private Call<ReviewResponse> callMovieReviews() {
        return apiService.getMovieReviews(
                getMovieId()
        );
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

}
