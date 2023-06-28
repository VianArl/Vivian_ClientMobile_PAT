package id.ac.petra.library2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListBuku extends AppCompatActivity {

    private ListView listViewBuku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_buku);
        listViewBuku = findViewById(R.id.listViewBuku);

        new databuku().execute();
    }

    private class databuku extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result;
            try {
                URL url = new URL("http://172.22.90.63:7000/bookdata");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            List<String> booklist = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject book = jsonArray.getJSONObject(i);


                    String judul = book.getString("judul");
                    String author = book.getString("author");

                    String detailbuku = "Judul Buku: " + judul + "\n"
                            + "Author: " + author;
                    booklist.add(detailbuku);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListBuku.this,
                        android.R.layout.simple_list_item_1, booklist);
                listViewBuku.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(ListBuku.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
