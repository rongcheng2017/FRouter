package com.rong.cheng.frouter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.rong.cheng.router_runtime.Router

import com.rongcheng.router.annotations.Destination

@Destination(url = "router://page-home", description = "应用主页")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)
        val findViewById = findViewById<TextView>(R.id.center)
        findViewById.setOnClickListener {
            Router.go(this,"router://rongcheng/second")
        }
    }
}