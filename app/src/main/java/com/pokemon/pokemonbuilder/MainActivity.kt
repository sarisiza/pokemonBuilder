package com.pokemon.pokemonbuilder

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.pokemon.pokemonbuilder.ui.views.DexScreens
import com.pokemon.pokemonbuilder.ui.views.PokemonDetailsScreen
import com.pokemon.pokemonbuilder.ui.views.PokemonInfo
import com.pokemon.pokemonbuilder.ui.views.PokemonItemView
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonBuilderTheme {
                val navController = rememberNavController()
                val dexViewModel = hiltViewModel<DexViewModel>()
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Pokemon Builder")
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
                                        dexViewModel.changeLanguage = true
                                        Log.d(TAG, "go to language: ${dexViewModel.changeLanguage}")
                                        goToLoginActivity()
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
                        DrawerHeader(dexViewModel = dexViewModel)
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
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }

    private fun goToLoginActivity(){
        Intent(this,LoginActivity::class.java).apply {
            startActivity(this)
        }
    }

}

@Composable
fun PokemonNavGraph(
    navController: NavHostController,
    dexViewModel: DexViewModel,
    modifier: Modifier
) {
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
                dexViewModel.getIntent(ViewIntents.GET_POKEMON(dexViewModel.selectedGeneration.generation.id))
                dexViewModel.updateBackTrack(false)
                PokemonInfo(
                    dexViewModel = dexViewModel,
                    navController = navController
                )
            }
            composable("pokemon_details") {
                dexViewModel.updateBackTrack(true)
                dexViewModel.selectedPokemon?.let {
                    PokemonDetailsScreen(selectedPokemon = it)
                }
            }
        }
    }
}

@Composable
fun DrawerHeader(dexViewModel: DexViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)
    ) {
        Text(text = stringResource(R.string.label_hello))
        dexViewModel.loggedUser.value?.let {
            Text(text = it.firstName + it.lastName + "!")
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
            DexScreens.POKEDEX
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
        Icon(
            painter = painterResource(id = screen.iconId),
            contentDescription = null
        )
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