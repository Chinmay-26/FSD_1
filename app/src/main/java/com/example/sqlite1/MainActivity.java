package com.example.sqlite1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword, editTextPhone;
    Button buttonInsert, buttonDelete, buttonView, buttonUpdate;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditTexts
        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPhone = findViewById(R.id.phone);

        // Initialize Buttons
        buttonInsert = findViewById(R.id.button_insert);
        buttonDelete = findViewById(R.id.button_delete);
        buttonView = findViewById(R.id.button_view);
        buttonUpdate = findViewById(R.id.button_update);

        // Create or open the database
        db = openOrCreateDatabase("UserDB", MODE_PRIVATE, null);

        // Create the users table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, email VARCHAR, password VARCHAR, phone VARCHAR);");

        // Insert Button Click
        buttonInsert.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            db.execSQL("INSERT INTO users(username, email, password, phone) VALUES(?, ?, ?, ?);",
                    new String[]{username, email, password, phone});
            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();

            clearFields();
        });

        // Delete Button Click
        buttonDelete.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter Username to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            db.execSQL("DELETE FROM users WHERE username = ?;", new String[]{username});
            Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();

            clearFields();
        });

        // View Button Click
        buttonView.setOnClickListener(v -> {
            StringBuilder result = new StringBuilder();
            Cursor cursor = db.rawQuery("SELECT * FROM users", null);

            if (cursor.moveToFirst()) {
                do {
                    result.append("ID: ").append(cursor.getInt(0)).append("\n");
                    result.append("Username: ").append(cursor.getString(1)).append("\n");
                    result.append("Email: ").append(cursor.getString(2)).append("\n");
                    result.append("Phone: ").append(cursor.getString(4)).append("\n\n");
                } while (cursor.moveToNext());
            } else {
                result.append("No records found.");
            }

            cursor.close();
            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
        });

        // Update Button Click
        buttonUpdate.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter Username to update", Toast.LENGTH_SHORT).show();
                return;
            }

            db.execSQL("UPDATE users SET email = ?, password = ?, phone = ? WHERE username = ?;",
                    new String[]{email, password, phone, username});
            Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();

            clearFields();
        });
    }

    // Clear input fields
    private void clearFields() {
        editTextUsername.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextPhone.setText("");
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
