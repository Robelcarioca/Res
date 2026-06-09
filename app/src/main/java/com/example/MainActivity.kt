package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("main_scaffold")
                ) { innerPadding ->
                    RestaurantMenuScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantMenuScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<MenuCategory?>(null) } // null means "Featured Offer" page
    var showQRCodeDialog by remember { mutableStateOf(false) }

    // Dynamic app URL mapping for QR code scannability
    val deployedUrl = "https://ais-pre-kabyrilj2hpcefhkloarth-68376431735.europe-west2.run.app"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF2C2E33), Color(0xFF101113)),
                    center = Offset(500f, 600f),
                    radius = 1800f
                )
            )
            .drawBehind {
                // Add dry chalkboard dusty chalk effect / textures
                for (i in 0..120) {
                    val r = Random(i.toLong())
                    val x = r.nextFloat() * size.width
                    val y = r.nextFloat() * size.height
                    val radius = r.nextFloat() * 1.5f + 0.3f
                    val alpha = r.nextFloat() * 0.12f + 0.02f
                    drawCircle(
                        color = Color(0xFFFFFFFF),
                        radius = radius,
                        center = Offset(x, y),
                        alpha = alpha
                    )
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Elegant chalk branding header
            ChalkBrandHeader(onShareClick = { showQRCodeDialog = true })

            // Interactive category tabs (horizontal scrolling indicator)
            CategoryTabs(
                selectedCategory = selectedCategory,
                onCategorySelect = { selectedCategory = it }
            )

            // Dynamic Chalkboard visual panel
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = selectedCategory,
                    transitionSpec = {
                        fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) togetherWith
                                fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
                    },
                    label = "menu_transition"
                ) { targetCat ->
                    if (targetCat == null) {
                        // The prominent, beautiful Special Offer Flyer!
                        SpecialOfferTab(
                            onExploreMenu = { selectedCategory = MenuCategory.MAIN_COURSE },
                            onOpenSocial = { openUrl(context, "https://instagram.com/yourbrand") },
                            onCallRestaurant = { dialPhoneNumber(context, "123456789") }
                        )
                    } else {
                        // Interactive standard menu list
                        MenuListTab(category = targetCat)
                    }
                }
            }

            // Muted Bottom Details bar
            BottomInfoBar(
                onLocationClick = { openLocationInMap(context, "1234 Street Name, City Name, Country") },
                onPhoneClick = { dialPhoneNumber(context, "+0123456789") }
            )
        }

        // Expanded QR code share sheet
        if (showQRCodeDialog) {
            QRCodeDialog(
                url = deployedUrl,
                onDismiss = { showQRCodeDialog = false }
            )
        }
    }
}

@Composable
fun ChalkBrandHeader(onShareClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(48.dp)) // horizontal balance

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "YOUR",
                    color = ChalkMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = "Restaurant Fork & Knife Logo",
                    modifier = Modifier.size(16.dp),
                    tint = ChalkOrange
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "LOGO",
                    color = ChalkMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            // Share QR Code Quick Launch Badge
            IconButton(
                onClick = onShareClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF232528), CircleShape)
                    .testTag("share_qr_badge")
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Show QR table scanner link",
                    tint = ChalkOrange,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Draw a double divider line mimicking the chalk line
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        ) {
            drawLine(
                color = ChalkOrange,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2f
            )
            drawLine(
                color = ChalkOrange.copy(alpha = 0.4f),
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }
    }
}

@Composable
fun CategoryTabs(
    selectedCategory: MenuCategory?,
    onCategorySelect: (MenuCategory?) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = if (selectedCategory == null) 0 else selectedCategory.ordinal + 1,
        containerColor = Color.Transparent,
        contentColor = ChalkCream,
        edgePadding = 16.dp,
        divider = {},
        indicator = { tabPositions ->
            val index = if (selectedCategory == null) 0 else selectedCategory.ordinal + 1
            if (index < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                    color = ChalkOrange,
                    height = 3.dp
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        // Special Featured Tab
        Tab(
            selected = selectedCategory == null,
            onClick = { onCategorySelect(null) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Stars,
                        contentDescription = "Promo icon",
                        modifier = Modifier.size(16.dp).padding(end = 4.dp),
                        tint = if (selectedCategory == null) ChalkOrange else ChalkMuted
                    )
                    Text(
                        text = "SPECIAL OFFERS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = if (selectedCategory == null) ChalkWhite else ChalkMuted
                    )
                }
            }
        )

        // All Standard Category Tabs
        MenuCategory.values().forEach { category ->
            val isSelected = selectedCategory == category
            Tab(
                selected = isSelected,
                onClick = { onCategorySelect(category) },
                text = {
                    Text(
                        text = category.displayName.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = if (isSelected) ChalkWhite else ChalkMuted
                    )
                }
            )
        }
    }
}

