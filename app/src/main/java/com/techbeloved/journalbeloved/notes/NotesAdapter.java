package com.techbeloved.journalbeloved.notes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techbeloved.journalbeloved.R;
import com.techbeloved.journalbeloved.data.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private ListItemClickListener mOnClickListener;

    private List<Note> mNotes;

    private Context mContext;

    public interface ListItemClickListener {
        void onListItemClick(String noteId);
    }

    public NotesAdapter(Context context, ListItemClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.note_item,
                viewGroup, false);
        NotesViewHolder viewHolder = new NotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        // Determine the values of the note data
        Note note = mNotes.get(position);
        String title = note.getTitle();
        String detail = note.getDetail();

        // set values
        holder.tvNoteTitle.setText(title);
        holder.tvNoteSummary.setText(detail);
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    /**
     * When data changes, this method updates the list of notes
     * and notifies the adapter to use the new values on it
     */
    public void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNoteTitle;
        TextView tvNoteSummary;

        NotesViewHolder(View itemView) {
            super(itemView);

            tvNoteTitle = itemView.findViewById(R.id.text_note_title);
            tvNoteSummary = itemView.findViewById(R.id.text_note_detail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String noteId = mNotes.get(clickedPosition).getId();
            mOnClickListener.onListItemClick(noteId);
        }
    }
}
