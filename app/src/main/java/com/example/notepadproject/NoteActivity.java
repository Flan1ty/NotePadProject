package com.example.notepadproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

public class NoteActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etDescription;
    private ImageButton deleteNoteBtn;

    private DatabaseReference rootDatabaseRef;

    private String userId;

    private Note note = null;

    private boolean isPublicMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(    // Дизайн Action bar'а
                new ColorDrawable(getResources().getColor(R.color.BackgroundElements)));

        // Инициализация
        init();  // метод инициализации

        loadData();  // Загрузка ID пользователя

        setOnClickListeners();
    }

    // Метод инициализации взаимодействия с элементами экрана и базой данных
    private void init() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        deleteNoteBtn = findViewById(R.id.deleteNoteBtn);

        rootDatabaseRef = FirebaseDatabase.getInstance().getReference();

        // Получение данных из Intent
        Intent intent = this.getIntent();
        isPublicMode = intent.getBooleanExtra("publicMode", false);
        if (intent.hasExtra("value")) {
            Bundle bundle = intent.getExtras();
            note = (Note) bundle.getSerializable("value");

            etTitle.setText(note.getTitle());
            etDescription.setText(note.getDescription());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Создание меню для окна
        getMenuInflater().inflate(R.menu.note_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Получение идентификатора выбранного элемента меню.
        int id = item.getItemId();

        // Обработка выбора элемента меню miPermit
        if (id == R.id.miPermit) {
            // Проверка режима отображения (публичный или приватный)
            if (isPublicMode) {
                // проверяем, существует ли заметка
                if (note == null) {
                    // Создаем новую заметку и добавляем в базу данных
                    String noteId = UUID.randomUUID().toString();
                    Note note = new Note(
                            noteId,
                            etTitle.getText().toString(),
                            etDescription.getText().toString()
                    );

                    rootDatabaseRef.child("public").child(noteId).setValue(note);
                } else {
                    // Обновляем публичную заметку с введенными данными
                    note.setDescription(etDescription.getText().toString());
                    note.setTitle(etTitle.getText().toString());

                    rootDatabaseRef.child("public").child(note.getId()).setValue(note);

                }
            } else {
                // проверяем, существует ли заметка
                if (note == null) {
                    // Создаем новую приватную заметку и добавляем в базу данных
                    String noteId = UUID.randomUUID().toString();
                    Note note = new Note(
                            noteId,
                            etTitle.getText().toString(),
                            etDescription.getText().toString()
                    );

                    rootDatabaseRef.child(userId).child(noteId).setValue(note);
                } else {
                    // Обновляем существующую приватную заметку с введенными данными
                    note.setDescription(etDescription.getText().toString());
                    note.setTitle(etTitle.getText().toString());

                    rootDatabaseRef.child(userId).child(note.getId()).setValue(note);

                }
            }
            finish(); // Закрываем окно после сохранения заметки
        }
        return super.onOptionsItemSelected(item);

    }


    // Метод загрузки данных пользователя
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        Log.e("11", userId); // Вывод в лог ID пользователя
    }


    // Удаление заметки
    private void setOnClickListeners() {
        deleteNoteBtn.setOnClickListener(v -> {
            rootDatabaseRef.child(userId).child(note.getId()).removeValue();
            finish();
        });
    }

}
