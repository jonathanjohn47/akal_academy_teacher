package com.rainbow.teacheriiy.ui.DailyMarks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rainbow.teacheriiy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.ViewHolder> {

    private Context mCtx;
    private ArrayList<MarksModel> mAllStudentMarks;
    private String mMaximumMarks, mSubject;


    public String getmSubject() {
        return mSubject;
    }

    public void setmSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public String getmMaximumMarks() {
        return mMaximumMarks;
    }

    public void setmMaximumMarks(String mMaximumMarks) {
        this.mMaximumMarks = mMaximumMarks;
    }

    public MarkListAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public Context getmCtx() {
        return mCtx;
    }

    public void setmCtx(Context mCtx) {
        this.mCtx = mCtx;
    }

    public ArrayList<MarksModel> getmAllStudentMarks() {
        return mAllStudentMarks;
    }

    public void setmAllStudentMarks(ArrayList<MarksModel> mAllStudentMarks) {
        this.mAllStudentMarks = mAllStudentMarks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_marks_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.single_mark_name.setText(mAllStudentMarks.get(position).getStudent_name());
        holder.single_mark_roll.setText(mAllStudentMarks.get(position).getStudent_Id());
        holder.single_marks.setText(mAllStudentMarks.get(position).getStudent_marks());

        holder.single_marks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newmarks = charSequence.toString();

                MarksModel model = new MarksModel();
                model.setStudent_name(mAllStudentMarks.get(position).getStudent_name());
                model.setStudent_class(mAllStudentMarks.get(position).getStudent_class());
                model.setStudent_marks(newmarks);
                model.setMaximum_marks(mAllStudentMarks.get(position).getMaximum_marks());
                model.setStudent_Id(mAllStudentMarks.get(position).getStudent_Id());
                model.setDate(mAllStudentMarks.get(position).getDate());
                model.setSubject(mAllStudentMarks.get(position).getSubject());

                mAllStudentMarks.set(position, model);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void savedata(){
        int studentmarkssize = mAllStudentMarks.size();
        for (int i = 0; i < studentmarkssize ; i++){
            MarksModel marksModel = new MarksModel();
            marksModel.setStudent_name(mAllStudentMarks.get(i).getStudent_name());
            marksModel.setStudent_Id(mAllStudentMarks.get(i).getStudent_Id());
            marksModel.setStudent_class(mAllStudentMarks.get(i).getStudent_class());
            marksModel.setDate(mAllStudentMarks.get(i).getDate());
            marksModel.setStudent_marks(mAllStudentMarks.get(i).getStudent_marks());
            marksModel.setMaximum_marks(mMaximumMarks);
            marksModel.setSubject(mSubject);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("DailyMarks").document(/*mAllStudentMarks.get(0).getDate()*/ gettodaysdate()).collection(mSubject)
                    .document(mAllStudentMarks.get(i).getStudent_Id()).set(marksModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error", e+"");
                        }
                    });
        }
        Toast.makeText(mCtx, "All Data Saved", Toast.LENGTH_LONG).show();

    }

    private String gettodaysdate(){
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yyyy");
        String format = dt.format(date);
        return format;
    }

    @Override
    public int getItemCount() {
        return mAllStudentMarks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView single_mark_name, single_mark_roll;
        private EditText single_marks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            single_mark_name = itemView.findViewById(R.id.single_mark_name);
            single_mark_roll = itemView.findViewById(R.id.single_mark_roll);
            single_marks = itemView.findViewById(R.id.single_marks);
        }
    }
}
