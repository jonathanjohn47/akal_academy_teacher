package com.rainbow.teacheriiy.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rainbow.teacheriiy.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SelectFromRecyclerView extends Dialog {

    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<String> listOfItems;
    FirebaseFirestore db;

    public SelectFromRecyclerView(@NonNull Context context, View view) {
        super(context);
        this.context = context;
        this.view = view;

        if (view.getId() == R.id.class_spinner){
            listOfItems = extractClasses();
        }
        if (view.getId() == R.id.sub_spinner){
            listOfItems = extractSubjects();
        }
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.selectionRecyclerView);
        db = FirebaseFirestore.getInstance();
    }

    private ArrayList<String> extractClasses(){

        ArrayList<String> arrayList = new ArrayList<String>();
        db.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e(TAG, document.getId() + " => " + document.getData());
                    }
                }
                else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return arrayList;
    }

    private ArrayList<String> extractSubjects(){
        ArrayList<String> arrayList = new ArrayList<String>();
        return arrayList;
    }
}
