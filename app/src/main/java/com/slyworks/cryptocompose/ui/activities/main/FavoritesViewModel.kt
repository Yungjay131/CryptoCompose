package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 *Created by Joshua Sylvanus, 4:04 AM, 16-Jun-22.
 */
class FavoritesViewModel(private var dataManager:DataManager) : ViewModel(), IMainActivityViewModel {
    //region Vars
    private val TAG: String? = FavoritesViewModel::class.simpleName

    private val _favoriteStateLiveData: MutableLiveData<Outcome> = MutableLiveData(Outcome.ERROR(null))
    val favoriteStateLiveData: LiveData<Outcome>
        get() = _favoriteStateLiveData

    private val _favoriteDataListLiveData: MutableLiveData<List<CryptoModel>> = MutableLiveData()
    val favoriteDataListLiveData: LiveData<List<CryptoModel>>
        get() = _favoriteDataListLiveData

    private val mSubscriptions = CompositeDisposable()
    //endregion

    fun getFavorites(){
       val d = dataManager.getFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                if(it.isNullOrEmpty()){
                    _favoriteStateLiveData.postValue(Outcome.FAILURE("you currently have"+
                            " no favorites, check the \"Favorite icon\" to add an item to favorites"))
                }else{
                    _favoriteStateLiveData.postValue(Outcome.SUCCESS(null))
                    _favoriteDataListLiveData.postValue(it)
                }
            }

        mSubscriptions.add(d)
    }

    override fun setItemFavoriteStatus(entity:CryptoModel,state:Boolean){
        val o: Observable<Boolean> =
            if (state)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity)

        val d  = o.observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe { _ -> }

        mSubscriptions.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        mSubscriptions.clear()
    }
}