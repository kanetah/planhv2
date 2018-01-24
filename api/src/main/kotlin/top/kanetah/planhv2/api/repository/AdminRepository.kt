package top.kanetah.planhv2.api.repository

import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Admin

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface AdminRepository {
    
    @Select("SELECT * From admin_tab WHERE psd = #{password}")
    @Results(
            Result(property = "adminId", column = "id", javaType = Int::class),
            Result(property = "password", column = "psd", javaType = String::class)
    )
    fun getByPassword(password: String): Admin
}
