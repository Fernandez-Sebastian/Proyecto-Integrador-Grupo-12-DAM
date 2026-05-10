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

    private lateinit var contenedorVencimientos: LinearLayout
    private lateinit var filtroTodos: TextView
    private lateinit var filtroVencidos: TextView
    private lateinit var filtroAlDia: TextView
    private lateinit var btnBack: ImageButton

    private enum class EstadoVencimiento {
        VENCIDO,
        AL_DIA
    }

    private data class VencimientoSocio(
        val nombreCompleto: String,
        val dni: String,
        val estado: EstadoVencimiento
    )

    //Por ahora simulamos con 4 socios fijos y luego se traeran los socios de base de datos
    private val vencimientosSocios = listOf(
        VencimientoSocio("Glaucia Ferreira", "95789456", EstadoVencimiento.VENCIDO),
        VencimientoSocio("Andrea Maslucan", "95639789", EstadoVencimiento.AL_DIA),
        VencimientoSocio("Sebastián Fernández", "95471268", EstadoVencimiento.VENCIDO),
        VencimientoSocio("Ignacio Grosman", "95843258", EstadoVencimiento.AL_DIA)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vencimientos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar Header Unificado
        btnBack = findViewById(R.id.btnBack)
        val tvHeaderTitle = findViewById<TextView>(R.id.tvHeaderTitle)
        tvHeaderTitle?.text = "Vencimientos"
        btnBack.setOnClickListener {
            finish()
        }

        contenedorVencimientos = findViewById(R.id.contenedorVencimientos)
        filtroTodos = findViewById(R.id.filtroTodos)
        filtroVencidos = findViewById(R.id.filtroVencidos)
        filtroAlDia = findViewById(R.id.filtroAlDia)

        filtroTodos.setOnClickListener {
            cargarVencimientos(vencimientosSocios)
            seleccionarFiltro(filtroTodos)
        }

        filtroVencidos.setOnClickListener {
            val vencidos = vencimientosSocios.filter { it.estado == EstadoVencimiento.VENCIDO }
            cargarVencimientos(vencidos)
            seleccionarFiltro(filtroVencidos)
        }

        filtroAlDia.setOnClickListener {
            val alDia = vencimientosSocios.filter { it.estado == EstadoVencimiento.AL_DIA }
            cargarVencimientos(alDia)
            seleccionarFiltro(filtroAlDia)
        }

        cargarVencimientos(vencimientosSocios)
        seleccionarFiltro(filtroTodos)

        FooterManager.setupFooter(this, showWhiteBar = true, showHome = true, showSettings = true, showLogout = true)
    }

    private fun cargarVencimientos(listaSocios: List<VencimientoSocio>) {
        contenedorVencimientos.removeAllViews()
        for (socio in listaSocios) {
            val item = LayoutInflater.from(this).inflate(R.layout.item_vencimiento, contenedorVencimientos, false)
            val barraEstado = item.findViewById<View>(R.id.barraEstado)
            val tvNombreSocio = item.findViewById<TextView>(R.id.tvNombreSocio)
            val tvDniSocio = item.findViewById<TextView>(R.id.tvDniSocio)
            val tvEstadoVencimiento = item.findViewById<TextView>(R.id.tvEstadoVencimiento)

            tvNombreSocio.text = socio.nombreCompleto
            tvDniSocio.text = "DNI: ${socio.dni}"

            if (socio.estado == EstadoVencimiento.VENCIDO) {
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

    private fun seleccionarFiltro(filtroSeleccionado: TextView) {
        val filtros = listOf(filtroTodos, filtroVencidos, filtroAlDia)
        for (filtro in filtros) {
            val seleccionado = filtro == filtroSeleccionado
            filtro.backgroundTintList = ColorStateList.valueOf(Color.parseColor(if (seleccionado) "#4A7BD1" else "#FFFFFF"))
            filtro.setTextColor(Color.parseColor(if (seleccionado) "#FFFFFF" else "#000000"))
        }
    }
}