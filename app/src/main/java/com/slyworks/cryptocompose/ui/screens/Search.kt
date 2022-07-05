package com.slyworks.cryptocompose.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.airbnb.lottie.compose.*
import com.slyworks.cryptocompose.R
import com.slyworks.cryptocompose.ui.activities.main.SearchViewModel
import com.slyworks.models.CryptoModelCombo
import java.util.regex.Pattern


/**
 *Created by Joshua Sylvanus, 1:29 AM, 13-Jun-22.
 */

fun check(query:String):Boolean = !Pattern.matches("[0-9.,]", query)

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun SearchMain(viewModel: SearchViewModel){
    val successState:State<Boolean> = viewModel.successState.observeAsState(initial = false)
    val successData:State<CryptoModelCombo?> = viewModel.successData.observeAsState()
    val failureState:State<Boolean> = viewModel.failureState.observeAsState(initial = false)
    val failureData:State<String?> = viewModel.failureData.observeAsState()
    val errorState:State<Boolean> = viewModel.errorState.observeAsState(initial = false)
    val errorData:State<String?> = viewModel.errorData.observeAsState()
    val progressState:State<Boolean> = viewModel.progressState.observeAsState(initial = false)

    remember("KEY"){ mutableStateOf(viewModel.initSearch()) }

    Column {
        SearchViewComposable(
            onSearchTextChanged = {
                viewModel.progressState.value = true
                viewModel.searchObservable.onNext(it)
            },
            onClearClick = {
                viewModel.progressState.value = true
                viewModel.searchObservable.onNext(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

           when {
             progressState.value ->{
                ProgressBar()
             }
             successState.value ->{
                DetailsScrollColumn(viewModel = viewModel, entity = successData.value!!)
             }
             failureState.value ->{
                 NoResultFoundComposable()
             }
             errorState.value ->{
                 ErrorComposable2(text = errorData.value!!)
             }
           }

        }
    }

@Composable
fun SearchMain(modifier: Modifier = Modifier,
               entity:CryptoModelCombo){

    val scrollState = rememberScrollState()

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {

    }
}
@ExperimentalComposeUiApi
@Composable
fun SearchViewComposable(onSearchTextChanged:(String) -> Unit,
                         onClearClick:(String) -> Unit,
                         placeHolderText:String = "search for a cryptocurrency"){

    var showClearButton:Boolean by remember{ mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }

    var state:String by remember{ mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(vertical = 2.dp)
            .onFocusChanged { focusState ->
                showClearButton = focusState.isFocused
            }
            .focusRequester(focusRequester),
        value = state,
        onValueChange ={
           state = it

            if(!check(it))
                return@OutlinedTextField

           onSearchTextChanged(it.trim())
        },
        placeholder = {
            Text(text = placeHolderText)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        ),
        trailingIcon = {
            AnimatedVisibility(
                visible = showClearButton,
                enter = fadeIn(),
                exit = fadeOut())
            {
                IconButton(onClick = { onClearClick(state) }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "" )
                }
            }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
            onSearchTextChanged(state)
        })
       )
}

/*TODO: add lottie json here ( find how - maybe a .gif)*/
@ExperimentalUnitApi
@Composable
fun NoResultFoundComposable(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        DisplayLottieAnim(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = TextUnit(20F, TextUnitType.Sp),
            text = "No matches found")
    }
}

@Composable
fun DisplayLottieAnim(resourceId:Int = R.raw.not_found,
                      modifier: Modifier){
    val animationSpec by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resourceId))
    LottieAnimation(
        modifier = modifier,
        composition = animationSpec,
        iterations = 100,
    )
}

@ExperimentalUnitApi
@Composable
fun NetworkUnAvailable(){
    Row {
       Text(
           modifier = Modifier.align(Alignment.CenterVertically),
           fontSize = TextUnit(20F, TextUnitType.Sp),
           text = "no network connectivity available" )
    }
}