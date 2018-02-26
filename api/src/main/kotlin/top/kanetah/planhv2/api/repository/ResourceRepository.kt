package top.kanetah.planhv2.api.repository

import org.apache.ibatis.annotations.Param
import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Resource

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface ResourceRepository {
    
    fun save(resource: Resource): Int
    
    fun delete(id: Int): Int
    
    fun update(resource: Resource): Int
    
    fun findAllWithoutForeignKeyWithSubmission(): ArrayList<Resource>?
    
    fun find(id: Int): Resource?
    
    fun findByNameLike(resourceName: String): Array<Resource>?
    
    fun findByUrl(resourceUrl: String): Resource?

    fun findByTokenAndTaskId(@Param("token") token: String, @Param("taskId") taskId: Int): Resource?
}
