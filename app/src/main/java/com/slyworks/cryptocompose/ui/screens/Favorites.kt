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
    val successData:State<List<CryptoModel>?> = viewModel.successData.observeAsState()
    val successState:State<Boolean> = viewModel.successState.observeAsState(initial = false)
    val noDataState:State<Boolean> = viewModel.noDataState.observeAsState(initial = false)
    val noNetworkState:State<Boolean> = viewModel.noNetworkState.observeAsState(initial = false)
    val errorData:State<String?> = viewModel.errorData.observeAsState()
    val errorState:State<Boolean> = viewModel.errorState.observeAsState(initial = false)
    val progressState:State<Boolean> = viewModel.progressState.observeAsState(initial = true)

    remember("KEY") { mutableStateOf(viewModel.getFavorites()) }

    when{
        progressState.value ->{
            ProgressBar()
        }
        successState.value ->{
            FavoritesList(viewModel = viewModel, items = successData.value!!)
        }
        noDataState.value ->{
            ErrorComposable(text = "you have no favorites at the moment."+
                    "Check the \"Favorite\" icon to add an item to your Favorites")
        }
        noNetworkState.value ->{
            NoInternetComposable()
        }
        errorState.value ->{
            ErrorComposable(text = errorData.value!!)
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