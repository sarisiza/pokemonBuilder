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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
import com.pokemon.pokemonbuilder.utils.DatabaseAction
import com.pokemon.pokemonbuilder.utils.QueryType
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
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
            PokemonBuilderTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val navController = rememberNavController()
                val dexViewModel = hiltViewModel<DexViewModel>()
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val builderViewModel = hiltViewModel<BuilderViewModel>()
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                val headerSize =
                    when(windowSizeClass.widthSizeClass){
                        WindowWidthSizeClass.Compact -> 28.sp
                        WindowWidthSizeClass.Medium -> 30.sp
                        WindowWidthSizeClass.Expanded -> 32.sp
                        else -> 20.sp
                    }
                val titleSize =
                    when(windowSizeClass.widthSizeClass){
                        WindowWidthSizeClass.Compact -> 22.sp
                        WindowWidthSizeClass.Medium -> 24.sp
                        WindowWidthSizeClass.Expanded -> 26.sp
                        else -> 20.sp
                    }
                val textSize =
                    when(windowSizeClass.widthSizeClass){
                        WindowWidthSizeClass.Compact -> 16.sp
                        WindowWidthSizeClass.Medium -> 18.sp
                        WindowWidthSizeClass.Expanded -> 20.sp
                        else -> 14.sp
                    }
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.padding(5.dp),
                            title = {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentRoute = navBackStackEntry?.destination?.route
                                Text(
                                    text = stringResource(id = getScreenName(currentRoute)),
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
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        FabCreator(
                            builderViewModel = builderViewModel,
                            navController = navController
                        )
                    }
                ) {
                    PokemonNavGraph(
                        navController = navController,
                        dexViewModel = dexViewModel,
                        loginViewModel = loginViewModel,
                        builderViewModel = builderViewModel,
                        modifier = Modifier.padding(it),
                        headerSize = headerSize,
                        titleSize = titleSize,
                        textSize = textSize,
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

    private fun getScreenName(route: String?): Int {
        return when (route) {
            DexScreens.POKEDEX.route -> DexScreens.POKEDEX.resourceId
            DexScreens.POKEMON_LIST.route -> DexScreens.POKEMON_LIST.resourceId
            DexScreens.POKEMON_DETAILS.route -> DexScreens.POKEMON_DETAILS.resourceId
            DexScreens.CHANGE_LANGUAGE.route -> DexScreens.CHANGE_LANGUAGE.resourceId
            DexScreens.LANGUAGE_PAGE.route -> DexScreens.LANGUAGE_PAGE.resourceId
            DexScreens.TEAMS.route -> DexScreens.TEAMS.resourceId
            DexScreens.TEAMS_LIST.route -> DexScreens.TEAMS_LIST.resourceId
            DexScreens.TEAMS_DETAIL.route -> DexScreens.TEAMS_DETAIL.resourceId
            DexScreens.CREATE_TEAM.route -> DexScreens.CREATE_TEAM.resourceId
            DexScreens.SAVED_POKEMON.route -> DexScreens.SAVED_POKEMON.resourceId
            DexScreens.SAVED_POKEMON_PAGE.route -> DexScreens.SAVED_POKEMON_PAGE.resourceId
            DexScreens.SAVED_POKEMON_DETAILS.route -> DexScreens.SAVED_POKEMON_DETAILS.resourceId
            DexScreens.SAVED_POKEMON_DETAILS_PAGE.route -> DexScreens.SAVED_POKEMON_DETAILS_PAGE.resourceId
            DexScreens.CREATE_POKEMON.route -> DexScreens.CREATE_POKEMON.resourceId
            DexScreens.CREATE_POKEMON_PAGE.route -> DexScreens.CREATE_POKEMON_PAGE.resourceId
            DexScreens.SEARCH_POKEMON.route -> DexScreens.SEARCH_POKEMON.resourceId
            DexScreens.SEARCH_POKEMON_PAGE.route -> DexScreens.SEARCH_POKEMON_PAGE.resourceId
            DexScreens.EDIT_TEAM.route -> DexScreens.EDIT_TEAM.resourceId
            DexScreens.ABOUT.route -> DexScreens.ABOUT.resourceId
            DexScreens.ABOUT_PAGE.route -> DexScreens.ABOUT_PAGE.resourceId
            else -> R.string.app_name
        }
    }

}

@Composable
fun PokemonNavGraph(
    navController: NavHostController,
    dexViewModel: DexViewModel,
    loginViewModel: LoginViewModel,
    builderViewModel: BuilderViewModel,
    modifier: Modifier,
    headerSize: TextUnit,
    titleSize: TextUnit,
    textSize: TextUnit,
    configuration: Boolean,
    updateConfig: () -> Unit
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = DexScreens.POKEDEX.route
    ) {
        navigation(
            startDestination = DexScreens.POKEMON_LIST.route,
            route = DexScreens.POKEDEX.route
        ) {
            composable(DexScreens.POKEMON_LIST.route) {
                builderViewModel.shouldCreate.value = false
                val language = loginViewModel.appLanguage.value
                loginViewModel.getIntent(ViewIntents.GET_LANGUAGE)
                language?.let {
                    if(!configuration) {
                        dexViewModel.getIntent(
                            ViewIntents.GET_POKEMON(
                                language = it,
                                generation = dexViewModel.selectedGeneration.generation.id
                            )
                        )
                    } else{
                        updateConfig()
                    }
                }
                dexViewModel.updateBackTrack(false)
                PokemonInfo(
                    dexViewModel = dexViewModel,
                    loginViewModel = loginViewModel,
                    navController = navController,
                    headerSize = headerSize,
                    textSize = textSize,
                    queryType = QueryType.GENERATION
                )
            }
            composable(DexScreens.POKEMON_DETAILS.route) {
                builderViewModel.shouldCreate.value = false
                updateConfig()
                dexViewModel.updateBackTrack(true)
                dexViewModel.selectedPokemon?.let {
                    PokemonDetailsScreen(
                        selectedPokemon = it,
                        headerSize = headerSize,
                        titleSize = titleSize,
                        textSize = textSize
                    )
                }
            }
        }
        navigation(
            startDestination = DexScreens.LANGUAGE_PAGE.route,
            route = DexScreens.CHANGE_LANGUAGE.route
        ){
            composable(DexScreens.LANGUAGE_PAGE.route){
                builderViewModel.shouldCreate.value = false
                dexViewModel.updateBackTrack(true)
                LanguagePicker(
                    loginViewModel = loginViewModel,
                    headerSize = headerSize,
                    titleSize = titleSize,
                    textSize = textSize
                ) {
                    navController.navigate(DexScreens.POKEDEX.route)
                }
            }
        }
        navigation(
            startDestination = DexScreens.TEAMS_LIST.route,
            route = DexScreens.TEAMS.route
        ){
            composable(DexScreens.TEAMS_LIST.route){
                builderViewModel.shouldCreate.value = true
                builderViewModel.createTeam.value = true
                dexViewModel.updateBackTrack(false)
                TeamsInfo(
                    builderViewModel = builderViewModel,
                    navController = navController,
                    headerSize = headerSize
                )
            }
            composable(DexScreens.TEAMS_DETAIL.route){
                builderViewModel.shouldCreate.value = true
                builderViewModel.createTeam.value = false
                dexViewModel.updateBackTrack(true)
                CreatedPokemonList(
                    builderViewModel = builderViewModel,
                    navController = navController,
                    titleSize = titleSize,
                    textSize = textSize,
                    selectedTeam = builderViewModel.selectedTeam
                )
            }
            composable(DexScreens.CREATE_TEAM.route){
                builderViewModel.selectedTeam = null
                builderViewModel.shouldCreate.value = false
                dexViewModel.updateBackTrack(true)
                CreateTeamName(
                    builderViewModel = builderViewModel,
                    headerSize = headerSize,
                    titleSize = titleSize,
                    textSize = textSize,
                    navController = navController,
                    action = DatabaseAction.ADD
                )
            }
            composable(DexScreens.EDIT_TEAM.route){
                builderViewModel.shouldCreate.value = false
                dexViewModel.updateBackTrack(true)
                CreateTeamName(
                    builderViewModel = builderViewModel,
                    headerSize = headerSize,
                    titleSize = titleSize,
                    textSize = textSize,
                    navController = navController,
                    action = DatabaseAction.UPDATE
                )
            }
        }
        navigation(
            startDestination = DexScreens.SAVED_POKEMON_PAGE.route,
            route = DexScreens.SAVED_POKEMON.route
        ){
            composable(DexScreens.SAVED_POKEMON_PAGE.route){
                builderViewModel.shouldCreate.value = true
                builderViewModel.createTeam.value = false
                dexViewModel.updateBackTrack(true)
                CreatedPokemonList(
                    builderViewModel = builderViewModel,
                    navController = navController,
                    titleSize = titleSize,
                    textSize = textSize
                )
            }

        }
        navigation(
            startDestination = DexScreens.SAVED_POKEMON_DETAILS_PAGE.route,
            route = DexScreens.SAVED_POKEMON_DETAILS.route
        ){
            composable(DexScreens.SAVED_POKEMON_DETAILS_PAGE.route){
                builderViewModel.shouldCreate.value = false
                dexViewModel.updateBackTrack(true)
                //todo pokemon details
            }
        }
        navigation(
            startDestination = DexScreens.CREATE_POKEMON_PAGE.route,
            route = DexScreens.CREATE_POKEMON.route
        ){
            composable(DexScreens.CREATE_POKEMON_PAGE.route){
                builderViewModel.shouldCreate.value = false
                dexViewModel.updateBackTrack(true)
                CreatePokemon(
                    dexViewModel = dexViewModel,
                    builderViewModel = builderViewModel,
                    loginViewModel = loginViewModel,
                    headerSize = headerSize,
                    titleSize = titleSize,
                    textSize = textSize,
                    action = DatabaseAction.ADD,
                    inTeam = builderViewModel.selectedTeam!=null,
                    navController = navController
                )
            }
        }
        navigation(
            route = DexScreens.SEARCH_POKEMON.route,
            startDestination = DexScreens.SEARCH_POKEMON_PAGE.route
        ){
            composable(DexScreens.SEARCH_POKEMON_PAGE.route){
                SearchPokemon(
                    dexViewModel = dexViewModel,
                    headerSize = headerSize,
                    textSize = textSize,
                    loginViewModel = loginViewModel,
                    navController = navController
                )
            }
        }
        navigation(
            route = DexScreens.ABOUT.route,
            startDestination = DexScreens.ABOUT_PAGE.route
        ){
            composable(DexScreens.ABOUT_PAGE.route){
                builderViewModel.shouldCreate.value = false
                dexViewModel.updateBackTrack(true)
                AboutUsPage(
                    headerSize = headerSize,
                    textSize = textSize
                )
            }
        }
    }
}

@Composable
fun FabCreator(
    builderViewModel: BuilderViewModel,
    navController: NavHostController
) {
    if(builderViewModel.shouldCreate.value){
        val route = if(builderViewModel.createTeam.value){
             DexScreens.CREATE_TEAM.route
        } else{
            DexScreens.SEARCH_POKEMON.route
        }
        FloatingActionButton(
            onClick = {navController.navigate(route)},
            shape = CircleShape,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null)
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
        DrawerMenuItem(screen = DexScreens.TEAMS) {
            navController.navigate(DexScreens.TEAMS.route)
            closeNavDrawer()
        }
        DrawerMenuItem(screen = DexScreens.SAVED_POKEMON) {
            navController.navigate(DexScreens.SAVED_POKEMON.route)
            closeNavDrawer()
        }
        DrawerMenuItem(screen = DexScreens.ABOUT) {
            navController.navigate(DexScreens.ABOUT.route)
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
    val headerSize = 28.sp
    val titleSize = 22.sp
    val textSize = 16.sp
    PokemonBuilderTheme {
        AboutUsPage(headerSize = headerSize, textSize = textSize)
    }
}