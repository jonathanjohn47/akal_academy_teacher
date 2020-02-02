package com.rainbow.teacheriiy.ui.send;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rainbow.teacheriiy.HomeWorkAdapter;
import com.rainbow.teacheriiy.MainActivity;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.models.HomeWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;
    private Spinner classSpinner, subSpinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog dialog;
    private EditText homework;
    private Button save;
    private SharedPreferences sharedPreferences;
    private LinearLayoutManager layoutManager;
    private HomeWorkAdapter adapter;
    private RecyclerView list;
    private Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        toolbar = root.findViewById(R.id.homeworktoolbar);
        toolbar.setTitle("HomeWork");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uid = sharedPreferences.getString("id","NULL");

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Fetching Information");
        dialog.setMessage("Please wait for a second");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        homework = root.findViewById(R.id.homework_edit);
        save = root.findViewById(R.id.save_homework);
        list = root.findViewById(R.id.homework_list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //spinner setting
        classSpinner = root.findViewById(R.id.class_spinner);
        subSpinner = root.findViewById(R.id.sub_spinner);

        /*classSpinner.setSelection(0, false);
        subSpinner.setSelection(0, false);*/

        //selection of class
        db.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> cate = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getData().get("class_name").toString();
                        cate.add("Class " + name);
                    }
                    ArrayAdapter<String> madapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, cate);

                    madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    classSpinner.setAdapter(madapter);
                }
            }
        });

        db.collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> cate = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getId();
                        cate.add(name);
                        dialog.dismiss();
                    }
                    ArrayAdapter<String> madapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, cate);

                    madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    subSpinner.setAdapter(madapter);
                }
            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchAllHomeWorks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchAllHomeWorks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //saving the homework information
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setTitle("Saving...");

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
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                String work = homework.getText().toString();
                String cl = classSpinner.getSelectedItem().toString();
                String sub = subSpinner.getSelectedItem().toString();

                if (!sub.equals("Select Subject") && !cl.equals("Select Class") && ! work.equals("")) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("date", formattedDate);
                    hashMap.put("homework", work);
                    hashMap.put("subject", sub);
                    hashMap.put("class", cl);

                    db.collection("HomeWork").document("001").collection("List").document()
                            .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "saved information", Toast.LENGTH_SHORT).show();
                                homework.setText("");

                                dialog.dismiss();
                                thread.interrupt();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "Please fill all inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void fetchAllHomeWorks(){

        final ProgressDialog dialog = new ProgressDialog(getContext());
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
        db.collection("HomeWork").document("001").collection("List")
                .whereEqualTo("class", classSpinner.getSelectedItem()+"")
                .whereEqualTo("subject", subSpinner.getSelectedItem()+"")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<HomeWork> listOfHomeWorks = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String date = snapshot.getString("date");
                        String homework = snapshot.getString("homework");
                        String subject = snapshot.getString("subject");
                        String clas = snapshot.getString("class");

                        HomeWork homeworkmodel = new HomeWork(date, homework, subject, clas);

                        listOfHomeWorks.add(homeworkmodel);
                    }
                    adapter = new HomeWorkAdapter(getContext(), listOfHomeWorks);
                    //adapter.notifyDataSetChanged();
                    list.setAdapter(adapter);
                    dialog.dismiss();
                    thread.interrupt();
                }
            }
        });
    }

    private int extractclass (String selectedclass){
        int selectedclasslength = selectedclass.length();
        String actualclass = selectedclass.substring(selectedclasslength-1, selectedclasslength);
        return Integer.parseInt(actualclass);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).hideActionBar();
    }

    @Override
    public void onStop() {
        ((MainActivity)getActivity()).showActionBar();
        super.onStop();
    }

    /*@Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/
}