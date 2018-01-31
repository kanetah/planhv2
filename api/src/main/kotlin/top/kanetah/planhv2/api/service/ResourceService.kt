package top.kanetah.planhv2.api.service

import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.entity.Resource

/**
 * created by kane on 2018/1/31
 */
interface ResourceService {
    fun findAllResourceWithoutSubmissionResource(): ArrayList<Resource>?
    fun createResource(file: MultipartFile): Resource?
    fun deleteResource(resourceId: Int): Boolean
    fun download(fileName: String): ResponseEntity<ByteArray>?
}
