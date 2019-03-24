package top.kanetah.planhv2.backend.service

import top.kanetah.planhv2.backend.entity.Subject

/**
 * created by kane on 2018/1/30
 */
interface SubjectService {
    fun getAllSubject(): ArrayList<Subject>?
    fun createSubject(subjectName: String, teacherName: String, emailAddress: String, teamLimit: IntArray?, recommendProcessorId: Int = 0): Boolean
    fun deleteSubject(id: Int): Boolean
    fun updateSubject(id: Int, subjectName: String, teacherName: String, emailAddress: String, teamLimit: IntArray?, recommendProcessorId: Int): Boolean
    fun findSubject(id: Int): Subject?
}