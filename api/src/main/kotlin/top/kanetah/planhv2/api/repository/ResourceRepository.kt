package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Resource

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface ResourceRepository {
    
    fun save(resource: Resource): Int
    
    fun findByNameLike(resourceName: String): Array<Resource>?
}