package com.example.fitnessstudio.event;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EventDatabaseHandler extends SQLiteOpenHelper {
	Context context;
	private static final String DATABASE_NAME = "EVENT.db";
	private static final int DATABASE_VERSION = 1;

	public EventDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, completed INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Todos;");
		onCreate(db);
	}

	public List<Event> getAllTodoItems() {
		List<Event> todoItems = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Todos", null);

		if (cursor.moveToFirst()) {
			do {
				@SuppressLint("Range")
				long id = cursor.getLong(cursor.getColumnIndex("_id"));
				@SuppressLint("Range")
				String task = cursor.getString(cursor.getColumnIndex("task"));
				@SuppressLint("Range")
				int completedInt = cursor.getInt(cursor.getColumnIndex("completed"));
				boolean completed = completedInt == 1;
				Event todoItem = new Event(task, completed);
				todoItem.setId(id);
				todoItem.setCompleted(completed);
				todoItems.add(todoItem);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return todoItems;
	}

	public long insertTodoItem(String task, boolean completed) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("task", task);
		values.put("completed", completed ? 1 : 0);
		long itemId = db.insert("Todos", null, values);
		db.close();
		return itemId;
	}

	public void deleteTodoItem(long itemId) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.delete("Todos", "_id" + " = ?", new String[]{String.valueOf(itemId)});
		} catch (SQLException e) {
			Log.e("DeleteData", "Error deleting data: " + e.getMessage());
		}
		db.close();
	}
}
