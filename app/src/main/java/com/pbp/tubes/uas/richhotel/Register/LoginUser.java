package com.pbp.tubes.uas.richhotel.Register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pbp.tubes.uas.richhotel.MainActivity.MainActivity;
import com.pbp.tubes.uas.richhotel.MainActivity.MainActivity2;
import com.pbp.tubes.uas.richhotel.R;

public class LoginUser extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private EditText txtPassword, txtEmail;
    private Button login;
    SharedPreferences sharedPreferences;
    String emailString, passwordString;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login) ;

        register = (TextView) findViewById(R.id.registerHere);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.loginUser);
        login.setOnClickListener(this);

        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registerHere:
                startActivity(new Intent(this, UserRegister.class));
                break;

            case R.id.loginUser:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(email.isEmpty()){
            txtEmail.setError("Email is required!");
            txtEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txtPassword.setError("Password is required!");
            txtPassword.requestFocus();
            return;
        }
        if(!txtEmail.getText().toString().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")){
            txtEmail.setError("Please provide valid email!");
            txtEmail.requestFocus();
            return;
        }

        if(password.length() <6){
            txtPassword.setError("Password is require!");
            txtPassword.requestFocus();
            return;
        }
        if(txtEmail.getText().toString().equalsIgnoreCase("admin@gmail.com") && txtPassword.getText().toString().equalsIgnoreCase("admin123")){
            Toast.makeText(LoginUser.this, "Selamat datang admin!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginUser.this, MainActivity.class));
        }else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user.isEmailVerified()) {
                            Intent intent = new Intent(LoginUser.this, MainActivity2.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(LoginUser.this, "Please check your email!", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(LoginUser.this, "Failed to login!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
