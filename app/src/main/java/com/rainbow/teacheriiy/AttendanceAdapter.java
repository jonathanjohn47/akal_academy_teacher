package com.rainbow.teacheriiy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rainbow.teacheriiy.models.Attendance;
import com.rainbow.teacheriiy.models.HomeWork;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends FirestoreRecyclerAdapter<Attendance, AttendanceAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Attendance> attendances;

    OnItemClick onItemClick;
    GetUid getUid;

    public AttendanceAdapter(Context mContext, FirestoreRecyclerOptions<Attendance> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();

        attendances = new ArrayList<>();

    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public void setGetUid(GetUid getUid) {
        this.getUid = getUid;
    }

    public interface OnItemClick {

        void getPosition(long pos ,String userId);

    }
    public interface GetUid {

        void getPosition(String userId);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Attendance attendance) {

       holder.name.setText(attendance.getStudent_name());
       holder.roll.setText(attendance.getStudent_admno()+"");

        /*List<String> cate = new ArrayList<>();
        cate.add("P");
        cate.add("A");
        cate.add("L");
        ArrayAdapter<String> madapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, cate);

        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.att.setAdapter(madapter);

        holder.att.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemClick.getPosition(position, "S"+attendance.getStudent_admno());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_att_layout, viewGroup, false);

        return new AttendanceAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, roll, att;
        //private Spinner att;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.single_att_name);
            roll = itemView.findViewById(R.id.single_att_roll);
            att = itemView.findViewById(R.id.att_spinner);

        }

    }
}
