package com.rongcheng.router.processor;

import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rongcheng.router.annotations.Destination;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * @author: frc
 * @description: Destination注解的注解处理器
 * @date: 4/19/21 7:17 PM
 */
@AutoService(Processor.class)
public class DestinationProcessor extends AbstractProcessor {
    private static final String TAG = "DestinationProcessor";

    /**
     * 编译器找到我们关心的注解后，会回调这个方法
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        System.out.println(TAG + " >>> process start ...");
        //获取注解处理器参数
        String rootDir = processingEnv.getOptions().get("root_project_dir");

        if (rootDir == null) {
            System.out.println(TAG + " Error :>>>  rootDir is null, user root_project_dir to set  ");
            throw new RuntimeException("rootDir = null");
        }

        //获取所有标记了@Destination 注解的类的信息
        Set<Element> allDestinationElements = (Set<Element>) roundEnv.getElementsAnnotatedWith(Destination.class);

        System.out.println(TAG + " >>> all Destination elements count = " + allDestinationElements.size());
        if (allDestinationElements.size() < 1) {
            return false;
        }
        //将要自动生成的类的类名
        String className = "RouterMapping_" + System.currentTimeMillis();

        StringBuilder builder = new StringBuilder();
        builder.append("package com.rong.cheng.router.mapping;\n\n");
        builder.append("import java.util.HashMap;\n");
        builder.append("import java.util.Map;\n");
        builder.append("public class ").append(className).append("{\n\n");
        builder.append("    public static Map<String,String> get(){\n\n");
        builder.append("        Map<String,String> mapping= new HashMap<>();\n");

        final JsonArray destinationJsonArray = new JsonArray();

        //遍历所有注解信息
        for (Element element : allDestinationElements) {
            final TypeElement typeElement = (TypeElement) element;
            //从当前类上获取注解@Destination信息
            final Destination destination = typeElement.getAnnotation(Destination.class);
            final String url = destination.url();
            final String description = destination.description();
            //获取全路径名
            final String realPath = typeElement.getQualifiedName().toString();

            System.out.println(TAG + " >>> url = " + url);
            System.out.println(TAG + " >>> description = " + description);
            System.out.println(TAG + " >>> realPath = " + realPath);

            builder.append("        ")
                    .append("mapping.put(")
                    .append("\"" + url + "\"")
                    .append(",")
                    .append("\"" + realPath + "\"")
                    .append(");\n");

            JsonObject item = new JsonObject();
            item.addProperty("url", url);
            item.addProperty("description", description);
            item.addProperty("realPath", realPath);
            destinationJsonArray.add(item);
        }
        builder.append("        return mapping;\n");
        builder.append("    }\n");
        builder.append("}\n");


        String mappingFullClassName = "com.rong.cheng.router.mapping." + className;
        System.out.println(TAG + " >>> mappingFullClassName = " + mappingFullClassName);

        System.out.println(TAG + " >>> class content =\n" + builder);
        //写入自动生成的类到本地文件
        try {
            JavaFileObject source = processingEnv.getFiler()
                    .createSourceFile(mappingFullClassName);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException("Error while create file", e);
        }
        //写入JSON到本地文件中
        writeJsonFile(rootDir, destinationJsonArray);


        System.out.println(TAG + " >>> process finish ...");

        return false;
    }

    private void writeJsonFile(String rootDir, JsonArray destinationJsonArray) {
        File rootDirFile = new File(rootDir);
        if (!rootDirFile.exists()) {
            throw new RuntimeException("root_project_dir not exist");
        }
        File routerFileDir = new File(rootDirFile, "router_mapping");
        if (!routerFileDir.exists()) {
            routerFileDir.mkdir();
        }
        File mappingFile = new File(routerFileDir, "mapping_" + System.currentTimeMillis() + ".json");
        //写入JSON内容
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(mappingFile));
            String jsonString = destinationJsonArray.toString();
            out.write(jsonString);
            out.flush();
            out.close();
        } catch (Throwable throwable) {
            throw new RuntimeException("Error while writing json", throwable);
        }
    }

    /**
     * 告诉javac编译器，当前处理器支持的注解类型
     *
     * @return 注解类型
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(
                Destination.class.getCanonicalName()
        );
    }
}
