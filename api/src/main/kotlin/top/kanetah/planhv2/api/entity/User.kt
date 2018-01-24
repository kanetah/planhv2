package top.kanetah.planhv2.api.entity

/**
 * created by kane on 2018/1/23
 */
data class User(
        val userId: Int,
        val userCode: String,
        val userName: String,
        val userConfig: UserConfig?,
        val accessToken: String?
)
