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
import com.example.myapplication.utils.FooterManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CobrarCuotaActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var helper: SQLiteHelper
    private var idSocioActual: Int? = null
    private var idCuotaActual: Int? = null
    private var montoActual: Double = 45000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobrar_cuota)

        // Inicializar SQLiteHelper
        helper = SQLiteHelper(this)

        // Configurar Header
        btnBack = findViewById(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)

        tvHeaderTitle?.text = "Cobrar Cuota"
        btnBack.setOnClickListener {
            finish()
        }

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

        // OCULTO LOS BOTONES AL INICIO
        spMetodoPago.visibility = View.GONE
        btnCobrar.visibility = View.GONE
        btnVerComprobante.visibility = View.GONE

        // METODOS DE PAGO
        val metodosPago = arrayOf("Seleccione", "Efectivo", "Tarjeta", "Transferencia")
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, metodosPago) {
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
            spMetodoPago.visibility = View.GONE
            btnCobrar.visibility = View.GONE
            btnVerComprobante.visibility = View.GONE
            idSocioActual = null
            idCuotaActual = null

            val dni = etDniSocio.text.toString().trim()

            if (dni.isEmpty()) {
                limpiarDatosSocio()
                Toast.makeText(this, "Debe ingresar un DNI", Toast.LENGTH_SHORT).show()
            } else {
                // Buscar socio por DNI
                val idSocio = helper.buscarIdSocioPorDni(dni)

                if (idSocio == null) {
                    // Socio no encontrado
                    tvNombreSocio.text = "No encontrado"
                    tvDniSocio.text = ""
                    tvEstadoSocio.text = ""
                    tvMonto.text = "$ 00.00"
                    Toast.makeText(this, "No se encontró el socio. Debe darlo de alta como nuevo Socio", Toast.LENGTH_SHORT).show()
                } else {
                    // Socio encontrado, verificar su estado de cuota
                    idSocioActual = idSocio
                    val socioInfo = obtenerInfoSocio(idSocio)

                    if (socioInfo != null) {
                        tvNombreSocio.text = socioInfo
                        tvDniSocio.text = "DNI: $dni"

                        val estadoCuota = helper.obtenerEstadoCuota(idSocio)

                        if (estadoCuota.tieneDeuda) {
                            // Tiene deuda (Vencido o Próximo a vencer)
                            idCuotaActual = estadoCuota.idCuota
                            montoActual = estadoCuota.monto
                            tvMonto.text = String.format("$ %.0f", estadoCuota.monto)

                            when (estadoCuota.estado) {
                                "Vencido" -> {
                                    tvEstadoSocio.text = "Vencido"
                                    tvEstadoSocio.setTextColor(getColor(R.color.estado_rojo))
                                    spMetodoPago.visibility = View.VISIBLE
                                    btnCobrar.visibility = View.VISIBLE
                                }
                                "Próximo a vencer" -> {
                                    tvEstadoSocio.text = "Próximo a vencer (${estadoCuota.diasRestantes} días)"
                                    tvEstadoSocio.setTextColor(getColor(R.color.estado_naranja))
                                    spMetodoPago.visibility = View.VISIBLE
                                    btnCobrar.visibility = View.VISIBLE
                                    Toast.makeText(this, "La cuota vence en ${estadoCuota.diasRestantes} días", Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    tvEstadoSocio.text = "Al día"
                                    tvEstadoSocio.setTextColor(getColor(R.color.estado_verde))
                                    tvMonto.text = "$ 0"
                                    Toast.makeText(this, "El socio no posee deuda de cuotas", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            // No tiene deuda
                            tvEstadoSocio.text = "Al día"
                            tvEstadoSocio.setTextColor(getColor(R.color.estado_verde))
                            tvMonto.text = "$ 0"
                            Toast.makeText(this, "El socio no posee deuda de cuotas", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        // BOTON COBRAR
        btnCobrar.setOnClickListener {
            val metodoSeleccionado = spMetodoPago.selectedItem.toString()
            if (metodoSeleccionado == "Seleccione") {
                Toast.makeText(this, "Seleccione un método de pago", Toast.LENGTH_SHORT).show()
            } else if (idCuotaActual != null && idSocioActual != null) {
                // Registrar el pago
                val fechaPago = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
                val resultado = helper.registrarPagoCuota(idCuotaActual!!, fechaPago, metodoSeleccionado)

                if (resultado) {
                    spMetodoPago.visibility = View.GONE
                    btnCobrar.visibility = View.GONE
                    tvEstadoSocio.text = "Al día"
                    tvEstadoSocio.setTextColor(getColor(R.color.estado_verde))
                    tvMonto.text = "$ 0"
                    Toast.makeText(this, "Cuota cobrada correctamente", Toast.LENGTH_SHORT).show()
                    btnVerComprobante.visibility = View.VISIBLE

                    // Generar siguiente cuota solo si la cuota estaba vencida o al día
                    helper.generarSiguienteCuota(idSocioActual!!)
                } else {
                    Toast.makeText(this, "Error al procesar el pago", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnVerComprobante.setOnClickListener {
            val intent = Intent(this, ComprobantePagoCuotaActivity::class.java)
            intent.putExtra("nombre", tvNombreSocio.text.toString())
            intent.putExtra("dni", etDniSocio.text.toString().trim())
            intent.putExtra("monto", String.format("%.0f", montoActual))
            intent.putExtra("metodo", spMetodoPago.selectedItem.toString())
            intent.putExtra("cantidad_cuotas", "1")
            startActivity(intent)
        }

        btnVolverMenu.setOnClickListener { finish() }

        // --- LÓGICA DEL FOOTER ---
        FooterManager.setupFooter(this, showWhiteBar = true, showHome = true, showSettings = true, showLogout = true)
    }

    private fun limpiarDatosSocio() {
        val tvNombreSocio = findViewById<TextView>(R.id.tvNombreSocio)
        val tvDniSocio = findViewById<TextView>(R.id.tvDniSocio)
        val tvEstadoSocio = findViewById<TextView>(R.id.tvEstadoSocio)
        val tvMonto = findViewById<TextView>(R.id.tvMonto)

        tvNombreSocio.text = "Ingrese DNI y presione buscar"
        tvDniSocio.text = ""
        tvEstadoSocio.text = ""
        tvMonto.text = "$ 00.00"
    }

    private fun obtenerInfoSocio(idSocio: Int): String? {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT nombre, apellido FROM Socios WHERE id_socio = ?",
            arrayOf(idSocio.toString())
        )
        return if (cursor.moveToFirst()) {
            val nombreCompleto = "${cursor.getString(0)} ${cursor.getString(1)}"
            cursor.close()
            nombreCompleto
        } else {
            cursor.close()
            null
        }
    }
}