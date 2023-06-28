package id.ac.petra.library2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistIn extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText nrpEditText;
    private Button registInButton2;
    private Button cancelButton;


    private static final String REGISTIN_URL = "http://172.22.90.63:7000/registin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_in);

        usernameEditText = findViewById(R.id.editTextNama2);
        nrpEditText = findViewById(R.id.editTextNRP);
        passwordEditText = findViewById(R.id.editTextPassword2);
        registInButton2 = findViewById(R.id.buttonRegistIn2);
        cancelButton = findViewById(R.id.buttonCancel);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        registInButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registInUser();
            }
        });
    }

    private void registInUser() {
        String username = usernameEditText.getText().toString().trim();
        String nrp = nrpEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Perform your validations here
        if (username.isEmpty() || password.isEmpty() || nrp.isEmpty()) {
            Toast.makeText(RegistIn.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a JSON request body with the user data
        String requestBody = "{\"username\":\"" + username + "\",\"nrp\":\"" + nrp + "\",\"password\":\"" + password + "\"}";

        // Start the registration task
        new RegistInTask().execute(requestBody);
    }

    private class RegistInTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String requestBody = params[0];
            String response = "";

            try {
                URL url = new URL(REGISTIN_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Write the request body to the connection's output stream
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(requestBody);
                writer.flush();
                writer.close();

                // Get the response from the server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = "Registration successful";
                    Intent intent = new Intent(RegistIn.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    response = "Registration failed";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(RegistIn.this, result, Toast.LENGTH_SHORT).show();
        }
    }

}
