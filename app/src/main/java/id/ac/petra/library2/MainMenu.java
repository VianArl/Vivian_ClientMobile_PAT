package id.ac.petra.library2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button listBuku;
    private Button pinjamBuku;
    private Button logoutButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        listBuku = findViewById(R.id.listBukuButton);
        pinjamBuku = findViewById(R.id.pinjamBukuButton);
        logoutButton = findViewById(R.id.logoutButton);

        listBuku.setOnClickListener(this);
        pinjamBuku.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.listBukuButton) {
            listbuku();
        } else if (v.getId() == R.id.pinjamBukuButton) {
            pinjambuku();
        } else if (v.getId() == R.id.logoutButton) {
            logout();
        }
    }

    private void listbuku() {
        Intent intent = new Intent(this, ListBuku.class);
        startActivity(intent);
    }

    private void pinjambuku() {
        Intent intent = new Intent(this, BorrowBooks.class);
        startActivity(intent);
    }
    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
