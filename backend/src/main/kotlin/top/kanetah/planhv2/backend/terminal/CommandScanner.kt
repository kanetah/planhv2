package top.kanetah.planhv2.backend.terminal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import top.kanetah.planhv2.backend.entity.Admin
import top.kanetah.planhv2.backend.service.AdminService
import top.kanetah.planhv2.backend.service.RepositoryService
import java.util.*
import java.util.logging.Logger

/**
 * created by kane on 2019/4/7
 */
@Component
class CommandScanner @Autowired constructor(
        private val adminService: AdminService
) {
    private val logger = Logger.getLogger(CommandScanner::class.qualifiedName)

    companion object {
        const val CREATE_ADMIN = "create-admin"
        const val ALLOW_ADMIN = "allow-admin"
        const val RESET_ADMIN = "reset-admin"
    }

    init {
        Thread {
            val scanner = Scanner(System.`in`)
            while (scanner.hasNextLine()) {
                scanner.nextLine().apply {
                    when {
                        startsWith(CREATE_ADMIN) -> {
                            createAdmin(substring(CREATE_ADMIN.length).trim())
                        }
                        startsWith(ALLOW_ADMIN) -> {
                            allowAdmin(substring(ALLOW_ADMIN.length).trim())
                        }
                        startsWith(RESET_ADMIN) -> {
                            resetAdmin(substring(RESET_ADMIN.length).trim())
                        }
                        else -> {
                            logger.warning("Can not solved command: $this")
                        }
                    }
                }
            }
        }.start()
    }

    private fun createAdmin(adminWord: String) {
        if (adminService.createAdmin(adminWord)) {
            logger.info("Create admin: $adminWord")
        } else {
            logger.warning("Create admin failure: $adminWord")
        }
    }

    private fun allowAdmin(adminWord: String) {
        if (adminService.allowNewKey(adminWord)) {
            logger.info("Allow admin: $adminWord")
        } else {
            logger.warning("Allow admin failure: $adminWord")
        }
    }

    private fun resetAdmin(adminWord: String) {
        if (adminService.allowNewKey(adminWord, true)) {
            logger.info("Reset admin: $adminWord")
        } else {
            logger.warning("Reset admin failure: $adminWord")
        }
    }
}
