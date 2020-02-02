package com.rainbow.teacheriiy.ui.share;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.rainbow.teacheriiy.MarksAdapter;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.models.HomeWork;
import com.rainbow.teacheriiy.models.Marks;
import com.rainbow.teacheriiy.ui.send.SendViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;

    private SendViewModel sendViewModel;
    private Spinner classSpinner, subSpinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog dialog;
    private EditText homework;
    private Button save;
    private SharedPreferences sharedPreferences;
    private LinearLayoutManager layoutManager;
    private MarksAdapter adapter;
    private RecyclerView list;
    private Toolbar toolbar;

    List<Dictionary> takeList = new ArrayList<Dictionary>();
    Dictionary geek = new Hashtable();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        toolbar = root.findViewById(R.id.share_fragment_toolbar);
        toolbar.setTitle("Daily Marks");
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

        homework = root.findViewById(R.id.max_marks);
        save = root.findViewById(R.id.save_marks);
        list = root.findViewById(R.id.marks_list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //spinner setting
        classSpinner = root.findViewById(R.id.class_spinner);
        subSpinner = root.findViewById(R.id.sub_spinner);
        classSpinner.setSelection(0, false);
        subSpinner.setSelection(0, false);
        //selection of class
        db.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> cate = new ArrayList<>();
                    cate.add(0, "Select Class");
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getData().get("class_name").toString();
                        cate.add("Class " + name);
                        dialog.dismiss();
                    }
                    ArrayAdapter<String> madapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, cate);

                    madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    classSpinner.setAdapter(madapter);
                }
            }
        });
        //selection of subject
        db.collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> cate = new ArrayList<>();
                    cate.add(0, "Select Subject");
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getData().get("name").toString();
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

        //class selected item
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cl = classSpinner.getSelectedItem().toString();
                Toast.makeText(getActivity(), cl, Toast.LENGTH_SHORT).show();
                //fetching all home works list
                Query query = db.collection("HomeWork").document(uid).collection("Completed")
                        .whereEqualTo("student_class", cl).orderBy("date");
                final FirestoreRecyclerOptions<Marks> options = new FirestoreRecyclerOptions.Builder<Marks>()
                        .setQuery(query, Marks.class)
                        .build();

                adapter = new MarksAdapter(getContext(), options);
                list.setAdapter(adapter);
                adapter.startListening();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //subspinner selected listeners
        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sub = subSpinner.getSelectedItem().toString();
                Toast.makeText(getActivity(), sub, Toast.LENGTH_SHORT).show();

                //fetching all home works list
                Query query = db.collection("HomeWork").document(uid).collection("Completed")
                        .whereEqualTo("subject", sub);
                final FirestoreRecyclerOptions<Marks> options = new FirestoreRecyclerOptions.Builder<Marks>()
                        .setQuery(query, Marks.class)
                        .build();

                adapter = new MarksAdapter(getContext(), options);
                list.setAdapter(adapter);
                adapter.startListening();

                adapter.setOnItemClick(new MarksAdapter.OnItemClick() {
                    @Override
                    public void getPosition(String userId, String mark) {
                        geek.put(userId, mark);
                        takeList.add(geek);
                        String vale = String.valueOf(takeList.get(0));
                        Toast.makeText(getActivity(), vale, Toast.LENGTH_SHORT).show();
                    }
                });

                //saving the homework information
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        String formattedDate = df.format(c);
                        String ma = homework.getText().toString();
                        String cl = classSpinner.getSelectedItem().toString();
                        String sub = subSpinner.getSelectedItem().toString();
                        if (!sub.equals("Select Subject") && !cl.equals("Select Class") && ! ma.equals("")) {
                            dialog.setMessage("Please wait while we are saving data to server");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            for(int i=0; i<=adapter.getItemCount(); i++){
                                final Dictionary d = takeList.get(i);
                                String admn = adapter.getItem(i).getStudent_admno();
                                String id = "S"+admn;

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("date", formattedDate);
                                hashMap.put("marks", d.get(id));

                                db.collection("HomeWork").document(uid).collection("Completed")
                                        .document(id).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), "Data Stored", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }else {
                            Toast.makeText(getActivity(), "Please fill all inputs", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }
}