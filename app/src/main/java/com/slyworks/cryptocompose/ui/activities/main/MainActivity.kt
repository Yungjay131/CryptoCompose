package com.slyworks.cryptocompose.ui.activities.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.slyworks.cryptocompose.ui.theme.CryptoComposeTheme
import kotlinx.coroutines.launch
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.slyworks.cryptocompose.EXTRA_ACTIVITY_DETAILS
import com.slyworks.cryptocompose.appComp
import com.slyworks.cryptocompose.ui.activities.details.DetailsActivity
import com.slyworks.cryptocompose.Navigator
import com.slyworks.cryptocompose.ui.screens.main_activity.FavoriteMain
import com.slyworks.cryptocompose.ui.screens.main_activity.HomeMain
import com.slyworks.cryptocompose.ui.screens.main_activity.MainActivityScreen
import com.slyworks.cryptocompose.ui.screens.main_activity.SearchMain
import javax.inject.Inject

@ExperimentalUnitApi
class MainActivity : ComponentActivity() {
    //region Vars
    /*@Inject
    lateinit var mainActivityViewModel: MainActivityViewModel*/

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var searchViewModel: SearchViewModel

    @Inject
    lateinit var favoritesViewModel: FavoritesViewModel
    //endregion

    companion object{
        var _this:MainActivity? = null

        fun navigateToDetailsScreen(id:Int){
            Navigator.intentFor<DetailsActivity>(_this as Context)
                .addExtra(EXTRA_ACTIVITY_DETAILS, id)
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

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        initData()

        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initData(){
        application.appComp
            .activityComponentBuilder()
            .componentActivity(this)
            .build()
            .inject(this)
    }

    @ExperimentalComposeUiApi
    private fun initViews(){

        setContent {
                MainActivity_Main(homeViewModel,
                                 searchViewModel,
                                 favoritesViewModel)

        }
    }

}

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun MainActivity_Main(homeViewModel: HomeViewModel,
                      searchViewModel: SearchViewModel,
                      favoritesViewModel: FavoritesViewModel){

        CryptoComposeTheme {
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            val navHostController = rememberNavController()

            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()) {
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
                                            scaffoldState.drawerState.open()
                                        }
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null
                                    )
                                }
                            })
                    },
                    drawerContent = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(color = Color.LightGray)
                        ) {}

                        Divider(thickness = 1.dp)
                    },
                    drawerGesturesEnabled = true,
                    bottomBar = {
                            BottomNavigation(
                                modifier = Modifier
                                    .height(70.dp)
                                    .fillMaxWidth(),
                                backgroundColor = Color.LightGray
                            ) {
                                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination

                                MainActivityScreen.screensList.forEach { screen ->
                                    BottomNavigationItem(
                                        selected = currentDestination?.hierarchy?.any {
                                            it.route == screen.route
                                        } == true,
                                        onClick = {
                                            navHostController.navigate(screen.route) {
                                                launchSingleTop = true
                                            }
                                        },
                                        label = {
                                            Text(text = screen.route)
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(id = screen.icon),
                                                contentDescription = null)
                                        },
                                        alwaysShowLabel = true
                                    )
                                }
                            }

                    }
                ) {

                    NavHost(
                        navController = navHostController,
                        startDestination = MainActivityScreen.route_home) {
                        composable(MainActivityScreen.route_home) {
                            HomeMain(viewModel = homeViewModel)
                        }
                        composable(MainActivityScreen.route_search) {
                            SearchMain(viewModel = searchViewModel)
                        }
                        composable(MainActivityScreen.route_favorites) {
                            FavoriteMain(viewModel = favoritesViewModel)
                        }
                    }

                }
            }
        }
}


