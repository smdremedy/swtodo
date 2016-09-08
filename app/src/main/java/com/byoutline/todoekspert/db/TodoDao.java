package com.byoutline.todoekspert.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.byoutline.todoekspert.api.TodoFromApi;

import javax.inject.Inject;

public class TodoDao {

    public static final String C_ID = "_id";
    public static final String C_CONTENT = "content";
    public static final String C_DONE = "done";
    public static final String C_USER_ID = "user_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_UPDATED_AT = "updated_at";

    /**
     * Nazwa tabeli, w której przechowywane będa obiekty
     */
    public static final String TABLE_NAME = "todos";
    private final DbHelper dbHelper;


    @Inject
    public TodoDao(DbHelper dbHelper) {

        this.dbHelper = dbHelper;

    }

    public void create(TodoFromApi todoFromApi) {


        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(C_CONTENT, todoFromApi.content);
        contentValues.put(C_DONE, todoFromApi.done);
        contentValues.put(C_USER_ID, todoFromApi.userId);
        contentValues.put(C_ID, todoFromApi.objectId);


        database.insertWithOnConflict(TABLE_NAME, null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE);

    }


    public Cursor getTodos(String userId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        return database.query(TABLE_NAME, null, C_USER_ID + " = ?", new String[]{userId},
                null, null, null);
    }

}
