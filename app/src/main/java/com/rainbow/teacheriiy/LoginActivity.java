package com.rainbow.teacheriiy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private CheckBox show_hide_password;
    private EditText password;
    private EditText emailet;
    private Animation shakeAnimation;
    Button loginButton;
    ProgressDialog progressDialog;
    View CurrentFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CurrentFocus = getCurrentFocus();

        show_hide_password = findViewById(R.id.show_hide_password);
        password = findViewById(R.id.passwordField);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        loginButton = findViewById(R.id.LoginButton);
        emailet = findViewById(R.id.usernameField);

        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText("Hide Password");// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText("Show Password");// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

        if(!hasPermissions(this,PERMISSIONS)) {
            /**
             * Checks if the user has specified permissions specified in the string PERMISSIONS.
             * If not, then request all permissions.
             */
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean everythingCorrect = true;
                if(emailet.getText().toString().trim().equals("")){
                    everythingCorrect = false;
                    emailet.startAnimation(shakeAnimation);
                    view.startAnimation(shakeAnimation);
                    Toast.makeText(LoginActivity.this, "Enter a valid teacher id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().trim().length() < 6){
                    everythingCorrect = false;
                    password.startAnimation(shakeAnimation);
                    view.startAnimation(shakeAnimation);
                    Toast.makeText(LoginActivity.this, "Password should be greater than 6 digit", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(everythingCorrect){
                    StartLogin();
                }
            }
        });

    }

    private void StartLogin(){
        final String id = emailet.getText().toString().trim();
        final String passwordd = password.getText().toString().trim();
        progressDialog = ProgressDialog.show(LoginActivity.this, "",
                "Signing in Please wait...", false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("Teachers").document("T"+id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String pass = document.get("password")+"";
                        Log.e("docstatus", "Document exists!" + document.getData());
                        if(pass.equals(passwordd)) {
                            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("id", id);
                            editor.putString("pass",passwordd);
                            editor.putString("name",document.getString("name"));
                            editor.commit();
                            editor.apply();
                            Log.e("shared", sharedpreferences.getString("id", "NULL"));
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.cancel();
                    } else {
                        progressDialog.cancel();
                        Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_LONG).show();
                        Log.e("docstatus", "Document does not exist!");
                    }
                } else {
                    progressDialog.cancel();
                    Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_LONG).show();
                    Log.e("FailLoad", "Failed with: ", task.getException());
                }
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        /**
         * Checks if the user has specified the required permissions.
         * @return boolean variable. True if all permissions already provided. False if not.
         */
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
