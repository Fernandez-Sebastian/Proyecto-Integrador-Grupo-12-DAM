package com.example.myapplication.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.utils.FooterManager

class VencimientosActivity : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper

    private lateinit var contenedorVencimientos: LinearLayout
    private lateinit var filtroTodos: TextView
    private lateinit var filtroVencidos: TextView
    private lateinit var filtroAlDia: TextView
    private lateinit var btnBack: ImageButton

    private var vencimientosSocios =
        mutableListOf<Triple<String, String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vencimientos)

        dbHelper = SQLiteHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configurarHeader()
        inicializarControles()
        cargarDatosDesdeBase()
        configurarFiltros()

        cargarVencimientos(vencimientosSocios)
        seleccionarFiltro(filtroTodos)

        FooterManager.setupFooter(
            this,
            showWhiteBar = true,
            showHome = true,
            showSettings = true,
            showLogout = true
        )
    }

    private fun configurarHeader() {
        btnBack = findViewById(R.id.btnBack)

        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        tvHeaderTitle.text = "Vencimientos"

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun inicializarControles() {
        contenedorVencimientos = findViewById(R.id.contenedorVencimientos)
        filtroTodos = findViewById(R.id.filtroTodos)
        filtroVencidos = findViewById(R.id.filtroVencidos)
        filtroAlDia = findViewById(R.id.filtroAlDia)
    }

    private fun cargarDatosDesdeBase() {
        vencimientosSocios =
            dbHelper.obtenerVencimientosSocios().toMutableList()
    }

    private fun configurarFiltros() {
        filtroTodos.setOnClickListener {
            cargarVencimientos(vencimientosSocios)
            seleccionarFiltro(filtroTodos)
        }

        filtroVencidos.setOnClickListener {
            val vencidos = vencimientosSocios.filter {
                it.third.equals("Vencido", ignoreCase = true)
            }

            cargarVencimientos(vencidos)
            seleccionarFiltro(filtroVencidos)
        }

        filtroAlDia.setOnClickListener {
            val alDia = vencimientosSocios.filter {
                it.third.equals("Al día", ignoreCase = true)
            }

            cargarVencimientos(alDia)
            seleccionarFiltro(filtroAlDia)
        }
    }

    private fun cargarVencimientos(
        listaSocios: List<Triple<String, String, String>>
    ) {
        contenedorVencimientos.removeAllViews()

        //si no tenemos vencimientos se muestra mensaje
        if (listaSocios.isEmpty()) {
            mostrarMensajeSinVencimientos()
            return
        }

        //cargamos y configuramos cada item_vencimiento con los datos
        for (socio in listaSocios) {
            val item = LayoutInflater.from(this)
                .inflate(
                    R.layout.item_vencimiento,
                    contenedorVencimientos,
                    false
                )

            val barraEstado = item.findViewById<View>(R.id.barraEstado)
            val tvNombreSocio = item.findViewById<TextView>(R.id.tvNombreSocio)
            val tvDniSocio = item.findViewById<TextView>(R.id.tvDniSocio)
            val tvEstadoVencimiento =
                item.findViewById<TextView>(R.id.tvEstadoVencimiento)

            tvNombreSocio.text = socio.first
            tvDniSocio.text = "DNI: ${socio.second}"

            if (socio.third.equals("Vencido", ignoreCase = true)) {
                tvEstadoVencimiento.text = "Vencido"
                tvEstadoVencimiento.setTextColor(Color.parseColor("#D32F2F"))
                barraEstado.setBackgroundColor(Color.parseColor("#D32F2F"))
            } else {
                tvEstadoVencimiento.text = "Al día"
                tvEstadoVencimiento.setTextColor(Color.parseColor("#2E7D32"))
                barraEstado.setBackgroundColor(Color.parseColor("#2E7D32"))
            }

            contenedorVencimientos.addView(item)
        }
    }

    private fun mostrarMensajeSinVencimientos() {
        val mensaje = TextView(this)
        mensaje.text = "No hay vencimientos para mostrar."
        mensaje.textSize = 16f
        mensaje.setTextColor(Color.parseColor("#666666"))
        mensaje.setPadding(16, 32, 16, 16)

        contenedorVencimientos.addView(mensaje)
    }

    private fun seleccionarFiltro(filtroSeleccionado: TextView) {
        val filtros = listOf(filtroTodos, filtroVencidos, filtroAlDia)

        for (filtro in filtros) {
            val seleccionado = filtro == filtroSeleccionado

            filtro.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(if (seleccionado) "#4A7BD1" else "#FFFFFF")
            )

            filtro.setTextColor(
                Color.parseColor(if (seleccionado) "#FFFFFF" else "#000000")
            )
        }
    }
}
