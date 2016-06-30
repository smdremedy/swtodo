package com.byoutline.todoekspert;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    class LoginTask extends AsyncTask<String, Integer, Boolean> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            loginButton.setEnabled(true);
            loginButton.setText("" + result);

            if(result) {
                startActivity(new Intent(LoginActivity.this, TodoListActivity.class));
                finish();
            }

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                for (int i = 0; i < 100; i++) {

                    Thread.sleep(100);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result = "test".equals(params[0]) && "test".equals(params[1]);

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }


    }
}