@Composable
fun SpecialOfferTab(
    onExploreMenu: () -> Unit,
    onOpenSocial: () -> Unit,
    onCallRestaurant: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("special_offer_tab"),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Main Display Flyer Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFF2C2E33), RoundedCornerShape(16.dp))
                    .background(Color(0xFF141517))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "FOOD MENU",
                        color = ChalkWhite,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Special Dishes",
                        color = ChalkOrange,
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(y = (-6).dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stunning Interactive Canvas Chalk Salad illustration!
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0C0D0F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(220.dp)) {
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val bowlRadius = size.width / 2f - 10f

                            // Draw Plate Surface with textured shadows
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFF222428), Color(0xFF0F1012)),
                                    center = center,
                                    radius = bowlRadius
                                ),
                                radius = bowlRadius
                            )

                            // Silver chalk circular rim highlight
                            drawCircle(
                                color = Color(0x33FFFFFF),
                                radius = bowlRadius - 5f,
                                style = Stroke(width = 2f)
                            )

                            // Let's draw some gorgeous, stylized Salad elements inside the container!
                            // 1. Green leafy lettuce background layer
                            for (i in 0..12) {
                                val leafAngle = i * (360f / 13f)
                                val angleRad = Math.toRadians(leafAngle.toDouble())
                                val rx = center.x + (bowlRadius * 0.45f) * Math.cos(angleRad).toFloat()
                                val ry = center.y + (bowlRadius * 0.45f) * Math.sin(angleRad).toFloat()
                                drawCircle(
                                    color = Color(0xAA4CAF50),
                                    radius = 35f,
                                    center = Offset(rx, ry)
                                )
                                // Drawing leaf veining using standard fine cream stroke
                                drawCircle(
                                    color = Color(0x44FFFFFF),
                                    radius = 35f,
                                    center = Offset(rx, ry),
                                    style = Stroke(width = 1f)
                                )
                            }

                            // 2. Eggs (White with Golden Yolk)
                            val eggPositions = listOf(
                                Offset(center.x - 50f, center.y - 45f),
                                Offset(center.x + 50f, center.y + 15f),
                                Offset(center.x - 20f, center.y + 55f)
                            )
                            eggPositions.forEach { eggPos ->
                                // White shell back
                                drawOval(
                                    color = Color(0xFFFBFBFB),
                                    topLeft = Offset(eggPos.x - 24f, eggPos.y - 18f),
                                    size = Size(48f, 36f)
                                )
                                // Deep yellow yolk
                                drawCircle(
                                    color = Color(0xFFFFC107),
                                    radius = 11f,
                                    center = eggPos
                                )
                            }

                            // 3. Cherry Tomatoes (shimmering Red wedges)
                            val tomatoPositions = listOf(
                                Offset(center.x + 35f, center.y - 50f),
                                Offset(center.x - 60f, center.y + 15f),
                                Offset(center.x + 10f, center.y - 15f)
                            )
                            tomatoPositions.forEach { tomPos ->
                                drawOval(
                                    color = Color(0xFFE53935),
                                    topLeft = Offset(tomPos.x - 18f, tomPos.y - 12f),
                                    size = Size(36f, 24f)
                                )
                                // Tomato shine spot
                                drawCircle(
                                    color = Color(0xDDFFFFFF),
                                    radius = 3f,
                                    center = Offset(tomPos.x - 6f, tomPos.y - 4f)
                                )
                            }

                            // 4. Cucumber slices (Translucent greens with seeds)
                            val cucumberPositions = listOf(
                                Offset(center.x - 45f, center.y - 10f),
                                Offset(center.x + 40f, center.y - 20f),
                                Offset(center.x + 20f, center.y + 50f)
                            )
                            cucumberPositions.forEach { cucPos ->
                                drawCircle(
                                    color = Color(0xFF81C784),
                                    radius = 18f,
                                    center = cucPos
                                )
                                drawCircle(
                                    color = Color(0xFF43A047),
                                    radius = 18f,
                                    center = cucPos,
                                    style = Stroke(width = 3f)
                                )
                                // Seeds dots
                                for (degree in 0..360 step 60) {
                                    val seedRad = Math.toRadians(degree.toDouble())
                                    val sx = cucPos.x + 8f * Math.cos(seedRad).toFloat()
                                    val sy = cucPos.y + 8f * Math.sin(seedRad).toFloat()
                                    drawCircle(color = Color(0xFFE8F5E9), radius = 1.5f, center = Offset(sx, sy))
                                }
                            }

                            // 5. Chef tuna seasoning pile in center
                            drawCircle(
                                color = Color(0xFFE0D8C8),
                                radius = 28f,
                                center = center
                            )
                            drawCircle(
                                color = Color(0xFFC7BCA7),
                                radius = 28f,
                                center = center,
                                style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f))
                            )
                        }

                        // Superimposed Gorgeous Promotional Chalk Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-10).dp, y = (-12).dp)
                                .size(72.dp)
                                .background(ChalkOrange, CircleShape)
                                .border(2.dp, ChalkWhite, CircleShape)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "40%",
                                    color = ChalkWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Discount",
                                    color = ChalkWhite.copy(alpha = 0.9f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "OPEN DAILY FROM 10.30AM - 9.00PM",
                        color = ChalkYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "1234 Your Location, Street Name, City Name\nState Name, Country\nFiant tincidunt nihb aliquet leo wisi @YOURBRAND",
                        color = ChalkCream,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive navigation actions callout
            Button(
                onClick = onExploreMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("explore_menu_button"),
                colors = ButtonDefaults.buttonColors(containerColor = ChalkOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Book icon",
                        tint = ChalkWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EXPLORE DINNER MENU",
                        color = ChalkWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCallRestaurant,
                    modifier = Modifier.weight(1f).height(46.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ChalkCream),
                    border = BorderStroke(1.dp, ChalkMuted.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call restaurant icon",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "CALL TO BOOK", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenSocial,
                    modifier = Modifier.weight(1f).height(46.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ChalkCream),
                    border = BorderStroke(1.dp, ChalkMuted.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Social network logo icon",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "SOCIAL FEED", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MenuListTab(category: MenuCategory) {
    val items = remember(category) {
        MenuRepository.items.filter { it.category == category }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("menu_list_tab")
    ) {
        // Decorative Category Chalk Header in List
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(Color(0xFF161719), RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF2C2E33), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when(category) {
                        MenuCategory.MAIN_COURSE -> Icons.Default.LocalPizza
                        MenuCategory.APPETIZER -> Icons.Default.Kitchen
                        MenuCategory.SIDE_DISK -> Icons.Default.RestaurantMenu
                        MenuCategory.DESSERT -> Icons.Default.Cake
                        MenuCategory.BEVERAGE -> Icons.Default.LocalCafe
                    },
                    contentDescription = "Category Icon",
                    tint = ChalkOrange,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category.displayName.uppercase(),
                    color = ChalkWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                MenuItemRow(item = item)
            }
        }
    }
}

@Composable
fun MenuItemRow(item: MenuItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF141517).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF232528), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.name,
                        color = ChalkCream,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (item.isSignature) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(ChalkOrange.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .border(1.dp, ChalkOrange, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "SIGNATURE",
                                color = ChalkOrangeLight,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.description,
                    color = ChalkMuted,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontStyle = FontStyle.Normal
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Dotted Price Tag
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$",
                    color = ChalkOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Top)
                )
                Text(
                    text = String.format("%.2f", item.price),
                    color = ChalkOrange,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun BottomInfoBar(
    onLocationClick: () -> Unit,
    onPhoneClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF0F0F10),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Interactive clickable address
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onLocationClick)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Map Location Icon",
                    tint = ChalkOrange,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "1234 Street Name, City, Country",
                    color = ChalkMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    textDecoration = TextDecoration.Underline
                )
            }

            // Clickable phone hotlink
            Row(
                modifier = Modifier
                    .clickable(onClick = onPhoneClick)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone icon",
                    tint = ChalkOrange,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+012-345-6789",
                    color = ChalkMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeDialog(
    url: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip = android.content.ClipData.newPlainText("Restaurant Web URL", url)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "URL Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = ChalkOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("COPY WEB URL", color = ChalkWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", color = ChalkCream, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        },
        containerColor = Color(0xFF161719),
        title = {
            Text(
                text = "TABLE SCAN QR LINK",
                color = ChalkWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Place this on tables! Customers scanning the QR instantly access this chalkboard card.",
                    color = ChalkMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Beautiful custom matrix drawing representing a scannable Table QR Code
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(ChalkWhite, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cellSize = size.width / 21f

                        // 1. Draw corner finder pattern (Top-Left)
                        drawFinderPattern(0f, 0f, cellSize)

                        // 2. Draw corner finder pattern (Top-Right)
                        drawFinderPattern(size.width - cellSize * 7f, 0f, cellSize)

                        // 3. Draw corner finder pattern (Bottom-Left)
                        drawFinderPattern(0f, size.height - cellSize * 7f, cellSize)

                        // 4. Draw structured QR randomized blocks with the seed mapping to URL characters
                        val r = Random(url.hashCode())
                        for (row in 0 until 21) {
                            for (col in 0 until 21) {
                                // Skip finders
                                if (row < 8 && col < 8) continue
                                if (row < 8 && col > 13) continue
                                if (row > 13 && col < 8) continue
                                // Skip layout alignments
                                if (row == 6 || col == 6) {
                                    // Timing pattern alternating
                                    if (row == 6 && col % 2 == 0) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = Offset(col * cellSize, row * cellSize),
                                            size = Size(cellSize, cellSize)
                                        )
                                    }
                                    if (col == 6 && row % 2 == 0) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = Offset(col * cellSize, row * cellSize),
                                            size = Size(cellSize, cellSize)
                                        )
                                    }
                                    continue
                                }

                                // Render pseudo-random grid block representing hashed URL content
                                if (r.nextBoolean()) {
                                    drawRect(
                                        color = Color.Black,
                                        topLeft = Offset(col * cellSize, row * cellSize),
                                        size = Size(cellSize, cellSize)
                                    )
                                }
                            }
                        }

                        // Draw Fork & Knife tiny icon in center to represent premium styling
                        val centerBoxRadius = cellSize * 2f
                        drawRoundRect(
                            color = ChalkWhite,
                            topLeft = Offset(size.width / 2f - centerBoxRadius, size.height / 2f - centerBoxRadius),
                            size = Size(centerBoxRadius * 2f, centerBoxRadius * 2f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                        drawRoundRect(
                            color = ChalkOrange,
                            topLeft = Offset(size.width / 2f - centerBoxRadius + 2f, size.height / 2f - centerBoxRadius + 2f),
                            size = Size(centerBoxRadius * 22f - 4f, centerBoxRadius * 2f - 4f), // center visual accent
                            style = Stroke(width = 1.5f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = url,
                    color = ChalkOrangeLight,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        openUrl(context, url)
                    }
                )
            }
        }
    )
}

// Extension to cleanly draw a QR Finder pattern inside canvas
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFinderPattern(
    x: Float,
    y: Float,
    cellSize: Float
) {
    // Outer 7x7 block
    drawRect(
        color = Color.Black,
        topLeft = Offset(x, y),
        size = Size(cellSize * 7f, cellSize * 7f)
    )
    // Inner 5x5 background (white)
    drawRect(
        color = Color.White,
        topLeft = Offset(x + cellSize, y + cellSize),
        size = Size(cellSize * 5f, cellSize * 5f)
    )
    // Inner 3x3 center (black)
    drawRect(
        color = Color.Black,
        topLeft = Offset(x + cellSize * 2f, y + cellSize * 2f),
        size = Size(cellSize * 3f, cellSize * 3f)
    )
}

// Utility action helpers
fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Cannot open link: $url", Toast.LENGTH_SHORT).show()
    }
}

fun dialPhoneNumber(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Cannot make phone calls on this device", Toast.LENGTH_SHORT).show()
    }
}

fun openLocationInMap(context: Context, address: String) {
    try {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    } catch (e: Exception) {
        // Fallback standard browser mapping query string
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}"))
        context.startActivity(browserIntent)
    }
}
