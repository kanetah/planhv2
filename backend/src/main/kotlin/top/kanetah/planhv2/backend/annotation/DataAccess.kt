package top.kanetah.planhv2.backend.annotation

import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 * created by kane on 2018/1/24
 * 标注数据访问对象
 * 将其同时标注为 MyBatis 中的映射类与 Spring 中的仓库类
 */
@Mapper
@Repository
@Target(AnnotationTarget.CLASS)
annotation class DataAccess
