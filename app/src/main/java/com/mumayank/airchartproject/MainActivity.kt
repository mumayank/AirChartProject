package com.mumayank.airchartproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bar.setOnClickListener { startActivity(Intent(this, ChartActivity::class.java).putExtra(ChartActivity.INTENT_EXTRA_CHART_TYPE, ChartActivity.ChartType.BAR.toString())) }
        //horizontalBar.setOnClickListener { startActivity(Intent(this, ChartActivity::class.java).putExtra(ChartActivity.INTENT_EXTRA_CHART_TYPE, ChartActivity.ChartType.HORIZONTAL_BAR.toString())) }

    }
}
