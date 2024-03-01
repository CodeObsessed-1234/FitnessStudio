package com.example.fitnessstudio.login_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.fitnessstudio.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginScreen2 extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen2);
        TextView numberTextView=this.findViewById(R.id.number_text_view_login_screen2);
        TextView resendTextView=this.findViewById(R.id.resend_text_view_login_screen2);
        EditText[] editTexts=new EditText[6];
        editTexts[0]=this.findViewById(R.id.edit_text_otp_1);
        editTexts[1]=this.findViewById(R.id.edit_text_otp_2);
        editTexts[2]=this.findViewById(R.id.edit_text_otp_3);
        editTexts[3]=this.findViewById(R.id.edit_text_otp_4);
        editTexts[4]=this.findViewById(R.id.edit_text_otp_5);
        editTexts[5]=this.findViewById(R.id.edit_text_otp_6);
        for(int index=0;index<editTexts.length;index++) {
            int finalIndex = index;
            editTexts[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    StringBuilder stringBuilder=new StringBuilder(s);
                    if(stringBuilder.toString().isEmpty()){
                        editTexts[finalIndex].setBackground(ContextCompat.getDrawable(LoginScreen2.this,R.drawable.otp_edit_text_background1));
                        if(finalIndex-1>=0){
                            editTexts[finalIndex-1].requestFocus();
                            editTexts[finalIndex-1].setCursorVisible(true);
                        }

                    }
                    else{
                        editTexts[finalIndex].setBackground(ContextCompat.getDrawable(LoginScreen2.this,R.drawable.otp_edit_text_background2));
                        editTexts[finalIndex].setCursorVisible(false);
                        if(finalIndex+1<editTexts.length)
                            editTexts[finalIndex+1].requestFocus();
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        Intent intent=getIntent();
        String phoneNumberText=intent.getStringExtra("mobileNumber");
        assert phoneNumberText != null;
        String phoneNumber="+"+phoneNumberText.substring(0,phoneNumberText.indexOf(' '))+phoneNumberText.substring(phoneNumberText.indexOf(' ')+1);
        ImageView imageView=this.findViewById(R.id.arrow_back_image);
        imageView.setOnClickListener(event->backToLoginScreen(imageView));
        AppCompatButton verifyButton=this.findViewById(R.id.verify_button_login_screen2);
        numberTextView.setText(numberTextView.getText()+phoneNumberText);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        String[] code = {""};
        sendOneTimePassword(firebaseAuth,phoneNumber,code);
        verifyButton.setOnClickListener(event->{
            StringBuilder enteredCode= new StringBuilder();
            for(EditText editText:editTexts)
                enteredCode.append(editText.getText().toString());
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(code[0],enteredCode.toString());
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Intent intentNext=new Intent(this, LoginScreen3.class);
                    intentNext.putExtra("mobileNumber",phoneNumber);
                    startActivity(intentNext);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
                else
                    Toast.makeText(LoginScreen2.this, "Verification failed", Toast.LENGTH_SHORT).show();
            });
        });
        resendTextView.setOnClickListener(event-> sendOneTimePassword(firebaseAuth,phoneNumber,code));
    }
    public void backToLoginScreen(View view){
        Intent intent=new Intent(this, LoginScreen1.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    private void sendOneTimePassword(FirebaseAuth firebaseAuth,String phoneNumber,String[] code){
        PhoneAuthOptions options= PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        Toast.makeText(LoginScreen2.this,"Code sent.",Toast.LENGTH_SHORT).show();
                        code[0]=s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(LoginScreen2.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}