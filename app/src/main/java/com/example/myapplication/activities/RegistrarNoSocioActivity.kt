package com.example.myapplication.activities

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.activities.RegistrarSocioActivity.Socio
import com.example.myapplication.utils.FooterManager
import com.google.android.material.textfield.TextInputEditText
import java.util.Date
import java.util.Locale

class RegistrarNoSocioActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etFechaNac: TextInputEditText
    private lateinit var switchApto: Switch
    private lateinit var btnRegistrar: Button

    private data class NoSocio(
        val nombre: String,
        val apellido: String,
        val dni: String,
        val fechaNacimiento: String,
        val aptoMedico: Boolean
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_no_socio)

        // Configurar Header
        btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        
        tvHeaderTitle?.text = "Registrar No Socio"
        btnBack?.setOnClickListener {
            finish()
        }

        etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        etApellido = findViewById<TextInputEditText>(R.id.etApellido)
        etDni = findViewById<TextInputEditText>(R.id.etDni)
        etFechaNac = findViewById<TextInputEditText>(R.id.etFecha)
        switchApto = findViewById<Switch>(R.id.switchApto)
        btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        //Registrar al No Socio
        btnRegistrar.setOnClickListener {
            RegistrarNoSocio()
        }

        //Elegir fecha en calendario
        val etFecha = findViewById<TextInputEditText>(R.id.etFecha)

        etFecha.setOnClickListener {

            val calendario = Calendar.getInstance()

            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, y, m, d ->
                    etFecha.setText("$d/${m + 1}/$y")
                },
                year,
                month,
                day
            )

            datePicker.show()
        }

        // --- LÓGICA DEL FOOTER ---
        FooterManager.setupFooter(
            activity = this,
            showWhiteBar = true,
            showHome = true,
            showSettings = true,
            showLogout = true
        )
    }

    private fun RegistrarNoSocio(){
        val noSocio = NoSocio(
            nombre = etNombre.text.toString(),
            apellido = etApellido.text.toString(),
            dni = etDni.text.toString(),
            fechaNacimiento = etFechaNac.text.toString(),
            aptoMedico = switchApto.isChecked
        )

        if (!validarDatosNoSocio(noSocio.nombre, noSocio.apellido, noSocio.dni,
                noSocio.fechaNacimiento)) {
            return
        }

        val tvAptoMedico = if (noSocio.aptoMedico) "Sí" else "No"

        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val dialogView = layoutInflater.inflate(R.layout.dialog_registro_exitoso, null)

        val tv = dialogView.findViewById<TextView>(R.id.tvRegistro)

        tv.text = """
            No Socio: ${noSocio.nombre} ${noSocio.apellido}
            DNI: ${noSocio.dni}
            F. Nac.: ${noSocio.fechaNacimiento}
            Apto médico: $tvAptoMedico
            Fecha: $fecha
            Hora: $hora
            """.trimIndent()

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()
    }

    private fun validarDatosNoSocio(
        nombre: String,
        apellido: String,
        dni: String,
        fechaNacimiento: String
    ): Boolean {
        etNombre.error = null
        etApellido.error = null
        etDni.error = null
        etFechaNac.error = null

        if (nombre.isEmpty()) {
            etNombre.error = "Ingrese el nombre"
            return false
        }
        if (apellido.isEmpty()) {
            etApellido.error = "Ingrese el apellido"
            return false
        }
        if (dni.isEmpty()) {
            etDni.error = "Ingrese el DNI"
            return false
        }
        if (fechaNacimiento.isEmpty() || fechaNacimiento == "dd/mm/aaaa") {
            etFechaNac.error = "Ingrese la fecha de nacimiento"
            return false
        }
        if (!switchApto.isChecked) {
            mostrarDialogo(
                titulo = "Apto médico requerido",
                mensaje = "El no socio debe tener apto médico para registrarse."
            )
            return false
        }
        //faltan validaciones de dni valido, edad valida, etc
        return true
    }

    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}