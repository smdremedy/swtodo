package com.byoutline.todoekspert.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.byoutline.todoekspert.api.Api;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context context) {

        mContext = context;
    }

    @Provides
    Context getContext() {
        return mContext;
    }

    @Provides
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    Retrofit provideRetrofit() {
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
                .addNetworkInterceptor(new StethoInterceptor())

                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://api.parse.com");
        builder.client(client);
        builder.addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit;

    }

    @Provides
    Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}
