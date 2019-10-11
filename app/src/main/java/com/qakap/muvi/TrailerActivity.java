package com.qakap.muvi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qakap.muvi.adapter.ReviewAdapter;
import com.qakap.muvi.adapter.TrailerAdapter;
import com.qakap.muvi.config.RetroConfig;
import com.qakap.muvi.helper.ApiService;
import com.qakap.muvi.models.review.Review;
import com.qakap.muvi.models.review.ReviewResponse;
import com.qakap.muvi.models.trailer.Trailer;
import com.qakap.muvi.models.trailer.TrailerResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailerActivity extends AppCompatActivity {

    private ApiService apiService;


    private ArrayList<Trailer> getTrailer;
    @BindView(R.id.recyclerTrailer)
    RecyclerView recyclerTrailer;

    LinearLayoutManager linearLayoutManager;

    TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        ButterKnife.bind(this);

        apiService = RetroConfig.getClient().create(ApiService.class);
        LoadTrailer();
    }

    private void LoadTrailer() {
        SpotsDialog spotsDialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).build();
        spotsDialog.setMessage("loading");
        spotsDialog.show();

        callMovieTrailer().enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                spotsDialog.dismiss();
                getTrailer = response.body().getResults();
                trailerAdapter = new TrailerAdapter(TrailerActivity.this, getTrailer);
                recyclerTrailer.setLayoutManager(new LinearLayoutManager(TrailerActivity.this));
                recyclerTrailer.setItemAnimator(new DefaultItemAnimator());
                recyclerTrailer.setAdapter(trailerAdapter);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                spotsDialog.dismiss();
            }
        });
    }

    private String getMovieId() {
        final Intent intent = getIntent();
        String extraID = intent.getStringExtra("movieId");
        return extraID;
    }


    private Call<TrailerResponse> callMovieTrailer() {
        return apiService.getMovieTrailers(
                getMovieId()
        );
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

}
