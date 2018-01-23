package top.kanetah.planhv2.api.entity

import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Resource(
        val resourceId: Number,
        val resourceName: String,
        val resourceSize: Number,
        val resourceUrl: String
)
