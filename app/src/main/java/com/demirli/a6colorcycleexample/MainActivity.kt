package com.demirli.a6colorcycleexample

import android.graphics.Color
import android.graphics.ColorSpace
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Visibility
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.graphics.ColorUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    private var hue: Float? = null

    private var delay: Long? = null

    private var stopRunnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler()
        runnable = Runnable {  }

        hue = 0f

        start_btn.setOnClickListener {
            stopRunnable = false
            runnableStart(runnable,
                color_cycle_layout,
                setRGB(rgb_color_red_et),
                setRGB(rgb_color_green_et),
                setRGB(rgb_color_blue_et),
                setDelay(),
                hue!!)

            start_btn.visibility = View.GONE
            stop_btn.visibility = View.VISIBLE
        }

        stop_btn.setOnClickListener {
            stopRunnable = true
            handler.removeCallbacks(runnable)


            start_btn.visibility = View.VISIBLE
            stop_btn.visibility = View.GONE
        }
    }

    fun setRGB(editText: EditText): Int{
        var setRGB = 0
        if (editText.text.toString() != ""){
            setRGB = editText.text.toString().toInt()
        }else{
        }
        return setRGB
    }

    fun setDelay(): Long{
        if (increment_et.text.toString() != ""){
            delay = increment_et.text.toString().toLong()
        }else{
            delay = 20
        }
        return delay!!
    }

    fun runnableStart(runnable: Runnable, linearLayout: LinearLayout, red:Int, green:Int, blue:Int, delay: Long, hue: Float){

        var runnable = runnable
        var hue = hue

        var hslFloatArray = FloatArray(3)
        ColorUtils.RGBToHSL(red,green,blue,hslFloatArray)
        hue = hslFloatArray[0]

        var status: Boolean = true //increase(true), decrease(false)

        runnable = object: Runnable{
            override fun run() {

                if(hue >= 255f && status == true){
                    status = false
                }else if(hue <= 0f && status == false){
                    status = true
                }

                if(status == true){
                    hslFloatArray[0] = hue
                    val color = ColorUtils.HSLToColor(hslFloatArray)//floatArrayOf(hue, saturation, lightness)

                    linearLayout.setBackgroundColor(color)
                    setTextHslFormat(hslFloatArray)

                    hue = hue + 1f

                    if (stopRunnable == false){
                        handler.postDelayed(runnable, delay)
                    }
                }else if(status == false){
                    hslFloatArray[0] = hue
                    val color = ColorUtils.HSLToColor(hslFloatArray)//floatArrayOf(hue, saturation, lightness)

                    linearLayout.setBackgroundColor(color)
                    setTextHslFormat(hslFloatArray)

                    hue = hue - 1f

                    if (stopRunnable == false){
                        handler.postDelayed(runnable, delay)
                    }
                }
            }
        }

        handler.postDelayed(runnable,300)
    }

    fun setTextHslFormat(floatArray: FloatArray){
        var df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        hsl_tv.text = "hue:" + df.format(floatArray[0]) + " " + "saturation:" +
                df.format(floatArray[1]).toString() + " " + "lightness:" +
                df.format(floatArray[2]).toString()
    }
}
