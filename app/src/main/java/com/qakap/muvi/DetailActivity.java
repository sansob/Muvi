package com.qakap.muvi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.qakap.muvi.config.Config;
import com.qakap.muvi.config.RetroConfig;
import com.qakap.muvi.helper.ApiService;
import com.qakap.muvi.models.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ApiService apiService;
    String extraID;

    @BindView(R.id.movieTitle)
    TextView txt_movieTitle;
    @BindView(R.id.movieShort)
    TextView txt_movieShort;
    @BindView(R.id.movieImage)
    ImageView img_movieImage;

    @BindView(R.id.watchTrailer)
    Button btn_watch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        apiService = RetroConfig.getClient().create(ApiService.class);
        LoadDetailMovie();

        btn_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrailerActivity.class);
                intent.putExtra("movieId", getMovieId());
                startActivity(intent);
            }
        });

    }



    private Call<Result> callMovie() {

        return apiService.getMovieDetail(
                getMovieId()
        );
    }

    private String getMovieId(){
        final Intent intent = getIntent();
        String extraID = intent.getStringExtra("movieId");
        return extraID;
    }

    private void LoadDetailMovie() {
        SpotsDialog spotsDialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).build();
        spotsDialog.setMessage("loading");
        spotsDialog.show();
        callMovie().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                spotsDialog.dismiss();
                final Result result = response.body();
                txt_movieShort.setText(result.getOverview());
                txt_movieTitle.setText(result.getTitle());
                Glide.with(DetailActivity.this)
                        .load(Config.BASE_IMAGE + result.getPosterPath())
                        .into(img_movieImage);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                spotsDialog.dismiss();
            }
        });
    }

    public void goToReview(View view){
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        intent.putExtra("movieId", getMovieId());
        startActivity(intent);
    }

    public void goBack(View view){
        super.onBackPressed();
    }


}
