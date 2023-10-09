package com.thestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import db.Sqlite_OpenHelper;

public class MainActivity extends AppCompatActivity {
    private EditText inputUsuario;
    private EditText inputContraseña;
    private Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsuario = findViewById(R.id.inputUsuario);
        inputContraseña = findViewById(R.id.inputContraseña);
        btnAceptar = findViewById(R.id.btnAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputUsuario.getText().toString();
                String contrasena = inputContraseña.getText().toString();

                if (email.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean loginSuccessful = validarCredenciales(email, contrasena);
                    if (loginSuccessful) {
                        Intent intent = new Intent(MainActivity.this, ActividadPrincipal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });

    }
    private boolean validarCredenciales(String email, String contrasena) {
        Sqlite_OpenHelper dbHelper = new Sqlite_OpenHelper(getApplicationContext(), getResources().getString(R.string.DATABASE_NAME), null, 1);
        return dbHelper.login(email, contrasena);
    }
    }
