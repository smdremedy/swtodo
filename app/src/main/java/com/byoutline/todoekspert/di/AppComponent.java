package com.byoutline.todoekspert.di;


import com.byoutline.todoekspert.LoginActivity;
import com.byoutline.todoekspert.LoginPresenter;
import com.byoutline.todoekspert.TodoListActivity;
import com.byoutline.todoekspert.api.Api;

import dagger.Component;

@Component(
        modules = AppModule.class
)
public interface AppComponent {

    LoginPresenter getLoginPreseneter();

    void inject(LoginActivity activity);
    void inject(TodoListActivity activity);

    Api getApi();
}
