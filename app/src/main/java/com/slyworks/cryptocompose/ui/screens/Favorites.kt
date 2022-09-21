package com.slyworks.cryptocompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
    val successData:State<List<CryptoModel>?> = viewModel.successData.observeAsState()
    val successState:State<Boolean> = viewModel.successState.observeAsState(initial = false)
    val noDataState:State<Boolean> = viewModel.noDataState.observeAsState(initial = false)
    val noNetworkState:State<Boolean> = viewModel.noNetworkState.observeAsState(initial = false)
    val errorData:State<String?> = viewModel.errorData.observeAsState()
    val errorState:State<Boolean> = viewModel.errorState.observeAsState(initial = false)
    val progressState:State<Boolean> = viewModel.progressState.observeAsState(initial = true)

    val lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
    val latestLifecycleEvent:MutableState<Lifecycle.Event> = remember{ mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(key1 = "KEY"){
        val observer: LifecycleEventObserver = LifecycleEventObserver{ _, event: Lifecycle.Event ->
            latestLifecycleEvent.value = event
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    if(latestLifecycleEvent.value == Lifecycle.Event.ON_RESUME)
        remember("KEY"){ mutableStateOf(viewModel.getFavorites()) }

    if(latestLifecycleEvent.value == Lifecycle.Event.ON_PAUSE)
        remember("KEY") { mutableStateOf(viewModel.unbind()) }

    //remember("KEY") { mutableStateOf(viewModel.getFavorites()) }

    when{
        progressState.value -> ProgressBar()
        successState.value -> FavoritesList(viewModel = viewModel, items = successData.value!!)
        noNetworkState.value -> NoInternetComposable()
        errorState.value -> ErrorComposable(text = errorData.value!!)
        noDataState.value ->{
            ErrorComposable(text = "you have no favorites at the moment."+
                    "Check the \"Favorite\" icon to add an item to your Favorites")
        }
    }

}

@ExperimentalUnitApi
@Composable
fun FavoritesList(modifier:Modifier = Modifier,
                  viewModel: FavoritesViewModel,
                  items:List<CryptoModel>){

    LazyColumn {
        itemsIndexed(items = items) { index, item ->
            CardListItem(entity = item, viewModel)
            Spacer(modifier = modifier.height(4.dp))
        }
    }
}