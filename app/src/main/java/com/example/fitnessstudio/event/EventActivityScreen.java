package com.example.fitnessstudio.event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessstudio.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EventActivityScreen extends Activity {
	EventDatabaseHandler eventDatabaseHandler;
	RecyclerView recyclerView;
	FloatingActionButton addButton;
	EditText addEditText;
	EventItemAdapter todoAdapter;

	@SuppressLint("SuspiciousIndentation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_screen);

		eventDatabaseHandler = new EventDatabaseHandler(this);
		recyclerView = findViewById(R.id.recycler);
		addButton = findViewById(R.id.add_button);
		addEditText = findViewById(R.id.add_edittext);
		List<Event> todos = eventDatabaseHandler.getAllTodoItems();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		todoAdapter = new EventItemAdapter(this, todos, eventDatabaseHandler);
		recyclerView.setAdapter(todoAdapter);

		addButton.setOnClickListener(view -> {
			String text = addEditText.getText().toString();

			if (!text.isEmpty()) {
				long itemId = eventDatabaseHandler.insertTodoItem(text, false);

				if (itemId != -1) {
					Event newTodo = new Event(text, false);
					newTodo.setId(itemId);
					todoAdapter.addItem(newTodo);

					int position = todoAdapter.getItemCount() - 1;
					todoAdapter.notifyItemInserted(position);

					Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_LONG).show();
					addEditText.setText("");
				} else {
					Toast.makeText(getApplicationContext(), "Failed to add item", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Please enter something....", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
