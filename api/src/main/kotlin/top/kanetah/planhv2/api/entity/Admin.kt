package top.kanetah.planhv2.api.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Admin(
        @Id
        @GeneratedValue
        val adminId: Long,
        val password: String
)
