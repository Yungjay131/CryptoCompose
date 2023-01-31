package com.slyworks.cryptocompose.ui.activities.details

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
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 5:31 PM, 17-Jun-22.
 */
class DetailsActivityViewModel(private val dataManager: DataManager) : ViewModel() , IViewModel {
    //region Vars
    private val TAG: String? = DetailsActivityViewModel::class.simpleName

    private val _progressStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val progressStateLiveData:LiveData<Boolean>
    get() = _progressStateLiveData

    private val _networkStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val networkStateLiveData:LiveData<Boolean>
    get() = _networkStateLiveData

    private val _successDataLiveData:MutableLiveData<CryptoModelCombo> = MutableLiveData()
    val successDataLiveData:LiveData<CryptoModelCombo>
    get() = _successDataLiveData

    private val _successStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val successStateLiveData:LiveData<Boolean>
    get() = _successStateLiveData

    private val _failureDataLiveData:MutableLiveData<String> = MutableLiveData()
    val failureDataLiveData:LiveData<String>
    get() = _failureDataLiveData

    private val _failureStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val failureStateLiveData:LiveData<Boolean>
    get() = _failureStateLiveData

    private val _errorDataLiveData:MutableLiveData<String> = MutableLiveData()
    val errorDataLiveData:LiveData<String>
    get() = _errorDataLiveData

    private val _errorStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val errorStateLiveData:LiveData<Boolean>
    get() = _errorStateLiveData

    private val disposables:CompositeDisposable = CompositeDisposable()
    //endregion

    fun getData(query:String){
        if(!dataManager.getNetworkStatus()){
            _networkStateLiveData.setValue(false)
            return
        }

        _failureStateLiveData.setValue(false)
        _successStateLiveData.setValue(false)
        _networkStateLiveData.setValue(true)
        _progressStateLiveData.setValue(true)

        disposables +=
        dataManager.getSpecificCryptocurrency(query, SpecificCryptoSearchType.DETAILS)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
            {
              _progressStateLiveData.postValue(false)

              if(it.isSuccess){
                 _successDataLiveData.postValue(it.getTypedValue<CryptoModelCombo>())
                 _successStateLiveData.postValue(true)
              }else if(it.isFailure){
                  _failureDataLiveData.postValue(it.getTypedValue<String>())
                  _failureStateLiveData.postValue(true)
              }
            },{
               Timber.e("DetailsViewModel#getData: error occurred getting cryptoDetails")
               _errorDataLiveData.postValue("error occurred getting crypto-currency Details")
               _errorStateLiveData.postValue(true)
            })
    }

    override fun setItemFavoriteStatus(entity: Int, status: Boolean) {
        val o: Completable =
            if (status)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity)

        disposables +=
          o.observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({},
                {
                    Timber.e("DetailsViewModel#getData: error occurred getting cryptoDetails")
                    _errorDataLiveData.postValue("error occurred getting crypto-currency Details")
                    _errorStateLiveData.postValue(true)
                })
    }

    override fun observeNetworkState(): LiveData<Boolean> {
        disposables +=
        dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                _networkStateLiveData.postValue(it)
            }

        return networkStateLiveData
    }

    override fun onCleared() {
        super.onCleared()
        unbind()
    }

    fun unbind():Unit = disposables.clear()
}