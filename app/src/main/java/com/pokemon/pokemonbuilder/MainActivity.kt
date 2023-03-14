package com.pokemon.pokemonbuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.pokemon.pokemonbuilder.ui.theme.PokemonBuilderTheme
import com.pokemon.pokemonbuilder.ui.views.*
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var configurationChanged = false

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            PokemonBuilderTheme {
                val navController = rememberNavController()
                val dexViewModel = hiltViewModel<DexViewModel>()
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                val headerSize =
                    when(windowSizeClass.widthSizeClass){
                        WindowWidthSizeClass.Compact -> 28.sp
                        WindowWidthSizeClass.Medium -> 30.sp
                        WindowWidthSizeClass.Expanded -> 32.sp
                        else -> 20.sp
                    }
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.padding(5.dp),
                            title = {
                                Text(
                                    text = "Pokemon Builder",
                                    fontSize = headerSize
                                )
                            },
                            navigationIcon = {
                                Row {
                                    IconButton(onClick = {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = getString(R.string.label_menu)
                                        )
                                    }
                                    if (dexViewModel.hasBackTrack.value) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = getString(R.string.label_back)
                                            )
                                        }
                                    }
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        navController.navigate(DexScreens.CHANGE_LANGUAGE.route)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = getString(R.string.icon_pick_language)
                                    )
                                }
                            }
                        )
                    },
                    drawerContent = {
                        DrawerHeader(loginViewModel = loginViewModel)
                        DrawerBody(navController = navController) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    }
                ) {
                    PokemonNavGraph(
                        navController = navController,
                        dexViewModel = dexViewModel,
                        loginViewModel = loginViewModel,
                        modifier = Modifier.padding(it),
                        windowSizeClass = windowSizeClass,
                        configuration = configurationChanged
                    ){updateConfiguration(false)}
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateConfiguration(true)
    }

    private fun updateConfiguration(state: Boolean){
        configurationChanged = state
    }

}

@Composable
fun PokemonNavGraph(
    navController: NavHostController,
    dexViewModel: DexViewModel,
    loginViewModel: LoginViewModel,
    modifier: Modifier,
    windowSizeClass: WindowSizeClass,
    configuration: Boolean,
    updateConfig: () -> Unit
) {
    var goingBack = false
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = DexScreens.POKEDEX.route
    ) {
        navigation(
            startDestination = "pokemon_list",
            route = DexScreens.POKEDEX.route
        ) {
            composable("pokemon_list") {
                val language = loginViewModel.appLanguage.value
                loginViewModel.getIntent(ViewIntents.GET_LANGUAGE)
                language?.let {
                    if(!configuration && !goingBack) {
                        dexViewModel.getIntent(
                            ViewIntents.GET_POKEMON(
                                it,
                                dexViewModel.selectedGeneration.generation.id
                            )
                        )
                    } else if(configuration){
                        updateConfig()
                    } else if (goingBack){
                        goingBack = false
                    }
                }
                dexViewModel.updateBackTrack(false)
                PokemonInfo(
                    dexViewModel = dexViewModel,
                    loginViewModel = loginViewModel,
                    navController = navController,
                    windowSizeClass = windowSizeClass
                )
            }
            composable("pokemon_details") {
                updateConfig()
                goingBack = true
                dexViewModel.updateBackTrack(true)
                dexViewModel.selectedPokemon?.let {
                    PokemonDetailsScreen(
                        selectedPokemon = it,
                        windowSizeClass = windowSizeClass
                    )
                }
            }
        }
        navigation(
            startDestination = "change_language",
            route = DexScreens.CHANGE_LANGUAGE.route
        ){
            composable("change_language"){
                LanguagePicker(loginViewModel = loginViewModel) {
                    navController.navigate(DexScreens.POKEDEX.route)
                }
            }
        }
    }
}

@Composable
fun DrawerHeader(loginViewModel: LoginViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.label_hello)+",",
                fontSize = 30.sp
            )
            val user = loginViewModel.loggedUser.value
            loginViewModel.getIntent(ViewIntents.GET_USER)
            user?.let {
                Text(
                    text = it.firstName,
                    fontSize = 30.sp
                )
                Text(
                    text = it.lastName + "!",
                    fontSize = 30.sp
                )
            }
        }
    }
}

@Composable
fun DrawerBody(
    navController: NavHostController,
    closeNavDrawer: () -> Unit
) {
    Column {
        DrawerMenuItem(
            screen = DexScreens.POKEDEX
        ) {
            navController.navigate(DexScreens.POKEDEX.route)
            closeNavDrawer()
        }
    }
}

@Composable
fun DrawerMenuItem(
    screen: DexScreens,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        screen.iconId?.let {screenImg ->
            Icon(
                painter = painterResource(id = screenImg),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = stringResource(id = screen.resourceId))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    PokemonBuilderTheme {
    }
}