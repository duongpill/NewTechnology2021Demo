package com.duongnh.beertestdemo.ui.beer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.duongnh.beertestdemo.base.adapter.BaseViewModel
import com.duongnh.beertestdemo.commons.ExceptionHandler
import com.duongnh.beertestdemo.mappers.toPresentation
import com.duongnh.beertestdemo.models.BeerPresentation
import com.duongnh.beertestdemo.models.Error
import com.duongnh.domain.models.BeerRequest
import com.duongnh.domain.usecases.GetBeersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BeersViewModel @Inject constructor(
    private val getBeersUseCase: GetBeersUseCase
): BaseViewModel() {

    //region Members

    private var beersJob: Job? = null

    val beersViewState: LiveData<BeersViewState>
        get() = _beersViewState

    private var _beersViewState = MutableLiveData<BeersViewState>()

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val message = ExceptionHandler.parse(exception)
        _beersViewState.value = _beersViewState.value?.copy(error = Error(message))
    }

    //endregion

    // region Constructor

    init {
        _beersViewState.value =
            BeersViewState(
                isLoading = false,
                error = null,
                beers = null
            )
    }

    // endregion

    // region Android API

    override fun onCleared() {
        super.onCleared()
        beersJob?.cancel()
    }

    // endregion

    // region Public API

    fun getBeers(page: Int, perPage: Int) {
        beersJob = launchCoroutine {
            onBeersLoading()
            loadBeers(page, perPage)
        }
    }

    // endregion

    // region Private API

    private fun onBeersLoading() {
        _beersViewState.value = _beersViewState.value?.copy(isLoading = true)
    }

    private fun onBeersLoadingComplete(beers: List<BeerPresentation>) {
        _beersViewState.value =
            _beersViewState.value?.copy(isLoading = false, beers = beers)
    }

    private suspend fun loadBeers(page: Int, perPage: Int) {
        getBeersUseCase(BeerRequest(page, perPage)).collect { res ->
            val beers = res.map {
                it.toPresentation()
            }
            onBeersLoadingComplete(beers)
        }
    }

    // endregion
}