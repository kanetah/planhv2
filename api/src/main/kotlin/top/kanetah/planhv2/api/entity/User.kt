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
        val userConfig: UserConfig,
        val accessToken: String?
) {
    constructor(
            userId: Int, userCode: String, userName: String, theme: String?, accessByToken: Boolean, accessToken: String?
    ): this(userId, userCode, userName, UserConfig(theme, accessByToken), accessToken)
}
