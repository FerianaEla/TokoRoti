package com.breadsweet.app.data

import com.breadsweet.app.model.Product

data class PromoBanner(
    val id: Int,
    val title: String,
    val subtitle: String,
    val bgStart: Long, // Hex colors for Gradients in Compose
    val bgEnd: Long,
    val emoji: String
)

object BreadSweetData {
    val CATEGORIES = listOf("Semua", "Sweet Bread", "Savory Bread", "Pastry & Croissant", "Cakes")

    val PROMO_BANNERS = listOf(
        PromoBanner(
            id = 1,
            title = "Fresh From The Oven!",
            subtitle = "Roti hangat tersedia setiap pagi mulai pukul 06.00",
            bgStart = 0xFFD97706, // amber-600
            bgEnd = 0xFFF59E0B, // amber-400
            emoji = "🥐"
        ),
        PromoBanner(
            id = 2,
            title = "Diskon Spesial Hari Ini",
            subtitle = "Cinnamon Roll & Red Velvet Cake diskon hingga 20%!",
            bgStart = 0xFFEA580C, // orange-600
            bgEnd = 0xFFF59E0B, // amber-500
            emoji = "🎉"
        ),
        PromoBanner(
            id = 3,
            title = "Gratis Ongkir",
            subtitle = "Pesan min. Rp 100.000 & dapatkan gratis ongkos kirim",
            bgStart = 0xFFCA8A04, // yellow-600
            bgEnd = 0xFFF59E0B, // amber-400
            emoji = "🚚"
        )
    )

