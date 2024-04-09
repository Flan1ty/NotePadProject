package com.example.notepadproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PublicNotesActivity extends AppCompatActivity implements NotesAdapter.ItemClickListener {

    //Меню навигации
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    NotesAdapter adapter;

    String userId;

    ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_notes);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(    // Дизайн Action bar'а
                new ColorDrawable(getResources().getColor(R.color.BackgroundElements)));

        // Инициализация.
        init();    // метод инициализации
        loadData();    // метод загрузки данных

        setUpBottomNavBar();    // метод навигации

        // список заметок
        setUpRecyclerView();

        // данные из БД
        getDataFromFirebase();

    }

    // Инициализация нижней панели навигации
    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    // Метод навигации
    private void setUpBottomNavBar() {
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.miGroup);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Обработчик нажатий кнопок

                // кнопка Home
                if (menuItem.getItemId() == R.id.miHome) {
                    Intent intent = new Intent(PublicNotesActivity.this, SecondActivity.class); // Открываем основное окно при нажатии
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);     // Убирает анимки
                    startActivity(intent);
                    finish();
                }
                // кнопка Settings
                if (menuItem.getItemId() == R.id.miSettings) {
                    Intent intent = new Intent(PublicNotesActivity.this, SettingsActivity.class); // Открываем окно настроек при нажатии
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);     // Убирает анимки
                    startActivity(intent);
                    finish();
                } else {

                }
                return true;

            }
        });
    }

    // Получение данных из Firebase
    public void getDataFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("public");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Получаем данные и добавляем их в список
                    Note note = dataSnapshot.getValue(Note.class);
                    notes.add(note);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadData() {
        // Загружаем данные из SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        Log.e("11", userId);
    }

    //добавление заметки
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Настройка меню действий
        getMenuInflater().inflate(R.menu.notes_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //добавление заметки
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Кнопка "добавить"
        if (id == R.id.miAdd) {
            // Открываем окно добавления при нажатии на кнопку "добавить"
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra("publicMode", true);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //список заметок
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.rcView);
        // Настройка RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PublicNotesActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NotesAdapter(getBaseContext(), notes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // Добавление элемента в RecyclerView
    private void addToRecyclerView(Note note) {
        adapter.addItem(note);
    }

    @Override
    public void onItemClick(View view, int position) {
        // Обработка нажатия на элемент RecyclerView
        Intent intent = new Intent(PublicNotesActivity.this, NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", notes.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}