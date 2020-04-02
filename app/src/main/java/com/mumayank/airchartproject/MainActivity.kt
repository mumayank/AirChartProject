package com.mumayank.airchartproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.main_activity.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bar.setOnClickListener { startActivity(Intent(this, ChartActivity::class.java).putExtra(ChartActivity.INTENT_EXTRA_CHART_TYPE, ChartActivity.ChartType.BAR.toString())) }
        horizontalBar.setOnClickListener { startActivity(Intent(this, ChartActivity::class.java).putExtra(ChartActivity.INTENT_EXTRA_CHART_TYPE, ChartActivity.ChartType.HORIZONTAL_BAR.toString())) }
        supplyJson.setOnClickListener {
            val editText = EditText(this)
            editText.maxHeight = 600
            var alertDialog: AlertDialog? = null
            val builder = AlertDialog.Builder(this)
                .setTitle("Paste your json object")
                .setMessage("A valid json object should contain information for only 1 bar chart as given in the air charts lib documentation")
                .setView(editText)
                .setPositiveButton("Submit") { _, _ ->
                    val string = editText.text.toString()
                    try {
                        val json = JSONObject(string)
                        startActivity(Intent(this, ChartActivity::class.java).putExtra(ChartActivity.INTENT_EXTRA_DATA, string))
                        alertDialog?.hide()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Invalid json + ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                .setNegativeButton("cancel") { _, _ ->
                    // do nothing
                }
            alertDialog = builder.create()
            alertDialog.show()
        }
    }
}
