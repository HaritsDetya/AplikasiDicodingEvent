package com.example.aplikasidicodingeventnavigationdanapi.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.aplikasidicodingeventnavigationdanapi.R
import com.example.aplikasidicodingeventnavigationdanapi.databinding.ActivityDetailEventBinding
import com.google.android.material.button.MaterialButton

class DetailEvent : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailId = intent.getStringExtra("idDetail")

        val detailName: TextView = findViewById(R.id.detail_name)
        val detailOwner: TextView = findViewById(R.id.detail_owner)
        val detailSummary: TextView = findViewById(R.id.detail_summary)
        val detailQuota: TextView = findViewById(R.id.detail_quota)
        val detailBeginTime: TextView = findViewById(R.id.detail_begin_time)
        val detailDescription: TextView = findViewById(R.id.detail_description)
        val detailButton: MaterialButton = findViewById(R.id.detail_button)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        detailId?.let { viewModel.getEventDetail(it) }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.detailProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe event details
        viewModel.eventDetail.observe(this) { event ->
            event?.let {
                Glide.with(this)
                    .load(it.imageLogo)
                    .centerCrop()
                    .into(findViewById(R.id.detail_image))
                detailName.text = it.name
                detailOwner.text = it.ownerName
                detailSummary.text = it.summary

                detailQuota.text = (it.registrants?.let { registrants -> it.quota?.minus(registrants) } ?: "N/A").toString()
                detailBeginTime.text = it.beginTime

                detailDescription.text = HtmlCompat.fromHtml(it.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

                val link = it.link
                detailButton.setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                }
            } ?: run {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}