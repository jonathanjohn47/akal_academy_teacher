package com.rainbow.teacheriiy.ui.TeacherAttendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.ui.slideshow.TeacherLeaveModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TeacherAttendanceActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    private FirebaseFirestore db;

    private Toolbar toolbar;

    private TextView mTxtHolidayReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);
        init();

        getSupportActionBar().hide();
        toolbar.setTitle("Teacher Attendance");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            fetchAllDetails();
        } catch (ParseException e) {
            Log.e("Error", e+"");
        }
    }

    @Override
    protected void onStop() {
        getSupportActionBar().show();
        super.onStop();
    }

    private void init() {
        mCalendarView = findViewById(R.id.calendar_view);
        db = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar_teacher_attendance);
        mTxtHolidayReason = findViewById(R.id.txtHolidayReason);
    }


    ArrayList<TeacherAttendanceModel> allAttendances = new ArrayList<>();
    ArrayList<EventDay> events = new ArrayList<>();
    private void fetchAllDetails() throws ParseException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String id = sharedPreferences.getString("id", "NULL");
        final ArrayList<String> listOfDates = getDatesBetween();
        for(final String date: listOfDates){
            db.collection("Attendance").document(date)
                    .collection("List").whereEqualTo("teacherID", "T" + id)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot snapshot: task.getResult()){
                            String value = snapshot.getString("attendanceValue");
                            TeacherAttendanceModel model = new TeacherAttendanceModel(date, value);
                            allAttendances.add(model);
                        }

                        for (TeacherAttendanceModel model: allAttendances){
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String d = model.getDate();
                            String v = model.getAttendancevalue();
                            if (v.equals("P")){
                                try {
                                    Date date1 = df.parse(d);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date1);
                                    events.remove(new EventDay(calendar));
                                    mCalendarView.setEvents(events);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (v.equals("A")){
                                try {
                                    Date date1 = df.parse(d);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date1);
                                    events.add(new EventDay(calendar, R.drawable.back2));
                                    mCalendarView.setEvents(events);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (v.equals("L")){
                                try {
                                    Date date1 = df.parse(d);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date1);
                                    events.add(new EventDay(calendar, R.drawable.back1));
                                    mCalendarView.setEvents(events);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        Date d = new Date();
                        try {
                            mCalendarView.setDate(d);
                        } catch (OutOfDateRangeException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public ArrayList<String> getDatesBetween() throws ParseException {

        Date startDate = new SimpleDateFormat("dd-MMM-yyyy").parse("01-Jan-2020");
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);

        Date endDate = new Date();

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        endCalendar.add(Calendar.DAY_OF_YEAR, 1);

        ArrayList<Date> datesInRange = new ArrayList<>();
        while (startCalendar.before(endCalendar)) {
            Date result = startCalendar.getTime();
            datesInRange.add(result);
            startCalendar.add(Calendar.DATE, 1);
        }
        ArrayList<String> returnDates = new ArrayList<>();
        for(Date d: datesInRange){
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String date = df.format(d);
            returnDates.add(date);
        }
        return returnDates;
    }
}
