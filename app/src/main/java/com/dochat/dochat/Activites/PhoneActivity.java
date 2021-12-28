package com.dochat.dochat.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dochat.dochat.R;
import com.dochat.dochat.databinding.ActivityPhoneBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    ActivityPhoneBinding binding;
    FirebaseAuth auth;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_phone);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() !=null)
        {
            Intent intent = new Intent(PhoneActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        final EditText editText =  findViewById(R.id.phoneBox);
        Button buttonClick = findViewById(R.id.continueBtn1);

      ProgressBar progressBar = findViewById(R.id.progressbar);
       //progressBar = (ProgressBar) findViewById(R.id.progressbar);
//       binding.phoneBox.requestFocus();

        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PhoneActivity.this, "ENTER MOBILE NUMBER", Toast.LENGTH_SHORT).show();
                    return;
                }

                    progressBar.setVisibility(View.VISIBLE);
                    buttonClick.setVisibility(View.INVISIBLE);


                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+editText.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        PhoneActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                        {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                               buttonClick.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                               progressBar.setVisibility(View.GONE);
                               buttonClick.setVisibility(View.VISIBLE);
                                Toast.makeText(PhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                buttonClick.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(),OTPActivity.class);
                                intent.putExtra("phoneNumber",editText.getText().toString());
                                intent.putExtra("verificationId",verificationId);
                                startActivity(intent);
                            }
                        }
                );


            }
        });
    }
}