package com.rainbow.teacheriiy.ui.Attendance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rainbow.teacheriiy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private Spinner mClassSelect;
    private TextView mDateSelect;
    private RecyclerView mAttendanceRecyclerView;

    private String selectedDate;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.attendance_activity);
        init();

        getSupportActionBar().hide();

        mToolbar.setTitle("Daily Attendance");
        mToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTitle("Attendance");

        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        mDateSelect.setText(df.format(date));
        mDateSelect.setOnClickListener(new OnDateSelectClickListener());
        mDateSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                load();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onStop() {
        getSupportActionBar().show();
        super.onStop();
    }

    Button btnssave;
    AttendanceRecyclerViewAdapter adapter = new AttendanceRecyclerViewAdapter(AttendanceActivity.this);
    public void setadapter(ArrayList<AttendanceModel> arrayList){
        adapter.setmAttendance(arrayList);
        adapter.setDate(mDateSelect.getText().toString());
        adapter.setStudentclass(mClassSelect.getSelectedItem().toString());
        adapter.notifyDataSetChanged();
        mAttendanceRecyclerView.setAdapter(adapter);
        btnssave = findViewById(R.id.btnSave);
        btnssave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
                adapter.saveData();
            }
        });
    }

    ArrayList<StudentDetails> details = new ArrayList<>();
    ArrayList<String> attendancevalues = new ArrayList<>();
    private void load(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }
        };

        final Thread thread = new Thread(runnable);
        thread.start();
        db.collection("Parent").whereEqualTo("student_class", extractclass(mClassSelect.getSelectedItem()+""))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String student_name = snapshot.getString("student_name");
                        String student_Id = snapshot.getString("uid");
                        String student_class = extractclass(mClassSelect.getSelectedItem()+"")+"";
                        StudentDetails student = new StudentDetails(student_name, student_Id, student_class);
                        details.add(student);
                    }
                    db.collection("Attendance")
                            .document(mDateSelect.getText().toString())
                            .collection("List")
                            .whereEqualTo("student_class", extractclass(mClassSelect.getSelectedItem()+"")+"")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot snapshot: task.getResult()){
                                            //Log.e("AttendanceValues", snapshot.getString("student_name") +" "+snapshot.getString("student_attendanceValue"));
                                            attendancevalues.add(snapshot.getString("student_attendanceValue"));
                                        }

                                        ArrayList<AttendanceModel> modelList = new ArrayList<>();
                                        Log.e("ModellistLength", modelList.size()+"");
                                        for (int i = 0; i < details.size(); i++) {
                                            String studenname = details.get(i).getStudent_name();
                                            String studentclass = details.get(i).getStudent_class();
                                            String studentid = details.get(i).getStudent_Id();
                                            AttendanceModel model = new AttendanceModel();
                                            if (attendancevalues.size()!=0) {
                                                try {
                                                    String studentattendancevalue = attendancevalues.get(i);
                                                    model.setStudent_name(studenname);
                                                    model.setStudent_Id(studentid);
                                                    model.setStudent_class(studentclass);
                                                    model.setStudent_attendanceValue(studentattendancevalue);
                                                }catch (Exception e){}
                                            }
                                            else{
                                                model.setStudent_name(studenname);
                                                model.setStudent_Id(studentid);
                                                model.setStudent_class(studentclass);
                                                model.setStudent_attendanceValue("P");
                                            }
                                            modelList.add(model);
                                        }
                                        setadapter(modelList);
                                        Log.e("ModellistLength", modelList.size()+"");
                                        details.clear();
                                        attendancevalues.clear();

                                        dialog.dismiss();
                                        thread.interrupt();
                                    }
                                }
                            });
                }
            }
        });
    }

    private int extractclass (String selectedclass){
        int selectedclasslength = selectedclass.length();
        String actualclass = selectedclass.substring(selectedclasslength-1, selectedclasslength);
        return Integer.parseInt(actualclass);
    }

    private class OnDateSelectClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceActivity.this);
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String date_s = i + "-" + (i1+1) + "-" + i2;
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-M-d");
                    Date date;
                    try {
                        date = dt.parse(date_s);
                        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
                        mDateSelect.setText(dt1.format(date));
                        selectedDate = dt1.format(date);
                    } catch (ParseException e) {
                        Log.e("error", e+"");
                    }

                }
            });
            datePickerDialog.show();
        }
    }

    private void init() {
        mClassSelect = findViewById(R.id.select_class_spinner);
        mDateSelect = findViewById(R.id.edt_date_select);
        mAttendanceRecyclerView = findViewById(R.id.attendance_recyclerView);
        mAttendanceRecyclerView = findViewById(R.id.attendance_recyclerView);
        mAttendanceRecyclerView.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this));
        mToolbar = findViewById(R.id.attendance_activity_toolbar);
        fetchAllClasses();
    }

    private void fetchAllClasses() {
        db.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<String> cate = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        cate.add(snapshot.getId());
                    }
                    ArrayAdapter<String> classarrayadapter = new ArrayAdapter<String>(AttendanceActivity.this, android.R.layout.simple_list_item_1, cate);
                    classarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mClassSelect.setAdapter(classarrayadapter);
                    mClassSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mClassSelect.setSelection(i);

                            load();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
        });
    }
}
