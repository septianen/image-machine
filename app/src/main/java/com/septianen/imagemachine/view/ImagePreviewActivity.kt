package com.septianen.imagemachine.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.septianen.imagemachine.R
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.databinding.ActivityImagePreviewBinding
import com.septianen.imagemachine.model.Temporary

class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding

    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagePath = Temporary.imagePath
        updateImage()

        binding.btnDelete.setOnClickListener {
             setResult(RESULT_OK)
            finish()
        }
    }

    private fun updateImage() {
        Glide
            .with(applicationContext)
            .load(imagePath)
            .into(binding.ivMachine)
    }
}