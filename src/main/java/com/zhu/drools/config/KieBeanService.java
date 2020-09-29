package com.zhu.drools.config;

import org.apache.commons.lang3.StringUtils;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.core.impl.InternalKieContainer;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.scanner.KieScannersRegistry;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;


public class KieBeanService {

    private  static ConcurrentMap<String, KieContainer> kieContainers;
    static {
        Class clazz = getKieServices().getClass();
        try {
            Field field = clazz.getDeclaredField("kContainers");
            field.setAccessible(true);
            kieContainers = (ConcurrentMap<String, KieContainer>) field.get(getKieServices());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }



    public static KieServices getKieServices(){
        return KieServices.Factory.get();
    }


    static ConcurrentMap<String, KieContainer> getKieContainers(){
        return kieContainers;
    }


    public static KieSession getKieSession(KieContainer kieContainer,String sessionName){

        if(StringUtils.isEmpty(sessionName)){
            return ((KieContainerImpl)kieContainer).getKieSession();
        }
        return ((KieContainerImpl)kieContainer).getKieSession(sessionName);
    }
    public static StatelessKieSession getStatelessKieSession(KieContainer kieContainer,String sessionName){
        if(StringUtils.isEmpty(sessionName)){
            return ((KieContainerImpl)kieContainer).getStatelessKieSession();
        }
        return ((KieContainerImpl)kieContainer).getStatelessKieSession(sessionName);
    }

    public static KieContainer getKieContainer(ReleaseId releaseId){
        String kieContainerId = releaseId.toString();
        KieContainer kieContainer = getKieContainers().get(kieContainerId);
        if(kieContainer==null){
            kieContainer = getKieServices().newKieContainer(releaseId.toString(),releaseId);
        }
        return kieContainer;
    }
    static KieContainer getKieContainerFromKieScanner(KieScanner kieScanner){
        KieContainer kieContainer = null;
        Class<?> searchType = kieScanner.getClass();

        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ("kieContainer".equals(field.getName()) && InternalKieContainer.class.equals(field.getType())) {
                    field.setAccessible(true);
                    try {
                        kieContainer = (KieContainer) field.get(kieScanner);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return kieContainer;
    }
    public static void setKieScanner(KieContainer kieContainer,long scannerInterval){

        for(KieScanner kScanner: KieScannersRegistry.getAllKieScanners()){
            if(kieContainer == getKieContainerFromKieScanner(kScanner)){
                return;
            }
        }
        KieScanner kieScanner = getKieServices().newKieScanner(kieContainer);
        kieScanner.start( scannerInterval );
    }
    public static void setKieScanner(KieContainer kieContainer,String kjarPath,long scannerInterval){

        for(KieScanner kScanner: KieScannersRegistry.getAllKieScanners()){
            if(kieContainer == getKieContainerFromKieScanner(kScanner)){
                return;
            }
        }
        KieScanner   kieScanner = getKieServices().newKieScanner(kieContainer,kjarPath);
        kieScanner.start( scannerInterval );
    }


    public static KieContainer getKieContainer(){
        String kieContainerId = getKieServices().getRepository().getDefaultReleaseId().toString();
        KieContainer kieContainer = getKieContainers().get(kieContainerId);
        if(kieContainer==null){
            kieContainer = getKieServices().newKieClasspathContainer(kieContainerId);
        }
        return kieContainer;
    }

    public static KieContainer getKieContainer(String kjarPath){
        KieServices kieServices = getKieServices();
        Resource resource = kieServices.getResources().newFileSystemResource(kjarPath);
        KieModule kieModule = kieServices.getRepository().addKieModule(resource);
        ReleaseId releaseId = kieModule.getReleaseId();
        return kieServices.newKieContainer(releaseId);
    }

    public static KieBase getKieBase(KieContainer kieContainer, String baseName){
        if(StringUtils.isEmpty(baseName)){
            return getKieBase(kieContainer);
        }
        return kieContainer.getKieBase(baseName);
    }
    public static  KieBase getKieBase(KieContainer kieContainer){
        return kieContainer.getKieBase();
    }
}
