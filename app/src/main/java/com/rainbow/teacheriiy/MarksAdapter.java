package com.rainbow.teacheriiy;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rainbow.teacheriiy.models.Attendance;
import com.rainbow.teacheriiy.models.Marks;

import java.util.ArrayList;
import java.util.List;

public class MarksAdapter extends FirestoreRecyclerAdapter<Marks, MarksAdapter.ViewHolder> {

    private Context mContext;

    OnItemClick onItemClick;
    GetUid getUid;

    public MarksAdapter(Context mContext, FirestoreRecyclerOptions<Marks> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public void setGetUid(GetUid getUid) {
        this.getUid = getUid;
    }

    public interface OnItemClick {

        void getPosition(String userId, String mark);

    }
    public interface GetUid {

        void getPosition(String userId);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Marks model) {

       holder.name.setText(model.getStudent_name());
       holder.roll.setText(model.getHomework());

       holder.marks.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               String mark = holder.marks.getText().toString();
               onItemClick.getPosition("S"+model.getStudent_admno(), mark);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_marks_layout, viewGroup, false);

        return new MarksAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, roll;
        private EditText marks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.single_mark_name);
            roll = itemView.findViewById(R.id.single_mark_roll);
            marks = itemView.findViewById(R.id.single_marks);

        }

    }
}
