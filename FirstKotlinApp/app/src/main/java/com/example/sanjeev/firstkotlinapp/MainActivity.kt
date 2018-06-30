package com.example.sanjeev.firstkotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun ToastMe(view : View){
        Toast.makeText(this, "Hello Toast !", Toast.LENGTH_SHORT).show()
    }

    fun CountMe(view : View){
        val count = findViewById<TextView>(R.id.textView)
        val countString = count.text.toString()
        var cnt : Int = Integer.parseInt(countString);
        cnt++
        count.text = cnt.toString()
    }

    fun randomMe(view : View){
        val intent = Intent(this, SecondActivity::class.java)
        val count = findViewById<TextView>(R.id.textView)
        val countString = count.text.toString()
        var cnt : Int = Integer.parseInt(countString)
        intent.putExtra(SecondActivity.TOTAL_COUNT, cnt)
        startActivity(intent)
    }
}
