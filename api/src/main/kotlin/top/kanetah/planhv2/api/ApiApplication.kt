package top.kanetah.planhv2.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import top.kanetah.planhv2.api.property.PropertyListener

@SpringBootApplication
class ApiApplication {
    companion object {
        private var context: ApplicationContext? = null
        internal fun context() = context!!
        
        @JvmStatic
        fun main(args: Array<String>
        ) = SpringApplication(ApiApplication::class.java).run {
            addListeners(PropertyListener())
            context = run(*args) as ApplicationContext
        }
    }
}

internal val APP_CONTEXT by lazy {
    ApiApplication.context()
}
