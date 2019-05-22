package top.kanetah.planhv2.backend.repository

import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.Subject

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
