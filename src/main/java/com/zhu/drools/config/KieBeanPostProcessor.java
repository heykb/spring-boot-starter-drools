package com.zhu.drools.config;


import com.zhu.drools.annotation.KBase;
import com.zhu.drools.annotation.KContainer;
import com.zhu.drools.annotation.KServices;
import com.zhu.drools.annotation.KSession;
import com.zhu.drools.strategy.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.settings.*;
import org.apache.maven.settings.io.DefaultSettingsReader;
import org.apache.maven.settings.io.SettingsReader;
import org.appformer.maven.integration.MavenRepositoryConfiguration;
import org.appformer.maven.integration.embedder.MavenSettings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The type Kie bean post processor.
 *
 * @author heykb
 */
public class KieBeanPostProcessor  implements BeanPostProcessor {
    private final static String defaultServerXml = "<settings><servers><server>\n" +
            "            <username>%s<password>\n" +
            "            <configuration>\n" +
            "                <wagonProvider>httpclient<usePreemptive>\n" +
            "                    <httpConfiguration>\n" +
            "            <server><settings>";

    private final Map<Class<?>, KieInjectStrategy> strategyMap = new HashMap(){
        {
            put(KContainer.class,new KieContainerInjectStrategy());
            put(KBase.class,new KieBaseInjectStrategy());
            put(KSession.class,new KieSessionInjectStrategy());
            put(KServices.class,new KieServicesInjectStrategy());
        }
    };
    private DroolsConfiguration droolsConfiguration;


    /**
     * Instantiates a new Kie bean post processor.
     *
     * @param droolsConfiguration the drools configuration
     */
    public KieBeanPostProcessor(DroolsConfiguration droolsConfiguration) {
        this.droolsConfiguration = droolsConfiguration;
        try {
            if(droolsConfiguration!=null && !CollectionUtils.isEmpty(droolsConfiguration.getRepositories())){
                initRep();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Init rep.
     *
     * @throws IOException the io exception
     */
    void initRep() throws IOException {
        Settings oldSettings = MavenSettings.getSettings();
        String excludeMirrorOfs = droolsConfiguration.getRepositories().keySet().stream()
                .map(repId->"!"+repId).collect(Collectors.joining(","));
        for(Mirror mirror:oldSettings.getMirrors()){
            String[] mirrorOfArr = mirror.getMirrorOf().split(",");
            for(String item:mirrorOfArr){
                if(item.trim().equals("*")){
                    mirror.setMirrorOf(mirror.getMirrorOf()+","+excludeMirrorOfs);
                }
            }
        }
        String serverXml = defaultServerXml;
        for(String repId:droolsConfiguration.getRepositories().keySet()){
            RepItem repItem =droolsConfiguration.getRepositories().get(repId);
            Profile profile = new Profile();
            profile.setId(repId);
            List<Repository> repositories = new ArrayList<>();
            Repository repository = new Repository();
            repository.setId(repId);
            repository.setUrl(repItem.getUrl());
            RepositoryPolicy repositoryPolicy = new RepositoryPolicy();
            repositoryPolicy.setUpdatePolicy("always");
            repositoryPolicy.setEnabled(true);
            repository.setReleases(repositoryPolicy);
            repository.setSnapshots(repositoryPolicy);
            repositories.add(repository);
            profile.setRepositories(repositories);
            oldSettings.addProfile(profile);
            oldSettings.addActiveProfile(repId);
            boolean hasServer = true;
            if(!StringUtils.isEmpty(repItem.getServerXml())){
                serverXml = String.format("<settings><servers>%s<settings>",repItem.getServerXml());
            }else if(!StringUtils.isEmpty(repItem.getUsername()) && !StringUtils.isEmpty(repItem.getPassword())){
                serverXml = String.format(serverXml,repItem.getUsername(),repItem.getPassword());
            }else{
                hasServer = false;
            }
            if(hasServer){
                DefaultSettingsReader settingsReader = new DefaultSettingsReader();
                Map<String, ?> options = Collections.singletonMap( SettingsReader.IS_STRICT, Boolean.TRUE );
                Settings settings = settingsReader.read(new ByteArrayInputStream(serverXml.getBytes("UTF-8")),options);
                Server server = settings.getServers().get(0);
                server.setId(repId);
                oldSettings.addServer(server);
            }
        }
        reloadMavenSetting(oldSettings);

    }



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        try{
            for(Field field:fields){
                field.setAccessible(true);
                KieInjectStrategy strategy = null;
                if(field.isAnnotationPresent(KContainer.class)){
                    strategy = strategyMap.get(KContainer.class);
                    field.set(bean,strategy.generator(bean,field));
                }
                if(field.isAnnotationPresent(KBase.class)){
                    strategy = strategyMap.get(KBase.class);
                    field.set(bean,strategy.generator(bean,field));
                }
                if(field.isAnnotationPresent(KSession.class)){
                    strategy = strategyMap.get(KSession.class);
                    field.set(bean,strategy.generator(bean,field));
                }
                if(field.isAnnotationPresent(KServices.class)){
                    strategy = strategyMap.get(KServices.class);
                    field.set(bean,strategy.generator(bean,field));
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /**
     * Reload maven setting.
     *
     * @param settings the settings
     */
    void reloadMavenSetting(Settings settings){
        for(Class clazz: MavenSettings.class.getDeclaredClasses()){
            if("org.appformer.maven.integration.embedder.MavenSettings$SettingsHolder".equals(clazz.getName())){
                Field field = null;
                try {
                    field = clazz.getDeclaredField("mavenConf");
                    field.setAccessible(true);
                    field.set(clazz, new MavenRepositoryConfiguration( settings));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
