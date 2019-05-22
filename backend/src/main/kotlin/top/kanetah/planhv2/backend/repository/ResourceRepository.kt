package top.kanetah.planhv2.backend.repository

import org.apache.ibatis.annotations.Param
import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.Resource

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
