package com.example.myapplication.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ComprobantePagoCuotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprobante_pago_cuota)

        // BOTÓN REGRESAR (del header.xml)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)

        tvHeaderTitle?.text = "Comprobante Pago de Cuota"
        btnBack?.setOnClickListener {
            finish()
        }

        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvDni = findViewById<TextView>(R.id.tvDni)
        val tvMonto = findViewById<TextView>(R.id.tvMonto)
        val tvMetodo = findViewById<TextView>(R.id.tvMetodo)
        val tvCantidadCuotas = findViewById<TextView>(R.id.tvCantidadCuotas)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)
        val tvHora = findViewById<TextView>(R.id.tvHora)
        val btnDescargar = findViewById<Button>(R.id.btnDescargar)
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu)

        // DATOS RECIBIDOS
        val nombre = intent.getStringExtra("nombre") ?: "---"
        val dni = intent.getStringExtra("dni") ?: "---"
        val monto = intent.getStringExtra("monto") ?: "0"
        val metodo = intent.getStringExtra("metodo") ?: "---"
        val cantidadCuotas = intent.getStringExtra("cantidad_cuotas") ?: "1"

        // Obtener fecha y hora actual
        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
        val horaActual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time)

        // SETEO DATOS
        tvNombre.text = "Socio: $nombre"
        tvDni.text = "DNI: $dni"
        tvMonto.text = "Monto: $$monto"
        tvMetodo.text = "Método: $metodo"

        // Mostrar cantidad de cuotas solo si es más de 1
        if (cantidadCuotas.toIntOrNull() ?: 1 > 1) {
            tvCantidadCuotas.text = "Cuotas pagadas: $cantidadCuotas"
            tvCantidadCuotas.visibility = android.view.View.VISIBLE
        } else {
            tvCantidadCuotas.visibility = android.view.View.GONE
        }

        tvFecha.text = "Fecha: $fechaActual"
        tvHora.text = "Hora: $horaActual"

        btnDescargar.setOnClickListener {
            Toast.makeText(this, "Comprobante descargado con éxito", Toast.LENGTH_SHORT).show()
        }

        btnVolverMenu.setOnClickListener {
            finish()
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