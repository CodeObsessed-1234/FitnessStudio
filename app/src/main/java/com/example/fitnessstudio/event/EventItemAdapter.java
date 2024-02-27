package com.example.fitnessstudio.event;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessstudio.R;

import java.util.List;

public class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.ViewHolder> {
    private final List<Event> mData;
    private final LayoutInflater mInflater;
    private final EventDatabaseHandler eventDatabaseHandler;

    EventItemAdapter(Context context, List<Event> data, EventDatabaseHandler dbHelper) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.eventDatabaseHandler = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event todo = mData.get(position);
        holder.todoText.setText(todo.getTask());
        holder.todoText.setPaintFlags(todo.isCompleted() ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView todoText;
        ImageButton deleteButton;
        ViewHolder(View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.event_text);
			deleteButton = itemView.findViewById(R.id.delete_button);
			deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    Event todo = mData.get(position);
                    eventDatabaseHandler.deleteTodoItem(todo.getId());
                    mData.remove(position);
                    notifyItemRemoved(position);
                }
            });
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

        }
    }
	
	public void addItem(Event todo) {
		mData.add(todo);
	}
}
