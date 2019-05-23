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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.text.DateFormat
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList

const val CLASS_TITLE = "15移动春2班"

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
    val storePath: String
        get() = PropertyListener.getProperty("submission-path") ?: "/planh/submission/"
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
                } catch (e: Exception) {
                    e.printStackTrace()
                    report(e)
                }
                logger.info("no task need to be sent, sleep a hour.")
                Thread.sleep(A_HOUR)
            }
        }.start()
    }

    override fun afterPropertiesSet() {
        if (PortConfiguration.PORT == 9713) {
            startMailProcessor()
        }
        report(DateFormat.getInstance().format(Date()) + "&nbsp;&nbsp;服务启动, 端口：${PortConfiguration.PORT}")
    }

    fun report(e: Exception) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<h3>${DateFormat.getInstance().format(Date())}&nbsp;&nbsp;邮件服务异常</h3>\n")
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        e.printStackTrace(printStream)
        val stackTrace = outputStream.toString()
        stackTrace.split(System.lineSeparator()).forEach {
            stringBuilder.append("<p>").append(it
                    .replace(" ", "&nbsp;")
                    .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
            ).append("</p>${System.lineSeparator()}")
        }
        outputStream.close()
        report(stringBuilder.toString())
    }

    fun report(content: String, targetAddress: String = mailUsername) {
        logger.info("send report mail to $targetAddress, content: $content")
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setFrom(mailUsername)
        helper.setTo(targetAddress)
        helper.setSubject("PlanH v2 报告")
        helper.setText(content, true)
        mailSender.send(message)
        logger.info("send report mail success.")
    }

    fun sendMail(taskId: Int) {
        logger.info("start send mail for task: $taskId")
        val task = repositoryService.taskRepository.find(taskId)
                ?: throw Exception("can not find task for send: $taskId")
        val subject = repositoryService.subjectRepository.find(task.subjectId)
                ?: throw Exception("can not find subject for send: ${task.subjectId}")

        logger.info("mail content processing...")
        val content = emailText(task, subject)
        val submitZip = createTaskZipFile(task, subject)

        logger.info("mail mime processing...")
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setFrom(mailUsername)
        helper.setTo(subject.emailAddress)
        logger.info("mail mime subject title: 作业提交：${task.title}_$CLASS_TITLE")
        helper.setSubject("作业提交：${task.title}_$CLASS_TITLE")
        helper.addAttachment(submitZip.name, submitZip)
        helper.setText(content, true)
        logger.info("mail sending...")
        mailSender.send(message)
        if (!submitZip.delete())
            throw Exception("can not delete zip file: '${submitZip.name}'.")
        logger.info("send mail success")
    }

    private fun emailText(
            task: Task, subject: Subject = repositoryService.subjectRepository.find(task.subjectId)!!
    ): String {
        val submissionList = repositoryService.submissionRepository.findByTaskId(task.taskId) ?: ArrayList()
        val users = repositoryService.userRepository.findAll() ?: throw Exception("can not find users")
        // 切换提交与未交名单的阈值
        val flagSize = 20
        val context = Context().apply {
            setVariable("teacherName", subject.teacherName)
            setVariable("taskTitle", task.title)
            setVariable("submitSize", submissionList.size)
            setVariable("userCount", users.size)
            setVariable("flagSize", flagSize)
            val submittedUserIds = submissionList.map { it.userId }
            setVariable("table", users.filter {
                submittedUserIds.contains(it.userId) and (submissionList.size < flagSize)
            }.map { it.userCode to it.userName })
        }

        return thymeleaf.process("mail", context)
    }

    private fun createTaskZipFile(task: Task, subject: Subject): File {
        val srcPath = "$storePath${subject.subjectName}/${task.taskId}-${task.title}"
        logger.info("create zip file for $srcPath")
        return File(srcPath).zip()
    }
}
