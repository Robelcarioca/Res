package com.example

import androidx.compose.ui.graphics.Color

enum class MenuCategory(val displayName: String) {
    MAIN_COURSE("Main Course"),
    APPETIZER("Appetizer"),
    SIDE_DISK("Side Dish"),
    DESSERT("Desserts"),
    BEVERAGE("Beverage")
}

data class MenuItem(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val category: MenuCategory,
    val isSignature: Boolean = false,
    val imagePlaceholderText: String = ""
)

object MenuRepository {
    val items = listOf(
        // MAIN COURSE
        MenuItem(
            id = "mc1",
            name = "VITAE NUNC SED VELIT",
            price = 24.50,
            description = "Se feugiat hac dulcis innocentia id est peccantibus. Futuri probations",
            category = MenuCategory.MAIN_COURSE
        ),
        MenuItem(
            id = "mc2",
            name = "EU FACILISIS",
            price = 15.20,
            description = "Invita carthaginem odit ad piogenetant culpa dimittere proper rerum",
            category = MenuCategory.MAIN_COURSE,
            isSignature = true
        ),
        MenuItem(
            id = "mc3",
            name = "INTEGER FEUGIAT",
            price = 13.50,
            description = "Tempora amet caedis vele dignissim",
            category = MenuCategory.MAIN_COURSE
        ),
        MenuItem(
            id = "mc4",
            name = "ULTRICES SAGITTIS ORCI",
            price = 25.30,
            description = "Tot praesentium id electionem. Masa lacus defensionem detrimenta",
            category = MenuCategory.MAIN_COURSE
        ),
        MenuItem(
            id = "mc5",
            name = "NULLAM CRAS",
            price = 21.80,
            description = "Vel nibh elementum pulvinar ac",
            category = MenuCategory.MAIN_COURSE
        ),
        MenuItem(
            id = "mc6",
            name = "AC SEO SOLLICITUDIN",
            price = 21.80,
            description = "Eget sit amet tellus curasud adipiscing enim eu. Ultrices vitae auctor eu",
            category = MenuCategory.MAIN_COURSE
        ),

        // APPETIZER
        MenuItem(
            id = "ap1",
            name = "ODIO FACILISIS",
            price = 14.20,
            description = "Ac feugiat sed lectus vestibulum ullamcorper vel. Mattis ullamcorper sed ullamcorper morbi",
            category = MenuCategory.APPETIZER
        ),
        MenuItem(
            id = "ap2",
            name = "VITAE PROIN SAGITTIS",
            price = 9.50,
            description = "Mattis ullamcorper velit ac ullamcorper morbi tincidunt ornare massa",
            category = MenuCategory.APPETIZER
        ),
        MenuItem(
            id = "ap3",
            name = "SUSPENDISSE",
            price = 10.50,
            description = "Integer quis auctor elit sed vulputate Tempus urna pharetra pharetra masa massa ultricies mi",
            category = MenuCategory.APPETIZER,
            isSignature = true
        ),
        MenuItem(
            id = "ap4",
            name = "ARCU ADIPISCING",
            price = 12.50,
            description = "Sit amet consectetur adipiscing Ipsum dolor amet consectetur",
            category = MenuCategory.APPETIZER
        ),

        // SIDE DISH
        MenuItem(
            id = "sd1",
            name = "ARCU ID ODIO",
            price = 4.30,
            description = "Light savory side accompaniment",
            category = MenuCategory.SIDE_DISK
        ),
        MenuItem(
            id = "sd2",
            name = "VARIUS PHARETRA",
            price = 6.50,
            description = "Signature crispy spiced potato preparation",
            category = MenuCategory.SIDE_DISK
        ),
        MenuItem(
            id = "sd3",
            name = "ELEIFEND",
            price = 10.20,
            description = "Steamed seasoned select greens with cold butter glaze",
            category = MenuCategory.SIDE_DISK
        ),

        // DESSERTS
        MenuItem(
            id = "ds1",
            name = "ODIO ENIM",
            price = 5.20,
            description = "Artisan sweet cream custard pastry",
            category = MenuCategory.DESSERT
        ),
        MenuItem(
            id = "ds2",
            name = "ULTRICES SAGITTIS",
            price = 9.50,
            description = "Rich dark cacao crumb baked dessert cake",
            category = MenuCategory.DESSERT
        ),
        MenuItem(
            id = "ds3",
            name = "TURPIS EGEST",
            price = 6.80,
            description = "Baked puff sweet glaze with wild berry reduction",
            category = MenuCategory.DESSERT
        ),

        // BEVERAGE
        MenuItem(
            id = "bv1",
            name = "ARCU ID ODIO",
            price = 5.20,
            description = "Sed libero aliquam",
            category = MenuCategory.BEVERAGE
        ),
        MenuItem(
            id = "bv2",
            name = "ALIQUET",
            price = 4.30,
            description = "Freshly brewed citrus infusion",
            category = MenuCategory.BEVERAGE
        ),
        MenuItem(
            id = "bv3",
            name = "ODIO TEMPOR",
            price = 5.20,
            description = "Egestas tellus odio",
            category = MenuCategory.BEVERAGE
        ),
        MenuItem(
            id = "bv4",
            name = "EU PRETIUM",
            price = 3.50,
            description = "Chilled sparkling pure spring water",
            category = MenuCategory.BEVERAGE
        ),
        MenuItem(
            id = "bv5",
            name = "ALIQUET",
            price = 4.30,
            description = "Muted herbal botanic mocktail infusion",
            category = MenuCategory.BEVERAGE
        ),
        MenuItem(
            id = "bv6",
            name = "ARCU ID ODIO",
            price = 5.20,
            description = "Volutpat vele augue",
            category = MenuCategory.BEVERAGE
        ),
        MenuItem(
            id = "bv7",
            name = "ARCU ID ODIO",
            price = 5.20,
            description = "Nec ullamcorper amet",
            category = MenuCategory.BEVERAGE
        )
    )
}
