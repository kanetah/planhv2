package top.kanetah.planhv2.backend.service

import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.entity.Resource

/**
 * created by kane on 2018/1/31
 */
interface ResourceService {
    fun findAllResourceWithoutSubmissionResource(): List<Resource>?
    fun createResource(name: String, size: Double, url: String): Resource?
    fun createResource(file: MultipartFile): Resource?
    fun deleteResource(resourceId: Int): Boolean
    fun download(fileName: String): ResponseEntity<ByteArray>?
    fun download(subjectName: String, taskTitle: String, fileName: String): ResponseEntity<ByteArray>?
    fun find(resourceId: Int): Resource?
    fun findUrlByTokenAndTaskId(token: String, taskId: Int): String?
}
