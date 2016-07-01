package com.byoutline.todoekspert;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.byoutline.todoekspert.api.Api;
import com.byoutline.todoekspert.api.ErrorResponse;
import com.byoutline.todoekspert.api.LoginResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginPresenter {

    public static final String USER_ID = "userId";
    public static final String SESSION_TOKEN = "sessionToken";

    private LoginActivity loginActivity;
    private LoginTask loginTask;

    private final SharedPreferences sharedPreferences;
    private final Api api;
    private final Retrofit retrofit;


    public LoginPresenter(SharedPreferences sharedPreferences, Api api, Retrofit retrofit) {

        this.sharedPreferences = sharedPreferences;
        this.api = api;
        this.retrofit = retrofit;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void login(String username, String password) {
        if (loginTask == null) {
            loginTask = new LoginTask();
            loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    username, password);
        }
    }

    public boolean hasToLogin() {
        return sharedPreferences.getString(SESSION_TOKEN, "").isEmpty();
    }

    public void logout() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

    public String getToken() {
        return sharedPreferences.getString(SESSION_TOKEN,"");
    }


    class LoginTask extends AsyncTask<String, Integer, ErrorResponse> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (loginActivity != null) {
                loginActivity.showProgress(true);
            }

        }

        @Override
        protected void onPostExecute(ErrorResponse result) {
            super.onPostExecute(result);
            loginTask = null;
            if (loginActivity != null) {
                loginActivity.showProgress(false);
            }

            if (result == null) {
                if (loginActivity != null) {
                    loginActivity.loginOk();
                }

            } else {
                if (loginActivity != null) {
                    loginActivity.showError(result);
                }
            }

        }

        @Override
        protected ErrorResponse doInBackground(String... params) {





            Call<LoginResponse> call = api.getLogin(params[0], params[1]);

            try {

                Thread.sleep(5000);

                Response<LoginResponse> response = call.execute();

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(USER_ID, loginResponse.objectId);
                    editor.putString(SESSION_TOKEN, loginResponse.sessionToken);
                    editor.apply();


                } else {
                    ResponseBody errorBody = response.errorBody();

                    Converter<ResponseBody, ErrorResponse> converter
                            = retrofit.responseBodyConverter(ErrorResponse.class,
                            new Annotation[]{});

                    ErrorResponse errorResponse =
                            converter.convert(errorBody);

                    return errorResponse;

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }


    }
}
