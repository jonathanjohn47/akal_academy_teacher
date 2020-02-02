package com.rainbow.teacheriiy.ui.Attendance;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rainbow.teacheriiy.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRecyclerViewAdapter extends RecyclerView.Adapter<AttendanceRecyclerViewAdapter.ViewHolder> {

    private Context mCtx;
    private ArrayList<AttendanceModel> mAttendance ;

    private String date;
    private String studentclass;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentclass() {
        return studentclass;
    }

    public void setStudentclass(String studentclass) {
        this.studentclass = studentclass;
    }

    public AttendanceRecyclerViewAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public ArrayList<AttendanceModel> getmAttendance() {
        return mAttendance;
    }

    public void setmAttendance(ArrayList<AttendanceModel> mAttendance) {
        this.mAttendance = mAttendance;
    }

    public Context getmCtx() {
        return mCtx;
    }
    public void setmCtx(Context mCtx) {
        this.mCtx = mCtx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        AttendanceModel attendance = mAttendance.get(position);
        holder.studentname.setText(attendance.getStudent_name());

        final List<String> possiblevalues = new ArrayList<>();
        possiblevalues.add("P");
        possiblevalues.add("L");
        possiblevalues.add("A");
        ArrayAdapter<String> attendancevaluesadapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_list_item_1, possiblevalues);
        attendancevaluesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.studentattendancevalue.setAdapter(attendancevaluesadapter);
        holder.studentattendancevalue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                holder.studentattendancevalue.setSelection(i);
                changeAttendanceValue(position, possiblevalues.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.studentattendancevalue.setSelection(setSelectionPosition(attendance.getStudent_attendanceValue()));
    }

    private void changeAttendanceValue(int position, String value){
        String studentname = mAttendance.get(position).getStudent_name();
        String studentid = mAttendance.get(position).getStudent_Id();
        String studentclass = mAttendance.get(position).getStudent_class();
        AttendanceModel attendanceModel = new AttendanceModel();
        attendanceModel.setStudent_name(studentname);
        attendanceModel.setStudent_attendanceValue(value);
        attendanceModel.setStudent_Id(studentid);
        attendanceModel.setStudent_class(studentclass);

        mAttendance.set(position, attendanceModel);
    }

    public void saveData(){
        for (int i = 0; i <mAttendance.size() ; i++) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Attendance").document(date).collection("List")
                    .document(mAttendance.get(i).getStudent_Id()).set(mAttendance.get(i))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error", e+"");
                        }
                    });

        }
        Toast.makeText(mCtx, "All Data Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mAttendance.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView studentname;
        public Spinner studentattendancevalue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentname = itemView.findViewById(R.id.student_name);
            studentattendancevalue = itemView.findViewById(R.id.spinner_student_attendance_value);
        }
    }

    private int setSelectionPosition(String attendancevalue){
        if (attendancevalue.equals("P")){
            return 0;
        }
        else if (attendancevalue.equals("L")){
            return 1;
        }
        else if (attendancevalue.equals("A")){
            return 2;
        }
        else if (attendancevalue == null){
            return 0;
        }
        else{
            return 0;
        }
    }
}