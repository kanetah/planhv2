package top.kanetah.planhv2.api.entity

import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class UserConfig(
        val theme: String,
        val accessByToken: Boolean
)
