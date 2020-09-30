package com.zhu.drools.config;

import org.apache.commons.lang3.StringUtils;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.core.impl.InternalKieContainer;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.scanner.KieScannersRegistry;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;


/**
 * The type Kie bean service.
 * @author heykb
 */
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


    /**
     * Get kie services kie services.
     *
     * @return the kie services
     */
    public static KieServices getKieServices(){
        return KieServices.Factory.get();
    }


    /**
     * Get kie containers concurrent map.
     *
     * @return the concurrent map
     */
    static ConcurrentMap<String, KieContainer> getKieContainers(){
        return kieContainers;
    }


    /**
     * Get kie session kie session.
     *
     * @param kieContainer the kie container
     * @param sessionName  the session name
     * @return the kie session
     */
    public static KieSession getKieSession(KieContainer kieContainer,String sessionName){

        if(StringUtils.isEmpty(sessionName)){
            return ((KieContainerImpl)kieContainer).getKieSession();
        }
        return ((KieContainerImpl)kieContainer).getKieSession(sessionName);
    }

    /**
     * Get stateless kie session stateless kie session.
     *
     * @param kieContainer the kie container
     * @param sessionName  the session name
     * @return the stateless kie session
     */
    public static StatelessKieSession getStatelessKieSession(KieContainer kieContainer,String sessionName){
        if(StringUtils.isEmpty(sessionName)){
            return ((KieContainerImpl)kieContainer).getStatelessKieSession();
        }
        return ((KieContainerImpl)kieContainer).getStatelessKieSession(sessionName);
    }

    /**
     * Get kie container kie container.
     *
     * @param releaseId the release id
     * @return the kie container
     */
    public static KieContainer getKieContainer(ReleaseId releaseId){
        String kieContainerId = releaseId.toString();
        KieContainer kieContainer = getKieContainers().get(kieContainerId);
        if(kieContainer==null){
            kieContainer = getKieServices().newKieContainer(releaseId.toString(),releaseId);
        }
        return kieContainer;
    }

    /**
     * Get kie container from kie scanner kie container.
     *
     * @param kieScanner the kie scanner
     * @return the kie container
     */
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

    /**
     * Set kie scanner.
     *
     * @param kieContainer    the kie container
     * @param scannerInterval the scanner interval
     */
    public static void setKieScanner(KieContainer kieContainer,long scannerInterval){

        for(KieScanner kScanner: KieScannersRegistry.getAllKieScanners()){
            if(kieContainer == getKieContainerFromKieScanner(kScanner)){
                return;
            }
        }
        KieScanner kieScanner = getKieServices().newKieScanner(kieContainer);
        kieScanner.start( scannerInterval );
    }

    /**
     * Set kie scanner.
     *
     * @param kieContainer    the kie container
     * @param kjarPath        the kjar path
     * @param scannerInterval the scanner interval
     */
    public static void setKieScanner(KieContainer kieContainer,String kjarPath,long scannerInterval){

        for(KieScanner kScanner: KieScannersRegistry.getAllKieScanners()){
            if(kieContainer == getKieContainerFromKieScanner(kScanner)){
                return;
            }
        }
        KieScanner kieScanner = getKieServices().newKieScanner(kieContainer,kjarPath);
        kieScanner.start( scannerInterval );
    }

    public static KieContainer getKieContainer(){

       return getKieServices().getKieClasspathContainer(Thread.currentThread().getContextClassLoader());
    }
    /**
     * Get kie container kie container.
     *
     * @return the kie container
     */
    public static KieContainer getKieContainerByRules(){
        String RULES_PATH = "rules/";
        String kieContainerId = getKieServices().getRepository().getDefaultReleaseId().toString();
        KieContainer kieContainer = getKieContainers().get(kieContainerId);
        if(kieContainer==null){
            KieFileSystem kfs = getKieServices().newKieFileSystem();
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            try {
                org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
                Arrays.stream(resources).forEach(resource ->kfs.write(ResourceFactory.newClassPathResource(RULES_PATH + resource.getFilename(), "UTF-8")) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            KieBuilder kieBuilder = getKieServices().newKieBuilder( kfs ).buildAll();
            if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                throw new RuntimeException("Build Errors:\n" + kieBuilder.getResults().toString());
            }
            kieContainer = getKieServices().newKieContainer(kieContainerId,getKieServices().getRepository().getDefaultReleaseId());

        }
        return kieContainer;
    }


    /**
     * Get kie container kie container.
     *
     * @param kjarPath the kjar path
     * @return the kie container
     */
    public static KieContainer getKieContainer(String kjarPath){
        KieServices kieServices = getKieServices();
        Resource resource = kieServices.getResources().newFileSystemResource(kjarPath);
        KieModule kieModule = kieServices.getRepository().addKieModule(resource);
        ReleaseId releaseId = kieModule.getReleaseId();
        return kieServices.newKieContainer(releaseId);
    }

    /**
     * Get kie base kie base.
     *
     * @param kieContainer the kie container
     * @param baseName     the base name
     * @return the kie base
     */
    public static KieBase getKieBase(KieContainer kieContainer, String baseName){
        if(StringUtils.isEmpty(baseName)){
            return getKieBase(kieContainer);
        }
        return kieContainer.getKieBase(baseName);
    }

    /**
     * Get kie base kie base.
     *
     * @param kieContainer the kie container
     * @return the kie base
     */
    public static  KieBase getKieBase(KieContainer kieContainer){
        return kieContainer.getKieBase();
    }
}
