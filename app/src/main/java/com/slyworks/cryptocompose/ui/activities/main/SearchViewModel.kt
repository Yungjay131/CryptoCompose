package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.plusAssign
import com.slyworks.data.DataManager
import com.slyworks.data.SpecificCryptoSearchType
import com.slyworks.models.CryptoModelCombo
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 *Created by Joshua Sylvanus, 4:02 AM, 16-Jun-22.
 */
class SearchViewModel(private var dataManager: DataManager) : ViewModel(), IViewModel {
    //region Vars
    private val TAG: String? = SearchViewModel::class.simpleName

    val searchObservable: PublishSubject<String> = PublishSubject.create()

    private val _successState:MutableLiveData<Boolean> = MutableLiveData()
    val successState:LiveData<Boolean>
    get() = _successState

    private val _successData:MutableLiveData<CryptoModelCombo> = MutableLiveData()
    val successData:LiveData<CryptoModelCombo>
    get() = _successData

    private val _failureState:MutableLiveData<Boolean> = MutableLiveData()
    val failureState:LiveData<Boolean>
    get() = _failureState

    private val _failureData:MutableLiveData<String> = MutableLiveData()
    val failureData:LiveData<String>
    get() = _failureData

    private val _errorState:MutableLiveData<Boolean> = MutableLiveData()
    val errorState:LiveData<Boolean>
    get() = _errorState

    private val _errorData:MutableLiveData<String> = MutableLiveData()
    val errorData:LiveData<String>
    get() = _errorData

    private val _networkStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val networkStateLiveData:LiveData<Boolean>
    get() = _networkStateLiveData

    private val _progressStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val progressStateLiveData:LiveData<Boolean>
    get() = _progressStateLiveData

    lateinit var query:String

    private val disposables = CompositeDisposable()
    //endregion


    fun initSearch(){
        /*fixme: mutating outside state before subscribe()*/
        disposables +=
            searchObservable
            //.debounce(3_000, TimeUnit.MILLISECONDS)
            .doOnNext{
                _errorState.postValue(false)
                _successState.postValue(false)
                _progressStateLiveData.postValue(true)
            }
            .filter { _ ->
                val status:Boolean = dataManager.getNetworkStatus()
                if(!status){
                   _progressStateLiveData.postValue(false)
                   _networkStateLiveData.postValue(false)
                }

                return@filter status
            }
            .switchMap { _ ->
                dataManager.getSpecificCryptocurrency(query, SpecificCryptoSearchType.SEARCH)
             }
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({
                _progressStateLiveData.postValue(false)

                when{
                    it.isSuccess ->{
                        _successData.postValue(it.getTypedValue<CryptoModelCombo>())
                        _successState.postValue(true)
                    }
                    it.isFailure ->{
                        _failureData.postValue(it.getTypedValue<String>())
                        _failureState.postValue(true)
                    }
                }
            },{
                Timber.e(it, "initSearch: error occurred ${it.message}")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)

            })

    }

    override fun setItemFavoriteStatus(entity:Int,status:Boolean){
       disposables +=
           (if (status)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity))
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({},{
                Timber.e(it, "setItemFavorites: error occurred ${it.message}")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)
            })

    }

    override fun observeNetworkState(): LiveData<Boolean> {
        disposables +=
        dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                _networkStateLiveData.postValue(it)
            },{
                Timber.e(it, "observeNetworkState: error occurred ${it.message}")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)
            })

        return networkStateLiveData
    }

    override fun onCleared() {
        super.onCleared()
        unbind()
    }

    fun unbind():Unit = disposables.clear()
}