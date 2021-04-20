package com.rongcheng.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import groovy.json.JsonSlurper


class RouterPlugin implements Plugin<Project> {
    //引用插件后，apply中逻辑都会执行
    @Override
    void apply(Project project) {

        //1.自动帮助用户传递路径参数到注解处理器中
        if (project.extensions.findByName("kapt") != null) {
            project.extensions.findByName("kapt").arguments {
                arg("root_project_dir", project.rootProject.projectDir);
            }
        }

        //2.实现一个旧的构建产物自动清理
        project.clean.doFirst {
            //clean task 执行前执行下面逻辑
            File routerMappingDir = new File(project.rootProject.projectDir, "router_mapping")
            if (routerMappingDir.exists()) {
                routerMappingDir.deleteDir()
            }
        }

        println("I am  from RouterPlugin ,apply from ${project.name}")
        //注册Extension
        project.getExtensions().create("router", RouterExtension)
        //当前project配置阶段结束
        project.afterEvaluate {
            RouterExtension extension = project["router"]
            println("用户设置的WIKI路径为： ${extension.wikiDir}")

            //3.在javac任务后，汇总生成文档
            project.tasks.findAll { task ->
                task.name.startsWith('compile') &&
                        task.name.endsWith('JavaWithJavac')
            }.each { task ->
                task.doLast {
                   writeMkFile(project)
                }
            }
        }
    }

    private  void writeMkFile(Project project){
        File routerMappingDir = new File(project.rootProject.projectDir, "router_mapping")
        if (!routerMappingDir.exists()) {
            return
        }
        File[] allChildFiles = routerMappingDir.listFiles()
        if (allChildFiles.length < 1) {
            return
        }

        StringBuilder markdownBuilder = new StringBuilder()
        markdownBuilder.append("# 页面文档\n\n")
        allChildFiles.each { child ->
            if (child.name.endsWith(".json")) {
                JsonSlurper jsonSlurper = new JsonSlurper()
                def content = jsonSlurper.parse(child)

                content.each { innerContent ->
                    def url = innerContent['url']
                    def description = innerContent['description']
                    def realPath = innerContent['realPath']

                    markdownBuilder.append("## $description \n")
                    markdownBuilder.append("- url :$url \n")
                    markdownBuilder.append("- realPath$realPath \n\n")
                }
            }
        }
        File wikiFileDir = new File(extension.wikiDir)
        if (!wikiFileDir.exists()) {
            wikiFileDir.mkdir()
        }
        File wikiFile = new File(wikiFileDir, "页面文档.md")
        if (wikiFile.exists()) {
            wikiFile.delete()
        }
        wikiFile.write(markdownBuilder.toString())
    }
}

