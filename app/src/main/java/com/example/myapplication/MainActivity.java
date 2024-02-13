package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.logging.FileHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editTextCardNumber);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }

            @Override
            public void afterTextChanged(Editable s) {
                String originalText = s.toString().replaceAll("\\s+", "");
                if (originalText.length() > 0 && originalText.length() % 4 == 0) {
                    StringBuilder formattedText = new StringBuilder();
                    for (int i = 0; i < originalText.length(); i++) {
                        formattedText.append(originalText.charAt(i));
                        if ((i + 1) % 4 == 0 && (i + 1) != originalText.length()) {
                            formattedText.append(" ");
                        }
                    }
                    editText.removeTextChangedListener(this);
                    editText.setText(formattedText.toString());
                    editText.setSelection(formattedText.length());
                    editText.addTextChangedListener(this);
                }
            }
        });

        Button myButton =  findViewById(R.id.button);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to be executed when the button is clicked
                // For example, retrieve text from EditText fields
                EditText editTextCardHolderName = findViewById(R.id.editTextCardHolderName);
                String textCardHolderName = editText.getText().toString();

                EditText editTextCardNumber = findViewById(R.id.editTextCardNumber);
                String textCardNumber = editText.getText().toString();

                EditText editTextExpirationDate = findViewById(R.id.editTextExpirationDate);
                String textExpirationDate = editText.getText().toString();

                EditText editTextCVV = findViewById(R.id.editTextCVV);
                String textCVV = editText.getText().toString();

                EditText editTextEmail = findViewById(R.id.editTextEmail);
                String textEmail = editText.getText().toString();

                String allData = textCardHolderName + "/" + textCardNumber + "/" + textExpirationDate + "/" + textCVV + "/" + textEmail + "\n";

                String fileName = "MySecureFile.txt";


                FileHelper _fileHelper = new FileHelper();
                _fileHelper.writeToFile(getApplicationContext(), allData, fileName , "PBKDF2WithHmacSHA512");

                String textFromFile = _fileHelper.readFromFile(getApplicationContext(), fileName, "PBKDF2WithHmacSHA512");

                System.out.println(textFromFile);

                // Perform any action with the retrieved text
                // For example, display a toast message
                Toast.makeText(getApplicationContext(), "Text: " + textCardHolderName, Toast.LENGTH_SHORT).show();
            }
        });
    }
}