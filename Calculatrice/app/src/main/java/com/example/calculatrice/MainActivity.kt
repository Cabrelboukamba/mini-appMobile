package com.example.calculatrice

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.mariuszgromada.math.mxparser.Expression
import java.text.DecimalFormat


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
        val input: TextView = findViewById(R.id.input)
        val output: TextView = findViewById(R.id.output)
        val btnClear: Button = findViewById(R.id.button_clear)
        val btnBracketLeft: Button = findViewById(R.id.button_bracket_left)
        val btnBracketRight: Button = findViewById(R.id.button_bracket_right)
        val btnDivide: Button = findViewById(R.id.button_divide)
        val btn7: Button = findViewById(R.id.button_7)
        val btn8: Button = findViewById(R.id.button_8)
        val btn9: Button = findViewById(R.id.button_9)
        val btnMultiply: Button = findViewById(R.id.button_multiply)
        val btn4: Button = findViewById(R.id.button_4)
        val btn5: Button = findViewById(R.id.button_5)
        val btn6: Button = findViewById(R.id.button_6)
        val btnMinus: Button = findViewById(R.id.button_minus)
        val btn1: Button = findViewById(R.id.button_1)
        val btn2: Button = findViewById(R.id.button_2)
        val btn3: Button = findViewById(R.id.button_3)
        val btnPlus: Button = findViewById(R.id.button_plus)
        val btn0: Button = findViewById(R.id.button_0)
        val btnDot: Button = findViewById(R.id.button_dot)
        val btnMod: Button = findViewById(R.id.button_mod)
        val btnEqual: Button = findViewById(R.id.button_equal)


        btnClear.setOnClickListener {
            input.text = ""
            output.text = ""
        }
        btnBracketLeft.setOnClickListener {
            input.append("(")
        }
        btnBracketRight.setOnClickListener {
            input.append(")")

        }
        btnDivide.setOnClickListener {
            input.append("÷")
        }
        btn7.setOnClickListener {
            input.append("7")
        }
        btn8.setOnClickListener {
            input.append("8")
        }
        btn9.setOnClickListener {
            input.append("9")
        }
        btnMultiply.setOnClickListener {
            input.append("×")
        }
        btn4.setOnClickListener {
            input.append("4")
        }
        btn5.setOnClickListener {
            input.append("5")
        }
        btn6.setOnClickListener {
            input.append("6")
        }
        btnMinus.setOnClickListener {
            input.append("-")
        }
        btn1.setOnClickListener {
            input.append("1")
        }
        btn2.setOnClickListener {
            input.append("2")
        }
        btn3.setOnClickListener {
            input.append("3")
        }
        btnPlus.setOnClickListener {
            input.append("+")
        }
        btn0.setOnClickListener {
            input.append("0")
        }
        btnDot.setOnClickListener {
            input.append(".")
        }
        btnMod.setOnClickListener {
            input.append("%")
        }
        btnEqual.setOnClickListener {
            showResultat()
        }
    }
    private fun getInputExepression(): String {
        val input: TextView = findViewById(R.id.input)
        var expression = input.text.replace(Regex("÷"), "/")
        expression = expression.replace(Regex("×"), "*")
        expression = expression.replace(Regex("%"), "/100")
        return expression
    }
    private fun showResultat(){
        val output: TextView = findViewById(R.id.output)
        try {
            val expression = getInputExepression()
            val resultat = Expression(expression).calculate()
            if (resultat.isNaN()){
                output.text = "Erreur"
                output.setTextColor(ContextCompat.getColor(this, R.color.red))
                return
            }else{
                output.text = DecimalFormat("0.######").format(resultat).toString()
                output.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
        }catch (e: Exception){
            output.text = "Erreur"
            output.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }
}





