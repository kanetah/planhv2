package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Subject

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface SubjectRepository {
    
    fun save(subject: Subject): Int
    
    fun delete(id: Int): Int
    
    fun update(subject: Subject): Int
    
    fun findAll(): ArrayList<Subject>?
    
    fun find(id: Int): Subject?
    
    fun findBySubjectName(subjectName: String): Subject?
}
