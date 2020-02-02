package com.rainbow.teacheriiy.ui.send;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rainbow.teacheriiy.R;

public class SendFragment2 extends Fragment {

    private TextView classSpinner, subjectSpinner;
    private EditText setHomework;
    private Button showHomework, saveHomework;
    private RecyclerView list;
    private ProgressDialog dialog;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, null);
        init(view);
        showHomework.setOnClickListener(new ShowHomeWorkClickListener());
        saveHomework.setOnClickListener(new SaveHomeWorkClickListener());

        classSpinner.setOnClickListener(new ClassSpinnerClickListener());
        subjectSpinner.setOnClickListener(new SubjectSpinnerClickListener());
        return view;
    }

    private void init(View view){
        classSpinner = view.findViewById(R.id.class_spinner);
        subjectSpinner = view.findViewById(R.id.sub_spinner);
        setHomework = view.findViewById(R.id.homework_edit);
        saveHomework = view.findViewById(R.id.save_homework);
        list = view.findViewById(R.id.homework_list);

        dialog = new ProgressDialog(getContext());
        /*dialog.setTitle("Fetching Information");
        dialog.setMessage("Please wait for a second");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/

        db = FirebaseFirestore.getInstance();
    }

    private class ShowHomeWorkClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            showAllHomeWork();
        }
    }

    private class SaveHomeWorkClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            saveAllHomeWork();
        }
    }

    private class ClassSpinnerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }

    private class SubjectSpinnerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }

    private void showAllHomeWork() {
    }

    private void saveAllHomeWork() {
    }
}
