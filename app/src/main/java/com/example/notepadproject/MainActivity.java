package com.example.notepadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText inputLoginET;

    private EditText inputPassET;

    private Button registrationBtn;

    private Button authorizationBtn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(    // Дизайн Action bar'а
                new ColorDrawable(getResources().getColor(R.color.BackgroundElements)));


        // Инициализация
        init();

        // Обработчики нажатий
        setOnClickListeners();

    }


    // Метод Инициализации
    private void init() {
        inputLoginET = findViewById(R.id.inputLoginET);
        inputPassET = findViewById(R.id.inputPassET);
        registrationBtn = findViewById(R.id.registrationBtn);
        authorizationBtn = findViewById(R.id.authorizationBtn);
        mAuth = FirebaseAuth.getInstance();
    }

    // Метод обработчика нажатий
    private void setOnClickListeners() {
        // Обработчк регистрации
        registrationBtn.setOnClickListener((v -> {

                    // Получить почту и пароль из полей ввода
                    String email = inputLoginET.getText().toString();
                    String pass = inputPassET.getText().toString();
                    // Проверить, что email и пароль не пустые
                    if (!email.isEmpty() && !pass.isEmpty()) {
                        // Создать пользователя с помощью email и пароля
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener((it -> {
                            // Проверить успешность регистрации пользователя
                            if (it.isSuccessful()) {
                                // Если успешно, перейти на основное окно
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(intent);
                                saveData();   // Сохранить данные.
                                loadData();   // Загрузить данные.
                                finish();
                            } else {
                                Log.e("11", "ошибка");  // Вывод об ощибке в логи

                            }
                        }));
                    } else {
                        Toast.makeText(this, "Пустые поля не допускаются !!", Toast.LENGTH_SHORT).show(); // Вывод об ошибке
                    }
                })
        );

        // Обработчк авторизации
        authorizationBtn.setOnClickListener((v -> {
                    // Получить почту и пароль из полей ввода
                    String email = inputLoginET.getText().toString();
                    String pass = inputPassET.getText().toString();
                    // Проверить, что email и пароль не пустые.
                    if (!email.isEmpty() && !pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener((it -> {
                            // Проверить успешность авториззации пользователя
                            if (it.isSuccessful()) {
                                // Если успешно, перейти на основное окно
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(intent);

                                finish();
                            } else {
                                Log.e("11", "ошибка");

                            }
                        }));
                    } else {
                        Toast.makeText(this, "Пустые поля не допускаются !!", Toast.LENGTH_SHORT).show();   // Вывод об ошибке в логи
                    }
                })
        );

    }


    // Метод для сохранения ID пользователя в SharedPreferences
    private void saveData() {
        String userId = mAuth.getUid(); // Получаем ID пользователя из Firebase Authentication
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);  // Получаем доступ к файлу SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();  // Получаем редактор для внесения изменений
        editor.putString("userId", userId); // Записываем ID пользователя в Shared Preferences
        editor.apply(); // Применяем изменения.
    }

    // Метод для загрузки ID пользователя в SharedPreferences.
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE); // Получаем доступ к файлу SharedPreferences
        String userId = sharedPreferences.getString("userId", null); // Получаем ID пользователя из SharedPreferences
        Log.e("11", userId);  // Логируем ID пользователя для проверки
    }

    // Переопределенный метод onStart для проверки авторизации текущего пользователя
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {  // Проверяем, что текущий пользователь авторизован
            Intent intent = new Intent(MainActivity.this, SecondActivity.class); // Переход на основное окно
            startActivity(intent);
            finish(); // завершение текущего окна
        }
    }

}