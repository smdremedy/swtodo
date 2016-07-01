package com.byoutline.todoekspert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.byoutline.todoekspert.api.Api;
import com.byoutline.todoekspert.api.TodoFromApi;
import com.byoutline.todoekspert.db.DbHelper;
import com.byoutline.todoekspert.db.TodoDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static timber.log.Timber.d;

public class TodoListActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 123;

    @BindView(R.id.todosListView)
    ListView todosListView;

    private LoginPresenter loginPresenter;
    //private NewTodoAdapter arrayAdapter;

    private SimpleCursorAdapter cursorAdapter;
    private TodoDao dao;

    private String[] from = new String[]{TodoDao.C_CONTENT, TodoDao.C_DONE};
    private int[] to = new int[]{R.id.itemCheckBox, R.id.itemCheckBox};
    private Api api;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }


    };

    private void refresh() {
        d("Refresh in activity");

        Cursor cursor = dao.getTodos(loginPresenter.getUserId());
        cursorAdapter.swapCursor(cursor);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(RefreshIntentService.ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loginPresenter = ((App) getApplication()).getLoginPresenter();

        if (loginPresenter.hasToLogin()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);
        //arrayAdapter = new NewTodoAdapter();
        dao = new TodoDao(new DbHelper(getApplicationContext()));
        api = ((App) getApplication()).getApi();
        Cursor cursor = dao.getTodos(loginPresenter.getUserId());
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_todo, cursor,
                from, to, 0);


        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if (cursor.getColumnIndex(TodoDao.C_DONE) == columnIndex) {
                    CheckBox checkBox = (CheckBox) view;

                    int value = cursor.getInt(columnIndex);

                    checkBox.setChecked(value > 0);
                    return true;
                }

                return false;
            }
        };

        cursorAdapter.setViewBinder(viewBinder);

        todosListView.setAdapter(cursorAdapter);


    }


    class NewTodoAdapter extends BaseAdapter {

        private List<TodoFromApi> todos = new ArrayList<>();

        @Override
        public int getCount() {
            return todos.size();
        }

        @Override
        public TodoFromApi getItem(int position) {
            return todos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            Log.d("TAG", "Pos:" + position + " cv:" + convertView);

            View view = convertView;
            ViewHolder todoViewHolder;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_todo, parent, false);

                todoViewHolder = new ViewHolder(view);
                view.setTag(todoViewHolder);
            } else {
                todoViewHolder = (ViewHolder) view.getTag();
            }

            TodoFromApi todoFromApi = getItem(position);

            if (todoViewHolder.itemCheckBox.isChecked() != todoFromApi.done) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    todoViewHolder.itemCheckBox.clearAnimation();
                }
                todoViewHolder.itemCheckBox.setChecked(todoFromApi.done);
            }
            todoViewHolder.itemCheckBox.setText(todoFromApi.content);


            todoViewHolder.itemImageView.setImageResource(todoFromApi.done ? R.drawable.ic_add
                    : R.drawable.ic_refresh);

            view.setBackgroundResource(position % 2 == 0 ? R.color.colorPrimary : R.color.colorPrimaryDark);


            return view;
        }

        public void addAll(List<TodoFromApi> results) {
            todos.addAll(results);
            notifyDataSetChanged();
        }

        class ViewHolder {
            @BindView(R.id.itemCheckBox)
            CheckBox itemCheckBox;
            @BindView(R.id.itemImageView)
            ImageView itemImageView;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    class TodoAdapter extends ArrayAdapter<TodoFromApi> {


        public TodoAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.itemCheckBox);
            TodoFromApi todoFromApi = getItem(position);
            checkBox.setChecked(todoFromApi.done);
            checkBox.setText(todoFromApi.content);

            ImageView imageView = (ImageView) view.findViewById(R.id.itemImageView);

            imageView.setImageResource(todoFromApi.done ? R.drawable.ic_add
                    : R.drawable.ic_refresh);

            view.setBackgroundResource(position % 2 == 0 ? R.color.colorPrimary : R.color.colorPrimaryDark);


            return view;
        }
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.todo_list, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddTodoActivity.class);

                intent.putExtra("id", 1234);

                startActivityForResult(intent, REQUEST_CODE_ADD);
                return true;
            case R.id.action_logout:


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginPresenter.logout();
                        goToLogin();
                    }
                });
                builder.setNegativeButton(android.R.string.no, null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return true;
            case R.id.action_refresh:

                startService(new Intent(this, RefreshIntentService.class));


//                Call<TodosResponse> call = api.getTodos(loginPresenter.getToken());
//                call.enqueue(new Callback<TodosResponse>() {
//                    @Override
//                    public void onResponse(Call<TodosResponse> call, Response<TodosResponse> response) {
//                        if (response.isSuccessful()) {
//                            TodosResponse todosResponse = response.body();
//
//                            //arrayAdapter.addAll(todosResponse.results);
//
//
//
//                            for (TodoFromApi todo : todosResponse.results) {
//                                Log.d("TAG", todo.toString());
//
//                                dao.create(todo);
//
//                            }
//
//                            Cursor cursor = dao.getTodos(loginPresenter.getUserId());
//                            cursorAdapter.swapCursor(cursor);
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<TodosResponse> call, Throwable t) {
//
//                    }
//                });


                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD
                && resultCode == AddTodoActivity.RESULT_ADD) {

            Todo todo = (Todo) data.getParcelableExtra("todo");

            Toast.makeText(TodoListActivity.this,
                    "Result:" + resultCode + todo,
                    Toast.LENGTH_SHORT).show();

            TodoFromApi todoFromApi = new TodoFromApi();
            todoFromApi.done = todo.done;
            todoFromApi.content = todo.task;

            Call<ResponseBody> responseBodyCall = api.postTodo(todoFromApi,
                    loginPresenter.getToken());

            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        }
    }
}
