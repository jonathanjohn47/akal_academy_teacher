package com.rainbow.teacheriiy.ui.slideshow;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rainbow.teacheriiy.MainActivity;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.ui.LeaveRequests.StudentsLeaveRequests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private EditText reason;
    private TextView txtFrom, txtUpto;
    private Button send;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sharedPreferences;
    private ProgressDialog dialog;
    private Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        init(root);

        ((MainActivity)getActivity()).hideActionBar();

        toolbar.setTitle("Leave Request");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        setOnClickListeners();
        return root;
    }

    @Override
    public void onStop() {
        ((MainActivity)getActivity()).showActionBar();
        super.onStop();
    }

    private void init(View root) {
        reason = root.findViewById(R.id.req_reason);

        txtFrom = root.findViewById(R.id.request_from);
        txtUpto = root.findViewById(R.id.request_upto);

        send = root.findViewById(R.id.send_request);
        toolbar = root.findViewById(R.id.slidshow_toolbar);

    }


    private void setOnClickListeners() {
        txtFrom.setOnClickListener(new OnTxtViewClickListener(txtFrom));
        txtUpto.setOnClickListener(new OnTxtViewClickListener(txtUpto));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String leavereason = reason.getText().toString();
                if (leavereason.equals("")){}
                else{
                    try {
                        saveData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveData() throws ParseException {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Saving Request...");
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
        ArrayList<String> datesbetween = getDatesBetween();
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name = sharedpreferences.getString("name", "NULL");
        String id = sharedpreferences.getString("id", "NULL");
        for (String dates: datesbetween) {
            String leavereason = reason.getText().toString();

            TeacherLeaveModel model = new TeacherLeaveModel(name, id, dates, leavereason);
            db.collection("TeachersLeaves").document(dates).collection("List")
                    .add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    dialog.dismiss();
                    thread.interrupt();
                }
            });
        }
    }

    private class OnTxtViewClickListener implements View.OnClickListener {
        public TextView textview;

        public OnTxtViewClickListener(TextView textview) {
            this.textview = textview;
        }

        @Override
        public void onClick(View view) {
            showDatePickerDialog(textview);
        }
    }

    private void showDatePickerDialog(final TextView view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date_s = i + "-" + (i1+1) + "-" + i2;
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-M-d");
                Date date;
                try {
                    date = dt.parse(date_s);
                    SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
                    view.setText(dt1.format(date));
                } catch (ParseException e) {
                    Log.e("error", e+"");
                }

            }
        });
        datePickerDialog.show();
    }

    public ArrayList<String> getDatesBetween() throws ParseException {
        Date startDate=new SimpleDateFormat("dd-MMM-yyyy").parse(txtFrom.getText().toString());
        ArrayList<Date> datesInRange = new ArrayList<>();
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);

        Date endDate = new SimpleDateFormat("dd-MMM-yyyy").parse(txtUpto.getText().toString());

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        endCalendar.add(Calendar.DAY_OF_YEAR, 1);

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



/*final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uid = sharedPreferences.getString("id","NULL");

        reason = root.findViewById(R.id.req_reasone);
        date = root.findViewById(R.id.req_date);
        send = root.findViewById(R.id.send_request);

        dialog = new ProgressDialog(getContext());


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Sending Request");
                dialog.setMessage("Please wait for a second");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                String r = reason.getText().toString();
                String d = date.getText().toString();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                HashMap<String, Object> map = new HashMap<>();
                map.put("date_from", formattedDate);
                map.put("date_upto", d);
                map.put("teacher_id", uid);
                map.put("reason", r);

                db.collection("TeacherRequests").document(uid).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "request sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });*/