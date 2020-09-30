package com.zhu.drools.strategy;


import com.zhu.drools.annotation.KBase;
import com.zhu.drools.annotation.KJarPath;
import com.zhu.drools.annotation.KReleaseId;
import com.zhu.drools.annotation.KSession;
import com.zhu.drools.config.DroolsConfiguration;
import com.zhu.drools.config.KieBeanService;
import org.kie.api.KieBase;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * The type Strategy util.
 * @author heykb
 */
public class StrategyUtil{

    /**
     * Generator kie container kie container.
     *
     * @param bean  the bean
     * @param field the field
     * @return the kie container
     */
    public static KieContainer generatorKieContainer(Object bean, Field field, DroolsConfiguration droolsConfiguration) {
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
            if(droolsConfiguration.getScannerEnable()||kReleaseId.enableScanner()){
                long scannerInterval = droolsConfiguration.getScannerInterval()!=null?droolsConfiguration.getScannerInterval():kReleaseId.scannerInterval();
                KieBeanService.setKieScanner(kieContainer,scannerInterval);
            }
        }else{
            kieContainer =  KieBeanService.getKieContainer();
        }
        return kieContainer;
    }

    /**
     * Generator kie base kie base.
     *
     * @param bean  the bean
     * @param field the field
     * @return the kie base
     */
    public static KieBase generatorKieBase(Object bean, Field field,DroolsConfiguration droolsConfiguration) {
        KieContainer kieContainer = generatorKieContainer(bean,field,droolsConfiguration);
        KBase kBase = field.getAnnotation(KBase.class);
        return KieBeanService.getKieBase(kieContainer,kBase.value());
    }

    /**
     * Generator kie session object.
     *
     * @param bean  the bean
     * @param field the field
     * @return the object
     */
    public static Object generatorKieSession(Object bean, Field field,DroolsConfiguration droolsConfiguration) {
        KieContainer kieContainer = generatorKieContainer(bean,field,droolsConfiguration);
        KSession kSession = field.getAnnotation(KSession.class);
        if(bean instanceof StatelessKieSession){
            return KieBeanService.getStatelessKieSession(kieContainer,kSession.value());
        }
        return KieBeanService.getKieSession(kieContainer,kSession.value());
    }
}
