package top.kanetah.planhv2.api.configuration

import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.net.InetAddress
import java.net.Socket


/**
 * created by kane on 2018/2/28
 */
@Configuration
class PortConfiguration : TomcatEmbeddedServletContainerFactory() {
    init {
        port = if (try {
                    Socket(InetAddress.getByName("127.0.0.1"), 9713)
                    true
                } catch (e: IOException) {
                    false
                })
            9913
        else
            9713
    }
}
