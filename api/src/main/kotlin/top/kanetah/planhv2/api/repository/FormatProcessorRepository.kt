package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.FormatProcessor

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface FormatProcessorRepository {
    
    fun save(formatProcessor: FormatProcessor): Int
    
    fun delete(id: Int)
    
    fun update(formatProcessor: FormatProcessor)
    
    fun findByName(formatProcessorName: String): FormatProcessor?
}
