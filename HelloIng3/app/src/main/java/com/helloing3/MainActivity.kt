package com.helloing3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.hello_tv)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val btnGallery: Button = findViewById(R.id.btnGallerys)
        val btnSubject: Button = findViewById(R.id.btnSubjects)
        val txtSalut: TextView = findViewById(R.id.hello_tv)
        val btnSendMail: Button = findViewById(R.id.btnSendMail)

        btnGallery.setOnClickListener({ view ->
            //txtSalut.setText("welcome to ING3 Gallery")
            val intent = Intent(this, GalleryActivity:: class.java)
            startActivity(intent)
        })

        btnSubject.setOnClickListener({ view ->
            //txtSalut.setText("welcome to ING3 Subject")
            val intent = Intent(this, SubjectsActivity:: class.java)
            startActivity(intent)
        })

        btnSendMail.setOnClickListener({ view ->
            sendMail("seniordoczer@gmail.com")})
    }
    fun sendMail(emailAddresses: String){
        val intent = Intent(Intent.ACTION_SEND)
        var message: String = "Hello welcome to ING3"
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddresses))
        intent.putExtra(Intent.EXTRA_SUBJECT, message)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
