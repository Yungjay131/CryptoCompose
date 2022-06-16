package com.slyworks.cryptocompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.slyworks.cryptocompose.R
import com.slyworks.cryptocompose.ui.activities.main.HomeViewModel
import com.slyworks.cryptocompose.ui.activities.main.IMainActivityViewModel
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

    viewModel.getData()

    HomeContent(state,list,progressState,viewModel)
}

@Composable
fun HomeContent(state:State<Outcome?>,
                dataListState:State<List<CryptoModel>?>,
                progressState:MutableState<Boolean>,
                viewModel: HomeViewModel){
    if(progressState.value) {
        ProgressBar()
    }

    when{
        state.value!!.isSuccess ->{
            progressState.value = false

            LazyColumn {
                itemsIndexed(items = dataListState.value!!) { index, item ->
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
@Preview
fun ErrorComposable(text:String = ""){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight(0.6F)
            .fillMaxWidth()
            .padding(16.dp)
            ) {

        /*Image(
            modifier = Modifier
                .weight(0.8F)
                .fillMaxWidth(),
            imageVector = Icons.Default.Warning,
            contentDescription = "" )*/

        DisplayLottieAnim(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = text)
    }
}

@ExperimentalUnitApi
@Composable
fun CardListItem(entity: CryptoModel,
                 mViewModel: IMainActivityViewModel
){
    /*TODO & fixme: make this use Box or ConstraintLayout*/
    
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(110.dp),
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
                        builder = {
                            scale(Scale.FILL)
                            placeholder(R.drawable.ic_placeholder)
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3F))

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
                        .weight(0.65F)
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
                                .weight(0.8F)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            fontSize = TextUnit(16F, TextUnitType.Sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = stringResource(id = R.string.price_placeholder, entity.priceUnit!!, entity.price!!)
                        )
                        
                        IconToggleButton(
                            modifier = Modifier
                                .weight(0.2F)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            checked = entity.isFavorite,
                            onCheckedChange = { mViewModel.setItemFavoriteStatus(entity, it) },
                            ) {
                               Icons.Default.Favorite
                            }
                    }
                }
            }
    }
}
