package com.example.myapplication.activities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager

class CarnetActivity : AppCompatActivity() {

    private var socioEncontrado = false
    private lateinit var dbHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        // Inicializar el helper de la base de datos
        dbHelper = SQLiteHelper(this)

        // Configurar Header
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        
        tvHeaderTitle?.text = "Carnet de Socio"
        btnBack?.setOnClickListener {
            finish()
        }

        // Referencias de la UI para búsqueda
        val etBuscarDni = findViewById<EditText>(R.id.etBuscarDni)
        val ivLupa = findViewById<ImageView>(R.id.ivLupa)
        val tvMensajeBusqueda = findViewById<TextView>(R.id.tvMensajeBusqueda)
        val clContenidoSocio = findViewById<ConstraintLayout>(R.id.clContenidoSocio)
        val ivBarcode = findViewById<ImageView>(R.id.ivBarcode)
        val btnImprimir = findViewById<Button>(R.id.btnImprimir)

        // Referencias de los campos del carnet
        val tvNombreSocio = findViewById<TextView>(R.id.tvNombreSocio)
        val tvDniSocio = findViewById<TextView>(R.id.tvDniSocio)
        val tvVencimiento = findViewById<TextView>(R.id.tvVencimiento)

        // Estado inicial del botón imprimir - Inactivo
        btnImprimir.alpha = 0.5f

        // Función de búsqueda reutilizable
        fun realizarBusqueda(dni: String) {
            // Consultar la base de datos
            val datos = dbHelper.obtenerDatosCarnetPorDni(dni)
            
            if (datos != null) {
                Toast.makeText(this, getString(R.string.socio_encontrado), Toast.LENGTH_SHORT).show()
                
                // Actualizar estado y UI
                socioEncontrado = true
                btnImprimir.alpha = 1.0f
                
                // Presentar los datos en el carnet
                tvNombreSocio.text = datos["nombreCompleto"]
                tvDniSocio.text = "DNI: $dni"
                tvVencimiento.text = "Vence: ${datos["vencimiento"]}"
                
                // Mostrar carnet y ocultar mensaje inicial
                tvMensajeBusqueda.visibility = View.GONE
                clContenidoSocio.visibility = View.VISIBLE

                // Generar código de barras dinámico basado en el DNI
                ivBarcode.post {
                    if (ivBarcode.width > 0 && ivBarcode.height > 0) {
                        val barcodeBitmap = renderCode39Digits(dni, ivBarcode.width, ivBarcode.height)
                        ivBarcode.setImageBitmap(barcodeBitmap)
                    }
                }
                
            } else {
                Toast.makeText(this, getString(R.string.socio_inexistente), Toast.LENGTH_SHORT).show()
                
                // Resetear estado
                socioEncontrado = false
                btnImprimir.alpha = 0.5f
                
                // Mostrar mensaje de búsqueda inicial
                tvMensajeBusqueda.visibility = View.VISIBLE
                clContenidoSocio.visibility = View.GONE
            }
        }

        // --- 1. Recibir DNI desde el Registro de Socio ---
        val dniIntent = intent.getStringExtra("dni")
        if (!dniIntent.isNullOrEmpty()) {
            etBuscarDni.setText(dniIntent)
            realizarBusqueda(dniIntent)
        }

        // --- 2. Lógica de búsqueda manual mediante la lupa ---
        ivLupa.setOnClickListener {
            val dni = etBuscarDni.text.toString().trim()
            if (dni.isNotEmpty()) {
                realizarBusqueda(dni)
            } else {
                Toast.makeText(this, "Por favor, ingrese un DNI", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Imprimir Carnet con restricción de búsqueda
        btnImprimir.setOnClickListener {
            if (socioEncontrado) {
                Toast.makeText(this, getString(R.string.archivo_descargado), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay socio para mostrar, intente una nueva búsqueda", Toast.LENGTH_LONG).show()
            }
        }

        // Configuración de botones de la pantalla
        findViewById<Button>(R.id.btnVolverInicio).setOnClickListener {
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

    /**
     * Genera un Bitmap de código de barras con el número de socio/DNI proporcionado.
     */
    private fun renderCode39Digits(codNumerico: String, anchoImg: Int, altoImg: Int): Bitmap {
        val patronBarras = mapOf(
            '0' to "NNNWWNWNN", '1' to "WNNWNNNNW", '2' to "NNWWNNNNW",
            '3' to "WNWWNNNNN", '4' to "NNNWWNNNW", '5' to "WNNWWNNNN",
            '6' to "NNWWWNNNN", '7' to "NNNWNWNNW", '8' to "WNNNWNNNN",
            '9' to "NNWNWNNNN", '*' to "NWNNWNWNN"
        )

        val texto = "*$codNumerico*"
        val ratioWide = 3f 

        var totalUnidades = 0f
        for (char in texto) {
            val patron = patronBarras[char] ?: continue
            for (p in patron) {
                totalUnidades += if (p == 'N') 1f else ratioWide
            }
            totalUnidades += 1f // Espacio entre caracteres
        }
        totalUnidades -= 1f

        val unidadPixel = anchoImg / totalUnidades
        val bitmap = Bitmap.createBitmap(anchoImg, altoImg, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        canvas.drawColor(Color.WHITE)
        var posX = 0f
        paint.color = Color.BLACK

        for (char in texto) {
            val patron = patronBarras[char] ?: continue
            for (i in patron.indices) {
                val esBarra = i % 2 == 0
                val anchoBarra = if (patron[i] == 'N') unidadPixel else unidadPixel * ratioWide
                
                if (esBarra) {
                    canvas.drawRect(posX, 0f, posX + anchoBarra, altoImg - 25f, paint)
                }
                posX += anchoBarra
            }
            posX += unidadPixel 
        }

        // Numero DNI debajo del código de barras
        paint.textSize = 24f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(codNumerico, (anchoImg / 2).toFloat(), (altoImg - 5).toFloat(), paint)

        return bitmap
    }
}
