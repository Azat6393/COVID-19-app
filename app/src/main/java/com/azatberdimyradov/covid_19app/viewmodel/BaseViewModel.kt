package com.azatberdimyradov.covid_19app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.azatberdimyradov.covid_19app.viewmodel.FeedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel (application: Application): AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}