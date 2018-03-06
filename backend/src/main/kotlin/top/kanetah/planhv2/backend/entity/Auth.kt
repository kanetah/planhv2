package top.kanetah.planhv2.backend.entity

/**
 * created by kane on 2018/1/27
 */
data class Auth (
        val authorizedId: Int = Int.MIN_VALUE,
        val adminId: Int,
        val authorized: String
)