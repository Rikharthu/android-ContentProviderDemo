package com.example.rikharthu.contentproviderdemo.ui.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.rikharthu.contentproviderdemo.R;
import com.example.rikharthu.contentproviderdemo.data.NotesContentProvider;
import com.example.rikharthu.contentproviderdemo.data.models.Note;
import com.example.rikharthu.contentproviderdemo.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Cursor mCursor;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    public void setNotes(Cursor cursor) {
        Timber.d("setting notes data");
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            holder.bind(Utils.extractNote(mCursor));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.note_item_title)
        TextView mNoteTitle;
        @BindView(R.id.note_item_description)
        TextView mNoteDescription;
        @BindView(R.id.note_item_remind_at)
        TextView mNoteRemindAt;
        @BindView(R.id.note_item_done_checkbox)
        CheckBox mNoteFinished;

        Note mNote;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Note note) {
            mNote = note;
            String title = mNote.getTitle();
            if (TextUtils.isEmpty(title)) {
                mNoteTitle.setVisibility(View.GONE);
            } else {
                mNoteTitle.setVisibility(View.VISIBLE);
                mNoteTitle.setText(title);
            }
            mNoteDescription.setText(mNote.getDescription());
            mNoteRemindAt.setText(mNote.getRemindAt().toString());
            mNoteFinished.setOnCheckedChangeListener(null);
            mNoteFinished.setChecked(mNote.isFinished());
            mNoteFinished.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Timber.d("onCheckedChanged: " + isChecked);
            mNote.setFinished(isChecked);
            new Thread(this::postNoteUpdate).start();
        }

        private void postNoteUpdate() {
            itemView.getContext().getContentResolver()
                    .update(
                            NotesContentProvider.getContentUriWithId(mNote.getId()),
                            Note.toContentValues(mNote),
                            null, null);
        }
    }
}
