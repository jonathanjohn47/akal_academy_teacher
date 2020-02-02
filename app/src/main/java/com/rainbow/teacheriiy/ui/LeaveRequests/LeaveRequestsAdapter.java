package com.rainbow.teacheriiy.ui.LeaveRequests;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainbow.teacheriiy.R;

import java.util.ArrayList;

public class LeaveRequestsAdapter extends RecyclerView.Adapter<LeaveRequestsAdapter.ViewHolder> {

    private Context mCtx;
    private ArrayList<LeaveModel> mAllLeaves;

    public LeaveRequestsAdapter(Context mCtx, ArrayList<LeaveModel> mAllLeaves) {
        this.mCtx = mCtx;
        this.mAllLeaves = mAllLeaves;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_leave_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.studentname.setText(mAllLeaves.get(position).getName());
        holder.leavedate.setText(mAllLeaves.get(position).getDate());
        holder.leavereason.setText(mAllLeaves.get(position).getReason());
    }

    @Override
    public int getItemCount() {
        return mAllLeaves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView studentname, leavedate, leavereason;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentname = itemView.findViewById(R.id.leave_student_name);
            leavedate = itemView.findViewById(R.id.leave_date);
            leavereason = itemView.findViewById(R.id.leave_reason);
        }
    }
}
