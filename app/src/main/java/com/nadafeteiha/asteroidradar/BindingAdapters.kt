package com.nadafeteiha.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.nadafeteiha.asteroidradar.domain.Asteroid
import com.nadafeteiha.asteroidradar.domain.PictureOfDay
import com.nadafeteiha.asteroidradar.ui.AsteroidAdapter
import com.nadafeteiha.asteroidradar.ui.main.ApiStatus
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription =
            imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("urlImage")
fun bindImageView(imgView: ImageView, imageData: PictureOfDay?) {
    if (imageData != null && imageData.mediaType == "image") {
        imageData.url.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            Picasso.get()
                .load(imgUri)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(imgView)
        }
    } else {
        imgView.setImageResource(R.drawable.ic_broken_image)
    }
}

@BindingAdapter("listAsteroid","visibilityRecycler")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?, status: ApiStatus) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)

    when (status) {
        ApiStatus.LOADING -> {
            recyclerView.visibility = View.GONE
        }
        else -> {
            recyclerView.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("visibilityProgress")
fun bindProgressView(progress: ProgressBar, status: ApiStatus) {
    when (status) {
        ApiStatus.LOADING -> {
            progress.visibility = View.VISIBLE
        }
        else -> {
            progress.visibility = View.GONE
        }
    }
}
