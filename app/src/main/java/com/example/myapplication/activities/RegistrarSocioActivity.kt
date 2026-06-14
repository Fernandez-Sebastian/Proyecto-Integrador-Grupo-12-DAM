package com.example.myapplication.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrarSocioActivity : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper

    private lateinit var btnBack: ImageButton
    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtApellido: TextInputEditText
    private lateinit var txtDni: TextInputEditText
    private lateinit var tvFechaNacimiento: TextInputEditText
    private lateinit var switchAptoMedico: Switch
    private lateinit var btnImprimirCarnet: Button
    private lateinit var btnRegistrar: Button

    private var fechaNacimientoSeleccionada: Calendar? = null
    private var socioRegistrado: Socio? = null

    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private data class Socio(
        val nombre: String,
        val apellido: String,
        val dni: String,
        val fechaNacimiento: String,
        val aptoMedico: Boolean
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_socio)

        dbHelper = SQLiteHelper(this)

        configurarHeader()
        inicializarControles()
        configurarEventos()
        configurarFooter()
    }

    private fun configurarHeader() {
        btnBack = findViewById(R.id.btnBack)

        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        tvHeaderTitle.text = "Registrar Socio"

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun inicializarControles() {
        txtNombre = findViewById(R.id.txtNombre)
        txtApellido = findViewById(R.id.txtApellido)
        txtDni = findViewById(R.id.txtDni)
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento)
        switchAptoMedico = findViewById(R.id.switchAptoMedico)
        btnImprimirCarnet = findViewById(R.id.btnImprimirCarnet)
        btnRegistrar = findViewById(R.id.btnRegistrar)
    }

    private fun configurarEventos() {
        tvFechaNacimiento.setOnClickListener {
            mostrarSelectorFecha()
        }

        btnRegistrar.setOnClickListener {
            registrarSocio()
        }

        btnImprimirCarnet.setOnClickListener {
            abrirCarnet()
        }
    }

    private fun configurarFooter() {
        FooterManager.setupFooter(
            activity = this,
            showWhiteBar = true,
            showHome = true,
            showSettings = true,
            showLogout = true
        )
    }

    private fun mostrarSelectorFecha() {
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

    private fun registrarSocio() {
        val nombre = txtNombre.text.toString().trim()
        val apellido = txtApellido.text.toString().trim()
        val dni = txtDni.text.toString().trim()
        val fechaNacimiento = tvFechaNacimiento.text.toString().trim()
        val aptoMedico = switchAptoMedico.isChecked

        //validamos los datos ingresados
        if (!validarDatosSocio(nombre, apellido, dni, fechaNacimiento)) {
            return
        }

        //verificamos si existe el socio ingresado por dni
        if (dbHelper.existeSocioPorDni(dni)) {
            mostrarDialogo(
                titulo = "Socio existente",
                mensaje = "Los datos ingresados corresponden a un socio registrado"
            )
            return
        }

        //insertamos el socio
        val resultado = dbHelper.insertarSocio(
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            fechaNacimiento = fechaNacimiento,
            aptoMedico = if (aptoMedico) 1 else 0,
            habilitado = 1
        )

        if (resultado == -1L) {
            mostrarDialogo(
                titulo = "Error",
                mensaje = "No se pudo registrar el socio."
            )
            return
        }

        socioRegistrado = Socio(
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            fechaNacimiento = fechaNacimiento,
            aptoMedico = aptoMedico
        )

        //Creamos la primera cuota para el socio registrado
        val idSocio = dbHelper.buscarIdSocioPorDni(dni)

        if (idSocio == null) {
            mostrarDialogo(
                titulo = "Error",
                mensaje = "No se encontró el socio registrado."
            )
            return
        }

        val resultadoCuota = dbHelper.insertarPrimeraCuotaSocio(idSocio)

        if (resultadoCuota == -1L) {
            mostrarDialogo(
                titulo = "Error",
                mensaje = "No se pudo generar la primera cuota del socio."
            )
            return
        }

        mostrarDialogo(
            titulo = "Registro exitoso",
            mensaje = "Socio $nombre $apellido registrado exitosamente"
        )
    }

    private fun validarDatosSocio(
        nombre: String,
        apellido: String,
        dni: String,
        fechaNacimiento: String
    ): Boolean {
        limpiarErrores()

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

        if (dni.length < 7 || dni.length > 8) {
            txtDni.error = "Ingrese un DNI válido"
            return false
        }

        if (fechaNacimiento.isEmpty() || fechaNacimiento == "dd/mm/aaaa") {
            tvFechaNacimiento.error = "Ingrese la fecha de nacimiento"
            return false
        }

        val fecha = fechaNacimientoSeleccionada ?: convertirFechaACalendar(fechaNacimiento)

        if (fecha == null) {
            tvFechaNacimiento.error = "Ingrese una fecha válida"
            return false
        }

        //calculamos la edad con la fecha ingresada y validamos que tenga entre 5 y 100 años
        val edad = calcularEdad(fecha)

        if (edad < 5 || edad > 100) {
            mostrarDialogo(
                titulo = "Fecha inválida",
                mensaje = "La edad debe estar comprendida entre 5 y 100 años."
            )
            return false
        }

        //validamos el apto medico requerido
        if (!switchAptoMedico.isChecked) {
            mostrarDialogo(
                titulo = "Apto médico requerido",
                mensaje = "El socio debe tener apto médico para registrarse."
            )
            return false
        }

        return true
    }

    private fun limpiarErrores() {
        txtNombre.error = null
        txtApellido.error = null
        txtDni.error = null
        tvFechaNacimiento.error = null
    }

    private fun abrirCarnet() {
        val socio = socioRegistrado

        //se habilita luego de registrar un socio
        if (socio == null) {
            mostrarDialogo(
                titulo = "Carnet no disponible",
                mensaje = "Primero debe registrar un socio."
            )
            return
        }

        //se busca el id del socio
        val idSocio = dbHelper.buscarIdSocioPorDni(socio.dni)

        if (idSocio == null) {
            mostrarDialogo(
                titulo = "Error",
                mensaje = "No se encontró el socio registrado."
            )
            return
        }

        //validamos si existe un carnet para el socio sino lo damos de alta
        if (!dbHelper.existeCarnetSocio(socio.dni)) {
            val resultado = dbHelper.crearCarnet(
                idSocio = idSocio,
                numero = socio.dni
            )

            if (resultado == -1L) {
                mostrarDialogo(
                    titulo = "Error",
                    mensaje = "No se pudo generar el carnet del socio."
                )
                return
            }
        }

        //abrimos el carnet del socio
        val intent = Intent(this, CarnetActivity::class.java).apply {
            putExtra("dni", socio.dni)
        }

        startActivity(intent)
    }

    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun convertirFechaACalendar(fecha: String): Calendar? {
        return try {
            val date = formatoFecha.parse(fecha) ?: return null

            Calendar.getInstance().apply {
                time = date
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun calcularEdad(fechaNacimiento: Calendar): Int {
        val hoy = Calendar.getInstance()

        var edad = hoy.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR)

        val mesActual = hoy.get(Calendar.MONTH)
        val diaActual = hoy.get(Calendar.DAY_OF_MONTH)

        val mesNacimiento = fechaNacimiento.get(Calendar.MONTH)
        val diaNacimiento = fechaNacimiento.get(Calendar.DAY_OF_MONTH)

        if (
            mesActual < mesNacimiento ||
            (mesActual == mesNacimiento && diaActual < diaNacimiento)
        ) {
            edad--
        }

        return edad
    }
}
