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
    val submissionPath = PropertyListener.getProperty("submission-path")

    override fun findAllResourceWithoutSubmissionResource(
    ) = repositoryService.resourceRepository.findAllWithoutForeignKeyWithSubmission()

    override fun createResource(
            name: String,
            size: Double,
            url: String
    ) = Resource(
            resourceName = name,
            resourceSize = size / 100,
            resourceUrl = resourceControllerUrl + url
    ).let {
        with(repositoryService.resourceRepository) {
            findByUrl(it.resourceUrl)?.let {
                if (!deleteResource(it.resourceId))
                    throw Exception("resource delete exception.")
            }
            if (save(it) > 0)
                findByUrl(it.resourceUrl)
            else null
        }
    }

    override fun createResource(
            file: MultipartFile
    ) = with(File(resourcePath + file.originalFilename)) {
        if (!exists()) createNewFile()
        file.transferTo(this)
        createResource(name, length().toDouble(), name)
    }

    override fun deleteResource(
            resourceId: Int
    ) = with(repositoryService.resourceRepository) {
        find(resourceId)?.let { resource ->
            takeIf {
                File(resourcePath + resource.resourceName).delete()
            }.let { delete(resourceId) > 0 }
        }
    } ?: false

    private fun download(
            file: File
    ) = ResponseEntity(
            FileUtils.readFileToByteArray(file),
            HttpHeaders().apply {
                setContentDispositionFormData("attachment",
                        String(file.name.toByteArray(), Charset.forName("iso-8859-1")))
                contentType = MediaType.APPLICATION_OCTET_STREAM
            },
            HttpStatus.CREATED
    )

    override fun download(
            fileName: String
    ) = download(File(resourcePath + fileName))

    override fun download(
            subjectName: String, taskTitle: String, fileName: String
    ) = download(File("$submissionPath$subjectName/$taskTitle/$fileName"))
}
