package com.byoutline.todoekspert;

import android.app.Application;
import android.preference.PreferenceManager;

import com.byoutline.todoekspert.api.Api;
import com.byoutline.todoekspert.di.AppComponent;
import com.byoutline.todoekspert.di.AppModule;
import com.byoutline.todoekspert.di.DaggerAppComponent;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.leakcanary.LeakCanary;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class App extends Application {


    public static AppComponent sComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        sComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
