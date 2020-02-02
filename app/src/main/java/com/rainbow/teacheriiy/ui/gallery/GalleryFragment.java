package com.rainbow.teacheriiy.ui.gallery;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rainbow.teacheriiy.AttendanceAdapter;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.models.Attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private Spinner classSpinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog dialog;
    private Button save;
    private SharedPreferences sharedPreferences;
    private LinearLayoutManager layoutManager;
    private AttendanceAdapter adapter;
    private RecyclerView list;
    private TextView currentDate;

    List<Dictionary> takeList = new ArrayList<Dictionary>();
    Dictionary geek = new Hashtable();

    private ArrayList<Attendance> attendance = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uid = sharedPreferences.getString("id","NULL");


        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Fetching Information");
        dialog.setMessage("Please wait for a second");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        currentDate = root.findViewById(R.id.att_date);

        save = root.findViewById(R.id.save_attendance);
        list = root.findViewById(R.id.attendance_list);
        layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        //spinner setting
        classSpinner = root.findViewById(R.id.class_spinner);
        classSpinner.setSelection(0, false);
        //selection of class
        db.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> cate = new ArrayList<>();
                    cate.add(0, "Select Class");
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String name = doc.getData().get("class_name").toString();
                        cate.add("Class " + name);

                        //getting current data
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        String formattedDate = df.format(c);

                        currentDate.setText(formattedDate);

                        dialog.dismiss();
                    }
                    ArrayAdapter<String> madapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, cate);

                    madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    classSpinner.setAdapter(madapter);
                }
            }
        });

        //spinner item selected listener
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long c = classSpinner.getSelectedItemPosition();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1000);


                //fetching all home works list
                Query query = db.collection("Parent").whereEqualTo("student_class", c).orderBy("student_admno");
                final FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>()
                        .setQuery(query, Attendance.class)
                        .build();


                /*ArrayList<String> values = new ArrayList<>();
                Query query1 = db.collection("Attendance").whereEqualTo("student_class", c).orderBy("value");
                final FirestoreRecyclerOptions<Attendance> options1 = new FirestoreRecyclerOptions.Builder<Attendance>()
                        .setQuery(query1, Attendance.class)
                        .build();*/

                /*
                TODO: Fetch Data from Firebase and push it into arraylist
                */
                adapter = new AttendanceAdapter(getContext(), options);

                list.setAdapter(adapter);
                adapter.startListening();

                if (layoutManager.getItemCount() != 0){
                    dialog.dismiss();
                }

                //adapter item select listener
                adapter.setOnItemClick(new AttendanceAdapter.OnItemClick() {
                    @Override
                    public void getPosition(long pos, String userId) {
                        geek.put(userId, pos);
                        takeList.add(geek);
                    }
                });

                //saving the homework information
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!classSpinner.getSelectedItem().toString().equals("Select Class")){
                            dialog.setMessage("Please wait while we are saving data to server");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            for(int i=0; i<adapter.getItemCount(); i++){
                                final Dictionary d = takeList.get(i);
                                String admn = String.valueOf(adapter.getItem(i).getStudent_admno());
                                String id = "S"+admn;
                                long at = (long) d.get(id);

                                HashMap<String, Object> hashMap = new HashMap<>();
                                if (at == 0){
                                    hashMap.put("value", "P");
                                    hashMap.put("student_id", id);
                                    hashMap.put("teacher_id", uid);
                                    hashMap.put("class", classSpinner.getSelectedItem().toString());
                                }else if (at == 1){
                                    hashMap.put("value", "A");
                                    hashMap.put("uid", id);
                                    hashMap.put("teacher_id", uid);
                                    hashMap.put("class", classSpinner.getSelectedItem().toString());
                                }else if (at == 2){
                                    hashMap.put("value", "L");
                                    hashMap.put("uid", id);
                                    hashMap.put("teacher_id", uid);
                                    hashMap.put("class", classSpinner.getSelectedItem().toString());
                                }

                                db.collection("Attendance").document(currentDate.getText().toString()).collection("List")
                                        .document(id).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            Toast.makeText(getActivity(), "Please select class", Toast.LENGTH_SHORT).show();
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