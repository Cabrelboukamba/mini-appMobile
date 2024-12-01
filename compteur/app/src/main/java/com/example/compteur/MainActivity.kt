package com.example.compteur

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView = findViewById(R.id.textView)
    }
    fun ajouter(view: View){
        var contenu = textView.text.toString().toInt();
        contenu = contenu + 1;
        textView.text = contenu.toString()
    }
    fun diminuer(view: View){
        var contenu = textView.text.toString().toInt();
        if(contenu>0){
            contenu = contenu - 1;
        }
        textView.text = contenu.toString()
    }
}