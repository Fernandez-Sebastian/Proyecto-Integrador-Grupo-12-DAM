package com.example.myapplication.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Date
import java.util.Locale

class CobrarActividad : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper
    data class NoSocio(
        val dni: String,
        val nombre: String
    )

    private var resultadoActual: NoSocio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobrar_actividad)

        //Instancia a la BD
        dbHelper = SQLiteHelper(this)

        // Configurar Header
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        
        tvHeaderTitle?.text = "Cobrar actividad"
        btnBack?.setOnClickListener {
            finish()
        }

        //Buscar No Socio
        val inputDni = findViewById<TextInputEditText>(R.id.etBuscarDni)
        val textInputLayout = findViewById<TextInputLayout>(R.id.tilBuscarDni)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        textInputLayout.setEndIconOnClickListener {
            val dni = inputDni.text.toString()
            val resultado = dbHelper.buscarNoSocioPorDni(dni)

            if (resultado != null) {
                val (nombre, apellido) = resultado
                resultadoActual = NoSocio(dni, "$nombre $apellido")
                tvResultado.text = "$nombre $apellido\nDNI: $dni"
            } else {
                resultadoActual = null
                tvResultado.text = "No Socio no encontrado"
            }
        }


        //Seleccionar actividad
        val autoCompleteActividad = findViewById<AutoCompleteTextView>(R.id.seleccionarActividad)

        val actividades = dbHelper.obtenerActividades()
        val itemsActividad = actividades.map { it.first }
        val precios = actividades.associate { it.first to it.third }

        val adapterActividad = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            itemsActividad
        )

        autoCompleteActividad.setAdapter(adapterActividad)

        val tvPrecio = findViewById<TextView>(R.id.tvPrecio)

        autoCompleteActividad.setOnItemClickListener { parent, _, position, _ ->
            val actividad = parent.getItemAtPosition(position).toString()
            val precio = precios[actividad] ?: 0.0

            tvPrecio.text = "$ ${"%,.2f".format(precio)}"
        }

        //Seleccionar medio de pago
        val autoCompleteMedioPago = findViewById<AutoCompleteTextView>(R.id.seleccionarMedioDePago)

        val itemsMedioPago = listOf("Efectivo", "Tarjeta", "Mercado Pago")

        val adapterMedioPago = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            itemsMedioPago
        )

        autoCompleteMedioPago.setAdapter(adapterMedioPago)

        //Navegar a Registrar No Socio
        val btnRegistrarNoSocio = findViewById<Button>(R.id.btnIrRegistrarNoSocio)

        btnRegistrarNoSocio.setOnClickListener {

            val intent = Intent(this, RegistrarNoSocioActivity::class.java)

            startActivity(intent)
        }

        //Comprobante de pago

        val btnCobrar = findViewById<Button>(R.id.btnCobrar)

        btnCobrar.setOnClickListener {

            if (resultadoActual == null) {
                Toast.makeText(this, "Debe ingresar un No Socio válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultado = resultadoActual ?: return@setOnClickListener

            val nombre = resultado.nombre
            val dni = resultado.dni

            if (autoCompleteActividad.text.isNullOrBlank()) {
                Toast.makeText(this, "Debe seleccionar una actividad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val actividad = autoCompleteActividad.text.toString()
            val medioPago = autoCompleteMedioPago.text.toString()

            if (autoCompleteMedioPago.text.isNullOrBlank()) {
                Toast.makeText(this, "Debe seleccionar un medio de pago", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val precioTexto = tvPrecio.text.toString()
            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            val intent = Intent(this, ComprobanteActivity::class.java)

            intent.putExtra("nombre", nombre)
            intent.putExtra("dni", dni)
            intent.putExtra("actividad", actividad)
            intent.putExtra("medioPago", medioPago)
            intent.putExtra("precio", precioTexto)
            intent.putExtra("fecha", fecha)
            intent.putExtra("hora", hora)

            startActivity(intent)
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
}