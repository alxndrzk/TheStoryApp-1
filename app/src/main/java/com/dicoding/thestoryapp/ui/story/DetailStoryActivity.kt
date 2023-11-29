package com.dicoding.thestoryapp.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.thestoryapp.databinding.ActivityDetailStoryBinding
import com.dicoding.thestoryapp.ui.auth.LoginActivity
import com.dicoding.thestoryapp.ui.story.viewmodel.StoryViewModel
import com.dicoding.thestoryapp.util.changeFormatDate

class DetailStoryActivity : AppCompatActivity() {
    companion object{
        const val STORY_ID = "STORY_ID"
    }
    private lateinit var viewbinding: ActivityDetailStoryBinding
    private val detailStoryViewModel: StoryViewModel by viewModels()

    private var id: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewbinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        val view = viewbinding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Detail Story"

        if (intent.extras != null) {
            id = intent.getStringExtra(STORY_ID).toString()
        }

        isLoading(true)
        detailStoryViewModel.getDetailStory(id)
        setDetailStory()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDetailStory() {
        detailStoryViewModel.detailStoryLiveData.observe(this) { detailStoryResponse ->
            if (detailStoryResponse != null) {
                isLoading(false)
                if (detailStoryResponse.error == false) {
                    with(viewbinding) {
                        Glide.with(this@DetailStoryActivity)
                            .load(detailStoryResponse.story?.photoUrl)
                            .into(img)
                        date.text = "Date created: ${changeFormatDate(detailStoryResponse.story?.createdAt as String)}"
                        createdBy.text = detailStoryResponse.story.name
                        description.text = detailStoryResponse.story.description
                    }
                } else {
                    if (detailStoryResponse.message.equals("unauthorized")) {
                        Toast.makeText(this@DetailStoryActivity, "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DetailStoryActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@DetailStoryActivity, detailStoryResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

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