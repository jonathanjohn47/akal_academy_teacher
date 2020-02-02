package com.rainbow.teacheriiy.ui.home;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.rainbow.teacheriiy.ui.Attendance.AttendanceActivity;
import com.rainbow.teacheriiy.LoginActivity;
import com.rainbow.teacheriiy.R;
import com.rainbow.teacheriiy.ui.DailyMarks.DailyMarksActivity;
import com.rainbow.teacheriiy.ui.TeacherAttendance.TeacherAttendanceActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ConstraintLayout profile, attendance, homework, marks, request, feedback, logout;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull final LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.textHome);
        /*homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uid = sharedPreferences.getString("id","NULL");


        profile = root.findViewById(R.id.constraintLayout1);
        attendance = root.findViewById(R.id.constraintLayout2);
        homework= root.findViewById(R.id.constraintLayout3);
        marks = root.findViewById(R.id.constraintLayout4);
        request = root.findViewById(R.id.constraintLayout5);
        feedback = root.findViewById(R.id.constraintLayout6);
        logout = root.findViewById(R.id.constraintLayout7);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scale_up_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_animation);
                scale_up_animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                profile.startAnimation(scale_up_animation);
            }
        });
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scale_up_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_animation);
                scale_up_animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_gallery);
                        Intent intent = new Intent(getContext(), AttendanceActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                attendance.startAnimation(scale_up_animation);
            }
        });
        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scale_up_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_animation);
                scale_up_animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_send);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                homework.startAnimation(scale_up_animation);
            }
        });
        marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scale_up_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_animation);
                scale_up_animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_share);
                        Intent intent = new Intent(getContext(), DailyMarksActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                marks.startAnimation(scale_up_animation);
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scale_up_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_animation);
                scale_up_animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_slideshow);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                request.startAnimation(scale_up_animation);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scale_up_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_animation);
                scale_up_animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_tools);
                        startActivity(new Intent(getContext(), TeacherAttendanceActivity.class));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                feedback.startAnimation(scale_up_animation);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scale_up_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_animation);
                scale_up_animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        ed.clear();
                        ed.commit();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                logout.startAnimation(scale_up_animation);
            }
        });


        return root;
    }
}