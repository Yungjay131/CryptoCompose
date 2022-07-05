package com.slyworks.cryptocompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.slyworks.cryptocompose.App
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.R
import com.slyworks.cryptocompose.ui.activities.main.HomeViewModel
import com.slyworks.cryptocompose.ui.activities.main.MainActivity
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome


/**
 *Created by Joshua Sylvanus, 5:07 AM, 11-Jun-22.
 */

@ExperimentalUnitApi
@Composable
fun HomeMain(viewModel: HomeViewModel){
    val state:State<Outcome?> = viewModel.homeStateLiveData.observeAsState()
    val list: State<List<CryptoModel>?> = viewModel.homeDataListLiveData.observeAsState()
    val progressState:MutableState<Boolean> = remember{ mutableStateOf(true) }


    remember("KEY"){ mutableStateOf(viewModel.getData()) }

    if(progressState.value)
        ProgressBar()


    when{
        state.value!!.isSuccess ->{

            LazyColumn(
                modifier = Modifier.padding(bottom = 70.dp)
                    .fillMaxSize()
            ){
                itemsIndexed(items = list.value!!) { index, item ->
                    CardListItem(entity = item, viewModel)
                }
            }

            progressState.value = false
        }
        state.value!!.isFailure ->{
            progressState.value = false

            ErrorComposable(text = state.value!!.getValue() as String)
        }
        state.value!!.isError -> progressState.value = true
    }
}

@Composable
fun ProgressBar(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center) {

        CircularProgressIndicator(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}

@Composable
fun ErrorComposable(text:String){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
            ) {

        DisplayLottieAnim(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            textAlign = TextAlign.Center,
            text = text)
    }
}

@ExperimentalUnitApi
@Composable
fun CardListItem(entity: CryptoModel,
                 mViewModel: IViewModel,
                 onItemClick:(Int) -> Unit = MainActivity.Companion::navigateToDetailsScreen){
    /*TODO & fixme: make this use Box or ConstraintLayout*/
    Card(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .height(110.dp)
            .clickable(onClick = { onItemClick(entity._id) }),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ){
                Image(
                    painter = rememberImagePainter(
                        data = entity.image.toString(),
                        builder = App.imageRequest
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2F))

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.05F)
                )
                
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.75F)
                        .padding(4.dp)) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.58F)
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.7F)
                                .fillMaxHeight(),
                            fontSize = TextUnit(18F, TextUnitType.Sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = entity.name!! )

                        Text(
                            modifier = Modifier
                                .weight(0.3F)
                                .fillMaxHeight(),
                            fontSize = TextUnit(18F, TextUnitType.Sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = stringResource(id = R.string.rank_placeholder, entity.cmcRank!!) )
                    }


                    Spacer(modifier = Modifier.weight(0.04F))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.38F)
                    ){

                        Text(
                            modifier = Modifier
                                .weight(0.7F)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            fontSize = TextUnit(16F, TextUnitType.Sp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            text = stringResource(id = R.string.price_placeholder, entity.priceUnit, entity.price)
                        )
                        
                        FavoriteIconButton(modifier = Modifier
                            .weight(0.3F)
                            .height(30.dp)
                            .align(Alignment.CenterVertically),
                                           entity = entity,
                                           viewModel = mViewModel)
                    }
                }
            }
    }
}

@Composable
fun FavoriteIconButton(modifier:Modifier,
                       entity:CryptoModel,
                       viewModel: IViewModel){
    var isChecked:Boolean by remember { mutableStateOf(entity.isFavorite) }

    IconToggleButton(
        modifier = modifier,
        checked = isChecked,
        onCheckedChange = {
            isChecked = it
            viewModel.setItemFavoriteStatus(entity._id, it)
        },
    ) {
        Icon(
            imageVector =
            if(isChecked)
                Icons.Default.Favorite
            else
                Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = Color.Magenta )
    }
}

@Composable
fun NetworkStatusNotifier(modifier: Modifier = Modifier,
                          viewModel:IViewModel){
    val networkState:Boolean by viewModel.observeNetworkState().observeAsState(false)

    if(!networkState){
        Row(
            modifier = modifier
                .height(25.dp)
                .fillMaxWidth()
                .background(color = Color.Black),
            horizontalArrangement = Arrangement.Center
        ){
           Image(
               imageVector = Icons.Filled.AccountCircle,
               modifier = Modifier.size(10.dp),
               contentDescription = "")
           Text(text = "not connected")
        }
    }
}
