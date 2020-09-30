package com.zhu.drools;


import com.zhu.drools.annotation.KBase;
import com.zhu.drools.annotation.KContainer;
import com.zhu.drools.annotation.KServices;
import com.zhu.drools.annotation.KSession;
import com.zhu.facts.HelloFact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author heykb
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClasspathDroolsTest {

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


    @Test
    public void test(){
//        KieContainer kieContainer = KieServices.Factory.get().newKieClasspathContainer();

        HelloFact helloFact = new HelloFact();
        // new default Kie session
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(helloFact);
        kieSession.fireAllRules();
        kieSession.dispose();
        assert("hello!! i am from classpath kie".equals(helloFact.getMessage()));
        System.out.println(helloFact.getMessage());
    }

}
