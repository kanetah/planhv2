package top.kanetah.planhv2.api.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import top.kanetah.planhv2.api.service.AccessSecurityService

/**
 * created by kane on 2018/1/22
 */
@Configuration
@EnableWebSecurity
open class SecurityConfiguration @Autowired constructor(
        private val accessSecurityService: AccessSecurityService
) : WebSecurityConfigurerAdapter(){

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(accessSecurityService)
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.authorizeRequests()
                ?.anyRequest()
                ?.permitAll()
                ?.and()
                ?.csrf()
                ?.disable()
    }
}
