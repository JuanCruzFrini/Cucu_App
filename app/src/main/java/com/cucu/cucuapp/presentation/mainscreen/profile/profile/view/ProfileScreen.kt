package com.cucu.cucuapp.presentation.mainscreen.profile.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cucu.cucuapp.R
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.application.firstCharToUpperCase
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.User
import com.cucu.cucuapp.presentation.detail.view.calculateDiscountPercent
import com.cucu.cucuapp.presentation.detail.view.isInDiscount
import com.cucu.cucuapp.presentation.mainscreen.profile.login.view.LoginScreen
import com.cucu.cucuapp.presentation.mainscreen.profile.login.viewmodel.LoginViewModel
import com.cucu.cucuapp.presentation.mainscreen.profile.profile.viewmodel.ProfileViewModel
import com.cucu.cucuapp.ui.theme.BlackTransparent
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.Purple80
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenController(
    mainNavController: NavHostController,
) {
    Scaffold{ paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            SetProfileContent(mainNavController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProfile(
    scrollBehavior: TopAppBarScrollBehavior,
    drawerState: DrawerState,
    mainNavController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    viewModel.authListener()

    Surface(shadowElevation = 3.dp) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = { Text(text = "Cucu app") },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                titleContentColor = Purple80,
                navigationIconContentColor = Purple80,
                scrolledContainerColor = Purple40,
                actionIconContentColor = Purple80
            ),
            navigationIcon = {
                IconButton(onClick = {
                    //enlazar la top bar al nav drawer
                    scope.launch { drawerState.open() }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                viewModel.user.observeAsState().value?.let {
                    IconButton(onClick = {
                        mainNavController.navigate(Routes.Notifications.route)
                    }) {
                        BadgedBox(badge = {
                            viewModel.getNotificationsNotOpenQuant()
                            viewModel.notOpenedNotifications.observeAsState().value?.let { quantity ->
                                if (quantity != 0) Badge { Text(text = quantity.toString()) }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }

                    }
                    //menu opciones top bar
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options"
                        )
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        content = {
                            // Elementos del menú overflow
                            DropdownMenuItem(
                                text = { Text(text = "Cerrar sesion") },
                                onClick = {
                                    isMenuExpanded = !isMenuExpanded
                                    viewModel.signOut()
                                }
                            )
                        }
                    )
                }
            }
        )
    }
}


@Composable
fun SetProfileContent(
    mainNavController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    viewModel.authListener()
    viewModel.user.observeAsState().value?.let { user ->
        ProfileScreen(user, mainNavController)
    } ?: LoginScreen()
}

@Composable
fun ProfileScreen(
    user:User,
    mainNavController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    viewModel.authListener()
    viewModel.getFavorites()
    viewModel.getUserHistory()

    var isFavsLoading by rememberSaveable { mutableStateOf(viewModel.loadingFavorites.value) }
    var isHistoryLoading by rememberSaveable { mutableStateOf(viewModel.loadingHistory.value) }

    LoadingDataIndicator(isFavsLoading)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ProfileHeader(user)

        viewModel.loadingFavorites.observeAsState().value?.let { isFavsLoading = it }
        viewModel.loadingHistory.observeAsState().value?.let { isHistoryLoading = it }

        viewModel.favorites.observeAsState().value?.let { favorites ->
            isFavsLoading = false
            if (favorites.isNotEmpty()){
                SubtitleText(text = "Favoritos")
                ProductsLazyRow(list = favorites, navController = mainNavController)
            }
        }

        viewModel.history.observeAsState().value?.let { history ->
            isHistoryLoading = false
            if (history.isNotEmpty()) {
                SubtitleText(text = "Vistos recientemente")
                ProductsLazyRow(list = history, navController = mainNavController)
            }
        }
    }

}

@Composable
fun ProductsLazyRow(list: List<Product>, navController:NavHostController) {
    LazyRow {
        items(list, key = { it.id!! }) {
            ProductRowItem(it, navController)
        }
    }
}

@Composable
fun SubtitleText(text:String) {
    Text(text = text, fontSize = 20.sp, modifier = Modifier.padding(0.dp, 16.dp))
}

@Composable
fun LoadingDataIndicator(loading: Boolean?) {
    if (loading == true){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ProductRowItem(product: Product, mainNavController: NavHostController) {
    Card(
        modifier = Modifier
            .size(300.dp, 180.dp)
            .padding(8.dp)
            .clickable {
                mainNavController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "product",
                    value = product
                )
                mainNavController.navigate(Routes.ProductDetail.createRoute(product))
            },
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(20.dp),

    ) {
        Row{
            AsyncImage(
                model = product.img,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.weight(1.5f))
            Column(
                Modifier
                    .weight(2f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.name?.firstCharToUpperCase()!!,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Text(
                    text = product.description?.firstCharToUpperCase()!!,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    fontWeight = FontWeight.Light
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$" + product.newPrice?.roundToInt().toString(),
                        fontSize = 28.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (isInDiscount(product)) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${calculateDiscountPercent(product)}% OFF",
                            color = Color.Green,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(user: User) {
    ConstraintLayout {
        val (backImg, profileImg, name) = createRefs()

        AsyncImage(
            model = R.drawable.purple_bg,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(backImg) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .height(200.dp)
        )

        AsyncImage(
            model = "${user.img}", contentDescription = "",
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(profileImg) {
                    top.linkTo(backImg.top)
                    end.linkTo(backImg.end)
                    start.linkTo(backImg.start)
                    bottom.linkTo(name.top)
                }
                .size(120.dp, 120.dp)
                .border(3.dp, Color.White, CircleShape)
                .background(Color.DarkGray, CircleShape)
                .shadow(8.dp, CircleShape),
        )

        Text(
            text = "${user.name}",
            modifier = Modifier
                .constrainAs(name) {
                    bottom.linkTo(backImg.bottom)
                    end.linkTo(backImg.end)
                    start.linkTo(backImg.start)
                }
                .fillMaxWidth()
                .background(BlackTransparent),
            color = Color.White,
            fontSize = 24.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center)

    }
}


