package com.example.notepadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    //Меню навигации
    BottomNavigationView bottomNavigationView;
    // Кнопка в навигации

    private FirebaseAuth mAuth;
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(    // Дизайн Action bar'а
                new ColorDrawable(getResources().getColor(R.color.BackgroundElements)));

        // Инициализация
        init(); // метод инициализации

        // Меню навигации
        setUpBottomNavBar();

        setOnClickListeners();


    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btnLogOut = findViewById(R.id.btnLogOut);

        mAuth = FirebaseAuth.getInstance();
    }

    private void setOnClickListeners() {
        btnLogOut.setOnClickListener((v -> {
            mAuth.signOut(); // выход с аккаунта
            mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        // переходим на окно регистрации
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Закрываем текущую активность
                    }
                }
            });
        }));
    }


    // Метод навигации
    private void setUpBottomNavBar() {
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.miSettings); //  выбор кнопки
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Обработчик нажатий кнопок

                // Кнопка "Заметки"
                if (menuItem.getItemId() == R.id.miHome) {
                    startActivity(new Intent(SettingsActivity.this, SecondActivity.class));
                    overridePendingTransition(0, 0); // Убирает анимки
                    finish();
                }

                // Кнопка "Общие"
                if (menuItem.getItemId() == R.id.miGroup) {
                    Intent intent = new Intent(SettingsActivity.this, PublicNotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
                return true;

            }
        });


    }
}