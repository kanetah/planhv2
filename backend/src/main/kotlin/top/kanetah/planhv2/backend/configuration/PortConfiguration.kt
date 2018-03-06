package top.kanetah.planhv2.backend.configuration

import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.io.PrintStream
import java.net.InetAddress
import java.net.Socket


/**
 * created by kane on 2018/2/28
 */
@Configuration
class PortConfiguration : TomcatEmbeddedServletContainerFactory(PortConfiguration.PORT) {
    companion object {
        val PORT by lazy {
            try {
                Socket(InetAddress.getByName("127.0.0.1"), 9713)
                9913
            } catch (e: IOException) {
                9713
            }
        }
    }
}
