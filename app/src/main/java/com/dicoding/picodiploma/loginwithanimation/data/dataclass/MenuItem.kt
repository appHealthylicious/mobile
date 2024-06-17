package com.dicoding.picodiploma.loginwithanimation.data.dataclass

data class MenuItem(
    val title: String,
    val listMenu: List<MenuList?>
)

data class MenuList(
    val title: String,
    val photo: String
) {
    override fun toString(): String {
        return "$title|$photo"
    }

    companion object {
        fun fromString(string: String): MenuList {
            val parts = string.split("|")
            return MenuList(parts[0], parts[1])
        }
    }
}
