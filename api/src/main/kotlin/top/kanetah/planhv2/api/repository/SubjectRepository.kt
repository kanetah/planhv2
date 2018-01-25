package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Subject

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface SubjectRepository {
    
    fun save(subject: Subject): Int
    
    fun findBySubjectName(subjectName: String): Subject?
}