package com.example.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.myapplication.R

class CobrarActividad : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobrar_actividad)

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

        //Seleccionar medio de pago
        val autoCompleteMedioPago = findViewById<AutoCompleteTextView>(R.id.seleccionarMedioDePago)

        val itemsMedioPago = listOf("Efectivo", "Tarjeta", "Mercado Pago")

        val adapterMedioPago = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            itemsMedioPago
        )

        autoCompleteMedioPago.setAdapter(adapterMedioPago)

        val medioDePagoSeleccionado = autoCompleteMedioPago.text.toString()
    }
}