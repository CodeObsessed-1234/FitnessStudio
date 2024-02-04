package com.example.fitnessstudio.login_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnessstudio.R;
import com.hbb20.CountryCodePicker;

// after 2053 SHA1 and SHA256 needs to be updated.
// 10 otp/day will be sent
public class LoginScreen1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen1);
        EditText phoneNumber=this.findViewById(R.id.phone_number_edit_text_login_screen1);
        CountryCodePicker countryCodePicker=this.findViewById(R.id.country_code_picker);
        AppCompatButton continueButton=this.findViewById(R.id.continue_button_login_screen1);
        continueButton.setOnClickListener(event->{
            String phoneNumberText=phoneNumber.getText().toString();
            String code=countryCodePicker.getSelectedCountryCode();
            if(TextUtils.isEmpty(phoneNumberText)||phoneNumberText.length()<10)
                Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            else{
                Intent intent=new Intent(this, LoginScreen2.class);
                intent.putExtra("mobileNumber",(code+" "+phoneNumberText));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }
}