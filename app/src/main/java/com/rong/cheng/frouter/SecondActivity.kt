package com.rong.cheng.frouter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rongcheng.router.annotations.Destination

@Destination(
    url = "router://rongcheng/second",
    description = "第二页面"
)
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}