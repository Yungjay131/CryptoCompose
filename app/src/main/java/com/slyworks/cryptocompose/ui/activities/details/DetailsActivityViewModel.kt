package com.slyworks.cryptocompose.ui.activities.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.data.DataManager
import com.slyworks.data.SpecificCryptoSearchType
import com.slyworks.models.CryptoModelCombo
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 5:31 PM, 17-Jun-22.
 */
class DetailsActivityViewModel(private var dataManager: DataManager) : ViewModel() , IViewModel {
    //region Vars
    private val TAG: String? = DetailsActivityViewModel::class.simpleName

    private val _detailsMessageLiveData:MutableLiveData<String> = MutableLiveData()
    val detailsMessageLiveData: LiveData<String>
    get() = _detailsMessageLiveData

    private val _detailsStateLiveData:MutableLiveData<Outcome> = MutableLiveData(Outcome.ERROR(null))
    val detailsStateLiveData:LiveData<Outcome>
    get() = _detailsStateLiveData

    private val _detailsDataLiveData:MutableLiveData<CryptoModelCombo> = MutableLiveData()
    val detailsDataLiveData:LiveData<CryptoModelCombo>
    get() = _detailsDataLiveData

    private val mSubscriptions:CompositeDisposable = CompositeDisposable()
    //endregion

    fun getData(query:String){
        if(!dataManager.getNetworkStatus()){
            _detailsStateLiveData.postValue(
                Outcome.FAILURE(value = 0, reason = "you are currently not connected to the internet")
            )
            return
        }

        val d = dataManager.getSpecificCryptocurrency(query, SpecificCryptoSearchType.DETAILS)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                if(it != null) {
                    _detailsStateLiveData.postValue(Outcome.SUCCESS(null))
                    _detailsDataLiveData.postValue(it)
                }else{
                    _detailsStateLiveData.postValue(Outcome.FAILURE(value = 1, reason="no results found for search query"))
                }
            },{
                Timber.e("DetailsViewModel#getData: error occurred getting cryptoDetails")
                _detailsStateLiveData.postValue(Outcome.FAILURE(value = 2,reason = "an error occurred while processing your request"))
            })

        mSubscriptions.add(d)
    }

    override fun setItemFavoriteStatus(entity: Int, status: Boolean) {
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