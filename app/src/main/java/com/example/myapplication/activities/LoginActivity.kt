package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

            // Credenciales hardcodeadas
            // To do reemplazar los valores por la consulta a base de datos.
            val usuarioCorrecto = "admin"
            val passwordCorrecta = "1234"

            if (usuario == usuarioCorrecto && password == passwordCorrecta) {

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
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}