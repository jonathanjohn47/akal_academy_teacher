package com.rainbow.teacheriiy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.rainbow.teacheriiy.models.HomeWork;

import java.util.ArrayList;

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.ViewHolder> {
    private Context mCtx;
    private ArrayList<HomeWork> mAllHomeWorks;

    public HomeWorkAdapter(Context mCtx, ArrayList<HomeWork> mAllHomeWorks) {
        this.mCtx = mCtx;
        this.mAllHomeWorks = mAllHomeWorks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_homework_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtHomework.setText(mAllHomeWorks.get(position).getHomework());
        holder.txtSubject.setText(mAllHomeWorks.get(position).getSubject());
        holder.txtDate.setText(mAllHomeWorks.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mAllHomeWorks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtHomework, txtDate, txtSubject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHomework = itemView.findViewById(R.id.single_homework);
            txtDate = itemView.findViewById(R.id.single_date);
            txtSubject = itemView.findViewById(R.id.single_subject);
        }
    }
}