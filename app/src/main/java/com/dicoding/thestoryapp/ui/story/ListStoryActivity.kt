package com.dicoding.thestoryapp.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.thestoryapp.R
import com.dicoding.thestoryapp.databinding.ActivityListStoryBinding
import com.dicoding.thestoryapp.model.Story
import com.dicoding.thestoryapp.ui.auth.LoginActivity
import com.dicoding.thestoryapp.ui.story.adapter.StoryAdapter
import com.dicoding.thestoryapp.ui.story.viewmodel.StoryViewModel

class ListStoryActivity : AppCompatActivity() {

    private lateinit var viewbinding: ActivityListStoryBinding
    private val storyViewModel: StoryViewModel by viewModels()
    private lateinit var listStoryAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewbinding = ActivityListStoryBinding.inflate(layoutInflater)
        val view = viewbinding.root
        setContentView(view)
        supportActionBar?.title = "List Story"
        initRecycleview()

        isLoading(true)
        storyViewModel.getAllStory()
        setData()

        viewbinding.swipeRefresh.setOnRefreshListener {
            viewbinding.swipeRefresh.isRefreshing = false
            isLoading(true)
            storyViewModel.getAllStory()
        }

        viewbinding.AddStory.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))
        }

        listStoryAdapter.setOnItemClicked(object : StoryAdapter.OnItemClickListener{
            override fun onItemClicked(id: String) {
                val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.STORY_ID, id)
                startActivity(intent)
            }

        })
    }

    override fun onResume() {
        super.onResume()

        if (intent.extras != null){
            val isRestart = intent.getBooleanExtra("reload", false)
            if (isRestart) {
                isLoading(true)
                storyViewModel.getAllStory()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData(){
        storyViewModel.storyListData.observe(this) { responseListStory ->
            isLoading(false)
            if (responseListStory != null) {
                if (!responseListStory.error) {
                    listStoryAdapter.setData(responseListStory.listStory as List<Story>)
                    listStoryAdapter.notifyDataSetChanged()
                }
                else {
                    if (responseListStory.message.equals("unauthorized")) {
                        Toast.makeText(this@ListStoryActivity, "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ListStoryActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@ListStoryActivity, responseListStory.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initRecycleview() {
        listStoryAdapter = StoryAdapter()
        with(viewbinding) {
            rvListStory.layoutManager = LinearLayoutManager(this@ListStoryActivity)
            rvListStory.setHasFixedSize(true)
            rvListStory.adapter = listStoryAdapter
        }


    }

    private fun isLoading(isL: Boolean) {
        if (isL) {
            viewbinding.rlLoading.visibility = View.VISIBLE
        } else {
            viewbinding.rlLoading.visibility = View.GONE
        }
    }

}