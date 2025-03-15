package com.mithilakshar.mithilaksharkeyboard.UI

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.mithilaksharkeyboard.R

class LayoutTester : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_layout_tester)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val layoutName = "फेस्टिवल_विशिंग_लेआउट"
        val txt1 = "शुभ दीपावली!"
        val txt2 = "अहाँकेँ एवं अहाँक परिवार केँ दीपावलीक हार्दिक शुभकामना। सुख, शांति आ समृद्धि बनल रहय।"
        val image1Url = "https://example.com/deepawali_lamp.jpg"
        val image2Url = "https://example.com/festival_crackers.jpg"
        val bgUrl = "https://example.com/diwali_background.jpg"






    }
}