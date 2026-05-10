package com.example.myapplication.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.myapplication.R
import com.example.myapplication.activities.LogoutActivity
import com.example.myapplication.activities.MenuActivity

object FooterManager {
    fun setupFooter(
        activity: Activity,
        showWhiteBar: Boolean = false,
        showHome: Boolean = false,
        showSettings: Boolean = false,
        showLogout: Boolean = false
    ) {
        val llCentro = activity.findViewById<LinearLayout>(R.id.ll_footer_centro) ?: return
        val ivHome = activity.findViewById<ImageView>(R.id.iv_home)
        val ivSettings = activity.findViewById<ImageView>(R.id.iv_settings)
        val ivLogout = activity.findViewById<ImageView>(R.id.iv_logout)

        llCentro.visibility = if (showWhiteBar) View.VISIBLE else View.GONE
        ivHome?.visibility = if (showHome) View.VISIBLE else View.GONE
        ivSettings?.visibility = if (showSettings) View.VISIBLE else View.GONE
        ivLogout?.visibility = if (showLogout) View.VISIBLE else View.GONE

        ivHome?.setOnClickListener {
            if (activity !is MenuActivity) {
                val intent = Intent(activity, MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity.startActivity(intent)
            }
        }

        ivLogout?.setOnClickListener {
            val intent = Intent(activity, LogoutActivity::class.java)
            activity.startActivity(intent)



        }
    }
}