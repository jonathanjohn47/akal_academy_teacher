package com.rainbow.teacheriiy.ui.tools;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rainbow.teacheriiy.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uid = sharedPreferences.getString("id","NULL");

        final EditText feedback = root.findViewById(R.id.feedback_edit);
        Button send = root.findViewById(R.id.feedback_button);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f = feedback.getText().toString();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("feedback", f);
                hashMap.put("uid", uid);
                hashMap.put("date", formattedDate);

                db.collection("Feedback").document(uid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Feedback Sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return root;
    }
}