package top.kanetah.planhv2.api.entity

import top.kanetah.planhv2.api.annotation.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Admin(
        val adminId: Int = Int.MIN_VALUE,
        val password: String
)
