package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class LoginActivity : AppCompatActivity() {

    lateinit var helper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        helper = SQLiteHelper(this)

        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        btnIngresar.setOnClickListener {

            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validar campos vacíos
            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (helper.validarUsuario(usuario, password)) {

                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("usuario", usuario)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        btnSalir.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas cerrar sesión y salir de la aplicación?")
                .setPositiveButton("Sí") { _, _ ->
                    // Limpiar datos de sesión si los hay
                    val sharedPref = getSharedPreferences("sesion", MODE_PRIVATE)
                    sharedPref.edit().clear().apply()

                    // Cerrar la app
                    finishAffinity()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}