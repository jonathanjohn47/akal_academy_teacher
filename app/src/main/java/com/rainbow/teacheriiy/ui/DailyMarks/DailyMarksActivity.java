package com.rainbow.teacheriiy.ui.DailyMarks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.ui.Attendance.AttendanceActivity;
import com.rainbow.teacheriiy.ui.Attendance.AttendanceModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DailyMarksActivity extends AppCompatActivity {

    private Spinner mSelectClass, mSelectSubject;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView mMarksRecyclerView;
    private Button mBtnSaveMarks;
    private EditText mEdtMaximumMarks;

    private SharedPreferences sharedPreferences;

    private String marks, subject;

    private MarkListAdapter adapter;

    public String getMarks() {
        return marks;
    }

    public String getSubject() {
        return subject;
    }

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share);

        setTitle("Daily Marks");
        getSupportActionBar().hide();

        init();

        toolbar.setTitle("Daily Marks");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEdtMaximumMarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                marks = charSequence.toString();
                /*SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MaximumMarks", charSequence.toString());*/
                adapter.setmMaximumMarks(marks);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fetchallclasses();
        fetchallsubjects();
    }

    @Override
    protected void onStop() {
        getSupportActionBar().show();
        super.onStop();
    }

    private int extractClass(String selectedclass){
        int selectedclasslength = selectedclass.length();
        String actualclass = selectedclass.substring(selectedclasslength-1, selectedclasslength);
        return Integer.parseInt(actualclass);
    }


    private void fetchlistofStudents() {
        final ProgressDialog dialog = new ProgressDialog(DailyMarksActivity.this);
        dialog.setTitle("Loading...");
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
        db.collection("Parent").whereEqualTo("student_class", extractClass(mSelectClass.getSelectedItem().toString()))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<StudentDetails> listofdetails = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String studentname = snapshot.getString("student_name");
                        String studentId = snapshot.getString("uid");
                        String studentclass = extractClass(mSelectClass.getSelectedItem().toString())+"";
                        StudentDetails details = new StudentDetails(studentname, studentId, studentclass);
                        listofdetails.add(details);
                    }
                    ArrayList<MarksModel> listofmarks = new ArrayList<>();
                    for (int i = 0; i < listofdetails.size(); i++) {
                        String studentname = listofdetails.get(i).getStudent_name();
                        String studentID = listofdetails.get(i).getStudent_Id();
                        String studentclass = listofdetails.get(i).getStudent_class();
                        String studentmarks = "0";
                        String date = gettodaysdate();

                        MarksModel model = new MarksModel();
                        model.setStudent_name(studentname);
                        model.setStudent_Id(studentID);
                        model.setStudent_class(studentclass);
                        model.setMaximum_marks(marks);
                        model.setStudent_marks(studentmarks);
                        model.setDate(date);
                        model.setSubject(subject);
                        listofmarks.add(model);
                    }
                    setadapter(listofmarks);
                    dialog.dismiss();
                    thread.interrupt();
                }
            }
        });

    }

    private void clearEditMaximumMarks(){
        mEdtMaximumMarks.setText("");
    }

    private void fetchallsubjects() {
        db.collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<String> cate = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        cate.add(snapshot.getId());
                    }
                    ArrayAdapter<String> classarrayadapter = new ArrayAdapter<String>(DailyMarksActivity.this, android.R.layout.simple_list_item_1, cate);
                    classarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSelectSubject.setAdapter(classarrayadapter);
                    mSelectSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mSelectSubject.setSelection(i);
                            subject = mSelectSubject.getSelectedItem().toString();
                            fetchlistofStudents();
                            clearEditMaximumMarks();
                            adapter.setmSubject(subject);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
        });
    }
    private void init(){
        mSelectClass = findViewById(R.id.class_spinner);
        mSelectSubject = findViewById(R.id.sub_spinner);
        mMarksRecyclerView = findViewById(R.id.marks_list);
        mEdtMaximumMarks = findViewById(R.id.max_marks);
        mBtnSaveMarks = findViewById(R.id.save_marks);
        adapter = new MarkListAdapter(DailyMarksActivity.this);

        sharedPreferences = getSharedPreferences("SetDetails", MODE_PRIVATE);

        toolbar = findViewById(R.id.share_fragment_toolbar);
    }

    private void fetchallclasses() {
        db.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<String> cate = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        cate.add(snapshot.getId());
                    }
                    ArrayAdapter<String> classarrayadapter = new ArrayAdapter<String>(DailyMarksActivity.this, android.R.layout.simple_list_item_1, cate);
                    classarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSelectClass.setAdapter(classarrayadapter);
                    mSelectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mSelectClass.setSelection(i);
                            fetchlistofStudents();
                            clearEditMaximumMarks();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
        });
    }

    public void setadapter(ArrayList<MarksModel> arrayList) {
        adapter.setmAllStudentMarks(arrayList);

        adapter.notifyDataSetChanged();

        mMarksRecyclerView.setAdapter(adapter);
        mMarksRecyclerView.setLayoutManager(new LinearLayoutManager(DailyMarksActivity.this));

        adapter.notifyDataSetChanged();
        mMarksRecyclerView.setAdapter(adapter);

        mBtnSaveMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.savedata();
                adapter.notifyDataSetChanged();
            }
        });
    }
    private String gettodaysdate(){
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("d-MMM-yyyy");
        String format = dt.format(date);
        return format;
    }
}
