package top.kanetah.planhv2.api.entity

import top.kanetah.planhv2.api.annotation.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class User(
        val userId: Int = Int.MIN_VALUE,
        val userCode: String,
        val userName: String,
        val userConfig: UserConfig = UserConfig(),
        val accessToken: String? = null
) {
    constructor(
            userId: Int, userCode: String, userName: String, theme: String?, enableAccessToken: Boolean, accessToken: String?
    ): this(userId, userCode, userName, UserConfig(theme, enableAccessToken), accessToken)
    
    fun getTheme() = userConfig.theme
    
    fun getEnableAccessToken() = userConfig.enableAccessToken
}
