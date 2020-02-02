package com.rainbow.teacheriiy.ui.LeaveRequests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.ui.Attendance.AttendanceActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StudentsLeaveRequests extends AppCompatActivity {

    private Spinner mSelectClass;
    private TextView mSelectDate;
    private RecyclerView mListOfRequests;
    private LeaveRequestsAdapter adapter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_leave_requests);

        setTitle("Students' Leave Requests");

        init();
        fetchAllClasses();
        fetchCurrentDate();

        mSelectDate.setOnClickListener(new DateSelectClickListener());
    }

    private void init() {
        mSelectClass = findViewById(R.id.class_select_spinner);
        mSelectDate = findViewById(R.id.selectDate);
        mListOfRequests = findViewById(R.id.studentsLeaveRequestsRecyclerView);
        db = FirebaseFirestore.getInstance();
    }

    private void fetchCurrentDate() {
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yyyy");
        mSelectDate.setText(dt.format(date));
    }
    private class DateSelectClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(StudentsLeaveRequests.this);
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String date_s = i + "-" + (i1+1) + "-" + i2;
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-M-d");
                    Date date;
                    try {
                        date = dt.parse(date_s);
                        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
                        mSelectDate.setText(dt1.format(date));

                        setAdapter();
                    } catch (ParseException e) {
                        Log.e("error", e+"");
                    }

                }
            });
            datePickerDialog.show();
        }
    }

    private void fetchAllClasses() {
        db.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<String> classes = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String clas = snapshot.getId();
                        classes.add(clas);
                    }

                    ArrayAdapter<String> classarrayadapter = new ArrayAdapter<String>(StudentsLeaveRequests.this, android.R.layout.simple_list_item_1, classes);
                    classarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSelectClass.setAdapter(classarrayadapter);
                    mSelectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mSelectClass.setSelection(i);
                            setAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
        });
    }

    private void setAdapter() {
        db.collection("LeaveRequests")
                .document(mSelectDate.getText().toString())
                .collection("List")
                .whereEqualTo("clas", extractclass(mSelectClass.getSelectedItem()+"")+"")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<LeaveModel> allLeaves = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String sName = snapshot.getString("name");
                        String sClass = snapshot.getString("clas");
                        String sDate = snapshot.getString("date");
                        String sReason = snapshot.getString("reason");

                        LeaveModel leave = new LeaveModel(sName, sClass, sDate, sReason);
                        allLeaves.add(leave);
                    }
                    adapter = new LeaveRequestsAdapter(StudentsLeaveRequests.this, allLeaves);
                    adapter.notifyDataSetChanged();
                    mListOfRequests.setAdapter(adapter);
                    mListOfRequests.setLayoutManager(new LinearLayoutManager(StudentsLeaveRequests.this));
                }
            }
        });
    }

    private int extractclass (String selectedclass){
        int selectedclasslength = selectedclass.length();
        String actualclass = selectedclass.substring(selectedclasslength-1, selectedclasslength);
        return Integer.parseInt(actualclass);
    }

}
