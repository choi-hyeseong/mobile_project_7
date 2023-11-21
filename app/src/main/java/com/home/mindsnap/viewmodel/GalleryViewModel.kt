package com.home.mindsnap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.model.Image
import com.home.mindsnap.usecase.GetAllImages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel(private val getAllImages: GetAllImages) : ViewModel() {

    val imageLiveData : MutableLiveData<List<Image>> = MutableLiveData()

    init {
        getAllImage()
    }

    private fun getAllImage() {
        CoroutineScope(Dispatchers.IO).launch {
            imageLiveData.postValue(getAllImages.getAllImages())
        }
    }
}