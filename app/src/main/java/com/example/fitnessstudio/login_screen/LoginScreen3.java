package com.example.fitnessstudio.login_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnessstudio.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginScreen3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen3);
        Intent intent=getIntent();
        String phoneNumber=intent.getStringExtra("mobileNumber");
        EditText name=this.findViewById(R.id.edit_text_name_login_screen3);
        AppCompatButton continueButton=this.findViewById(R.id.continue_button_login_screen3);
        continueButton.setOnClickListener(event->{
            String enteredName=name.getText().toString();
            if(TextUtils.isEmpty(enteredName))
                Toast.makeText(this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
            else
                addDataInDatabase(enteredName,phoneNumber);
        });
    }
    private void addDataInDatabase(String name,String phoneNumber){
        HashMap<String,String> detailsMap=new HashMap<>();
        detailsMap.put("User Name",name);
        detailsMap.put("Phone Number",phoneNumber);
        FirebaseDatabase.getInstance().getReference().child("User").setValue(detailsMap);
    }
}