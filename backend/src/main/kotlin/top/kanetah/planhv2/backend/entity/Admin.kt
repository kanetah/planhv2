package top.kanetah.planhv2.backend.entity

import top.kanetah.planhv2.backend.annotation.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Admin(
        val adminId: Int = Int.MIN_VALUE,
        val word: String,
        val allowNewKey: Int = ALLOW_NEW_KEY,
        val accessKeys: String? = null
) {
    companion object {
        const val ALLOW_NEW_KEY = 1
        const val NOT_ALLOW_NEW_KEY = 0
    }
}
