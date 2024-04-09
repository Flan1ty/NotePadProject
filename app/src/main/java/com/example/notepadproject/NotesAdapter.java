package com.example.notepadproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notepadproject.Note;
import com.example.notepadproject.R;

import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int adapterPosition;
    private List<Note> mData;


    public NotesAdapter(Context context, List<Note> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    public void addItem(Note note) {
        this.mData.add(note);
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNumber;
        TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            adapterPosition = getAdapterPosition();
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    public interface ItemClickListener {

        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        mClickListener.onItemClick(view, adapterPosition);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = String.valueOf(position + 1);
        String title = mData.get(position).getTitle();
        holder.tvNumber.setText(id);
        holder.tvTitle.setText(title);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
