package com.slyworks.cryptocompose.ui.screens.main_activity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.airbnb.lottie.compose.*
import com.slyworks.cryptocompose.R
import com.slyworks.cryptocompose.ui.activities.main.SearchViewModel
import com.slyworks.cryptocompose.ui.screens.details_activity.DetailsScrollColumn
import com.slyworks.cryptocompose.ui.screens.details_activity.ErrorComposable2
import com.slyworks.cryptocompose.ui.screens.details_activity.NoInternetComposable
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
    val progressState:State<Boolean> = viewModel.progressStateLiveData.observeAsState(initial = true)
    val noNetworkState:State<Boolean> = viewModel.noNetworkStateLiveData.observeAsState(initial = false)

    val lifecycle:Lifecycle = LocalLifecycleOwner.current.lifecycle
    val latestLifecycleEvent:MutableState<Lifecycle.Event> = remember{ mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(key1 = "KEY"){
        val observer:LifecycleEventObserver = LifecycleEventObserver{ _, event:Lifecycle.Event ->
            latestLifecycleEvent.value = event
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    /*if(latestLifecycleEvent.value == Lifecycle.Event.ON_CREATE)
        remember("KEY"){ mutableStateOf(viewModel.initSearch()) }*/

    if(latestLifecycleEvent.value == Lifecycle.Event.ON_DESTROY)
        remember("KEY"){ mutableStateOf(viewModel.unbind()) }

    Column(modifier = Modifier
        .padding(bottom = 70.dp, top = 12.dp)
        .fillMaxSize()) {
        SearchViewComposable(
            onSearchTextChanged = searchFunc@{
                if (it.trim().isEmpty())
                    return@searchFunc

                val query = it.trim()
                viewModel.query = query
                viewModel.searchObservable.accept(query)
            },
            onClearClick = {
                viewModel.query = ""
            })

        Spacer(modifier = Modifier.height(16.dp))

           when {
             progressState.value -> ProgressBar()
             successState.value -> DetailsScrollColumn(viewModel = viewModel, entity = successData.value!!)
             noNetworkState.value -> NoInternetComposable()
             failureState.value -> NoResultFoundComposable(text = failureData.value)
             errorState.value -> ErrorComposable2(text = errorData.value!!)
           }

        }
    }

@ExperimentalComposeUiApi
@Composable
fun SearchViewComposable(modifier:Modifier = Modifier,
                         onSearchTextChanged:(String) -> Unit,
                         onClearClick:(String) -> Unit,
                         placeHolderText:String = "search for a cryptocurrency"){

    var showClearButton:Boolean by remember{ mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    var isFocused:Boolean by remember{ mutableStateOf(true) }

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }

    var state:String by remember{ mutableStateOf("") }

    ConstraintLayout(modifier = modifier
        .padding(start = 8.dp, end = 8.dp, bottom = 70.dp)
        .height(55.dp)
        .fillMaxWidth()) {

        val (outlinedTextField, iconButton) = createRefs()

        OutlinedTextField(
            modifier = Modifier
                .constrainAs(outlinedTextField){
                    start.linkTo(parent.start)
                    end.linkTo(iconButton.start, margin = 8.dp)
                    top.linkTo(parent.top)
                }
                .height(55.dp)
                .border(
                    border = BorderStroke(if(isFocused) 2.dp else 1.dp,
                                          if(isFocused) Color.Blue else Color.Black),
                    shape = RoundedCornerShape(8.dp))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    showClearButton = focusState.isFocused
                }
                .focusRequester(focusRequester),
            value = state,
            onValueChange = {
                state = it.trim()

                /*if(!check(it))
                return@OutlinedTextField

           onSearchTextChanged(it.trim())*/
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
                    exit = fadeOut()
                )
                {
                    IconButton(onClick = {
                        onClearClick(state)
                        state = ""
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = ""
                        )
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

        Spacer(modifier = Modifier.width(6.dp))

        IconButton(modifier = Modifier.constrainAs(iconButton){
                                         end.linkTo(parent.end)
                                         top.linkTo(parent.top)
                                         bottom.linkTo(parent.bottom)
                                      }
                                      .size(32.dp),
                   onClick = { onSearchTextChanged(state) }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = ""
            )
        }
    }
}

@ExperimentalUnitApi
@Composable
fun NoResultFoundComposable(text:String? = null){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        DisplayLottieAnim(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = TextUnit(20F, TextUnitType.Sp),
            text = text ?: "No matches found")
    }
}

@Composable
fun DisplayLottieAnim(resourceId:Int = R.raw.not_found_2,
                      modifier: Modifier){
    val animationSpec by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resourceId))
    LottieAnimation(
        modifier = modifier,
        composition = animationSpec,
        iterations = 100,
    )
}
