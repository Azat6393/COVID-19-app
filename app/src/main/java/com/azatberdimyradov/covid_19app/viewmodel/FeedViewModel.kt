package com.azatberdimyradov.covid_19app.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.azatberdimyradov.covid_19app.model.CommonModel
import com.azatberdimyradov.covid_19app.service.CovidService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FeedViewModel @ViewModelInject constructor(
    private val covidService: CovidService,
    application: Application
) : BaseViewModel(application) {

    private val disposable = CompositeDisposable()
    val commonModel = MutableLiveData<CommonModel>()

    private val feedEventChannel = Channel<FeedEvent>()
    val feedEvent = feedEventChannel.receiveAsFlow()

    fun getData() = viewModelScope.launch {
        disposable.add(
            covidService.getDataFromApi()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CommonModel>() {
                    override fun onSuccess(t: CommonModel) {
                        commonModel.value = t
                        viewModelScope.launch { feedEventChannel.send(FeedEvent.OnSuccess) }
                    }

                    override fun onError(e: Throwable) {
                        viewModelScope.launch { feedEventChannel.send(FeedEvent.OnError(e.message.toString())) }
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    sealed class FeedEvent {
        object OnSuccess : FeedEvent()
        data class OnError(val message: String) : FeedEvent()
    }
}