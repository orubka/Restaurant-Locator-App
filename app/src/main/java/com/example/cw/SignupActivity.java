package com.example.cw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    // UI elements
    private EditText usernameSignup, passwordSignup;
    private Button signupButton, loginRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);

        // initialize UI Elements
        usernameSignup = findViewById(R.id.usernameSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        signupButton = findViewById(R.id.signupButton);
        loginRedirect = (Button) findViewById(R.id.loginRedirect);

        //if button Sign Up is clicked, perform signup User function
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signupUser(); }
        });

        //if button Login is clicked, direct to Login screen
        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void signupUser() {
        // retrieve entered username and password
        String username = usernameSignup.getText().toString();
        String password = passwordSignup.getText().toString();

        //username or password must not be empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //check if username already exists
        if (dbHelper.isUsernameExists(username)) {
            Toast.makeText(this, "Username already exists. Choose a different username.", Toast.LENGTH_SHORT).show();
            return;
        }

        long newRowId = dbHelper.insertUser(username, password);

        if (newRowId != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            // clear the input fields after successful registration
            usernameSignup.setText("");
            passwordSignup.setText("");
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }



}