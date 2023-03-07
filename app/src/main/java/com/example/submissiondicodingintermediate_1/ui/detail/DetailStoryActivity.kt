package com.example.submissiondicodingintermediate_1.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.submissiondicodingintermediate_1.R
import com.example.submissiondicodingintermediate_1.data.local.DetailStory
import com.example.submissiondicodingintermediate_1.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpData()
    }

    @Suppress("DEPRECATION")
    private fun setUpData() {
        val data = intent.getParcelableExtra<DetailStory>(EXTRA_DATA) as DetailStory
        val name = data.name
        val description = data.description
        val photoUrl = data.photoUrl

        binding.tvItemUsername.text = name
        binding.tvItemDescription.text = description
        Glide.with(applicationContext)
            .load(photoUrl)
            .into(binding.ivItemPhoto)
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        supportActionBar?.title = getString(R.string.story_app)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == android.R.id.home) {
                finish()
            }
            return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}