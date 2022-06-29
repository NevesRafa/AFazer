package com.nevesrafael.afazer.telas.splah_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nevesrafael.afazer.databinding.ActivitySplashScreenBinding
import com.nevesrafael.afazer.telas.tela_incial.TelaInicialActivity

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            run {
                val intent = Intent(this, TelaInicialActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

    }
}