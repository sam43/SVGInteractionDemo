package com.sam43.svginteractiondemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import okhttp3.ResponseBody

class FileDownloaderVM (application: Application) : AndroidViewModel(application) {
    fun downloadFileFromServer(url: String): LiveData<ResponseBody> {
        // svg map of USA -> https://upload.wikimedia.org/wikipedia/commons/1/1a/Blank_US_Map_(states_only).svg
        // Using the custom svg file -> https://svgshare.com/i/Gzd.svg [Thanks to SVG Share website]
        return Repository.downloadFileFromServer(url)
    }
}