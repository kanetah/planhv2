package top.kanetah.planhv2.api.entity

/**
 * created by kane on 2018/1/28
 */
data class Token(
        val tokenId: Int = Int.MIN_VALUE,
        val userId: Int,
        val token: String
)
