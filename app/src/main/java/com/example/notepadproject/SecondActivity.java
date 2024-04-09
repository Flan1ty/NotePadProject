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

public class SecondActivity extends AppCompatActivity implements NotesAdapter.ItemClickListener {

    //Меню навигации
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    NotesAdapter adapter;

    String userId;

    ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(     // Дизайн Action bar'а
                new ColorDrawable(getResources().getColor(R.color.BackgroundElements)));

        // Инициализация
        init();

        loadData();

        // Меню навигации
        setUpBottomNavBar();

        // список заметок
        setUpRecyclerView();

        // данные из БД
        getDataFromFirebase();

    }

    // Метод инициализации
    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    // Метод навигации
    private void setUpBottomNavBar() {
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Обработчик нажатий кнопок

                // Кнопка Group
                if (menuItem.getItemId() == R.id.miGroup) {
                    Intent intent = new Intent(SecondActivity.this, PublicNotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
                // Кнопка Settings
                if (menuItem.getItemId() == R.id.miSettings) {
                    Intent intent = new Intent(SecondActivity.this, SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } else {

                }
                return true;

            }
        });
    }

    // Взятие данных из БД
    public void getDataFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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


    // Загрузка ID пользователя
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        Log.e("11", userId);
    }

    // Добавление заметки
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Добавление заметки
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.miAdd) {

            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra("publicMode", false);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // Список заметок
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.rcView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SecondActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NotesAdapter(getBaseContext(), notes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void addToRecyclerView(Note note) {
        adapter.addItem(note);
    }

    // переход на заметку
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(SecondActivity.this, NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", notes.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}



