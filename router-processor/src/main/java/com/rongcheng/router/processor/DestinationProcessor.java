package com.rongcheng.router.processor;

import com.google.auto.service.AutoService;
import com.rongcheng.router.annotations.Destination;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

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
        System.out.println(TAG + " >>> process start ...");
        //获取所有标记了@Destination 注解的类的信息
        Set<Element> allDestinationElements = (Set<Element>) roundEnv.getElementsAnnotatedWith(Destination.class);


        System.out.println(TAG + " >>> all Destination elements count = " + allDestinationElements.size());
        if (allDestinationElements.size() < 1) {
            return false;
        }
        //遍历所有注解信息
        for (Element element : allDestinationElements) {
            final TypeElement typeElement = (TypeElement) element;
            //从当前类上获取注解@Destination信息
            final Destination destination = typeElement.getAnnotation(Destination.class);
            final String url = destination.url();
            final String description = destination.description();
            //获取全路径名
            final String realPath = typeElement.getQualifiedName().toString();

            System.out.println(TAG +" >>> url = "+url);
            System.out.println(TAG +" >>> description = "+description);
            System.out.println(TAG +" >>> realPath = "+realPath);
        }

        System.out.println(TAG + " >>> process finish ...");

        return false;
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
