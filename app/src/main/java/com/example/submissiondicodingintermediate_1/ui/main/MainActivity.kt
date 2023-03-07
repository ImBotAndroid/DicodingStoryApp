package com.example.submissiondicodingintermediate_1.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.submissiondicodingintermediate_1.ui.uploadstory.UploadActivity
import com.example.submissiondicodingintermediate_1.R
import com.example.submissiondicodingintermediate_1.ui.setting.SettingActivity
import com.example.submissiondicodingintermediate_1.databinding.ActivityMainBinding
import com.example.submissiondicodingintermediate_1.ui.adapter.LoadingStateAdapter
import com.example.submissiondicodingintermediate_1.ui.ViewModelFactory
import com.example.submissiondicodingintermediate_1.ui.maps.MapsActivity

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpViewModel()
        setUpStories()
    }

    private fun setUpStories() {
        val preference = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val bearer = "Bearer " + preference.getString("token", "").toString()

        val adapter = ItemStoryAdapter()
        showRecyclerView(adapter)

        mainViewModel.getAllStory(bearer).observe(this) {
            adapter.submitData(lifecycle, it)
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        val preference = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val bearer = "Bearer " + preference.getString("token", "").toString()

        val adapter = ItemStoryAdapter()
        showRecyclerView(adapter)
        mainViewModel.getAllStory(bearer).observe(this) {
            adapter.submitData(lifecycle, it)
        }

        binding.swipeRefreshLayout.isRefreshing = false
        binding.rvListStory.smoothScrollToPosition(0)
    }

    private fun showRecyclerView(adapter: ItemStoryAdapter) {
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.rvListStory.layoutManager = LinearLayoutManager(this)
        binding.rvListStory.setHasFixedSize(true)
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun setUpViewModel() {
        mainViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[MainViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.add_story -> {
                val intent = Intent(this, UploadActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.get_story_location -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}