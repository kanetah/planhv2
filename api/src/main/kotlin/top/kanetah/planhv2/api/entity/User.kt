package top.kanetah.planhv2.api.entity

import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class User(
        val userId: Number,
        val userCode: String,
        val userName: String,
        val userConfig: UserConfig?,
        val accessToken: String?
)
