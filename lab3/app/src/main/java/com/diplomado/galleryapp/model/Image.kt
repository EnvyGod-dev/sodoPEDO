package com.diplomado.galleryapp.model


data class Image(
  var id: String,
  var author: String,
  var width: Double,
  var height: Double,
  var url: String,
  var download_url: String,
) : java.io.Serializable