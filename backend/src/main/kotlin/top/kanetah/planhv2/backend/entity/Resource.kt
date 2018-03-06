package top.kanetah.planhv2.backend.entity

import top.kanetah.planhv2.backend.annotation.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Resource(
        val resourceId: Int = Int.MIN_VALUE,
        val resourceName: String,
        val resourceSize: Double,
        val resourceUrl: String
)
