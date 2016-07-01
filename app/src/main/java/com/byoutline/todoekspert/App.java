package com.byoutline.todoekspert;

import android.app.Application;
import android.preference.PreferenceManager;

import com.byoutline.todoekspert.api.Api;
import com.squareup.leakcanary.LeakCanary;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {


    private LoginPresenter loginPresenter;
    private Api api;
    private Retrofit retrofit;

    public LoginPresenter getLoginPresenter() {
        return loginPresenter;
    }

    public Api getApi() {
        return api;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request request = chain.request().newBuilder()
                                .addHeader("X-Parse-Application-Id", "EhZ7ps1nVRbYCz4d1IkLlOLUAMkuzaA6NGS89hDX")
                                .addHeader(
                                        "X-Parse-REST-API-Key", "0cc1iqhHHEg3j8do8b6DNkjC0nmnNNMKVJ11blov")
                                .addHeader("X-Parse-Revocable-Session", "1").build();


                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(interceptor)

                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://api.parse.com");
        builder.client(client);
        builder.addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        api = retrofit.create(Api.class);

        loginPresenter
                = new LoginPresenter(PreferenceManager.getDefaultSharedPreferences(this),
                api, retrofit);
    }
}
