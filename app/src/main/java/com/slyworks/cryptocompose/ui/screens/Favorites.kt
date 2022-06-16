package com.slyworks.cryptocompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.slyworks.cryptocompose.ui.activities.main.FavoritesViewModel
import com.slyworks.cryptocompose.ui.activities.main.HomeViewModel
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome


/**
 *Created by Joshua Sylvanus, 4:42 AM, 14-Jun-22.
 */

@ExperimentalUnitApi
@Composable
fun FavoriteMain(viewModel: FavoritesViewModel){
    val state: State<Outcome?> = viewModel.favoriteStateLiveData.observeAsState()
    val list: State<List<CryptoModel>?> = viewModel.favoriteDataListLiveData.observeAsState()
    val progressState: MutableState<Boolean> = remember{ mutableStateOf(true) }

    viewModel.getFavorites()

    if(progressState.value){
       ProgressBar()
    }

    when{
        state.value!!.isSuccess ->{
            progressState.value = false

            LazyColumn {
                itemsIndexed(items = list.value!!) { index, item ->
                    CardListItem(entity = item, viewModel)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        state.value!!.isFailure ->{
            progressState.value = false

            ErrorComposable(text = state.value!!.getValue() as String)
        }
        state.value!!.isError -> progressState.value = true
    }

}