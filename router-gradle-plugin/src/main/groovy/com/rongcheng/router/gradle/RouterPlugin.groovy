


package com.rongcheng.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


class RouterPlugin implements Plugin<Project>{
    //引用插件后，apply中逻辑都会执行
    @Override
    void apply(Project project) {
        println("I am  from RouterPlugin ,apply from ${project.name}")
        //注册Extension
        project.getExtensions().create("router",RouterExtension)

        //当前project配置阶段结束
        project.afterEvaluate {
             RouterExtension extension=project["router"]
            println("用户设置的WIKI路径为： ${extension.wikiDir}")
        }
    }
}

