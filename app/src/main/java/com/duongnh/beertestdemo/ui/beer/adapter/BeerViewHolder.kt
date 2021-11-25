package com.duongnh.beertestdemo.ui.beer.adapter

import coil.load
import coil.transform.CircleCropTransformation
import com.duongnh.beertestdemo.R
import com.duongnh.beertestdemo.base.adapter.BaseViewHolder
import com.duongnh.beertestdemo.databinding.BeerItemBinding
import com.duongnh.beertestdemo.models.BeerPresentation

class BeerViewHolder(private val binding: BeerItemBinding): BaseViewHolder(binding) {
    fun bind(beer: BeerPresentation?){
        binding.textName.text = beer?.name
        binding.textTagline.text = beer?.tagline
        val size = binding.root.resources.getDimension(R.dimen.size_image_beer).toInt()
        binding.imgBeer.load(beer?.imageUrl){
            size(size, size)
            crossfade(true)
            placeholder(R.drawable.image_place_holder)
            transformations(CircleCropTransformation())
            error(R.drawable.image_place_holder)
        }
    }
}