package com.slyworks.cryptocompose.ui.activities.details

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.slyworks.cryptocompose.EXTRA_ACTIVITY_DETAILS
import com.slyworks.cryptocompose.Navigator
import com.slyworks.cryptocompose.Navigator.Companion.getExtra
import com.slyworks.cryptocompose.appComp
import com.slyworks.cryptocompose.ui.activities.main.MainActivity
import com.slyworks.cryptocompose.ui.screens.details_activity.DetailMain
import com.slyworks.cryptocompose.ui.screens.details_activity.DetailsActivityScreen
import com.slyworks.cryptocompose.ui.theme.CryptoComposeTheme
import com.slyworks.models.CryptoModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsActivity : ComponentActivity() {
    //region Vars
    @Inject
    lateinit var detailsViewModel: DetailsViewModel

    private lateinit var mEntity:CryptoModel
    private lateinit var mId:String
    //endregion

    companion object{
        var _this:DetailsActivity? = null

        @ExperimentalUnitApi
        fun navigateToMainScreen(){
            Navigator.intentFor<MainActivity>(_this as Context)
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

    @OptIn(ExperimentalUnitApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        initDI()

        super.onCreate(savedInstanceState)

        initData()
        initViews()
    }

    private fun initDI(){
        //mEntity = intent.extras!!.getParcelable(EXTRA_ACTIVITY_DETAILS)!!
        mId = intent.getExtra<Int>(EXTRA_ACTIVITY_DETAILS).toString()
    }

    private fun initData(){
        application.appComp
            .activityComponentBuilder()
            .componentActivity(this)
            .build()
            .inject(this)
    }

    @ExperimentalUnitApi
    private fun initViews(){
        setContent {
            DetailsActivity_Main(viewModel = detailsViewModel, mId)
        }
    }

    @OptIn(ExperimentalUnitApi::class)
    override fun onBackPressed() {
        super.onBackPressed()

        navigateToMainScreen()
    }
}

@ExperimentalUnitApi
@Composable
fun DetailsActivity_Main(viewModel:DetailsViewModel, id:String){
    CryptoComposeTheme {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val navHostController = rememberNavController()

        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    backgroundColor = Color.LightGray,
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    DetailsActivity.navigateToMainScreen()
                                }
                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null)
                        }
                    })
            },
        ){
            NavHost(
                navController = navHostController,
                startDestination = DetailsActivityScreen.route_details){
                composable(DetailsActivityScreen.route_details){
                    DetailMain(viewModel = viewModel, id)
                }
            }
        }
    }
}

