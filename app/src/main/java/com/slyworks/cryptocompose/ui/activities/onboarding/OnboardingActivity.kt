package com.slyworks.cryptocompose.ui.activities.onboarding

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.slyworks.cryptocompose.Navigator
import com.slyworks.cryptocompose.R
import com.slyworks.cryptocompose.appComp
import com.slyworks.cryptocompose.ui.activities.main.MainActivity
import com.slyworks.cryptocompose.ui.activities.onboarding.ui.theme.CryptoComposeTheme
import com.slyworks.cryptocompose.ui.theme.Shapes
import javax.inject.Inject

class OnboardingActivity : ComponentActivity() {
    //region Vars
    @Inject
    lateinit var viewModel:OnboardingActivityViewModel
    //endregion

    companion object{
        var _this:OnboardingActivity? = null

        @ExperimentalUnitApi
        fun navigateToMainActivity(){
           Navigator.intentFor<MainActivity>(_this as Context)
               .newAndClearTask()
               .finishCaller()
               .navigate()
        }
    }

    override fun onStart() {
        super.onStart()
        _this = this
    }

    override fun onStop() {
        super.onStop()
        _this = null
    }

    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initViews()
    }

    private fun initData(){
       application.appComp
           .activityComponentBuilder()
           .componentActivity(this)
           .build()
           .inject(this)

        viewModel.startSequence()
    }

    @ExperimentalUnitApi
    private fun initViews(){
          setContent {
              CryptoComposeTheme {
                  Surface(color = MaterialTheme.colors.background) {
                    OnboardingMain(viewModel = viewModel)
                  }
              }
          }
    }
}

@ExperimentalUnitApi
@Composable
fun OnboardingMain(viewModel: OnboardingActivityViewModel){
    val progressState1:Float by viewModel.progressState1.observeAsState(initial = 0.1F)
    val progressState2:Float by viewModel.progressState2.observeAsState(initial = 0.0F)
    val progressState3:Float by viewModel.progressState3.observeAsState(initial = 0.0F)

    val currentId:Int by viewModel.tabToTint.observeAsState(initial = 0)

    val nextScreen = { viewModel.onboardingObservableNext.onNext(10L) }
    val previousScreen = { viewModel.onboardingObservablePrevious.onNext(10L) }

    val imageIdFunc:()-> Int = {
        when(currentId){
            0 -> R.drawable.onboarding_1
            1 -> R.drawable.onboarding_2
            2 -> R.drawable.onboarding_3
            else -> throw UnsupportedOperationException()
        }
    }

    Column(modifier = Modifier
        .systemBarsPadding()
        .padding(top = 16.dp)
        .fillMaxSize()) {
          OnboardingProgressBars(progress1 = progressState1,
                                 progress2 = progressState2,
                                 progress3 = progressState3)
          OnboardingScreen(
              imageId = currentId,
              text = "get real-time updates",
              currentId = currentId,
              nextFunc = nextScreen,
              prevFunc = previousScreen,
              startFunc = OnboardingActivity.Companion::navigateToMainActivity,)
    }
}
@ExperimentalUnitApi
@Composable
fun OnboardingScreen( imageId:Int,
                      text:String,
                      currentId:Int,
                      nextFunc:()->Unit,
                      prevFunc:()->Unit,
                      startFunc:() -> Unit,
                      modifier: Modifier = Modifier){
    Column(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier.weight(0.65F),
            painter = rememberImagePainter(
                data = when(imageId){
                    0 -> R.drawable.onboarding_1
                    1 -> R.drawable.onboarding_2
                    2 -> R.drawable.onboarding_3
                    else -> R.drawable.onboarding_1
                },
                builder = {
                    scale(Scale.FIT)
                }
            ),
            contentDescription = ""
        )
        Text(modifier = Modifier
            .weight(0.2F)
            .align(Alignment.CenterHorizontally)
            .padding(start = 16.dp, end = 16.dp),
            text = when(currentId){
                    0 -> "Improve your business with accurate data, and latest cryptocurrency information updates"
                    1 -> "Get in-depth analysis and forecasts for your favorites cryptocurrencies"
                    2 -> "Drive up your business revenue by saving costs otherwise spent on forecasting sites, by using our services"
                    else -> "something's wrong"
                   },
            color = Color.Magenta,
            textAlign = TextAlign.Center,
            fontSize = TextUnit(20F, TextUnitType.Sp))


        ConstraintLayout(
            modifier = Modifier.weight(0.15F)
                .fillMaxWidth()
        ){
            val (startButton,bottomCircleTabs,endButton) = createRefs()

            StartButtonHolder(
                onClick = prevFunc,
                currentId = currentId,
                modifier = Modifier.constrainAs(startButton) {
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

            BottomCircleTabs(
                IdToTint = currentId,
                modifier = Modifier.constrainAs(bottomCircleTabs){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(endButton.top, margin = 8.dp)
                })

            EndButtonHolder(
                onNext = nextFunc,
                onStart = startFunc,
                currentId = currentId,
                modifier = Modifier.constrainAs(endButton){
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })
        }

    }
}

@Composable
fun StartButtonHolder(modifier: Modifier = Modifier,
                      onClick: () -> Unit,
                      currentId: Int){
    if(currentId == 1 || currentId == 2){
        CryptoComposeTheme {
            Button(
                modifier = modifier
                    .height(45.dp)
                    .wrapContentWidth()
                    .padding(2.dp),
                onClick = onClick,
                shape = Shapes.small,
                ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription =  "")

                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "Previous")
            }
        }
    }
}

