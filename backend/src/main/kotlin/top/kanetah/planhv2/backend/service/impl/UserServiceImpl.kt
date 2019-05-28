package top.kanetah.planhv2.backend.service.impl

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.usermodel.CellType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.entity.Token
import top.kanetah.planhv2.backend.entity.User
import top.kanetah.planhv2.backend.entity.UserConfig
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.RepositoryService
import top.kanetah.planhv2.backend.service.UserService

/**
 * created by kane on 2018/1/28
 */
@Service
class UserServiceImpl @Autowired constructor(
        private val accessSecurityService: AccessSecurityService,
        private val repositoryService: RepositoryService
) : UserService {

    override fun login(
            userCode: String, userName: String
    ) = repositoryService.userRepository.findByCode(userCode)?.let { user ->
        repositoryService.tokenRepository.deleteByUserId(user.userId)
        if (user.userName == userName)
            accessSecurityService.computeToken(user).let { token ->
                if (repositoryService.tokenRepository.save(
                                Token(userId = user.userId, token = token)) > 0
                ) token else null
            }
        else null
    }

    override fun logout(
            token: String
    ) = repositoryService.tokenRepository.deleteByToken(token) > 0

    override fun findUserByToken(
            token: String
    ) = repositoryService.userRepository.findByToken(token)

    override fun configUser(
            token: String, theme: String?, enableAccessToken: Boolean
    ) = repositoryService.userRepository.findByToken(token)?.let {
        val accessTokenFlag = it.userConfig.enableAccessToken != enableAccessToken && enableAccessToken
        val accessToken =
                if (accessTokenFlag)
                    accessSecurityService.computeAccessToken(it)
                else null
        val saved = repositoryService.userRepository.update(it.copy(
                userConfig = UserConfig(theme, enableAccessToken),
                accessToken = if (accessTokenFlag) accessToken else it.accessToken
        ))
        object {
            @JsonValue
            val success = saved > 0
            @JsonValue
            val accessToken = accessToken
        }
    }

    override fun getAllUser() = ArrayList<Any>().also { list ->
        repositoryService.userRepository.findAll()?.forEach {
            list.add(object {
                @JsonValue
                val userId = it.userId
                @JsonValue
                val userCode = it.userCode
                @JsonValue
                val userName = it.userName
            })
        }
    }

    override fun getAllUserWithLastSubmission() = ArrayList<Any>().also { list ->
        repositoryService.userRepository.findAll()?.forEach {
            list.add(object {
                @JsonValue
                val userId = it.userId
                @JsonValue
                val userCode = it.userCode
                @JsonValue
                val userName = it.userName
                @JsonValue
                val lastSubmit = repositoryService.submissionRepository
                        .findLastByUserId(it.userId)?.submitDate ?: ""
            })
        }
    }

    override fun createUser(
            user: User
    ) = repositoryService.userRepository.save(user) > 0

    val userCodeMark = "学号"
    val userNameMark = "姓名"
    /**
     * 通过Excel文件，批量创建用户
     */
    override fun createUserBatch(
            file: MultipartFile
    ) = with(
            HSSFWorkbook(POIFSFileSystem(file.inputStream)).getSheetAt(0)
    ) {
        // 获取需要关注的列的索引值
        fun getIndexByValue(value: String): Int {
            getRow(0).forEach {
                if (it.stringCellValue == value)
                    return it.columnIndex
            }
            throw Exception("Column does not exist.")
        }
        val (userCodeIndex, userNameIndex) =
                (getIndexByValue(userCodeMark) to getIndexByValue(userNameMark))
        (1..lastRowNum).count {
            try {
                with(getRow(it)) {
                    createUser(User(
                            userCode = with(getCell(userCodeIndex)) {
                                setCellType(CellType.STRING)
                                stringCellValue
                            }, userName = getCell(userNameIndex).stringCellValue
                    ))
                }
            } catch (e: NullPointerException) {
                false
            }
        }
    }

    override fun deleteUser(
            id: Int
    ) = repositoryService.tokenRepository.deleteByUserId(id).let {
        repositoryService.userRepository.delete(id) > 0
    }

    override fun updateUser(
            id: Int, userCode: String?, userName: String?
    ) = with(repositoryService.userRepository) {
        find(id)?.let {
            update(it.copy(userName = userName ?: it.userName, userCode = userCode ?: it.userCode)) > 0
        }
    } ?: false

    override fun findUser(
            id: Int
    ) = repositoryService.userRepository.find(id)
}
