package id.ac.petra.library2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BorrowBooks extends AppCompatActivity {

    private EditText usernameField;
    private EditText nrpField;
    private EditText pilihBukuField;
    private EditText tglPeminjamanField;
    private EditText tglPengembalianField;
    private Button confirmButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_books);

        usernameField = findViewById(R.id.editTextNama3);
        nrpField = findViewById(R.id.editTextNrp2);
        pilihBukuField = findViewById(R.id.textViewPilihBuku);
        tglPeminjamanField = findViewById(R.id.editTextDate);
        tglPengembalianField = findViewById(R.id.editTextDate2);
        confirmButton = findViewById(R.id.confirmButton);

        tglPeminjamanField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tglPeminjamanField);
            }
        });

        tglPengembalianField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tglPengembalianField);
            }
        });



        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String peminjam = usernameField.getText().toString().trim();
                String nrp = nrpField.getText().toString().trim();
                String judul = pilihBukuField.getText().toString().trim();
                String tglpeminjaman = tglPeminjamanField.getText().toString().trim();
                String tglpengembalian = tglPengembalianField.getText().toString().trim();

                if (peminjam.isEmpty() || nrp.isEmpty()) {
                    Toast.makeText(BorrowBooks.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject bookingData = new JSONObject();
                    try {
                        bookingData.put("peminjam", peminjam);
                        bookingData.put("nrp", nrp);
                        bookingData.put("judul", judul);
                        bookingData.put("tglpeminjaman", tglpeminjaman);
                        bookingData.put("tglpengembalian", tglpengembalian);

                        new BookAsyncTask().execute(bookingData.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog and set the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(BorrowBooks.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        editText.setText(selectedDate);
                    }
                }, year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }



        private class BookAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String result = "";
                try {
                    String bookingData = params[0];
                    URL url = new URL("http://172.22.90.63:7000/borrowbook");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    OutputStream os = connection.getOutputStream();
                    os.write(bookingData.getBytes());
                    os.flush();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        result = responseBuilder.toString();
                    } else {
                        result = "Error: " + responseCode;
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "Error occurred while connecting to the server.";
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                Toast.makeText(BorrowBooks.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        }
}
