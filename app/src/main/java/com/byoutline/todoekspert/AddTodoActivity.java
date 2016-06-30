package com.byoutline.todoekspert;

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
    }

    @OnClick(R.id.addButton)
    public void onClick() {
        String task = taskEditText.getText().toString();
        boolean done = doneCheckBox.isChecked();

        if(task.isEmpty()) {
            taskEditText.setError("Nothing to do!");
        }
    }
}
