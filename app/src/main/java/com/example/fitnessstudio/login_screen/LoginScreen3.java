package com.example.fitnessstudio.login_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnessstudio.R;
import com.example.fitnessstudio.user_interface.UserInterface;
import com.google.firebase.database.DatabaseReference;
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
            else {
                addDataInDatabase(enteredName, phoneNumber);
                Intent intentNext=new Intent(this, UserInterface.class);
                startActivity(intentNext);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }
    private void addDataInDatabase(String name,String phoneNumber){
        HashMap<String,String> detailsMap=new HashMap<>();
        detailsMap.put("User Name",name);
        detailsMap.put("Phone Number",phoneNumber);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("Users");
        String key=usersReference.push().getKey();
        detailsMap.put("Key",key);
        assert key != null;
        usersReference.child(key).setValue(detailsMap);
    }
}