@Composable
fun EndButtonHolder(modifier: Modifier = Modifier,
                    onNext:() -> Unit,
                    onStart:() -> Unit,
                    currentId: Int){
    if(currentId == 0 || currentId == 1){
        CryptoComposeTheme {
            Button(
                modifier = modifier
                    .height(45.dp)
                    .wrapContentWidth()
                    .padding(2.dp),
                onClick = onNext,
                shape = Shapes.small) {

                Text(text = "Next")

                Spacer(modifier = Modifier.width(2.dp))

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription =  "")


            }
        }
    }
    if(currentId == 2){
        CryptoComposeTheme {
            Button(
                modifier = modifier
                    .height(45.dp)
                    .wrapContentWidth()
                    .padding(2.dp),
                onClick = onStart,
                shape = Shapes.small
            ) {
                Text(text = "Start")

                Spacer(modifier = Modifier.width(2.dp))

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomCircleTabs(modifier:Modifier = Modifier,
                     IdToTint:Int = 0){
    Row(modifier = modifier
        .wrapContentHeight()
        .wrapContentWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {

        CircleTab(shouldTint = IdToTint == 0)
        CircleTab(shouldTint = IdToTint == 1)
        CircleTab(shouldTint = IdToTint == 2)

    }
}
@Composable
fun CircleTab(shouldTint:Boolean){
    Image(modifier = Modifier
        .padding(start = 2.dp, end = 2.dp)
        .size(8.dp),
        painter = rememberImagePainter(
        data = R.drawable.circle,
    ),
        colorFilter = ColorFilter.tint(
            if(shouldTint)
                Color.Magenta
            else
                Color.LightGray
        ),
        contentDescription = "")
}
@Composable
fun OnboardingProgressBars(progress1:Float,
                           progress2:Float,
                           progress3:Float){
   

    Row(modifier = Modifier
        .padding(start = 16.dp, end = 16.dp)
        .fillMaxWidth()
        .height(4.dp)) {

        LinearProgressIndicator(
            modifier = Modifier.weight(0.3F)
                .fillMaxHeight(),
            color = Color.Green,
            progress = progress1)

        Spacer(modifier = Modifier.weight(0.05F))

        LinearProgressIndicator(
            modifier = Modifier.weight(0.3F)
                .fillMaxHeight(),
            color = Color.Green,
            progress = progress2)

        Spacer(modifier = Modifier.weight(0.05F))

        LinearProgressIndicator(
            modifier = Modifier.weight(0.3F)
                .fillMaxHeight(),
            color = Color.Green,
            progress = progress3)
    }
}


