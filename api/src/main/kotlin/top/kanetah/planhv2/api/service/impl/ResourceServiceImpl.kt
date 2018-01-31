package top.kanetah.planhv2.api.service.impl

import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.entity.Resource
import top.kanetah.planhv2.api.property.PropertyListener
import top.kanetah.planhv2.api.service.RepositoryService
import top.kanetah.planhv2.api.service.ResourceService
import java.io.File
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.nio.charset.Charset


/**
 * created by kane on 2018/1/31
 */
@Service
class ResourceServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService
) : ResourceService {
    
    val resourceControllerUrl = PropertyListener.getProperty("resource-controller-url")
    val resourcePath = PropertyListener.getProperty("resource-path")
    
    override fun findAllResourceWithoutSubmissionResource() = repositoryService.resourceRepository.findAllWithoutForeignKeyWithSubmission()
    
    override fun createResource(
            file: MultipartFile
    ): Resource? {
        val target = File(resourcePath + file.originalFilename)
        if (!target.exists())
            if (!target.createNewFile())
                return null
        file.transferTo(target)
        val resource = Resource(
                resourceName = target.name,
                resourceSize = target.length().toDouble() / 100,
                resourceUrl = resourceControllerUrl + target.name
        )
        return if (repositoryService.resourceRepository.save(resource) > 0) resource else null
    }
    
    override fun deleteResource(
            resourceId: Int
    ) = with(repositoryService.resourceRepository) {
        find(resourceId)?.let { resource ->
            takeIf { File(resourcePath + resource.resourceName).delete() }
                    ?.let { delete(resourceId) > 0 }
        }
    } ?: false
    
    override fun download(
            fileName: String
    ) = ResponseEntity(
            FileUtils.readFileToByteArray(File(
                    resourcePath + fileName
            )),
            HttpHeaders().apply {
                setContentDispositionFormData("attachment",
                        String(fileName.toByteArray(), Charset.forName("iso-8859-1")))
                contentType = MediaType.APPLICATION_OCTET_STREAM
            },
            HttpStatus.CREATED
    )
}
