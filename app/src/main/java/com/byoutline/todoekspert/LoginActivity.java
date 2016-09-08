package com.byoutline.todoekspert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.byoutline.todoekspert.api.Api;
import com.byoutline.todoekspert.api.ErrorResponse;

import javax.inject.Inject;

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

    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        App.sComponent.inject(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.setLoginActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginPresenter.setLoginActivity(null);
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

            loginPresenter.login(username, password);


        }
    }

    public void showProgress(boolean show) {
        loginButton.setEnabled(!show);
    }

    public void loginOk() {
        startActivity(new Intent(LoginActivity.this, TodoListActivity.class));
        finish();
    }

    public void showError(ErrorResponse result) {
        Toast.makeText(LoginActivity.this, result.error, Toast.LENGTH_SHORT).show();
    }
}
