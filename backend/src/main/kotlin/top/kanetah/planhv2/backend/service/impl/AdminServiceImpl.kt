package top.kanetah.planhv2.backend.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.endpoint.ShutdownEndpoint
import org.springframework.stereotype.Service
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.configuration.PortConfiguration
import top.kanetah.planhv2.backend.entity.Admin
import top.kanetah.planhv2.backend.entity.Auth
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.AdminService
import top.kanetah.planhv2.backend.service.RepositoryService
import java.util.*

/**
 * created by kane on 2018/1/26
 */
@Service
class AdminServiceImpl @Autowired constructor(
        private val accessSecurityService: AccessSecurityService,
        private val repositoryService: RepositoryService,
        private val shutdownEndpoint: ShutdownEndpoint
) : AdminService {
    override fun shutdown(port: Int?) = (PortConfiguration.PORT == port).also {
        if (it) shutdownEndpoint.invoke()
    }

    private infix fun String?.join(key: String
    ) = this@join?.let { arrayOf(this@join, key).joinToString(",") } ?: key

    override fun adminWriteIn(
            word: String, key: String
    ) = repositoryService.adminRepository.findByWord(word)?.let { admin ->
        if (admin.accessKeys?.contains(key) != true) {
            if (admin.allowNewKey == Admin.ALLOW_NEW_KEY) {
                repositoryService.adminRepository.update(admin.copy(
                        allowNewKey = Admin.NOT_ALLOW_NEW_KEY,
                        accessKeys = (admin.accessKeys join key)
                ))
            } else {
                return@let null
            }
        }
        repositoryService.authRepository.deleteByAdminId(admin.adminId)
        accessSecurityService.computeAuth(admin).let { auth ->
            if (repositoryService.authRepository.save(
                            Auth(adminId = admin.adminId, authorized = auth)) > 0
            ) auth else null
        }
    }

    override fun adminCrossOut(
            authorized: String
    ) = repositoryService.authRepository.deleteByAuthorized(authorized) > 0

    override fun allowNewKey(
            authorized: String, clearAll: Boolean
    ) = repositoryService.authRepository.findByAuthorized(authorized)?.let {
        repositoryService.adminRepository.run {
            find(it.adminId)?.let {
                update(it.copy(
                        allowNewKey = Admin.ALLOW_NEW_KEY,
                        accessKeys = if (clearAll) "" else it.accessKeys
                )) > 0
            }
        }
    } ?: false

    override fun getAllAdmins() = ArrayList<Any>().also { list ->
        repositoryService.adminRepository.findAll().forEach {
            list.add(object {
                @JsonValue
                val word = it.word
                @JsonValue
                val allowNewKey = it.allowNewKey == Admin.ALLOW_NEW_KEY
                @JsonValue
                val accessKeysCount = it.accessKeys?.split(",")?.size ?: 0
            })
        }
    }

    override fun createAdmin(
            word: String
    ) = repositoryService.adminRepository.save(Admin(word = word)) > 0

    override fun deleteAdmin(
            id: Int
    ) = repositoryService.adminRepository.delete(id) > 0

    override fun findAdmin(
            id: Int
    ) = repositoryService.adminRepository.find(id)

    override fun findAdmin(
            word: String
    ) = repositoryService.adminRepository.findByWord(word)
}
