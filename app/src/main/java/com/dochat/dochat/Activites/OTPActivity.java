package com.dochat.dochat.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dochat.dochat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    TextView textView;
    public String VerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        getSupportActionBar().hide();

        TextView textMobile = findViewById(R.id.tv_Verify);
        textMobile.setText(String.format("Verify+ %s",getIntent().getStringExtra("phoneNumber")));

        inputCode1  = findViewById(R.id.inputcode1);
        inputCode2  = findViewById(R.id.inputcode2);
        inputCode3  = findViewById(R.id.inputcode3);
        inputCode4  = findViewById(R.id.inputcode4);
        inputCode5  = findViewById(R.id.inputcode5);
        inputCode6  = findViewById(R.id.inputcode6);
        textView = findViewById(R.id.text);
        setupOTPInput();

        final ProgressBar progressBar1 = findViewById(R.id.progressbar1);
        final Button btnVerify =  findViewById(R.id.continueBtn2);

            VerificationId = getIntent().getStringExtra("verificationId");

        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d"
                        ,TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        ,TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                textView.setText(sDuration);
            }

            @Override
            public void onFinish() {
                textView.setVisibility(View.GONE);
                Toast.makeText(OTPActivity.this, "Time Laps", Toast.LENGTH_SHORT).show();
            }
        }.start();

            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputCode1.getText().toString().trim().isEmpty()
                    ||inputCode2.getText().toString().trim().isEmpty()
                            ||inputCode3.getText().toString().trim().isEmpty()
                            ||inputCode4.getText().toString().trim().isEmpty()
                            ||inputCode5.getText().toString().trim().isEmpty()
                            ||inputCode6.getText().toString().trim().isEmpty())
                    {
                        Toast.makeText(OTPActivity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String code =
                            inputCode1.getText().toString() +
                                    inputCode2.getText().toString() +
                                    inputCode3.getText().toString() +
                                    inputCode4.getText().toString() +
                                    inputCode5.getText().toString() +
                                    inputCode6.getText().toString();

                             if(VerificationId !=null)
                             {
                                progressBar1.setVisibility(View.VISIBLE);
                                btnVerify.setVisibility(View.INVISIBLE);
                                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                        VerificationId,
                                        code
                                );
                                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progressBar1.setVisibility(View.GONE);

                                                btnVerify.setVisibility(View.VISIBLE);
                                                if(task.isSuccessful())
                                                {


                                                    Intent intent = new Intent(getApplicationContext(),SetupProfileActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                }
                                                else {
                                                    Toast.makeText(OTPActivity.this, "The Verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                             }

                }
            });

            findViewById(R.id.ResendOtp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+getIntent().getStringExtra("phoneNumber"),
                            60,
                            TimeUnit.SECONDS,
                            OTPActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                            {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                    Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String newverificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                   VerificationId = newverificationId;
                                    Toast.makeText(OTPActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();


                                }
                            }
                    );

                }
            });
    }

    private void setupOTPInput()
    {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode5.addTextChangedListener(new TextWatcher()  {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
