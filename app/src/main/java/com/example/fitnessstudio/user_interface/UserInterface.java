package com.example.fitnessstudio.user_interface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.os.Bundle;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.user_interface_fragments.MainUserInterface;

public class UserInterface extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        setFragment(new MainUserInterface());
    }
    private void setFragment(MainUserInterface fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_user_interface,fragment);
        fragmentTransaction.commit();
    }
}