


package com.rongcheng.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


class RouterPlugin implements Plugin<Project>{
    //引用插件后，apply中逻辑都会执行
    @Override
    void apply(Project project) {
        println("I am  from RouterPlugin ,apply from ${project.name}")
    }
}

