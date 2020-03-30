package com.mumayank.airchartproject

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.mumayank.airchart.data_classes.AirChartAdditionalData
import com.mumayank.airchart.AirChart
import com.mumayank.airchart.charts.bar.AirChartBar
import com.mumayank.airchart.data_classes.AirChartValueItem
import com.mumayank.aircoroutine.AirCoroutine
import kotlinx.android.synthetic.main.chart_activity.*
import kotlinx.android.synthetic.main.chart_rv_item.view.*
import kotlinx.coroutines.delay
import mumayank.com.airrecyclerview.AirRv
import kotlin.random.Random

class ChartActivity : AppCompatActivity() {

    companion object {
        const val INTENT_EXTRA_CHART_TYPE = "INTENT_EXTRA_CHART_TYPE"
    }

    enum class ChartType {
        BAR,
        HORIZONTAL_BAR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_activity)

        val string = intent.getStringExtra(INTENT_EXTRA_CHART_TYPE) ?: ""

        when (ChartType.valueOf(string)) {
            ChartType.BAR -> {
                showBarCharts()
            }
            ChartType.HORIZONTAL_BAR -> {
                showHorizontalBarCharts()
            }
        }
    }

    class BarData(
        val title: String,
        val xLabels: ArrayList<String>,
        val yLeftItems: java.util.ArrayList<AirChartValueItem>
    )

    private fun showBarCharts() {
        showCharts(arrayListOf(

            /**
             * todo: POSITIVE VALUES
             */

            BarData(
                "No value",
                arrayListOf(),
                arrayListOf()
            ),
            BarData(
                "1 value",
                arrayListOf("val1"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f)))
            ),
            BarData(
                "2 values",
                arrayListOf<String>("val1", "val2"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f)))
            ),
            BarData(
                "3 values",
                arrayListOf<String>("val1", "val2", "val3"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, 9f)))
            ),
            BarData(
                "4 values",
                arrayListOf<String>("val1", "val2", "val3", "val4"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, 9f, 1f)))
            ),
            BarData(
                "8 values",
                arrayListOf<String>("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, 9f, 1f, 2f, 3f, 8f, 7.5f)))
            ),
            BarData(
                "16 values",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(19f,0f,0f,17f,23f,29f,2f,25f,15f,29f,5f,29f,17f,14f,17f,10f)))
            ),
            BarData(
                "32 values",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,20f,4f,4f,16f,8f,28f,17f,19f,12f,9f,12f,14f,2f,15f,24f,11f,5f,18f,6f,3f,26f,3f,22f,2f,20f,21f,14f,23f,4f,0f,19f)))
            ),
            BarData(
                "32 values - far apart",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,200f,4f,4f,16f,8f,280f,17f,19f,12f,9f,12f,14f,2f,150f,24f,11f,5f,18f,6f,3f,26f,3f,22f,2f,20f,21f,14f,23f,400f,0f,19f)))
            ),
            BarData(
                "50 values",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(4f,25f,15f,11f,13f,22f,27f,4f,8f,19f,3f,16f,11f,21f,23f,13f,22f,24f,27f,7f,19f,15f,26f,21f,0f,22f,13f,12f,0f,5f,19f,22f,24f,6f,7f,16f,3f,14f,17f,17f,5f,5f,12f,0f,20f,12f,10f,9f,21f,6f)))
            ),
            BarData(
                "100 values",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50","val51","val52","val53","val54","val55","val56","val57","val58","val59","val60","val61","val62","val63","val64","val65","val66","val67","val68","val69","val70","val71","val72","val73","val74","val75","val76","val77","val78","val79","val80","val81","val82","val83","val84","val85","val86","val87","val88","val89","val90","val91","val92","val93","val94","val95","val96","val97","val98","val99","val100"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(16f,7f,21f,13f,20f,19f,6f,10f,15f,21f,3f,22f,1f,6f,4f,16f,11f,11f,4f,8f,8f,14f,21f,20f,13f,15f,12f,28f,9f,26f,5f,1f,3f,28f,25f,19f,26f,15f,23f,12f,17f,16f,2f,16f,23f,18f,25f,17f,8f,17f,28f,3f,20f,26f,26f,10f,2f,17f,17f,10f,11f,9f,24f,4f,20f,24f,23f,18f,16f,25f,12f,0f,22f,23f,16f,5f,3f,1f,10f,14f,18f,12f,8f,21f,24f,26f,25f,17f,17f,16f,10f,29f,11f,19f,14f,11f,10f,17f,18f,24f)))
            ),
            BarData(
                "500 values",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50","val51","val52","val53","val54","val55","val56","val57","val58","val59","val60","val61","val62","val63","val64","val65","val66","val67","val68","val69","val70","val71","val72","val73","val74","val75","val76","val77","val78","val79","val80","val81","val82","val83","val84","val85","val86","val87","val88","val89","val90","val91","val92","val93","val94","val95","val96","val97","val98","val99","val100","val101","val102","val103","val104","val105","val106","val107","val108","val109","val110","val111","val112","val113","val114","val115","val116","val117","val118","val119","val120","val121","val122","val123","val124","val125","val126","val127","val128","val129","val130","val131","val132","val133","val134","val135","val136","val137","val138","val139","val140","val141","val142","val143","val144","val145","val146","val147","val148","val149","val150","val151","val152","val153","val154","val155","val156","val157","val158","val159","val160","val161","val162","val163","val164","val165","val166","val167","val168","val169","val170","val171","val172","val173","val174","val175","val176","val177","val178","val179","val180","val181","val182","val183","val184","val185","val186","val187","val188","val189","val190","val191","val192","val193","val194","val195","val196","val197","val198","val199","val200","val201","val202","val203","val204","val205","val206","val207","val208","val209","val210","val211","val212","val213","val214","val215","val216","val217","val218","val219","val220","val221","val222","val223","val224","val225","val226","val227","val228","val229","val230","val231","val232","val233","val234","val235","val236","val237","val238","val239","val240","val241","val242","val243","val244","val245","val246","val247","val248","val249","val250","val251","val252","val253","val254","val255","val256","val257","val258","val259","val260","val261","val262","val263","val264","val265","val266","val267","val268","val269","val270","val271","val272","val273","val274","val275","val276","val277","val278","val279","val280","val281","val282","val283","val284","val285","val286","val287","val288","val289","val290","val291","val292","val293","val294","val295","val296","val297","val298","val299","val300","val301","val302","val303","val304","val305","val306","val307","val308","val309","val310","val311","val312","val313","val314","val315","val316","val317","val318","val319","val320","val321","val322","val323","val324","val325","val326","val327","val328","val329","val330","val331","val332","val333","val334","val335","val336","val337","val338","val339","val340","val341","val342","val343","val344","val345","val346","val347","val348","val349","val350","val351","val352","val353","val354","val355","val356","val357","val358","val359","val360","val361","val362","val363","val364","val365","val366","val367","val368","val369","val370","val371","val372","val373","val374","val375","val376","val377","val378","val379","val380","val381","val382","val383","val384","val385","val386","val387","val388","val389","val390","val391","val392","val393","val394","val395","val396","val397","val398","val399","val400","val401","val402","val403","val404","val405","val406","val407","val408","val409","val410","val411","val412","val413","val414","val415","val416","val417","val418","val419","val420","val421","val422","val423","val424","val425","val426","val427","val428","val429","val430","val431","val432","val433","val434","val435","val436","val437","val438","val439","val440","val441","val442","val443","val444","val445","val446","val447","val448","val449","val450","val451","val452","val453","val454","val455","val456","val457","val458","val459","val460","val461","val462","val463","val464","val465","val466","val467","val468","val469","val470","val471","val472","val473","val474","val475","val476","val477","val478","val479","val480","val481","val482","val483","val484","val485","val486","val487","val488","val489","val490","val491","val492","val493","val494","val495","val496","val497","val498","val499","val500"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(26f,0f,3f,16f,25f,0f,15f,3f,23f,11f,5f,8f,7f,21f,11f,29f,13f,14f,24f,11f,19f,12f,5f,12f,15f,22f,24f,4f,6f,21f,2f,0f,12f,27f,12f,9f,2f,27f,7f,19f,18f,6f,21f,6f,26f,12f,19f,6f,4f,29f,20f,29f,22f,25f,23f,4f,15f,29f,17f,22f,14f,17f,20f,20f,28f,2f,19f,3f,5f,29f,27f,29f,25f,7f,6f,2f,25f,7f,27f,12f,21f,20f,11f,17f,10f,22f,18f,14f,18f,15f,16f,5f,14f,24f,18f,22f,4f,6f,8f,4f,15f,16f,17f,8f,18f,7f,13f,5f,28f,22f,5f,8f,18f,6f,12f,29f,22f,4f,6f,22f,5f,24f,26f,22f,7f,22f,15f,10f,1f,6f,10f,4f,8f,28f,4f,29f,6f,3f,12f,7f,27f,3f,19f,19f,29f,19f,8f,3f,23f,27f,7f,29f,8f,13f,25f,21f,24f,17f,3f,24f,7f,21f,28f,11f,22f,6f,6f,8f,27f,26f,16f,26f,2f,8f,20f,28f,24f,7f,12f,23f,17f,8f,29f,8f,17f,5f,11f,26f,9f,25f,9f,21f,21f,5f,13f,20f,3f,12f,15f,12f,24f,10f,2f,28f,21f,19f,16f,28f,19f,28f,2f,26f,14f,26f,0f,16f,7f,2f,21f,25f,24f,26f,26f,27f,24f,1f,20f,18f,13f,9f,21f,18f,22f,12f,23f,29f,28f,5f,4f,12f,19f,21f,10f,28f,11f,2f,10f,10f,7f,25f,11f,11f,12f,25f,4f,13f,10f,24f,22f,27f,12f,15f,4f,11f,13f,22f,18f,2f,17f,13f,22f,29f,6f,12f,29f,14f,6f,24f,19f,24f,15f,23f,2f,28f,11f,10f,1f,6f,14f,28f,21f,4f,24f,11f,5f,4f,1f,3f,8f,7f,28f,11f,0f,17f,3f,21f,26f,3f,28f,6f,3f,5f,24f,3f,4f,11f,25f,21f,25f,5f,26f,14f,20f,14f,3f,18f,0f,18f,11f,20f,22f,2f,23f,17f,6f,17f,3f,21f,20f,8f,3f,20f,20f,14f,17f,13f,0f,6f,13f,17f,28f,22f,16f,18f,26f,11f,21f,29f,4f,6f,18f,5f,1f,23f,18f,8f,12f,2f,26f,9f,6f,22f,23f,5f,3f,26f,4f,24f,26f,27f,24f,24f,10f,22f,4f,23f,14f,6f,2f,20f,19f,20f,6f,14f,20f,16f,22f,25f,20f,23f,10f,22f,2f,15f,15f,19f,20f,6f,25f,27f,11f,21f,20f,2f,27f,7f,20f,10f,10f,21f,6f,15f,5f,11f,24f,17f,19f,4f,29f,6f,28f,16f,26f,9f,28f,20f,2f,2f,26f,20f,9f,24f,4f,16f,15f,2f,16f,11f,20f,24f,29f,25f,8f,7f,1f,8f,28f,28f,4f,13f,23f,0f,25f,26f,13f,2f,17f,19f,2f,15f,13f,15f,23f,26f,22f,26f,3f,18f,24f,2f,3f,8f,29f,15f,18f,16f,15f,7f,5f,7f,12f,7f,15f,24f,23f,15f,9f,7f,10f,4f)))
            ),
            BarData(
                "1000 values",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50","val51","val52","val53","val54","val55","val56","val57","val58","val59","val60","val61","val62","val63","val64","val65","val66","val67","val68","val69","val70","val71","val72","val73","val74","val75","val76","val77","val78","val79","val80","val81","val82","val83","val84","val85","val86","val87","val88","val89","val90","val91","val92","val93","val94","val95","val96","val97","val98","val99","val100","val101","val102","val103","val104","val105","val106","val107","val108","val109","val110","val111","val112","val113","val114","val115","val116","val117","val118","val119","val120","val121","val122","val123","val124","val125","val126","val127","val128","val129","val130","val131","val132","val133","val134","val135","val136","val137","val138","val139","val140","val141","val142","val143","val144","val145","val146","val147","val148","val149","val150","val151","val152","val153","val154","val155","val156","val157","val158","val159","val160","val161","val162","val163","val164","val165","val166","val167","val168","val169","val170","val171","val172","val173","val174","val175","val176","val177","val178","val179","val180","val181","val182","val183","val184","val185","val186","val187","val188","val189","val190","val191","val192","val193","val194","val195","val196","val197","val198","val199","val200","val201","val202","val203","val204","val205","val206","val207","val208","val209","val210","val211","val212","val213","val214","val215","val216","val217","val218","val219","val220","val221","val222","val223","val224","val225","val226","val227","val228","val229","val230","val231","val232","val233","val234","val235","val236","val237","val238","val239","val240","val241","val242","val243","val244","val245","val246","val247","val248","val249","val250","val251","val252","val253","val254","val255","val256","val257","val258","val259","val260","val261","val262","val263","val264","val265","val266","val267","val268","val269","val270","val271","val272","val273","val274","val275","val276","val277","val278","val279","val280","val281","val282","val283","val284","val285","val286","val287","val288","val289","val290","val291","val292","val293","val294","val295","val296","val297","val298","val299","val300","val301","val302","val303","val304","val305","val306","val307","val308","val309","val310","val311","val312","val313","val314","val315","val316","val317","val318","val319","val320","val321","val322","val323","val324","val325","val326","val327","val328","val329","val330","val331","val332","val333","val334","val335","val336","val337","val338","val339","val340","val341","val342","val343","val344","val345","val346","val347","val348","val349","val350","val351","val352","val353","val354","val355","val356","val357","val358","val359","val360","val361","val362","val363","val364","val365","val366","val367","val368","val369","val370","val371","val372","val373","val374","val375","val376","val377","val378","val379","val380","val381","val382","val383","val384","val385","val386","val387","val388","val389","val390","val391","val392","val393","val394","val395","val396","val397","val398","val399","val400","val401","val402","val403","val404","val405","val406","val407","val408","val409","val410","val411","val412","val413","val414","val415","val416","val417","val418","val419","val420","val421","val422","val423","val424","val425","val426","val427","val428","val429","val430","val431","val432","val433","val434","val435","val436","val437","val438","val439","val440","val441","val442","val443","val444","val445","val446","val447","val448","val449","val450","val451","val452","val453","val454","val455","val456","val457","val458","val459","val460","val461","val462","val463","val464","val465","val466","val467","val468","val469","val470","val471","val472","val473","val474","val475","val476","val477","val478","val479","val480","val481","val482","val483","val484","val485","val486","val487","val488","val489","val490","val491","val492","val493","val494","val495","val496","val497","val498","val499","val500","val501","val502","val503","val504","val505","val506","val507","val508","val509","val510","val511","val512","val513","val514","val515","val516","val517","val518","val519","val520","val521","val522","val523","val524","val525","val526","val527","val528","val529","val530","val531","val532","val533","val534","val535","val536","val537","val538","val539","val540","val541","val542","val543","val544","val545","val546","val547","val548","val549","val550","val551","val552","val553","val554","val555","val556","val557","val558","val559","val560","val561","val562","val563","val564","val565","val566","val567","val568","val569","val570","val571","val572","val573","val574","val575","val576","val577","val578","val579","val580","val581","val582","val583","val584","val585","val586","val587","val588","val589","val590","val591","val592","val593","val594","val595","val596","val597","val598","val599","val600","val601","val602","val603","val604","val605","val606","val607","val608","val609","val610","val611","val612","val613","val614","val615","val616","val617","val618","val619","val620","val621","val622","val623","val624","val625","val626","val627","val628","val629","val630","val631","val632","val633","val634","val635","val636","val637","val638","val639","val640","val641","val642","val643","val644","val645","val646","val647","val648","val649","val650","val651","val652","val653","val654","val655","val656","val657","val658","val659","val660","val661","val662","val663","val664","val665","val666","val667","val668","val669","val670","val671","val672","val673","val674","val675","val676","val677","val678","val679","val680","val681","val682","val683","val684","val685","val686","val687","val688","val689","val690","val691","val692","val693","val694","val695","val696","val697","val698","val699","val700","val701","val702","val703","val704","val705","val706","val707","val708","val709","val710","val711","val712","val713","val714","val715","val716","val717","val718","val719","val720","val721","val722","val723","val724","val725","val726","val727","val728","val729","val730","val731","val732","val733","val734","val735","val736","val737","val738","val739","val740","val741","val742","val743","val744","val745","val746","val747","val748","val749","val750","val751","val752","val753","val754","val755","val756","val757","val758","val759","val760","val761","val762","val763","val764","val765","val766","val767","val768","val769","val770","val771","val772","val773","val774","val775","val776","val777","val778","val779","val780","val781","val782","val783","val784","val785","val786","val787","val788","val789","val790","val791","val792","val793","val794","val795","val796","val797","val798","val799","val800","val801","val802","val803","val804","val805","val806","val807","val808","val809","val810","val811","val812","val813","val814","val815","val816","val817","val818","val819","val820","val821","val822","val823","val824","val825","val826","val827","val828","val829","val830","val831","val832","val833","val834","val835","val836","val837","val838","val839","val840","val841","val842","val843","val844","val845","val846","val847","val848","val849","val850","val851","val852","val853","val854","val855","val856","val857","val858","val859","val860","val861","val862","val863","val864","val865","val866","val867","val868","val869","val870","val871","val872","val873","val874","val875","val876","val877","val878","val879","val880","val881","val882","val883","val884","val885","val886","val887","val888","val889","val890","val891","val892","val893","val894","val895","val896","val897","val898","val899","val900","val901","val902","val903","val904","val905","val906","val907","val908","val909","val910","val911","val912","val913","val914","val915","val916","val917","val918","val919","val920","val921","val922","val923","val924","val925","val926","val927","val928","val929","val930","val931","val932","val933","val934","val935","val936","val937","val938","val939","val940","val941","val942","val943","val944","val945","val946","val947","val948","val949","val950","val951","val952","val953","val954","val955","val956","val957","val958","val959","val960","val961","val962","val963","val964","val965","val966","val967","val968","val969","val970","val971","val972","val973","val974","val975","val976","val977","val978","val979","val980","val981","val982","val983","val984","val985","val986","val987","val988","val989","val990","val991","val992","val993","val994","val995","val996","val997","val998","val999","val1000"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(29f,6f,28f,9f,9f,9f,5f,19f,15f,6f,5f,11f,18f,14f,17f,26f,0f,4f,4f,5f,18f,22f,5f,10f,12f,3f,29f,13f,19f,9f,26f,26f,2f,17f,25f,26f,28f,18f,21f,29f,20f,17f,22f,14f,8f,13f,2f,0f,13f,22f,19f,2f,19f,21f,27f,6f,12f,13f,5f,24f,24f,1f,27f,1f,15f,15f,29f,10f,20f,16f,21f,22f,18f,27f,22f,5f,21f,13f,17f,24f,28f,22f,10f,8f,4f,4f,15f,13f,29f,18f,28f,27f,10f,18f,0f,24f,10f,28f,15f,15f,22f,20f,28f,6f,5f,26f,1f,3f,23f,23f,3f,22f,11f,28f,24f,24f,22f,7f,5f,4f,13f,22f,12f,28f,14f,27f,18f,20f,16f,2f,23f,26f,9f,11f,2f,14f,14f,17f,2f,5f,16f,6f,4f,24f,2f,6f,6f,17f,4f,6f,26f,15f,17f,20f,16f,22f,18f,2f,16f,10f,18f,18f,15f,29f,3f,1f,21f,10f,24f,6f,9f,11f,1f,16f,26f,28f,29f,16f,28f,11f,29f,11f,13f,18f,16f,14f,9f,1f,24f,14f,8f,21f,12f,24f,25f,25f,27f,23f,12f,16f,3f,17f,10f,23f,7f,6f,16f,2f,21f,27f,4f,10f,11f,8f,25f,1f,26f,24f,7f,3f,12f,22f,26f,3f,24f,12f,9f,5f,17f,4f,18f,23f,23f,19f,21f,16f,1f,7f,12f,1f,19f,12f,20f,8f,0f,18f,7f,14f,7f,29f,13f,13f,0f,16f,3f,23f,25f,24f,7f,21f,15f,26f,6f,22f,26f,22f,14f,22f,14f,26f,16f,29f,7f,17f,6f,24f,13f,28f,16f,26f,18f,7f,10f,23f,20f,29f,2f,25f,17f,23f,27f,26f,8f,7f,22f,1f,29f,20f,7f,25f,10f,1f,19f,12f,27f,10f,7f,9f,25f,6f,29f,11f,15f,16f,26f,20f,27f,6f,24f,11f,28f,22f,17f,15f,11f,10f,29f,11f,19f,21f,8f,10f,9f,3f,1f,10f,20f,19f,24f,25f,10f,5f,23f,9f,6f,9f,23f,26f,1f,9f,16f,15f,29f,0f,9f,21f,16f,1f,5f,16f,10f,20f,13f,23f,15f,5f,1f,29f,17f,4f,5f,0f,16f,11f,25f,10f,15f,14f,19f,8f,11f,25f,27f,21f,29f,15f,14f,27f,28f,26f,14f,25f,22f,15f,14f,19f,3f,21f,2f,22f,20f,16f,13f,20f,11f,25f,18f,12f,16f,17f,24f,9f,8f,23f,20f,12f,20f,6f,8f,6f,26f,25f,29f,24f,18f,12f,1f,22f,18f,14f,19f,19f,25f,24f,17f,4f,17f,16f,11f,11f,9f,27f,27f,7f,12f,7f,7f,2f,18f,12f,12f,17f,28f,12f,12f,14f,2f,24f,4f,1f,3f,7f,16f,22f,2f,17f,3f,3f,28f,13f,17f,0f,26f,0f,3f,1f,14f,20f,12f,21f,24f,27f,14f,8f,23f,11f,27f,0f,14f,27f,11f,11f,10f,16f,13f,3f,6f,7f,2f,13f,25f,18f,19f,1f,22f,12f,0f,20f,17f,3f,10f,25f,28f,11f,3f,6f,15f,18f,7f,6f,10f,15f,18f,18f,15f,10f,29f,2f,8f,0f,26f,8f,2f,8f,3f,20f,12f,5f,17f,1f,23f,18f,3f,20f,21f,2f,3f,6f,19f,0f,7f,12f,10f,11f,13f,19f,14f,20f,13f,23f,2f,18f,11f,25f,12f,22f,6f,10f,9f,12f,11f,5f,28f,26f,18f,1f,18f,24f,4f,26f,28f,6f,6f,7f,16f,17f,11f,3f,29f,29f,10f,11f,20f,10f,10f,16f,4f,26f,12f,0f,2f,29f,22f,9f,13f,24f,17f,2f,11f,20f,25f,23f,11f,7f,9f,20f,8f,9f,12f,9f,14f,11f,19f,0f,17f,19f,1f,10f,0f,9f,19f,22f,28f,22f,7f,23f,7f,18f,19f,23f,26f,17f,24f,11f,7f,22f,7f,15f,12f,20f,23f,6f,22f,4f,0f,6f,27f,12f,9f,0f,11f,6f,10f,1f,15f,24f,12f,28f,24f,7f,12f,20f,23f,9f,8f,27f,15f,23f,1f,25f,2f,22f,23f,13f,25f,10f,5f,21f,12f,9f,18f,0f,10f,20f,21f,3f,10f,9f,3f,21f,21f,26f,17f,13f,11f,17f,21f,7f,8f,14f,5f,24f,26f,7f,10f,9f,26f,16f,17f,1f,5f,26f,18f,13f,23f,7f,28f,4f,9f,1f,28f,25f,12f,20f,21f,25f,4f,10f,9f,21f,29f,21f,28f,25f,10f,3f,8f,2f,26f,10f,3f,9f,27f,7f,27f,26f,2f,27f,0f,3f,12f,15f,2f,25f,19f,23f,1f,4f,28f,19f,12f,4f,10f,11f,6f,4f,29f,21f,25f,7f,11f,16f,2f,9f,24f,12f,19f,2f,8f,17f,12f,22f,12f,29f,25f,29f,14f,29f,26f,25f,23f,0f,19f,0f,27f,16f,12f,14f,13f,3f,4f,17f,2f,1f,12f,5f,6f,10f,7f,24f,22f,9f,5f,26f,27f,9f,18f,20f,5f,22f,17f,7f,29f,3f,14f,22f,7f,6f,20f,18f,18f,23f,28f,2f,9f,20f,24f,7f,22f,20f,29f,9f,10f,25f,22f,0f,0f,2f,19f,7f,16f,3f,9f,1f,28f,24f,10f,12f,4f,22f,7f,12f,3f,8f,15f,29f,18f,6f,18f,11f,5f,12f,14f,4f,14f,11f,6f,19f,9f,24f,24f,27f,8f,16f,25f,8f,19f,16f,17f,6f,9f,15f,11f,2f,21f,4f,21f,24f,17f,29f,13f,4f,25f,26f,14f,7f,11f,6f,2f,18f,4f,17f,10f,5f,18f,3f,18f,26f,14f,9f,5f,18f,25f,29f,29f,7f,20f,26f,13f,11f,4f,26f,11f,8f,16f,6f,13f,25f,24f,27f,10f,27f,8f,4f,25f,14f,3f,27f,7f,18f,5f,3f,13f,11f,12f,7f,11f,15f,18f,10f,18f,21f,13f,16f,13f,17f,7f,2f,2f,19f,6f,19f,10f,1f,20f,19f,18f,22f,13f,17f,11f,10f,4f,5f,7f,15f,15f,12f,16f,6f)))
            ),

            /**
             * todo: NEGATIVE VALUES
             */

            BarData(
                "1 value - negative",
                arrayListOf<String>("val1"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f)))
            ),
            BarData(
                "2 values - negative",
                arrayListOf<String>("val1", "val2"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, -8f)))
            ),
            BarData(
                "3 values - negative",
                arrayListOf<String>("val1", "val2", "val3"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, -9f)))
            ),
            BarData(
                "4 values - negative",
                arrayListOf<String>("val1", "val2", "val3", "val4"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, -1f, 1f)))
            ),
            BarData(
                "8 values - negative",
                arrayListOf<String>("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f, -8f, -9f, -1f, -2f, -3f, -8f, -7.5f)))
            ),
            BarData(
                "32 values - far apart - negative",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,-200f,4f,4f,16f,8f,280f,17f,19f,12f,9f,12f,14f,2f,150f,24f,11f,5f,18f,6f,3f,26f,3f,22f,2f,20f,21f,-14f,23f,400f,0f,-19f)))
            ),

            /**
             * todo: GROUPED POSITIVE
             */

            BarData(
                "1 grouped",
                arrayListOf<String>("val1"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f)), AirChartValueItem("legend2", arrayListOf(8f)))
            ),
            BarData(
                "2 grouped",
                arrayListOf<String>("val1", "val2"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f)), AirChartValueItem("legend2", arrayListOf(8f, 16f)))
            ),
            BarData(
                "some grouped",
                arrayListOf<String>("val1", "val2", "val3", "val4"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 9f, 4f, 0f)), AirChartValueItem("legend2", arrayListOf(8f, 9f, 10f, 9f)), AirChartValueItem("legend3", arrayListOf(8f, 5f, 3f, 1f)), AirChartValueItem("legend4", arrayListOf(8f, 9.1f, 1f, 1f)))
            ),
            BarData(
                "some grouped - far apart",
                arrayListOf<String>("val1", "val2", "val3", "val4"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 200f, 4f, 0f)), AirChartValueItem("legend2", arrayListOf(8f, 9f, 10f, 9f)), AirChartValueItem("legend3", arrayListOf(8f, 5f, 3f, 1f)), AirChartValueItem("legend4", arrayListOf(8f, 9.1f, 1f, 1f)))
            ),
            BarData(
                "many grouped",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,20f,4f,4f,16f,8f,28f,17f,19f,12f,9f,12f,14f,2f,15f,24f,11f,5f,18f,6f,3f,26f,3f,22f,2f,20f,21f,14f,23f,4f,0f,19f)), AirChartValueItem("legend2", arrayListOf(4f,25f,15f,11f,13f,22f,27f,4f,8f,19f,3f,16f,11f,21f,23f,13f,22f,24f,27f,7f,19f,15f,26f,21f,0f,22f,13f,12f,0f,5f,19f)), AirChartValueItem("legend3", arrayListOf(16f,7f,21f,13f,20f,19f,6f,10f,15f,21f,3f,22f,1f,6f,4f,16f,11f,11f,4f,8f,8f,14f,21f,20f,13f,15f,12f,28f,9f,26f,5f,1f)), AirChartValueItem("legend4", arrayListOf(26f,0f,3f,16f,25f,0f,15f,3f,23f,11f,5f,8f,7f,21f,11f,29f,13f,14f,24f,11f,19f,12f,5f,12f,15f,22f,24f,4f,6f,21f,2f,0f)))
            ),

            /**
             * todo: GROUPED NEGATIVE
             */

            BarData(
                "1 grouped - negative",
                arrayListOf<String>("val1"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f)), AirChartValueItem("legend2", arrayListOf(8f)))
            ),
            BarData(
                "2 grouped - negative",
                arrayListOf<String>("val1", "val2"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, -8f)), AirChartValueItem("legend2", arrayListOf(-8f, 16f)))
            ),
            BarData(
                "some grouped - negative",
                arrayListOf<String>("val1", "val2", "val3", "val4"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f, -9f, -4f, 0f)), AirChartValueItem("legend2", arrayListOf(-8f, 9f, 10f, 9f)), AirChartValueItem("legend3", arrayListOf(-8f, 5f, 3f, 1f)), AirChartValueItem("legend4", arrayListOf(-8f, -9.1f, 1f, 1f)))
            ),
            BarData(
                "some grouped - negative - far apart",
                arrayListOf<String>("val1", "val2", "val3", "val4"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f, -200f, -4f, 0f)), AirChartValueItem("legend2", arrayListOf(-8f, 200f, 10f, 9f)), AirChartValueItem("legend3", arrayListOf(-8f, 5f, 3f, 1f)), AirChartValueItem("legend4", arrayListOf(-8f, -9.1f, 1f, 1f)))
            ),
            BarData(
                "many grouped - negative",
                arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16"),
                arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,-20f,4f,4f,16f,8f,28f,17f,19f,12f,9f,12f,14f,2f,15f,24f,11f,5f,18f,6f,3f,26f,3f,22f,2f,20f,21f,14f,23f,4f,0f,19f)), AirChartValueItem("legend2", arrayListOf(4f,-25f,15f,11f,13f,22f,27f,4f,8f,19f,3f,16f,11f,21f,23f,13f,22f,24f,27f,7f,19f,15f,26f,21f,0f,22f,13f,12f,0f,5f,19f)), AirChartValueItem("legend3", arrayListOf(16f,7f,21f,13f,20f,19f,6f,10f,15f,21f,3f,22f,1f,6f,4f,16f,11f,11f,4f,8f,8f,14f,21f,20f,13f,15f,12f,28f,9f,26f,5f,1f)), AirChartValueItem("legend4", arrayListOf(26f,0f,3f,16f,25f,0f,15f,3f,23f,11f,5f,8f,7f,21f,11f,29f,13f,14f,24f,11f,19f,12f,5f,12f,15f,22f,24f,4f,6f,21f,2f,0f)))
            )
        ))
    }

    private fun showCharts(barDatas: ArrayList<BarData>) {

        AirRv(object: AirRv.Callback {

            override fun getAppContext(): Context? {
                return this@ChartActivity
            }

            override fun getBindView(
                viewHolder: RecyclerView.ViewHolder,
                viewType: Int,
                position: Int
            ) {
                val customViewHolder = viewHolder as CustomViewHolder
                val barData = barDatas[position]
                showBarChartsInternal(
                    customViewHolder.chartHolder,
                    barData.title,
                    barData.xLabels,
                    barData.yLeftItems
                )
            }

            override fun getEmptyView(): View? {
                return null
            }

            override fun getLayoutManager(appContext: Context?): RecyclerView.LayoutManager? {
                return LinearLayoutManager(appContext)
            }

            override fun getRvHolderViewGroup(): ViewGroup? {
                return rvHolder
            }

            override fun getSize(): Int? {
                return barDatas.size
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return CustomViewHolder(view)
            }

            override fun getViewLayoutId(viewType: Int): Int? {
                return R.layout.chart_rv_item
            }

            override fun getViewType(position: Int): Int? {
                return 0
            }

        })



/*


        // positive


        showBarChartsInternal("1 value", chart2, fun ():ArrayList<String> {
            return arrayListOf<String>("val1")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f)))
        })

        showBarChartsInternal("2 values", chart3, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f)))
        })

        showBarChartsInternal("3 values", chart4, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2", "val3")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, 9f)))
        })

        showBarChartsInternal("4 values", chart5, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2", "val3", "val4")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, 9f, 1f)))
        })

        showBarChartsInternal("8 values", chart6, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, 9f, 1f, 2f, 3f, 8f, 7.5f)))
        })

        showBarChartsInternal("16 values", chart7, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(19f,0f,0f,17f,23f,29f,2f,25f,15f,29f,5f,29f,17f,14f,17f,10f)))
        })

        showBarChartsInternal("32 values", chart8, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,20f,4f,4f,16f,8f,28f,17f,19f,12f,9f,12f,14f,2f,15f,24f,11f,5f,18f,6f,3f,26f,3f,22f,2f,20f,21f,14f,23f,4f,0f,19f)))
        })

        showBarChartsInternal("50 values", chart9, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(4f,25f,15f,11f,13f,22f,27f,4f,8f,19f,3f,16f,11f,21f,23f,13f,22f,24f,27f,7f,19f,15f,26f,21f,0f,22f,13f,12f,0f,5f,19f,22f,24f,6f,7f,16f,3f,14f,17f,17f,5f,5f,12f,0f,20f,12f,10f,9f,21f,6f)))
        })

        showBarChartsInternal("100 values", chart10, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50","val51","val52","val53","val54","val55","val56","val57","val58","val59","val60","val61","val62","val63","val64","val65","val66","val67","val68","val69","val70","val71","val72","val73","val74","val75","val76","val77","val78","val79","val80","val81","val82","val83","val84","val85","val86","val87","val88","val89","val90","val91","val92","val93","val94","val95","val96","val97","val98","val99","val100")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(16f,7f,21f,13f,20f,19f,6f,10f,15f,21f,3f,22f,1f,6f,4f,16f,11f,11f,4f,8f,8f,14f,21f,20f,13f,15f,12f,28f,9f,26f,5f,1f,3f,28f,25f,19f,26f,15f,23f,12f,17f,16f,2f,16f,23f,18f,25f,17f,8f,17f,28f,3f,20f,26f,26f,10f,2f,17f,17f,10f,11f,9f,24f,4f,20f,24f,23f,18f,16f,25f,12f,0f,22f,23f,16f,5f,3f,1f,10f,14f,18f,12f,8f,21f,24f,26f,25f,17f,17f,16f,10f,29f,11f,19f,14f,11f,10f,17f,18f,24f)))
        })

        showBarChartsInternal("500 values", chart11, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50","val51","val52","val53","val54","val55","val56","val57","val58","val59","val60","val61","val62","val63","val64","val65","val66","val67","val68","val69","val70","val71","val72","val73","val74","val75","val76","val77","val78","val79","val80","val81","val82","val83","val84","val85","val86","val87","val88","val89","val90","val91","val92","val93","val94","val95","val96","val97","val98","val99","val100","val101","val102","val103","val104","val105","val106","val107","val108","val109","val110","val111","val112","val113","val114","val115","val116","val117","val118","val119","val120","val121","val122","val123","val124","val125","val126","val127","val128","val129","val130","val131","val132","val133","val134","val135","val136","val137","val138","val139","val140","val141","val142","val143","val144","val145","val146","val147","val148","val149","val150","val151","val152","val153","val154","val155","val156","val157","val158","val159","val160","val161","val162","val163","val164","val165","val166","val167","val168","val169","val170","val171","val172","val173","val174","val175","val176","val177","val178","val179","val180","val181","val182","val183","val184","val185","val186","val187","val188","val189","val190","val191","val192","val193","val194","val195","val196","val197","val198","val199","val200","val201","val202","val203","val204","val205","val206","val207","val208","val209","val210","val211","val212","val213","val214","val215","val216","val217","val218","val219","val220","val221","val222","val223","val224","val225","val226","val227","val228","val229","val230","val231","val232","val233","val234","val235","val236","val237","val238","val239","val240","val241","val242","val243","val244","val245","val246","val247","val248","val249","val250","val251","val252","val253","val254","val255","val256","val257","val258","val259","val260","val261","val262","val263","val264","val265","val266","val267","val268","val269","val270","val271","val272","val273","val274","val275","val276","val277","val278","val279","val280","val281","val282","val283","val284","val285","val286","val287","val288","val289","val290","val291","val292","val293","val294","val295","val296","val297","val298","val299","val300","val301","val302","val303","val304","val305","val306","val307","val308","val309","val310","val311","val312","val313","val314","val315","val316","val317","val318","val319","val320","val321","val322","val323","val324","val325","val326","val327","val328","val329","val330","val331","val332","val333","val334","val335","val336","val337","val338","val339","val340","val341","val342","val343","val344","val345","val346","val347","val348","val349","val350","val351","val352","val353","val354","val355","val356","val357","val358","val359","val360","val361","val362","val363","val364","val365","val366","val367","val368","val369","val370","val371","val372","val373","val374","val375","val376","val377","val378","val379","val380","val381","val382","val383","val384","val385","val386","val387","val388","val389","val390","val391","val392","val393","val394","val395","val396","val397","val398","val399","val400","val401","val402","val403","val404","val405","val406","val407","val408","val409","val410","val411","val412","val413","val414","val415","val416","val417","val418","val419","val420","val421","val422","val423","val424","val425","val426","val427","val428","val429","val430","val431","val432","val433","val434","val435","val436","val437","val438","val439","val440","val441","val442","val443","val444","val445","val446","val447","val448","val449","val450","val451","val452","val453","val454","val455","val456","val457","val458","val459","val460","val461","val462","val463","val464","val465","val466","val467","val468","val469","val470","val471","val472","val473","val474","val475","val476","val477","val478","val479","val480","val481","val482","val483","val484","val485","val486","val487","val488","val489","val490","val491","val492","val493","val494","val495","val496","val497","val498","val499","val500")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(26f,0f,3f,16f,25f,0f,15f,3f,23f,11f,5f,8f,7f,21f,11f,29f,13f,14f,24f,11f,19f,12f,5f,12f,15f,22f,24f,4f,6f,21f,2f,0f,12f,27f,12f,9f,2f,27f,7f,19f,18f,6f,21f,6f,26f,12f,19f,6f,4f,29f,20f,29f,22f,25f,23f,4f,15f,29f,17f,22f,14f,17f,20f,20f,28f,2f,19f,3f,5f,29f,27f,29f,25f,7f,6f,2f,25f,7f,27f,12f,21f,20f,11f,17f,10f,22f,18f,14f,18f,15f,16f,5f,14f,24f,18f,22f,4f,6f,8f,4f,15f,16f,17f,8f,18f,7f,13f,5f,28f,22f,5f,8f,18f,6f,12f,29f,22f,4f,6f,22f,5f,24f,26f,22f,7f,22f,15f,10f,1f,6f,10f,4f,8f,28f,4f,29f,6f,3f,12f,7f,27f,3f,19f,19f,29f,19f,8f,3f,23f,27f,7f,29f,8f,13f,25f,21f,24f,17f,3f,24f,7f,21f,28f,11f,22f,6f,6f,8f,27f,26f,16f,26f,2f,8f,20f,28f,24f,7f,12f,23f,17f,8f,29f,8f,17f,5f,11f,26f,9f,25f,9f,21f,21f,5f,13f,20f,3f,12f,15f,12f,24f,10f,2f,28f,21f,19f,16f,28f,19f,28f,2f,26f,14f,26f,0f,16f,7f,2f,21f,25f,24f,26f,26f,27f,24f,1f,20f,18f,13f,9f,21f,18f,22f,12f,23f,29f,28f,5f,4f,12f,19f,21f,10f,28f,11f,2f,10f,10f,7f,25f,11f,11f,12f,25f,4f,13f,10f,24f,22f,27f,12f,15f,4f,11f,13f,22f,18f,2f,17f,13f,22f,29f,6f,12f,29f,14f,6f,24f,19f,24f,15f,23f,2f,28f,11f,10f,1f,6f,14f,28f,21f,4f,24f,11f,5f,4f,1f,3f,8f,7f,28f,11f,0f,17f,3f,21f,26f,3f,28f,6f,3f,5f,24f,3f,4f,11f,25f,21f,25f,5f,26f,14f,20f,14f,3f,18f,0f,18f,11f,20f,22f,2f,23f,17f,6f,17f,3f,21f,20f,8f,3f,20f,20f,14f,17f,13f,0f,6f,13f,17f,28f,22f,16f,18f,26f,11f,21f,29f,4f,6f,18f,5f,1f,23f,18f,8f,12f,2f,26f,9f,6f,22f,23f,5f,3f,26f,4f,24f,26f,27f,24f,24f,10f,22f,4f,23f,14f,6f,2f,20f,19f,20f,6f,14f,20f,16f,22f,25f,20f,23f,10f,22f,2f,15f,15f,19f,20f,6f,25f,27f,11f,21f,20f,2f,27f,7f,20f,10f,10f,21f,6f,15f,5f,11f,24f,17f,19f,4f,29f,6f,28f,16f,26f,9f,28f,20f,2f,2f,26f,20f,9f,24f,4f,16f,15f,2f,16f,11f,20f,24f,29f,25f,8f,7f,1f,8f,28f,28f,4f,13f,23f,0f,25f,26f,13f,2f,17f,19f,2f,15f,13f,15f,23f,26f,22f,26f,3f,18f,24f,2f,3f,8f,29f,15f,18f,16f,15f,7f,5f,7f,12f,7f,15f,24f,23f,15f,9f,7f,10f,4f)))
        })

        showBarChartsInternal("1000 values", chart12, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32","val33","val34","val35","val36","val37","val38","val39","val40","val41","val42","val43","val44","val45","val46","val47","val48","val49","val50","val51","val52","val53","val54","val55","val56","val57","val58","val59","val60","val61","val62","val63","val64","val65","val66","val67","val68","val69","val70","val71","val72","val73","val74","val75","val76","val77","val78","val79","val80","val81","val82","val83","val84","val85","val86","val87","val88","val89","val90","val91","val92","val93","val94","val95","val96","val97","val98","val99","val100","val101","val102","val103","val104","val105","val106","val107","val108","val109","val110","val111","val112","val113","val114","val115","val116","val117","val118","val119","val120","val121","val122","val123","val124","val125","val126","val127","val128","val129","val130","val131","val132","val133","val134","val135","val136","val137","val138","val139","val140","val141","val142","val143","val144","val145","val146","val147","val148","val149","val150","val151","val152","val153","val154","val155","val156","val157","val158","val159","val160","val161","val162","val163","val164","val165","val166","val167","val168","val169","val170","val171","val172","val173","val174","val175","val176","val177","val178","val179","val180","val181","val182","val183","val184","val185","val186","val187","val188","val189","val190","val191","val192","val193","val194","val195","val196","val197","val198","val199","val200","val201","val202","val203","val204","val205","val206","val207","val208","val209","val210","val211","val212","val213","val214","val215","val216","val217","val218","val219","val220","val221","val222","val223","val224","val225","val226","val227","val228","val229","val230","val231","val232","val233","val234","val235","val236","val237","val238","val239","val240","val241","val242","val243","val244","val245","val246","val247","val248","val249","val250","val251","val252","val253","val254","val255","val256","val257","val258","val259","val260","val261","val262","val263","val264","val265","val266","val267","val268","val269","val270","val271","val272","val273","val274","val275","val276","val277","val278","val279","val280","val281","val282","val283","val284","val285","val286","val287","val288","val289","val290","val291","val292","val293","val294","val295","val296","val297","val298","val299","val300","val301","val302","val303","val304","val305","val306","val307","val308","val309","val310","val311","val312","val313","val314","val315","val316","val317","val318","val319","val320","val321","val322","val323","val324","val325","val326","val327","val328","val329","val330","val331","val332","val333","val334","val335","val336","val337","val338","val339","val340","val341","val342","val343","val344","val345","val346","val347","val348","val349","val350","val351","val352","val353","val354","val355","val356","val357","val358","val359","val360","val361","val362","val363","val364","val365","val366","val367","val368","val369","val370","val371","val372","val373","val374","val375","val376","val377","val378","val379","val380","val381","val382","val383","val384","val385","val386","val387","val388","val389","val390","val391","val392","val393","val394","val395","val396","val397","val398","val399","val400","val401","val402","val403","val404","val405","val406","val407","val408","val409","val410","val411","val412","val413","val414","val415","val416","val417","val418","val419","val420","val421","val422","val423","val424","val425","val426","val427","val428","val429","val430","val431","val432","val433","val434","val435","val436","val437","val438","val439","val440","val441","val442","val443","val444","val445","val446","val447","val448","val449","val450","val451","val452","val453","val454","val455","val456","val457","val458","val459","val460","val461","val462","val463","val464","val465","val466","val467","val468","val469","val470","val471","val472","val473","val474","val475","val476","val477","val478","val479","val480","val481","val482","val483","val484","val485","val486","val487","val488","val489","val490","val491","val492","val493","val494","val495","val496","val497","val498","val499","val500","val501","val502","val503","val504","val505","val506","val507","val508","val509","val510","val511","val512","val513","val514","val515","val516","val517","val518","val519","val520","val521","val522","val523","val524","val525","val526","val527","val528","val529","val530","val531","val532","val533","val534","val535","val536","val537","val538","val539","val540","val541","val542","val543","val544","val545","val546","val547","val548","val549","val550","val551","val552","val553","val554","val555","val556","val557","val558","val559","val560","val561","val562","val563","val564","val565","val566","val567","val568","val569","val570","val571","val572","val573","val574","val575","val576","val577","val578","val579","val580","val581","val582","val583","val584","val585","val586","val587","val588","val589","val590","val591","val592","val593","val594","val595","val596","val597","val598","val599","val600","val601","val602","val603","val604","val605","val606","val607","val608","val609","val610","val611","val612","val613","val614","val615","val616","val617","val618","val619","val620","val621","val622","val623","val624","val625","val626","val627","val628","val629","val630","val631","val632","val633","val634","val635","val636","val637","val638","val639","val640","val641","val642","val643","val644","val645","val646","val647","val648","val649","val650","val651","val652","val653","val654","val655","val656","val657","val658","val659","val660","val661","val662","val663","val664","val665","val666","val667","val668","val669","val670","val671","val672","val673","val674","val675","val676","val677","val678","val679","val680","val681","val682","val683","val684","val685","val686","val687","val688","val689","val690","val691","val692","val693","val694","val695","val696","val697","val698","val699","val700","val701","val702","val703","val704","val705","val706","val707","val708","val709","val710","val711","val712","val713","val714","val715","val716","val717","val718","val719","val720","val721","val722","val723","val724","val725","val726","val727","val728","val729","val730","val731","val732","val733","val734","val735","val736","val737","val738","val739","val740","val741","val742","val743","val744","val745","val746","val747","val748","val749","val750","val751","val752","val753","val754","val755","val756","val757","val758","val759","val760","val761","val762","val763","val764","val765","val766","val767","val768","val769","val770","val771","val772","val773","val774","val775","val776","val777","val778","val779","val780","val781","val782","val783","val784","val785","val786","val787","val788","val789","val790","val791","val792","val793","val794","val795","val796","val797","val798","val799","val800","val801","val802","val803","val804","val805","val806","val807","val808","val809","val810","val811","val812","val813","val814","val815","val816","val817","val818","val819","val820","val821","val822","val823","val824","val825","val826","val827","val828","val829","val830","val831","val832","val833","val834","val835","val836","val837","val838","val839","val840","val841","val842","val843","val844","val845","val846","val847","val848","val849","val850","val851","val852","val853","val854","val855","val856","val857","val858","val859","val860","val861","val862","val863","val864","val865","val866","val867","val868","val869","val870","val871","val872","val873","val874","val875","val876","val877","val878","val879","val880","val881","val882","val883","val884","val885","val886","val887","val888","val889","val890","val891","val892","val893","val894","val895","val896","val897","val898","val899","val900","val901","val902","val903","val904","val905","val906","val907","val908","val909","val910","val911","val912","val913","val914","val915","val916","val917","val918","val919","val920","val921","val922","val923","val924","val925","val926","val927","val928","val929","val930","val931","val932","val933","val934","val935","val936","val937","val938","val939","val940","val941","val942","val943","val944","val945","val946","val947","val948","val949","val950","val951","val952","val953","val954","val955","val956","val957","val958","val959","val960","val961","val962","val963","val964","val965","val966","val967","val968","val969","val970","val971","val972","val973","val974","val975","val976","val977","val978","val979","val980","val981","val982","val983","val984","val985","val986","val987","val988","val989","val990","val991","val992","val993","val994","val995","val996","val997","val998","val999","val1000")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(29f,6f,28f,9f,9f,9f,5f,19f,15f,6f,5f,11f,18f,14f,17f,26f,0f,4f,4f,5f,18f,22f,5f,10f,12f,3f,29f,13f,19f,9f,26f,26f,2f,17f,25f,26f,28f,18f,21f,29f,20f,17f,22f,14f,8f,13f,2f,0f,13f,22f,19f,2f,19f,21f,27f,6f,12f,13f,5f,24f,24f,1f,27f,1f,15f,15f,29f,10f,20f,16f,21f,22f,18f,27f,22f,5f,21f,13f,17f,24f,28f,22f,10f,8f,4f,4f,15f,13f,29f,18f,28f,27f,10f,18f,0f,24f,10f,28f,15f,15f,22f,20f,28f,6f,5f,26f,1f,3f,23f,23f,3f,22f,11f,28f,24f,24f,22f,7f,5f,4f,13f,22f,12f,28f,14f,27f,18f,20f,16f,2f,23f,26f,9f,11f,2f,14f,14f,17f,2f,5f,16f,6f,4f,24f,2f,6f,6f,17f,4f,6f,26f,15f,17f,20f,16f,22f,18f,2f,16f,10f,18f,18f,15f,29f,3f,1f,21f,10f,24f,6f,9f,11f,1f,16f,26f,28f,29f,16f,28f,11f,29f,11f,13f,18f,16f,14f,9f,1f,24f,14f,8f,21f,12f,24f,25f,25f,27f,23f,12f,16f,3f,17f,10f,23f,7f,6f,16f,2f,21f,27f,4f,10f,11f,8f,25f,1f,26f,24f,7f,3f,12f,22f,26f,3f,24f,12f,9f,5f,17f,4f,18f,23f,23f,19f,21f,16f,1f,7f,12f,1f,19f,12f,20f,8f,0f,18f,7f,14f,7f,29f,13f,13f,0f,16f,3f,23f,25f,24f,7f,21f,15f,26f,6f,22f,26f,22f,14f,22f,14f,26f,16f,29f,7f,17f,6f,24f,13f,28f,16f,26f,18f,7f,10f,23f,20f,29f,2f,25f,17f,23f,27f,26f,8f,7f,22f,1f,29f,20f,7f,25f,10f,1f,19f,12f,27f,10f,7f,9f,25f,6f,29f,11f,15f,16f,26f,20f,27f,6f,24f,11f,28f,22f,17f,15f,11f,10f,29f,11f,19f,21f,8f,10f,9f,3f,1f,10f,20f,19f,24f,25f,10f,5f,23f,9f,6f,9f,23f,26f,1f,9f,16f,15f,29f,0f,9f,21f,16f,1f,5f,16f,10f,20f,13f,23f,15f,5f,1f,29f,17f,4f,5f,0f,16f,11f,25f,10f,15f,14f,19f,8f,11f,25f,27f,21f,29f,15f,14f,27f,28f,26f,14f,25f,22f,15f,14f,19f,3f,21f,2f,22f,20f,16f,13f,20f,11f,25f,18f,12f,16f,17f,24f,9f,8f,23f,20f,12f,20f,6f,8f,6f,26f,25f,29f,24f,18f,12f,1f,22f,18f,14f,19f,19f,25f,24f,17f,4f,17f,16f,11f,11f,9f,27f,27f,7f,12f,7f,7f,2f,18f,12f,12f,17f,28f,12f,12f,14f,2f,24f,4f,1f,3f,7f,16f,22f,2f,17f,3f,3f,28f,13f,17f,0f,26f,0f,3f,1f,14f,20f,12f,21f,24f,27f,14f,8f,23f,11f,27f,0f,14f,27f,11f,11f,10f,16f,13f,3f,6f,7f,2f,13f,25f,18f,19f,1f,22f,12f,0f,20f,17f,3f,10f,25f,28f,11f,3f,6f,15f,18f,7f,6f,10f,15f,18f,18f,15f,10f,29f,2f,8f,0f,26f,8f,2f,8f,3f,20f,12f,5f,17f,1f,23f,18f,3f,20f,21f,2f,3f,6f,19f,0f,7f,12f,10f,11f,13f,19f,14f,20f,13f,23f,2f,18f,11f,25f,12f,22f,6f,10f,9f,12f,11f,5f,28f,26f,18f,1f,18f,24f,4f,26f,28f,6f,6f,7f,16f,17f,11f,3f,29f,29f,10f,11f,20f,10f,10f,16f,4f,26f,12f,0f,2f,29f,22f,9f,13f,24f,17f,2f,11f,20f,25f,23f,11f,7f,9f,20f,8f,9f,12f,9f,14f,11f,19f,0f,17f,19f,1f,10f,0f,9f,19f,22f,28f,22f,7f,23f,7f,18f,19f,23f,26f,17f,24f,11f,7f,22f,7f,15f,12f,20f,23f,6f,22f,4f,0f,6f,27f,12f,9f,0f,11f,6f,10f,1f,15f,24f,12f,28f,24f,7f,12f,20f,23f,9f,8f,27f,15f,23f,1f,25f,2f,22f,23f,13f,25f,10f,5f,21f,12f,9f,18f,0f,10f,20f,21f,3f,10f,9f,3f,21f,21f,26f,17f,13f,11f,17f,21f,7f,8f,14f,5f,24f,26f,7f,10f,9f,26f,16f,17f,1f,5f,26f,18f,13f,23f,7f,28f,4f,9f,1f,28f,25f,12f,20f,21f,25f,4f,10f,9f,21f,29f,21f,28f,25f,10f,3f,8f,2f,26f,10f,3f,9f,27f,7f,27f,26f,2f,27f,0f,3f,12f,15f,2f,25f,19f,23f,1f,4f,28f,19f,12f,4f,10f,11f,6f,4f,29f,21f,25f,7f,11f,16f,2f,9f,24f,12f,19f,2f,8f,17f,12f,22f,12f,29f,25f,29f,14f,29f,26f,25f,23f,0f,19f,0f,27f,16f,12f,14f,13f,3f,4f,17f,2f,1f,12f,5f,6f,10f,7f,24f,22f,9f,5f,26f,27f,9f,18f,20f,5f,22f,17f,7f,29f,3f,14f,22f,7f,6f,20f,18f,18f,23f,28f,2f,9f,20f,24f,7f,22f,20f,29f,9f,10f,25f,22f,0f,0f,2f,19f,7f,16f,3f,9f,1f,28f,24f,10f,12f,4f,22f,7f,12f,3f,8f,15f,29f,18f,6f,18f,11f,5f,12f,14f,4f,14f,11f,6f,19f,9f,24f,24f,27f,8f,16f,25f,8f,19f,16f,17f,6f,9f,15f,11f,2f,21f,4f,21f,24f,17f,29f,13f,4f,25f,26f,14f,7f,11f,6f,2f,18f,4f,17f,10f,5f,18f,3f,18f,26f,14f,9f,5f,18f,25f,29f,29f,7f,20f,26f,13f,11f,4f,26f,11f,8f,16f,6f,13f,25f,24f,27f,10f,27f,8f,4f,25f,14f,3f,27f,7f,18f,5f,3f,13f,11f,12f,7f,11f,15f,18f,10f,18f,21f,13f,16f,13f,17f,7f,2f,2f,19f,6f,19f,10f,1f,20f,19f,18f,22f,13f,17f,11f,10f,4f,5f,7f,15f,15f,12f,16f,6f)))
        })

        // negative
        showBarChartsInternal("1 negative value", chart13, fun ():ArrayList<String> {
            return arrayListOf<String>("val1")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f)))
        })

        showBarChartsInternal("8 negative values", chart14, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2", "val3", "val4", "val5", "val6", "val7", "val8")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f, -9f, 1f, 2f, -3f, 8f, 7.5f)))
        })

        showBarChartsInternal("50 negative values", chart15, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16","val17","val18","val19","val20","val21","val22","val23","val24","val25","val26","val27","val28","val29","val30","val31","val32")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(4f,25f,-15f,11f,13f,22f,27f,4f,8f,19f,3f,-16f,11f,21f,23f,13f,22f,-24f,27f,7f,19f,15f,-26f,21f,0f,22f,-13f,12f,0f,-5f,19f,22f,24f,6f,7f,16f,3f,14f,17f,17f,5f,5f,12f,0f,20f,12f,10f,9f,-21f,6f)))
        })

        // group
        showBarChartsInternal("2 groups 1 val", chart16, fun ():ArrayList<String> {
            return arrayListOf<String>("val1")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f)), AirChartValueItem("legend2", arrayListOf(8f)))
        })

        showBarChartsInternal("2 group 2 values", chart17, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f)), AirChartValueItem("legend2", arrayListOf(8f, 16f)))
        })

        showBarChartsInternal("some groups some vals", chart18, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2", "val3", "val4")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 9f, 4f, 0f)), AirChartValueItem("legend2", arrayListOf(8f, 9f, 10f, 9f)), AirChartValueItem("legend3", arrayListOf(8f, 5f, 3f, 1f)), AirChartValueItem("legend4", arrayListOf(8f, 9.1f, 1f, 1f)))
        })

        showBarChartsInternal("many groups many vals", chart19, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,20f,4f,4f,16f,8f,28f,17f,19f,12f,9f,12f,14f,2f,15f,24f,11f,5f,18f,6f,3f,26f,3f,22f,2f,20f,21f,14f,23f,4f,0f,19f)), AirChartValueItem("legend2", arrayListOf(4f,25f,15f,11f,13f,22f,27f,4f,8f,19f,3f,16f,11f,21f,23f,13f,22f,24f,27f,7f,19f,15f,26f,21f,0f,22f,13f,12f,0f,5f,19f)), AirChartValueItem("legend3", arrayListOf(16f,7f,21f,13f,20f,19f,6f,10f,15f,21f,3f,22f,1f,6f,4f,16f,11f,11f,4f,8f,8f,14f,21f,20f,13f,15f,12f,28f,9f,26f,5f,1f)), AirChartValueItem("legend4", arrayListOf(26f,0f,3f,16f,25f,0f,15f,3f,23f,11f,5f,8f,7f,21f,11f,29f,13f,14f,24f,11f,19f,12f,5f,12f,15f,22f,24f,4f,6f,21f,2f,0f)))
        })

        // group negative
        showBarChartsInternal("2 groups 1 val, negative", chart20, fun ():ArrayList<String> {
            return arrayListOf<String>("val1")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f)), AirChartValueItem("legend2", arrayListOf(8f)))
        })

        showBarChartsInternal("2 group 2 values", chart21, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(5f, 8f)), AirChartValueItem("legend2", arrayListOf(-8f, -16f)))
        })

        showBarChartsInternal("some groups some vals", chart22, fun ():ArrayList<String> {
            return arrayListOf<String>("val1", "val2", "val3", "val4")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(-5f, 9f, 4f, 0f)), AirChartValueItem("legend2", arrayListOf(8f, -9f, 10f, 9f)), AirChartValueItem("legend3", arrayListOf(8f, 5f, -3f, 1f)), AirChartValueItem("legend4", arrayListOf(8f, 9.1f, 1f, -1f)))
        })

        showBarChartsInternal("many groups many vals", chart23, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,-20f,4f,4f,-16f,8f,28f,17f,19f,12f,-9f,12f,14f,2f,15f,24f,11f,5f,18f,6f,3f,-26f,3f,22f,2f,20f,21f,14f,23f,4f,0f,19f)), AirChartValueItem("legend2", arrayListOf(4f,25f,15f,11f,13f,-22f,27f,4f,-8f,19f,3f,16f,-11f,21f,-23f,13f,22f,24f,27f,7f,19f,15f,26f,21f,0f,22f,13f,12f,0f,5f,19f)), AirChartValueItem("legend3", arrayListOf(16f,7f,21f,13f,20f,19f,6f,10f,15f,21f,3f,22f,1f,6f,4f,16f,11f,11f,4f,8f,8f,14f,21f,20f,13f,15f,12f,28f,9f,26f,5f,1f)), AirChartValueItem("legend4", arrayListOf(26f,0f,3f,16f,25f,0f,15f,3f,23f,11f,5f,8f,7f,21f,11f,29f,13f,14f,24f,11f,19f,12f,5f,12f,15f,22f,24f,4f,-6f,-21f,-2f,0f)))
        })

        showBarChartsInternal("many groups many vals", chart24, fun ():ArrayList<String> {
            return arrayListOf<String>("val1","val2","val3","val4","val5","val6","val7","val8","val9","val10","val11","val12","val13","val14","val15","val16")
        }, fun (): ArrayList<AirChartValueItem> {
            return arrayListOf(AirChartValueItem("legend1", arrayListOf(9f,200f,4f,4f,-16f,8f,28f,17f,19f,12f,-9f,12f,14f,2f,15f,24f,11f,5f,18f,6f,3f,-26f,3f,22f,2f,20f,21f,14f,23f,4f,0f,19f)), AirChartValueItem("legend2", arrayListOf(4f,25f,15f,11f,13f,-22f,27f,4f,-8f,19f,3f,16f,-11f,21f,-23f,13f,22f,24f,27f,7f,19f,15f,26f,21f,0f,22f,13f,12f,0f,5f,19f)), AirChartValueItem("legend3", arrayListOf(16f,7f,21f,13f,20f,19f,6f,10f,15f,21f,3f,22f,1f,6f,4f,16f,11f,11f,4f,8f,8f,14f,21f,20f,13f,15f,12f,28f,9f,26f,5f,1f)), AirChartValueItem("legend4", arrayListOf(26f,0f,3f,16f,25f,0f,15f,3f,23f,11f,5f,8f,7f,21f,11f,29f,13f,14f,24f,11f,19f,12f,5f,12f,15f,22f,24f,4f,-6f,-21f,-2f,0f)))
        })*/
    }

    private fun showBarChartsInternal(viewGroup: ViewGroup, title: String, xLabels: ArrayList<String>, yLeftItems: java.util.ArrayList<AirChartValueItem>) {
        AirChart(this, viewGroup).showBarChart(object: AirChartBar.BarInterface {
            override fun getTitle(): String? {
                return title
            }

            override fun getXLabel(): String {
                return "x label"
            }

            override fun getYLeftLabel(): String {
                return "y label"
            }

            override fun getXLabels(): ArrayList<String> {
                return xLabels
            }

            override fun getYLeftItems(): java.util.ArrayList<AirChartValueItem> {
                return yLeftItems
            }

            @SuppressLint("ResourceType")
            override fun getColors(): ArrayList<String>? {
                return arrayListOf(
                    resources.getString(R.color.red400),
                    resources.getString(R.color.purple400),
                    resources.getString(R.color.amber400),
                    resources.getString(R.color.green400),
                    resources.getString(R.color.brown300),
                    resources.getString(R.color.indigo400)
                )
            }

        })
    }

    private fun showHorizontalBarCharts() {

    }

    class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val chartHolder: LinearLayout = view.chartHolder
    }
}