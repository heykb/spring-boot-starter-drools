package com.zhu.drools.strategy;


import com.zhu.drools.annotation.KBase;
import com.zhu.drools.annotation.KJarPath;
import com.zhu.drools.annotation.KReleaseId;
import com.zhu.drools.annotation.KSession;
import com.zhu.drools.config.KieBeanService;
import org.kie.api.KieBase;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;


public class StrategyUtil{

    public static KieContainer generatorKieContainer(Object bean, Field field) {
        field.setAccessible(true);
        KReleaseId kReleaseId = field.getAnnotation(KReleaseId.class);
        KJarPath kJarPath = field.getAnnotation(KJarPath.class);
        KieContainer kieContainer = null;
        if(kJarPath != null){
            kieContainer = KieBeanService.getKieContainer(kJarPath.value());
            if(kJarPath.enableScanner()){
                Path path = Paths.get(kJarPath.value());
                KieBeanService.setKieScanner(kieContainer,path.getParent().toString(),kJarPath.scannerInterval());
            }
        }else if(kReleaseId != null){
            ReleaseId releaseId = KieBeanService.getKieServices().newReleaseId(kReleaseId.groupId(), kReleaseId.artifactId(), kReleaseId.version());
            kieContainer = KieBeanService.getKieContainer(releaseId);
            if(kReleaseId.enableScanner()){
                KieBeanService.setKieScanner(kieContainer,kReleaseId.scannerInterval());
            }
        }else{
            kieContainer =  KieBeanService.getKieContainer();
        }
        return kieContainer;
    }
    public static KieBase generatorKieBase(Object bean, Field field) {
        KieContainer kieContainer = generatorKieContainer(bean,field);
        KBase kBase = field.getAnnotation(KBase.class);
        return KieBeanService.getKieBase(kieContainer,kBase.value());
    }
    public static Object generatorKieSession(Object bean, Field field) {
        KieContainer kieContainer = generatorKieContainer(bean,field);
        KSession kSession = field.getAnnotation(KSession.class);
        if(bean instanceof StatelessKieSession){
            return KieBeanService.getStatelessKieSession(kieContainer,kSession.value());
        }
        return KieBeanService.getKieSession(kieContainer,kSession.value());
    }
}
