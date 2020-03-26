package com.mumayank.airchartproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.charts.bar.AirChartBarValueItem
import com.mumayank.airchart.util.AirChartUtil
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.delay
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bar.setOnClickListener { startActivity(Intent(this, ChartActivity::class.java).putExtra(ChartActivity.INTENT_EXTRA_CHART_TYPE, ChartActivity.ChartType.BAR.toString())) }
        horizontalBar.setOnClickListener { startActivity(Intent(this, ChartActivity::class.java).putExtra(ChartActivity.INTENT_EXTRA_CHART_TYPE, ChartActivity.ChartType.HORIZONTAL_BAR.toString())) }

    }
}
