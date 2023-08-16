package com.septianen.imagemachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.septianen.imagemachine.databinding.ActivityMainBinding
import com.septianen.imagemachine.utils.CommonUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}