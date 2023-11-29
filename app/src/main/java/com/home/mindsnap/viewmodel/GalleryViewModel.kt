package com.home.mindsnap.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.model.Image
import com.home.mindsnap.usecase.GetAllImages
import com.home.mindsnap.usecase.ShareImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel(private val getAllImages: GetAllImages, private val shareImage: ShareImage) : ViewModel() {

    val imageLiveData : MutableLiveData<List<Image>> = MutableLiveData()
    val intentLiveData : MutableLiveData<Intent> = MutableLiveData()

    init {
        getAllImage()
    }

    private fun getAllImage() {
        CoroutineScope(Dispatchers.IO).launch {
            imageLiveData.postValue(getAllImages.getAllImages())
        }
    }

    fun shareImage(fileName : String) {
        shareImage.shareImage(fileName)
    }
}