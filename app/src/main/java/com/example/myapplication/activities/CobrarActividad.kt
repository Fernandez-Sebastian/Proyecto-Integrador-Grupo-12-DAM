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
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Date
import java.util.Locale

class CobrarActividad : AppCompatActivity() {

    data class NoSocio(
        val dni: String,
        val nombre: String
    )

    private val noSocios = listOf(
        NoSocio("22345678", "Juan Pérez"),
        NoSocio("30111222", "María Gómez"),
        NoSocio("33444555", "Carlos López")
    )

    private var resultadoActual: NoSocio? = null

    private fun buscarNoSocioPorDni(dni: String): NoSocio? {
        return noSocios.find { it.dni == dni }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobrar_actividad)

        //Volver al menú principal
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }


        //Buscar No Socio

        val inputDni = findViewById<TextInputEditText>(R.id.etBuscarDni)
        val textInputLayout = findViewById<TextInputLayout>(R.id.tilBuscarDni)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        textInputLayout.setEndIconOnClickListener {
            val dni = inputDni.text.toString()
            resultadoActual = buscarNoSocioPorDni(dni)

            if (resultadoActual != null) {
                tvResultado.text = "${resultadoActual!!.nombre}\nDNI: ${resultadoActual!!.dni}"
            } else {
                tvResultado.text = "No Socio no encontrado"
            }
        }


        //Seleccionar actividad
        val autoCompleteActividad = findViewById<AutoCompleteTextView>(R.id.seleccionarActividad)

        val itemsActividad = listOf("Fútbol", "Padle", "Yoga")

        val adapterActividad = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            itemsActividad
        )

        autoCompleteActividad.setAdapter(adapterActividad)

        val actividadSelecionada = autoCompleteActividad.text.toString()

        //Precios
        val precios = mapOf(
            "Fútbol" to 10000,
            "Padle" to 20000,
            "Yoga" to 15000
        )

        val tvPrecio = findViewById<TextView>(R.id.tvPrecio)

        autoCompleteActividad.setOnItemClickListener { parent, _, position, _ ->
            val actividad = parent.getItemAtPosition(position).toString()
            val precio = precios[actividad] ?: 0

            tvPrecio.text = "$ ${"%,d".format(precio)}"
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

            val resultado = resultadoActual ?: return@setOnClickListener

            val nombre = resultado.nombre
            val dni = resultado.dni
            val actividad = autoCompleteActividad.text.toString()
            val medioPago = autoCompleteMedioPago.text.toString()
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
    }
}