package com.example.cw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;  //instance of the DB helper class
    private EditText usernameLogin, passwordLogin;
    private Button loginButton, goToSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        dbHelper = new DatabaseHelper(this);

        // initialize UI elements
        usernameLogin = findViewById(R.id.usernameLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        goToSignupButton = findViewById(R.id.signupRedirect);

        //if button is clicked, perform loginUser method
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        //if button is clicked, perform: go to Signup Screen
        goToSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });
    }

    private void loginUser() {
        String username = usernameLogin.getText().toString();
        String password = passwordLogin.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {    // make sure that username and password fields are not empty

            SQLiteDatabase db = dbHelper.getReadableDatabase();      //read data in the database

            String[] projection = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD};
            String selection = DatabaseHelper.COLUMN_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
            String[] selectionArgs = {username, password};

            Cursor cursor = db.query(
                    DatabaseHelper.TABLE_USERS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {        // if username and password are correct, then login the user
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                goToMainActivity();
            } else {                            // if wrong username or password, then login failed
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        } else {
            Toast.makeText(this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToSignupActivity() {       // direct to Signup screen
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    private void goToMainActivity() {        // direct to Main screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}