package com.byoutline.todoekspert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTodoActivity extends AppCompatActivity {

    @BindView(R.id.taskEditText)
    EditText taskEditText;
    @BindView(R.id.doneCheckBox)
    CheckBox doneCheckBox;
    @BindView(R.id.addButton)
    Button addButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra("id", 0);
    }

    public static final int RESULT_ADD = RESULT_FIRST_USER;
    public static final int RESULT_EDIT = RESULT_FIRST_USER + 1;

    @OnClick(R.id.addButton)
    public void onClick() {
        String task = taskEditText.getText().toString();
        boolean done = doneCheckBox.isChecked();

        if (task.isEmpty()) {
            taskEditText.setError("Nothing to do!");
        } else {

            Todo todo = new Todo();
            todo.task = task;
            todo.done = done;

            Intent data = new Intent();
            data.putExtra("todo", todo);
            setResult(RESULT_ADD, data);
            finish();
        }
    }
}
