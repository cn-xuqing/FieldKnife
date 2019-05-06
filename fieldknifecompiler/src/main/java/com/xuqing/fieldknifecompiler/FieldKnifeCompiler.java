package com.xuqing.fieldknifecompiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xuqing.fieldknifeannotation.BindObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class FieldKnifeCompiler extends AbstractProcessor {
    private Filer filer;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindObject.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element elem : roundEnvironment.getElementsAnnotatedWith(BindObject.class)) {
            if (elem.getKind() == ElementKind.CLASS) {
                MethodSpec main = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Object.class, "obj")
                        //.addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                        .addStatement("$T clazz=obj.getClass()", Class.class)
                        .addStatement("$T[] fields=clazz.getDeclaredFields()", Field.class)
                        .addCode("for(int i=0;i<fields.length;i++){try{")
                        .addStatement("$T field=fields[i]", Field.class)
                        .addStatement("field.setAccessible(true)")
                        .addStatement("field.set(obj,i)")
                        .addCode("}catch(Exception e){e.printStackTrace();}}")
                        .build();
                // class
                TypeSpec clazz = TypeSpec.classBuilder(elem.getSimpleName()+"_FieldKnife")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(main)
                        .build();
                try {
                    String packageName=processingEnv.getElementUtils().getPackageOf(elem).getQualifiedName().toString();
                    // build clazz.java
                    JavaFile javaFile = JavaFile.builder(packageName, clazz)
                            .addFileComment(" This codes are generated automatically. Do not modify!")
                            .build();
                    // write to file
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

}
