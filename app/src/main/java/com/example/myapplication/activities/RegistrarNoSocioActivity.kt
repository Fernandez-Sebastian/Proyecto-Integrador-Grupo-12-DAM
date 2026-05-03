package com.example.myapplication.activities

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Date
import java.util.Locale

class RegistrarNoSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_no_socio)

        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etApellido = findViewById<TextInputEditText>(R.id.etApellido)
        val etDni = findViewById<TextInputEditText>(R.id.etDni)
        val etFechaNac = findViewById<TextInputEditText>(R.id.etFecha)
        val switchApto = findViewById<Switch>(R.id.switchApto)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val dni = etDni.text.toString()
            val fechaNacimiento = etFechaNac.text.toString()

            val aptoMedico = if (switchApto.isChecked) "Sí" else "No"

            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            val nombreCompleto = "$nombre $apellido"


            val dialogView = layoutInflater.inflate(R.layout.dialog_registro_exitoso, null)

            val tv = dialogView.findViewById<TextView>(R.id.tvRegistro)

            tv.text = """
            No Socio: $nombreCompleto
            DNI: $dni
            Fecha: $fecha
            Hora: $hora
            F. nac.: $fechaNacimiento
            Apto médico: $aptoMedico
            """.trimIndent()

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            dialog.show()
        }

    }
}