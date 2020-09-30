# spring-boot-starter-drools
## 支持注解指定不同kjar源注入Kie相关bean
## 通过注解与属性配置轻松配置并行版本控制
- 从classPath查找kmodule
~~~java
     @KServices
     private KieServices kieServices;
 
     @KContainer
     private KieContainer kieContainer;
 
     @KBase
     private KieBase kieBase;
 
     @KSession
     private KieSession kieSession;
     
     // 通过类型判断注入的有状态还是无状态session
     @KSession
     private StatelessKieSession statelessKieSession;
~~~
- 从maven仓库指定kjar包注入
~~~java
    @KContainer
    @KReleaseId(groupId = "com.zhu",artifactId = "drools-kjar-maven",version = "LATEST",enableScanner = true)
    private KieContainer kieContainer;

    @KBase("kbase")
    @KReleaseId(groupId = "com.zhu",artifactId = "drools-kjar-maven",version = "LATEST")
    private KieBase kieBase;

    @KSession("hello")
    @KReleaseId(groupId = "com.zhu",artifactId = "drools-kjar-maven",version = "LATEST")
    private KieSession kieSession;
~~~
- 从本地文件系统指定kjar包注入
~~~java
    @KContainer
    @KJarPath(value="/path/xxx.jar",enableScanner = true)
    private KieContainer kieContainer;

    @KBase("kbase")
    @KJarPath("/path/xxx.jar")
    private KieBase kieBase;

    @KSession("hello")
    @KJarPath("/path/xxx.jar")
    private KieSession kieSession;
~~~
***note:***
- ***所有注入的kiebean针对同一个源都是单例以节省开销加快速度***
- ***除了从classPath获取之外，maven和本地文件系统都支持并行版本控制动态更新规则***


