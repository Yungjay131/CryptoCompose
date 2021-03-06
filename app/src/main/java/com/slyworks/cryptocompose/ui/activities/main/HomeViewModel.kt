package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 2:35 PM, 01-Jun-22.
 */
class HomeViewModel(private var dataManager: DataManager) : ViewModel(), IViewModel {
    //region Vars
    private val TAG: String? = HomeViewModel::class.simpleName

    private val _homeStateLiveData:MutableLiveData<Outcome> = MutableLiveData(Outcome.ERROR(null))
    val homeStateLiveData:LiveData<Outcome>
    get() = _homeStateLiveData

    private val _homeDataListLiveData:MutableLiveData<List<CryptoModel>> = MutableLiveData()
    val homeDataListLiveData: LiveData<List<CryptoModel>>
    get() = _homeDataListLiveData as LiveData<List<CryptoModel>>

    private val mSubscriptions = CompositeDisposable()
    //endregion

    fun getData(){
        val d =
            dataManager.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    if(it.isNullOrEmpty()){
                        _homeStateLiveData.postValue(Outcome.FAILURE("you are currently not"+
                        " connected to the internet and have no data saved offline"))
                    }else{
                        _homeStateLiveData.postValue(Outcome.SUCCESS(null))
                        _homeDataListLiveData.postValue(it)
                    }
                },
                {
                    Timber.e(it,"getData: error occurred")
                    _homeStateLiveData.postValue(Outcome.FAILURE("an error occurred while processing your request"))
                }
            )

        mSubscriptions.add(d)
    }


    override fun setItemFavoriteStatus(entity:Int, status:Boolean){
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

    override fun observeNetworkState(): LiveData<Boolean> {
        val l:MutableLiveData<Boolean> = MutableLiveData()
        val d = dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                l.postValue(it)
            }

        mSubscriptions.add(d)

        return l
    }

    override fun onCleared() {
        super.onCleared()
        mSubscriptions.clear()
    }

}