package com.qakap.muvi.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.qakap.muvi.config.Config.BASE_URL;

public class RetroConfig {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }


}
