package top.kanetah.planhv2.backend.format

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring4.SpringTemplateEngine
import top.kanetah.planhv2.backend.configuration.PortConfiguration
import top.kanetah.planhv2.backend.entity.Subject
import top.kanetah.planhv2.backend.entity.Task
import top.kanetah.planhv2.backend.property.PropertyListener
import top.kanetah.planhv2.backend.service.RepositoryService
import java.io.File
import java.text.DateFormat
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList


/**
 * created by kane on 2019/4/10
 */
@Component
class MailSenderHandle @Autowired constructor(
        val repositoryService: RepositoryService,
        val mailSender: JavaMailSender,
        val thymeleaf: SpringTemplateEngine
) : InitializingBean {

    private val logger = Logger.getLogger(MailSenderHandle::class.qualifiedName)
    val classTitle: String
        get() = PropertyListener.getProperty("class-title") ?: "PlanHv2"
    val storePath: String
        get() = PropertyListener.getProperty("submission-path")!!
    private val dateMap = TreeMap<Date, ArrayList<Int>>()
    @Value(value = "\${spring.mail.username}")
    private lateinit var mailUsername: String

    companion object {
        const val A_HOUR = 60 * 60 * 1000L
    }

    fun setTimer(task: Task) {
        if (Date().before(task.deadline)) {
            dateMap[task.deadline] = (dateMap[task.deadline] ?: ArrayList()).apply { add(task.taskId) }
        }
    }

    fun setTimer(task: Task, oldTime: Date) {
        (dateMap[oldTime] ?: ArrayList()).apply { remove(task.taskId) }
        setTimer(task)
    }

    private fun startMailProcessor() {
        repositoryService.taskRepository.allTasks()?.forEach { setTimer(it) }
        Thread {
            val exception: Exception
            while (true) {
                try {
                    logger.info("enter a new cycle for mail process.")
                    if (!dateMap.isEmpty()) {
                        val deadline = dateMap.firstKey()
                        val distanceTime = deadline.time - Date().time
                        if (distanceTime < 0) {
                            logger.info(
                                    "mail process for deadline ${DateFormat.getTimeInstance().format(deadline)}"
                            )
                            dateMap[deadline]?.forEach(this::sendMail)
                            dateMap.remove(deadline)
                            continue
                        }
                    }
                    Thread.sleep(A_HOUR)
                } catch (e: Exception) {
                    exception = e
                    break
                }
            }
            report(DateFormat.getInstance().format(Date()) + "\t邮件服务异常关闭")
            throw RuntimeException(exception)
        }.start()
    }

    override fun afterPropertiesSet() {
        if (PortConfiguration.PORT == 9713) {
            startMailProcessor()
        }
        report(DateFormat.getInstance().format(Date()) + "\t服务启动")
    }

    fun report(msg: String, targetAddress: String = mailUsername) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setFrom(mailUsername)
        helper.setTo(targetAddress)
        helper.setSubject("PlanH v2 报告")
        helper.setText(msg)
        mailSender.send(message)
        logger.info("send mail to $targetAddress, content: $msg")
    }

    private fun sendMail(taskId: Int) {
        val task = repositoryService.taskRepository.find(taskId) ?: throw Exception("can not find task for send")
        val subject = repositoryService.subjectRepository.find(task.subjectId)
                ?: throw Exception("can not find subject for send")
        val submitZip = createTaskZipFile(task, subject)
        val content = emailText(task, subject)

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setFrom(mailUsername)
        helper.setTo(subject.emailAddress)
        helper.setSubject("作业提交：${task.title}_$classTitle")
        helper.addAttachment(submitZip.name, submitZip)
        helper.setText(content, true)
        mailSender.send(message)
        if (!submitZip.delete())
            throw Exception("can not delete zip file: '${submitZip.name}'.")
    }

    private fun emailText(
            task: Task, subject: Subject = repositoryService.subjectRepository.find(task.subjectId)!!
    ): String {
        val submissionList = repositoryService.submissionRepository.findByTaskId(task.taskId) ?: ArrayList()
        val users = repositoryService.userRepository.findAll() ?: throw Exception("can not find users")
        // 切换提交与未交名单的阈值
        val flagSize = 20
        val context = Context()

        context.setVariable("teacherName", subject.teacherName)
        context.setVariable("taskTitle", task.title)
        context.setVariable("submitSize", submissionList.size)
        context.setVariable("userCount", users.size)
        context.setVariable("flagSize", flagSize)
        val submittedUserIds = submissionList.map { it.userId }
        val flag = submissionList.size < flagSize
        context.setVariable("table", users.filter { submittedUserIds.contains(it.userId) and flag }.map {
            it.userCode to it.userName
        })

        return thymeleaf.process("mail", context)
    }

    private fun createTaskZipFile(task: Task, subject: Subject): File {
        val srcPath = "$storePath${subject.subjectName}/${task.taskId}-${task.title}"
        return CompactTool("$srcPath.zip").zip(srcPath)
    }
}
