package com.thestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import db.Sqlite_OpenHelper;

public class Registro extends AppCompatActivity {

    private EditText inputNombreUsuario, inputEmail, inputContraseña, inputNombreTienda, inputDescripcionTienda;
    private Button btnRegistrar;
    private Sqlite_OpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper = new Sqlite_OpenHelper(this, getResources().getString(R.string.DATABASE_NAME), null, 1);
        inputNombreUsuario = findViewById(R.id.inputNombreUsuario);
        inputEmail = findViewById(R.id.inputEmail);
        inputContraseña = findViewById(R.id.inputContraseña);
        inputNombreTienda = findViewById(R.id.inputNombreTienda);
        inputDescripcionTienda = findViewById(R.id.inputDescripcionTienda);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = inputNombreUsuario.getText().toString();
                String email = inputEmail.getText().toString();
                String contrasena = inputContraseña.getText().toString();
                String nombreTienda = inputNombreTienda.getText().toString();
                String descripcionTienda = inputDescripcionTienda.getText().toString();

                if (TextUtils.isEmpty(nombreUsuario) || TextUtils.isEmpty(email) || TextUtils.isEmpty(contrasena) || TextUtils.isEmpty(nombreTienda) || TextUtils.isEmpty(descripcionTienda)) {
                    Toast.makeText(getApplicationContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else if (nombreUsuario.length() < 6) {
                    Toast.makeText(getApplicationContext(), "El nombre de usuario debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Ingrese un correo electrónico válido.", Toast.LENGTH_SHORT).show();
                } else if (nombreTienda.length() < 6) {
                    Toast.makeText(getApplicationContext(), "El nombre de la tienda debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                } else if (descripcionTienda.length() < 6) {
                    Toast.makeText(getApplicationContext(), "La descripción de la tienda debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                } else if (contrasena.length() < 6 || !containsLetterAndNumber(contrasena)) {
                    Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 6 caracteres y contener letras y números.", Toast.LENGTH_SHORT).show();
                } else {

                    long resultado = dbHelper.insertUser(nombreUsuario, email, nombreTienda, descripcionTienda, contrasena);
                    Toast.makeText(getApplicationContext(), "Res: "+resultado, Toast.LENGTH_SHORT).show();
                    if (resultado != -1) {
                        Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(getApplicationContext(), "Error al registrar el usuario. Inténtalo nuevamente.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean containsLetterAndNumber(String str) {
        boolean hasLetter = false;
        boolean hasNumber = false;
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }
        return hasLetter && hasNumber;
    }
}
