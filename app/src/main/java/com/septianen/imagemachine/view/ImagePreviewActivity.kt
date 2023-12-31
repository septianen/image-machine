package com.septianen.imagemachine.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.septianen.imagemachine.databinding.ActivityImagePreviewBinding
import com.septianen.imagemachine.model.Temporary

class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding

    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagePath = Temporary.image?.imagePath ?: ""
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