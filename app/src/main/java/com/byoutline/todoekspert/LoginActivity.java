package com.byoutline.todoekspert;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.lang.annotation.Annotation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.usernameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }


    @OnClick(R.id.loginButton)
    public void onClick() {

        boolean hasError = false;

        String username = usernameEditText.getText().toString();
        if (username.isEmpty()) {
            usernameEditText.setError(getString(R.string.username_not_empty));
            hasError = true;
        }

        String password = passwordEditText.getText().toString();
        if (password.isEmpty()) {
            hasError = true;
            passwordEditText.setError(getString(R.string.password_not_empty));
        }

        if (!hasError) {
            //login


            LoginTask loginTask = new LoginTask();
            loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    username, password);


        }


    }

    class LoginTask extends AsyncTask<String, Integer, ErrorResponse> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(ErrorResponse result) {
            super.onPostExecute(result);
            loginButton.setEnabled(true);
            loginButton.setText("" + result);

            if (result == null) {
                startActivity(new Intent(LoginActivity.this, TodoListActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, result.error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected ErrorResponse doInBackground(String... params) {


            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(interceptor)
                    .build();

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl("https://api.parse.com");
            builder.client(client);
            builder.addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            Api api = retrofit.create(Api.class);

            Call<LoginResponse> call = api.getLogin(params[0], params[1]);

            try {
                Response<LoginResponse> response = call.execute();

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
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
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }


    }
}
