package com.slyworks.cryptocompose.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.rememberImagePainter
import com.slyworks.cryptocompose.App
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.R
import com.slyworks.cryptocompose.ui.activities.details.DetailsActivityViewModel
import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelCombo
import com.slyworks.models.Outcome


/**
 * Created by Joshua Sylvanus, 3:00 PM, 17-Jun-22.
 */

private fun String.parseTags():List<String> = this.split(",")

@Composable
fun DetailMain(viewModel: IViewModel, entityID:String){
    val vModel = (viewModel as DetailsActivityViewModel)

    val successDataState:State<CryptoModelCombo?> = vModel.successDataLiveData.observeAsState()
    val successState:State<Boolean> = vModel.successStateLiveData.observeAsState(initial = false)
    val failureDataState:State<String?> = vModel.failureDataLiveData.observeAsState()
    val failureState:State<Boolean> = vModel.failureStateLiveData.observeAsState(initial = false)
    val errorDataState:State<String?> = vModel.errorDataLiveData.observeAsState()
    val errorState:State<Boolean> = vModel.errorStateLiveData.observeAsState(initial = false)
    val progressState:State<Boolean> = vModel.progressStateLiveData.observeAsState(initial = true)
    val networkState:State<Boolean> = vModel.networkStateLiveData.observeAsState(initial = false)

    val lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
    val latestLifecycleEvent: MutableState<Lifecycle.Event> = remember{ mutableStateOf(Lifecycle.Event.ON_ANY) }
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
        remember("KEY"){ mutableStateOf(viewModel.getData(entityID)) }

    if(latestLifecycleEvent.value == Lifecycle.Event.ON_PAUSE)
        remember("KEY"){ mutableStateOf(viewModel.unbind()) }

   Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
       /*TODO:show favorite icon on CollapsingToolBarLayout*/

       when{
           progressState.value -> ProgressBar()
           successState.value -> DetailsScrollColumn(viewModel = viewModel,
                                                     entity = successDataState.value!!)
           !networkState.value -> NoInternetComposable()
           failureState.value -> NoResultsFoundComposable()
           errorState.value -> ErrorComposable(text = errorDataState.value!!)
       }
    }
}

@Composable
fun ErrorComposable2(text:String){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        DisplayLottieAnim(
            resourceId = R.raw.error,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            textAlign = TextAlign.Center,
            text = text)
    }
}
@Composable
fun NoResultsFoundComposable(){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        DisplayLottieAnim(
            resourceId = R.raw.not_found_2,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "no results found")
    }
}
@Composable
fun NoInternetComposable(){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        DisplayLottieAnim(
            resourceId = R.raw.no_internet_1,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "internet connection unavailable")
    }
}

@Composable
fun DetailsScrollColumn(viewModel:IViewModel,
                        entity:CryptoModelCombo){

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxHeight()
                              .fillMaxWidth()
                              .verticalScroll(scrollState),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = rememberImagePainter(
                data = entity.model.image,
                builder = App.imageRequest,
            ),
            contentDescription = "cryptocurrency's logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Image(
            painter = rememberImagePainter(
                data = entity.model.image,
                builder = App.imageRequest,
            ),
            contentDescription = "cryptocurrency's logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Image(
            painter = rememberImagePainter(
                data = entity.model.image,
                builder = App.imageRequest,
            ),
            contentDescription = "cryptocurrency's logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Image(
            painter = rememberImagePainter(
                data = entity.model.image,
                builder = App.imageRequest,
            ),
            contentDescription = "cryptocurrency's logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Image(
            painter = rememberImagePainter(
                data = entity.model.image,
                builder = App.imageRequest,
            ),
            contentDescription = "cryptocurrency's logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Image(
            painter = rememberImagePainter(
                data = entity.model.image,
                builder = App.imageRequest,
            ),
            contentDescription = "cryptocurrency's logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Image(
            painter = rememberImagePainter(
                data = entity.model.image,
                builder = App.imageRequest,
            ),
            contentDescription = "cryptocurrency's logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        /*TextField(label = "Name", entity.model.name)
        TextField(label = "Description", entity.details.description)
        TextField(label = "Symbol", entity.model.symbol)
        TextField(label = "Cmc Rank", entity.model.cmcRank.toString())
        TextField(label = "Price Unit", entity.model.priceUnit)
        TextField(label = "Price", entity.model.price.toString())
        TextField(label = "Max Supply", entity.model.maxSupply.toString())
        TextField(label = "Circulating Supply", entity.model.circulatingSupply.toString())
        TextField(label = "Total Supply", entity.model.totalSupply.toString())
        TextField(label = "MarketCap", entity.model.marketCap.toString())
        TextField(label = "Date Added", entity.model.dateAdded)
        TagLayout(tags = entity.model.tags.parseTags())
        ButtonFavorites(viewModel = viewModel, entity = entity.model)*/
    }
}

@Composable
fun ButtonFavorites(viewModel: IViewModel,
                    entity: CryptoModel){
    IconToggleButton(
        modifier = Modifier
            .height(50.dp)
            .wrapContentWidth()
            .padding(6.dp),
        checked = entity.isFavorite,
        onCheckedChange = {
            viewModel.setItemFavoriteStatus(entity = entity._id, status = it)
        }) {
        Icon(
            tint = Color.Magenta,
            imageVector = Icons.Default.Favorite,
            contentDescription = "")
    }
}
@Composable
fun TextField(label:String,
              value:String){

    OutlinedTextField(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                           .fillMaxWidth()
                           .height(55.dp),
        readOnly = true,
        value = value,
        onValueChange = {},
        label = {
            Text(text = label)
        })
}

@Composable
fun TagView(tag:String){
     Row(modifier = Modifier
         .border(
             border = BorderStroke(1.dp, Color.Blue),
             shape = RoundedCornerShape(corner = CornerSize(6.dp))
         )
         .height(45.dp)
         .wrapContentWidth()
         .padding(4.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically) {
         
         Text(text = tag)    
     }
}

@Composable
fun TagLayout(tags:List<String>){

    CustomLayout {
        for(t in tags)
            TagView(tag = t)
    }
}

@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit){

    Layout(modifier = modifier.padding(start = 8.dp, end = 8.dp)
                              .fillMaxWidth()
                              .wrapContentHeight(),
           measurePolicy = customLayoutMeasurePolicy(),
           content = content)
}

fun customLayoutMeasurePolicy():MeasurePolicy{
    return MeasurePolicy{ measurables, constraints ->
        val placeables:List<Placeable> = measurables.map{ measurable ->
            measurable.measure(constraints)
        }

        layout(
            constraints.maxWidth,
            constraints.maxHeight){
            var yPos = 0
            var xPos = 0
            var maxY = 0

            placeables.forEach { placeable ->
                /*moving to next "line" since item won't fit on this line*/
                if(xPos + placeable.width > constraints.maxWidth){
                   xPos = 0
                   yPos += maxY
                   maxY = 0
                }

                placeable.placeRelative(
                    x = xPos,
                    y = yPos )

                xPos += placeable.width
                if(maxY < placeable.height){
                    maxY = placeable.height
                }
            }
        }
    }
}