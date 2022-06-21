package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelDetails
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


/**
 *Created by Joshua Sylvanus, 4:02 AM, 16-Jun-22.
 */
class SearchViewModel(private var dataManager: DataManager) : ViewModel(), IViewModel {
    //region Vars
    private val TAG: String? = SearchViewModel::class.simpleName

    val searchObservable: PublishSubject<String> = PublishSubject.create()

    private val _searchStateLiveData: MutableLiveData<Outcome> = MutableLiveData(Outcome.ERROR(null))
    val searchStateLiveData: LiveData<Outcome>
        get() = _searchStateLiveData

    private val _searchDataListLiveData: MutableLiveData<CryptoModelDetails> = MutableLiveData()
    val searchDataListLiveData: LiveData<CryptoModelDetails>
        get() = _searchDataListLiveData

    private val mSubscriptions = CompositeDisposable()
    //endregion


    fun search(){
        val d = searchObservable
            .debounce(3, TimeUnit.SECONDS)
            .switchMap {
                dataManager.getSpecificCryptocurrency(it)
            }
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                if(it == null) {
                    _searchStateLiveData.postValue(Outcome.FAILURE(value = "no results for query found"))
                } else{
                    _searchStateLiveData.postValue(Outcome.SUCCESS(value = null))
                    _searchDataListLiveData.postValue(it)
                }
            }

        mSubscriptions.add(d)
    }

    fun search(query:String){
        val d =  dataManager.getSpecificCryptocurrency(query)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                if(it == null) {
                    _searchStateLiveData.postValue(Outcome.FAILURE(value = "no results for query found"))
                } else{
                    _searchStateLiveData.postValue(Outcome.SUCCESS(value = null))
                    _searchDataListLiveData.postValue(it)
                }
            }

        mSubscriptions.add(d)
    }

    override fun setItemFavoriteStatus(entity:CryptoModel,status:Boolean){
        val o: Completable =
            if (status)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity)

        val d  = o.observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe()

        mSubscriptions.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        mSubscriptions.clear()
    }
}