    val INITIAL_PRODUCTS = listOf(
        // Sweet Bread
        Product(
            id = 1,
            name = "Roti Sobek",
            category = "Sweet Bread",
            variants = listOf("Cokelat Keju"),
            price = 18000.0,
            image = "android.resource://com.breadsweet.app/drawable/roti_sobek_cokelat_keju",
            description = "Roti sobek lembut dengan isian cokelat dan keju premium. Sempurna untuk dinikmati bersama keluarga.",
            ingredients = listOf("Tepung terigu", "Cokelat", "Keju", "Mentega", "Gula", "Ragi"),
            rating = 4.9,
            sold = 456,
            stock = 30
        ),
        Product(
            id = 2,
            name = "Roti Bun",
            category = "Sweet Bread",
            variants = listOf("Kopi (Meksiko)"),
            price = 12000.0,
            image = "android.resource://com.breadsweet.app/drawable/coffee_bun_aesthetic",
            description = "Roti bun kopi ala Meksiko dengan topping gula dan aroma kopi yang khas.",
            ingredients = listOf("Tepung terigu", "Kopi", "Gula", "Mentega", "Ragi"),
            rating = 4.7,
            sold = 234,
            stock = 25,
            isNew = true
        ),
        Product(
            id = 3,
            name = "Roti Isi",
            category = "Sweet Bread",
            variants = listOf("Selai Strawberry"),
            price = 10000.0,
            image = "android.resource://com.breadsweet.app/drawable/strawberry_jam_filled_bun",
            description = "Roti lembut dengan isian selai strawberry manis dan segar.",
            ingredients = listOf("Tepung terigu", "Selai strawberry", "Mentega", "Gula", "Ragi"),
            rating = 4.6,
            sold = 389,
            stock = 35
        ),
        Product(
            id = 4,
            name = "Cinnamon Roll",
            category = "Sweet Bread",
            variants = listOf("Kayu Manis Glaze"),
            price = 15000.0,
            originalPrice = 18000.0,
            image = "android.resource://com.breadsweet.app/drawable/cinnamon_rolls_with_glaze",
            description = "Roti gulung kayu manis dengan lapisan glaze manis yang lezat.",
            ingredients = listOf("Tepung terigu", "Kayu manis", "Gula", "Mentega", "Glaze"),
            rating = 4.9,
            sold = 512,
            stock = 28,
            isPromo = true
        ),
        Product(
            id = 5,
            name = "Roti Donat",
            category = "Sweet Bread",
            variants = listOf("Donat Gula Klasik"),
            price = 8000.0,
            image = "android.resource://com.breadsweet.app/drawable/classic_sugar_donuts",
            description = "Donat lembut dengan taburan gula klasik yang manis.",
            ingredients = listOf("Tepung terigu", "Gula", "Mentega", "Ragi", "Susu"),
            rating = 4.8,
            sold = 678,
            stock = 40
        ),
        Product(
            id = 6,
            name = "Roti Kepang",
            category = "Sweet Bread",
            variants = listOf("Pandan Srikaya"),
            price = 11000.0,
            image = "android.resource://com.breadsweet.app/drawable/pandan_sweet_bun_braided_bread",
            description = "Roti kepang dengan aroma pandan dan isian srikaya yang lembut.",
            ingredients = listOf("Tepung terigu", "Pandan", "Srikaya", "Mentega", "Gula"),
            rating = 4.7,
            sold = 298,
            stock = 22
        ),
        Product(
            id = 7,
            name = "Melon Pan",
            category = "Sweet Bread",
            variants = listOf("Vanilla Krunch"),
            price = 13000.0,
            image = "android.resource://com.breadsweet.app/drawable/japanese_melon_pan",
            description = "Roti Jepang dengan topping renyah vanilla yang khas.",
            ingredients = listOf("Tepung terigu", "Vanilla", "Gula", "Mentega", "Ragi"),
            rating = 4.8,
            sold = 345,
            stock = 26,
            isNew = true
        ),
        Product(
            id = 8,
            name = "Roti Sisir",
            category = "Sweet Bread",
            variants = listOf("Mentega Klasik"),
            price = 10000.0,
            image = "android.resource://com.breadsweet.app/drawable/indonesian_roti_sisir_sweet_butter_bread",
            description = "Roti sisir lembut dengan aroma mentega yang harum.",
            ingredients = listOf("Tepung terigu", "Mentega", "Gula", "Susu", "Ragi"),
            rating = 4.6,
            sold = 421,
            stock = 32
        ),

        // Savory Bread
        Product(
            id = 9,
            name = "Roti Abon",
            category = "Savory Bread",
            variants = listOf("Abon Sapi Pedas"),
            price = 15000.0,
            image = "android.resource://com.breadsweet.app/drawable/roti_abon",
            description = "Roti gurih dengan isian abon sapi pedas yang lezat.",
            ingredients = listOf("Tepung terigu", "Abon sapi", "Cabai", "Mentega", "Ragi"),
            rating = 4.8,
            sold = 387,
            stock = 28
        ),
        Product(
            id = 10,
            name = "Pizza Mini",
            category = "Savory Bread",
            variants = listOf("Sosis Keju"),
            price = 14000.0,
            image = "android.resource://com.breadsweet.app/drawable/pizza_mini",
            description = "Pizza mini dengan topping sosis dan keju mozzarella.",
            ingredients = listOf("Tepung terigu", "Sosis", "Keju mozzarella", "Saus tomat", "Oregano"),
            rating = 4.9,
            sold = 523,
            stock = 35,
            isNew = true
        ),
        Product(
            id = 11,
            name = "Roti Sosis",
            category = "Savory Bread",
            variants = listOf("Sosis Keju Mayo"),
            price = 13000.0,
            image = "android.resource://com.breadsweet.app/drawable/roti_sosis",
            description = "Roti lembut dengan isian sosis, keju, dan mayones.",
            ingredients = listOf("Tepung terigu", "Sosis", "Keju", "Mayones", "Ragi"),
            rating = 4.7,
            sold = 456,
            stock = 30
        ),
        Product(
            id = 12,
            name = "Roti Bawang",
            category = "Savory Bread",
            variants = listOf("Garlic Cheese"),
            price = 17000.0,
            originalPrice = 20000.0,
            image = "android.resource://com.breadsweet.app/drawable/roti_bawang_garlic_cheese",
            description = "Roti bawang putih dengan keju yang gurih and harum.",
            ingredients = listOf("Tepung terigu", "Bawang putih", "Keju", "Mentega", "Peterseli"),
            rating = 4.8,
            sold = 367,
            stock = 24,
            isPromo = true
        ),

        // Pastry & Croissant
        Product(
            id = 15,
            name = "Croissant",
            category = "Pastry & Croissant",
            variants = listOf("Butter Original"),
            price = 22000.0,
            image = "android.resource://com.breadsweet.app/drawable/croissant_butter_original",
            description = "Croissant klasik dengan butter premium, renyah di luar dan lembut di dalam.",
            ingredients = listOf("Tepung terigu", "Butter premium", "Gula", "Garam", "Ragi"),
            rating = 4.9,
            sold = 634,
            stock = 30
        ),
        Product(
            id = 16,
            name = "Croissant Almond",
            category = "Pastry & Croissant",
            variants = listOf("Almond Premium"),
            price = 25000.0,
            originalPrice = 28000.0,
            image = "android.resource://com.breadsweet.app/drawable/croissant_almond",
            description = "Croissant premium dengan filling almond dan taburan almond slice.",
            ingredients = listOf("Tepung terigu", "Butter", "Almond", "Gula", "Krim almond"),
            rating = 4.9,
            sold = 478,
            stock = 25,
            isNew = true,
            isPromo = true
        ),
        Product(
            id = 19,
            name = "Cromboloni",
            category = "Pastry & Croissant",
            variants = listOf("Chocolate Supreme"),
            price = 28000.0,
            image = "android.resource://com.breadsweet.app/drawable/cromboloni_chocolate",
            description = "Perpaduan sempurna croissant dan bomboloni dengan isian cokelat premium.",
            ingredients = listOf("Croissant dough", "Cokelat premium", "Cream", "Gula halus"),
            rating = 4.9,
            sold = 567,
            stock = 18,
            isNew = true
        ),

        // Cakes
        Product(
            id = 22,
            name = "Slice Cake Black Forest",
            category = "Cakes",
            variants = listOf("Black Forest"),
            price = 25000.0,
            image = "android.resource://com.breadsweet.app/drawable/slice_cake_black_forest",
            description = "Kue black forest klasik dengan cherry, cokelat, dan whipped cream.",
            ingredients = listOf("Cokelat", "Cherry", "Whipped cream", "Tepung", "Telur"),
            rating = 4.9,
            sold = 512,
            stock = 15
        ),
        Product(
            id = 23,
            name = "Slice Cake Red Velvet",
            category = "Cakes",
            variants = listOf("Red Velvet"),
            price = 25000.0,
            originalPrice = 28000.0,
            image = "android.resource://com.breadsweet.app/drawable/slice_cake_red_velvet",
            description = "Red velvet lembut dengan cream cheese frosting yang creamy.",
            ingredients = listOf("Tepung", "Cocoa powder", "Red coloring", "Cream cheese", "Butter"),
            rating = 4.9,
            sold = 478,
            stock = 15,
            isPromo = true
        ),
        Product(
            id = 27,
            name = "Tiramisu",
            category = "Cakes",
            variants = listOf("Espresso Slice"),
            price = 27000.0,
            image = "android.resource://com.breadsweet.app/drawable/tiramisu_cake_slice",
            description = "Tiramisu Italia dengan espresso kuat dan mascarpone cream.",
            ingredients = listOf("Mascarpone", "Espresso", "Ladyfinger", "Cocoa powder", "Gula"),
            rating = 4.9,
            sold = 367,
            stock = 10,
            isNew = true
        ),
        Product(
            id = 13,
            name = "Roti Daging Sapi",
            category = "Savory Bread",
            variants = listOf("Sapi Cincang"),
            price = 16000.0,
            image = "android.resource://com.breadsweet.app/drawable/beef_stuffed_bun_bakery",
            description = "Roti panggang dengan isian daging sapi cincang bumbu kecap yang gurih dan lezat.",
            ingredients = listOf("Tepung terigu", "Daging sapi", "Bawang bombay", "Kecap manis", "Mentega"),
            rating = 4.8,
            sold = 189,
            stock = 20
        ),
        Product(
            id = 14,
            name = "Croque Monsieur",
            category = "Savory Bread",
            variants = listOf("Beef & Cheese"),
            price = 20000.0,
            image = "android.resource://com.breadsweet.app/drawable/croque_monsieur_aesthetic",
            description = "Roti panggang khas Prancis dengan lapisan daging sapi asap, keju meleleh, dan saus béchamel.",
            ingredients = listOf("Roti tawar", "Daging asap", "Keju Mozzarella", "Saus béchamel", "Mentega"),
            rating = 4.9,
            sold = 142,
            stock = 15
        ),
        Product(
            id = 17,
            name = "Cruffin Karamel",
            category = "Pastry & Croissant",
            variants = listOf("Salted Caramel"),
            price = 24000.0,
            image = "android.resource://com.breadsweet.app/drawable/cruffin_pastrycaramel",
            description = "Perpaduan croissant berbentuk muffin dengan isian saus karamel gurih yang meleleh di mulut.",
            ingredients = listOf("Tepung terigu", "Mentega premium", "Gula", "Garam", "Karamel"),
            rating = 4.8,
            sold = 215,
            stock = 12,
            isNew = true
        ),
        Product(
            id = 18,
            name = "Danish Pastry Blueberry",
            category = "Pastry & Croissant",
            variants = listOf("Blueberry Jam"),
            price = 23000.0,
            image = "android.resource://com.breadsweet.app/drawable/danish_pastry_blueberry",
            description = "Pastry renyah dengan custard lembut dan topping selai blueberry segar di tengahnya.",
            ingredients = listOf("Tepung terigu", "Butter", "Custard", "Selai blueberry", "Gula"),
            rating = 4.7,
            sold = 198,
            stock = 18
        ),
        Product(
            id = 20,
            name = "Pain au Chocolat",
            category = "Pastry & Croissant",
            variants = listOf("Chocolate Filling"),
            price = 22000.0,
            image = "android.resource://com.breadsweet.app/drawable/pain_au_chocolat_aesthetic",
            description = "Pastry khas Prancis yang berlapis-lapis dan renyah dengan isian cokelat batangan premium di dalamnya.",
            ingredients = listOf("Tepung terigu", "Butter premium", "Cokelat batang", "Ragi", "Gula"),
            rating = 4.9,
            sold = 310,
            stock = 22
        ),
        Product(
            id = 21,
            name = "Apple Pie Puff Pastry",
            category = "Pastry & Croissant",
            variants = listOf("Apel Kayu Manis"),
            price = 21000.0,
            image = "android.resource://com.breadsweet.app/drawable/puff_pastry_apple_pie",
            description = "Puff pastry renyah dengan isian potongan buah apel segar beraroma kayu manis.",
            ingredients = listOf("Tepung terigu", "Butter", "Apel", "Kayu manis", "Gula", "Kismis"),
            rating = 4.8,
            sold = 175,
            stock = 15
        ),
        Product(
            id = 24,
            name = "Cupcake Vanilla Cokelat",
            category = "Cakes",
            variants = listOf("Vanilla Chocolate"),
            price = 12000.0,
            image = "android.resource://com.breadsweet.app/drawable/cupcake_vanila_cokelat",
            description = "Cupcake lembut dengan rasa vanilla dipadukan topping krim cokelat manis.",
            ingredients = listOf("Tepung", "Gula", "Mentega", "Vanilla", "Cokelat", "Telur"),
            rating = 4.6,
            sold = 124,
            stock = 20
        ),
        Product(
            id = 25,
            name = "Lapis Legit Slice",
            category = "Cakes",
            variants = listOf("Original Premium"),
            price = 18000.0,
            image = "android.resource://com.breadsweet.app/drawable/lapis_legit",
            description = "Kue lapis legit tradisional yang kaya akan bumbu spekoek dan mentega premium, dipanggang lapis demi lapis.",
            ingredients = listOf("Kuning telur", "Mentega wysman", "Tepung terigu", "Gula halus", "Bumbu spekoek"),
            rating = 4.9,
            sold = 289,
            stock = 10,
            isNew = true
        ),
        Product(
            id = 26,
            name = "Matcha Mille Crepe",
            category = "Cakes",
            variants = listOf("Uji Matcha Slice"),
            price = 26000.0,
            image = "android.resource://com.breadsweet.app/drawable/matcha_mille_crepe_cake_slice",
            description = "Lapisan crepe tipis dengan krim rasa matcha premium (teh hijau Jepang) yang lembut dan wangi.",
            ingredients = listOf("Tepung terigu", "Uji matcha powder", "Susu segar", "Whipped cream", "Telur"),
            rating = 4.8,
            sold = 167,
            stock = 12,
            isNew = true
        ),
        Product(
            id = 28,
            name = "Slice Cheesecake",
            category = "Cakes",
            variants = listOf("New York Style"),
            price = 26000.0,
            image = "android.resource://com.breadsweet.app/drawable/slice_cake_cheese_cake",
            description = "Cheesecake panggang ala New York yang lembut, padat, dan gurih dengan alas biskuit renyah.",
            ingredients = listOf("Cream cheese", "Sour cream", "Gula", "Telur", "Biskuit", "Butter"),
            rating = 4.9,
            sold = 215,
            stock = 14
        )
    )
}
