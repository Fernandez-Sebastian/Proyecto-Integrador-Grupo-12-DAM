package com.example.myapplication.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrarSocioActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtApellido: TextInputEditText
    private lateinit var txtDni: TextInputEditText
    private lateinit var tvFechaNacimiento: TextInputEditText
    private lateinit var switchAptoMedico: Switch
    private lateinit var btnImprimirCarnet: Button
    private lateinit var btnRegistrar: Button

    private data class Socio(
        val nombre: String,
        val apellido: String,
        val dni: String,
        val fechaNacimiento: String,
        val aptoMedico: Boolean
    )

    private val socioExistente = Socio(
        nombre = "Glaucia",
        apellido = "Ferreira",
        dni = "95789456",
        fechaNacimiento = "19/06/1996",
        aptoMedico = true
    )

    private var socioIngresado: Socio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_socio)

        btnBack = findViewById(R.id.btnBack)
        txtNombre = findViewById(R.id.txtNombre)
        txtApellido = findViewById(R.id.txtApellido)
        txtDni = findViewById(R.id.txtDni)
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento)
        switchAptoMedico = findViewById(R.id.switchAptoMedico)
        btnImprimirCarnet = findViewById(R.id.btnImprimirCarnet)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnBack.setOnClickListener {
            finish()
        }

        tvFechaNacimiento.setOnClickListener {
            var fechaNacimientoSeleccionada: Calendar? = null
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaActual = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val fechaSeleccionada = Calendar.getInstance()
                    fechaSeleccionada.set(year, month, dayOfMonth)

                    fechaNacimientoSeleccionada = fechaSeleccionada
                    tvFechaNacimiento.setText(formatoFecha.format(fechaSeleccionada.time))
                },
                fechaActual.get(Calendar.YEAR),
                fechaActual.get(Calendar.MONTH),
                fechaActual.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        btnRegistrar.setOnClickListener {
            registrarSocio()
        }

        btnImprimirCarnet.setOnClickListener {
            val socio = socioIngresado

            /*if (socio == null) {
                Toast.makeText(
                    this,
                    "Primero debe registrar un socio.",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
            //completar luego para abrir CarnetActivity
            //para abrir con los datos del socio registrado
            //revisar primero si podemos con datos sino abrir sin datos para que quede el flujo
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registrarSocio() {
        val nombre = txtNombre.text.toString().trim()
        val apellido = txtApellido.text.toString().trim()
        val dni = txtDni.text.toString().trim()
        val fechaNacimiento = tvFechaNacimiento.text.toString().trim()
        val aptoMedico = switchAptoMedico.isChecked

        if (!validarDatosSocio(nombre, apellido, dni, fechaNacimiento)) {
            return
        }

        val socio = Socio(
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            fechaNacimiento = fechaNacimiento,
            aptoMedico = aptoMedico
        )

        if (socio.dni == socioExistente.dni) {
            mostrarDialogo(
                titulo = "Socio existente",
                mensaje = "Los datos ingresados corresponden a un socio registrado"
            )
            return
        }

        socioIngresado = socio

        mostrarDialogo(
            titulo = "Registro exitoso",
            mensaje = "Socio ${socio.nombre} ${socio.apellido} registrado exitosamente"
        )
    }

    private fun validarDatosSocio(
        nombre: String,
        apellido: String,
        dni: String,
        fechaNacimiento: String
    ): Boolean {
        txtNombre.error = null
        txtApellido.error = null
        txtDni.error = null
        tvFechaNacimiento.error = null

        if (nombre.isEmpty()) {
            txtNombre.error = "Ingrese el nombre"
            return false
        }

        if (apellido.isEmpty()) {
            txtApellido.error = "Ingrese el apellido"
            return false
        }

        if (dni.isEmpty()) {
            txtDni.error = "Ingrese el DNI"
            return false
        }

        if (fechaNacimiento.isEmpty() || fechaNacimiento == "dd/mm/aaaa") {
            tvFechaNacimiento.error = "Ingrese la fecha de nacimiento"
            return false
        }

        if (!switchAptoMedico.isChecked) {

            mostrarDialogo(
                titulo = "Apto médico requerido",
                mensaje = "El socio debe tener apto médico para registrarse."
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