package com.example.goran.firebasedemo.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @BindView(R.id.txt_signup_display_name) EditText txtDisplayName;
    @BindView(R.id.txt_signup_email) EditText txtEmail;
    @BindView(R.id.txt_signup_password) EditText txtPassword;
    @BindView(R.id.txt_signup_confirm_password) EditText txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_signup_create)
    public void initUserCreation() {
        String displayName = txtDisplayName.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmedPassword = txtConfirmPassword.getText().toString();

        if (!displayName.isEmpty()
                && !email.isEmpty()
                && !password.isEmpty()
                && password.matches(confirmedPassword)) {
            createFirebaseUser(email, password, displayName);

        } else {
            Toast.makeText(this, "Check your password!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createFirebaseUser(String email, String password, String displayName) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setUserDisplayName(displayName);
                        startActivity(new Intent(this, MainActivity.class));

                    } else {
                        Toast.makeText(this, "Error! Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUserDisplayName(String displayName) {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
            user.updateProfile(profileChange);
        } else {
            Log.e("Error", "User null");
        }
    }
}
