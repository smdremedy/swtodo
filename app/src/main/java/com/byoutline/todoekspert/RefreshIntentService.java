package com.byoutline.todoekspert;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.byoutline.todoekspert.api.Api;
import com.byoutline.todoekspert.api.TodoFromApi;
import com.byoutline.todoekspert.api.TodosResponse;
import com.byoutline.todoekspert.db.DbHelper;
import com.byoutline.todoekspert.db.TodoDao;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RefreshIntentService extends IntentService {

    public static final String ACTION = "com.byoutline.todoekspert.REFRESH";

    public RefreshIntentService() {
        super(RefreshIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Api api = ((App) getApplication()).getApi();
        LoginPresenter loginPresenter = ((App) getApplication()).getLoginPresenter();

        TodoDao dao = new TodoDao(new DbHelper(getApplicationContext()));

        Call<TodosResponse> call = api.getTodos(loginPresenter.getToken());
        try {
            Response<TodosResponse> response = call.execute();

            if (response.isSuccessful()) {
                for (TodoFromApi todo : response.body().results) {
                    dao.create(todo);
                }

                NotificationCompat.Builder builder
                        = new NotificationCompat.Builder(getApplicationContext());


                builder.setContentTitle("New items fetched");
                builder.setContentText("Some items");
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setAutoCancel(true);


                Intent activityIntent = new Intent(getApplicationContext(), TodoListActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1,
                        activityIntent, 0);


                builder.setContentIntent(pendingIntent);

                NotificationManager notificationManager
                        = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1, builder.build());


                sendBroadcast(new Intent(ACTION));


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
