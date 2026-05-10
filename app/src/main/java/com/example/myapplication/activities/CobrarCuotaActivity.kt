package com.example.myapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import android.content.Intent

class CobrarCuotaActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobrar_cuota)

        // COMPONENTES
        val etDniSocio = findViewById<EditText>(R.id.etDniSocio)

        val btnBuscarSocio = findViewById<ImageButton>(R.id.btnBuscarSocio)

        val tvNombreSocio = findViewById<TextView>(R.id.tvNombreSocio)

        val tvDniSocio = findViewById<TextView>(R.id.tvDniSocio)

        val tvEstadoSocio = findViewById<TextView>(R.id.tvEstadoSocio)

        val tvMonto = findViewById<TextView>(R.id.tvMonto)

        val spMetodoPago = findViewById<Spinner>(R.id.spMetodoPago)

        val btnCobrar = findViewById<Button>(R.id.btnCobrar)

        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu)

        val btnVerComprobante = findViewById<Button>(R.id.btnVerComprobante)

        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        // OCULTO LOS BOTONES AL INICIO
        spMetodoPago.visibility = View.GONE
        btnCobrar.visibility = View.GONE

        // METODOS DE PAGO
        val metodosPago = arrayOf(
            "Seleccione",
            "Efectivo",
            "Tarjeta"
        )

        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            metodosPago
        ) {

            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                (view as TextView).setTextColor(getColor(R.color.texto_oscuro))

                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)

                val textView = view as TextView

                textView.setTextColor(getColor(R.color.texto_oscuro))
                textView.setBackgroundColor(getColor(R.color.blanco))

                return textView
            }
        }

        spMetodoPago.adapter = adapter

        // BUSCAR SOCIO

        btnBuscarSocio.setOnClickListener {
            // OCULTO LOS BOTONES DE COBRAR CUOTA Y METODO DE PAGO
            spMetodoPago.visibility = View.GONE
            btnCobrar.visibility = View.GONE

            val dni = etDniSocio.text.toString()

            if (dni.isEmpty()) {
                tvNombreSocio.text = "Ingrese DNI y presione buscar"
                tvDniSocio.text = ""
                tvEstadoSocio.text = ""
                tvMonto.text = "$ 00.00"

                tvNombreSocio.setTextColor(getColor(R.color.texto_oscuro))
                tvDniSocio.setTextColor(getColor(R.color.texto_oscuro))

                Toast.makeText(this,"Debe ingresar un DNI",Toast.LENGTH_SHORT).show()
            } else {
                /*
                    SOCIOS HARDCODEADOS
                    123456 -> Tiene deuda
                    7322 -> No tiene deuda
                */
                if (dni == "123456") {
                    // MUESTRO LOS BOTONES SOLO CUANDO HAY DEUDA
                    spMetodoPago.visibility = View.VISIBLE
                    btnCobrar.visibility = View.VISIBLE

                    // SOCIO CON DEUDA
                    tvNombreSocio.text = "Juan Pérez"
                    tvDniSocio.text = "DNI: 123456"
                    tvEstadoSocio.text = "Vencido"

                    tvNombreSocio.setTextColor(getColor(R.color.texto_oscuro))
                    tvDniSocio.setTextColor(getColor(R.color.texto_oscuro))
                    tvEstadoSocio.setTextColor(getColor(R.color.estado_rojo))

                    tvMonto.text = "$ 45.000"
                    tvMonto.setTextColor(getColor(R.color.texto_oscuro))

                } else if (dni == "7322") {

                    // SOCIO SIN DEUDA
                    tvNombreSocio.text = "María Gómez"
                    tvDniSocio.text = "DNI: 7322"
                    tvEstadoSocio.text = "Al día"

                    tvNombreSocio.setTextColor(getColor(R.color.texto_oscuro))
                    tvDniSocio.setTextColor(getColor(R.color.texto_oscuro))
                    tvEstadoSocio.setTextColor(getColor(R.color.estado_verde))

                    tvMonto.text = "$ 0"
                    tvMonto.setTextColor(getColor(R.color.texto_oscuro))

                    Toast.makeText(this,"El socio no posee deuda de cuotas",Toast.LENGTH_LONG).show()
                } else {
                    // SOCIO NO ENCONTRADO
                    tvNombreSocio.text = "Ingrese DNI y presione buscar"
                    tvDniSocio.text = ""
                    tvEstadoSocio.text = ""
                    tvMonto.text = "$ 00.00"

                    tvNombreSocio.setTextColor(getColor(R.color.texto_oscuro))
                    tvDniSocio.setTextColor(getColor(R.color.texto_oscuro))
                    tvMonto.setTextColor(getColor(R.color.texto_oscuro))

                    Toast.makeText(this,"No se encontró el socio. Debe darlo de alta como nuevo Socio",Toast.LENGTH_SHORT).show()
                }
            }
        }

        // BOTON COBRAR
        btnCobrar.setOnClickListener {

            val metodoSeleccionado = spMetodoPago.selectedItem.toString()

            if (metodoSeleccionado == "Seleccione") {
                Toast.makeText(this,"Seleccione un método de pago",Toast.LENGTH_SHORT).show()
            } else {
                spMetodoPago.visibility = View.GONE
                btnCobrar.visibility = View.GONE

                tvNombreSocio.text = "Juan Pérez"
                tvDniSocio.text = "DNI: 123456"
                tvEstadoSocio.text = "Al día"
                tvMonto.text = "$ 0"

                tvMonto.setTextColor(getColor(R.color.texto_oscuro))
                tvNombreSocio.setTextColor(getColor(R.color.texto_oscuro))
                tvDniSocio.setTextColor(getColor(R.color.texto_oscuro))
                tvEstadoSocio.setTextColor(getColor(R.color.estado_verde))

                Toast.makeText(this,"Cuota cobrada correctamente",Toast.LENGTH_SHORT).show()
                btnVerComprobante.visibility = View.VISIBLE
            }
        }

        // BOTON VER COMPROBANTE
        btnVerComprobante.setOnClickListener {
            val nombre = tvNombreSocio.text.toString()
            val dni = tvDniSocio.text.toString()
            val monto = "45.000"
            val metodo = spMetodoPago.selectedItem.toString()

            val intent = Intent(this, ComprobantePagoCuotaActivity::class.java)

            intent.putExtra("nombre", nombre)
            intent.putExtra("dni", dni)
            intent.putExtra("monto", monto)
            intent.putExtra("metodo", metodo)
            startActivity(intent)
        }

        // VOLVER AL MENU
        btnVolverMenu.setOnClickListener {
            finish()
        }
    }
}