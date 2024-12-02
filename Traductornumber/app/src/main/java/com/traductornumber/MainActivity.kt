package com.traductornumber

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnTraduct: Button = findViewById(R.id.btnTraduct)
        val editText1: TextView = findViewById(R.id.editText1)
        val editText2: TextView = findViewById(R.id.editText2)
        val txtlangue: TextView = findViewById(R.id.txtLangue)
        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btn4: Button = findViewById(R.id.btn4)
        val btn5: Button = findViewById(R.id.btn5)
        val btn6: Button = findViewById(R.id.btn6)
        val btn7: Button = findViewById(R.id.btn7)
        val btn8: Button = findViewById(R.id.btn8)
        val btn9: Button = findViewById(R.id.btn9)
        val btn0: Button = findViewById(R.id.btn0)

        btn1.setOnClickListener {
            editText2.append("1")
        }
        btn2.setOnClickListener {
            editText2.append("2")
        }
        btn3.setOnClickListener {
            editText2.append("3")
        }
        btn4.setOnClickListener {
            editText2.append("4")
        }
        btn5.setOnClickListener {
            editText2.append("5")
        }
        btn6.setOnClickListener {
            editText2.append("6")
        }
        btn7.setOnClickListener {
            editText2.append("7")
        }
        btn8.setOnClickListener {
            editText2.append("8")
        }
        btn9.setOnClickListener {
            editText2.append("9")
        }
        btn0.setOnClickListener {
            editText2.append("0")
        }

        btnTraduct.setOnClickListener {
            editText1.setText(traduct(editText2.text.toString(), txtlangue.text.toString()))
            editText2.setText("")
        }
        val spinnerLangues: Spinner = findViewById(R.id.spinnerLangues)

        val langues = arrayOf("Anglais", "Espagnol", "Italien", "Allemand", "Français", "Nzebi")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            langues
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLangues.adapter = adapter

        spinnerLangues.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            val txtLangue: TextView = findViewById(R.id.txtLangue)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                txtLangue.text = langues[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun frenshTraduct(text: String): String {
        return text.replace("1", "un")
            .replace("2", "deux")
            .replace("3", "trois")
            .replace("4", "quatre")
            .replace("5", "cinq")
            .replace("6", "six")
            .replace("7", "sept")
            .replace("8", "huit")
            .replace("9", "neuf")
            .replace("0", "zéro")
    }
    fun englishTraduct(text: String): String {
        return text.replace("1", "one")
            .replace("2", "two")
            .replace("3", "three")
            .replace("4", "four")
            .replace("5", "five")
            .replace("6", "six")
            .replace("7", "seven")
            .replace("8", "eight")
            .replace("9", "nine")
            .replace("0", "zero")
    }
    fun spanishTraduct(text: String): String {
        return text.replace("1", "uno")
            .replace("2", "dos")
            .replace("3", "tres")
            .replace("4", "cuatro")
            .replace("5", "cinco")
            .replace("6", "seis")
            .replace("7", "siete")
            .replace("8", "ocho")
            .replace("9", "nueve")
            .replace("0", "cero")
    }
    fun italianTraduct(text: String): String {
        return text.replace("1", "uno")
            .replace("2", "due")
            .replace("3", "tre")
            .replace("4", "quattro")
            .replace("5", "cinque")
            .replace("6", "sei")
            .replace("7", "sette")
            .replace("8", "otto")
            .replace("9", "nove")
            .replace("0", "zero")
    }
    fun germanTraduct(text: String): String {
        return text.replace("1", "eins")
            .replace("2", "zwei")
            .replace("3", "drei")
            .replace("4", "vier")
            .replace("5", "fünf")
            .replace("6", "sechs")
            .replace("7", "sieben")
            .replace("8", "acht")
            .replace("9", "neun")
            .replace("0", "null")
    }
    fun nzebiTraduct(text: String): String {
        return text.replace("1", "mo")
            .replace("2", "bioli")
            .replace("3", "bitate")
            .replace("4", "bina")
            .replace("5", "bitane")
            .replace("6", "bisamna")
            .replace("7", "tsambe")
            .replace("8", "pombo")
            .replace("9", "l'bwa")
    }
    fun traduct(text: String, langue: String): String {
        if (langue == "Anglais") {
            return englishTraduct(text)
        } else if (langue == "Espagnol") {
            return spanishTraduct(text)
        } else if (langue == "Italien") {
            return italianTraduct(text)
        } else if (langue == "Allemand") {
            return germanTraduct(text)
        }
        else if (langue == "Nzebi") {
            return nzebiTraduct(text)
        }
        return frenshTraduct(text)
    }


}