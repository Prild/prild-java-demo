package com.prild.microserviceconsumermovie;

public @interface ExcludeFromComponentScan {
    /*
    TestConfiguration必须加@Configuration，它不在主应用程序上下文的@ComponentScan中，否则将由所有@RibbonClients共享。
    如果您使用@ComponentScan（或@SpringBootApplication），则需要采取措施避免包含，例如将其放在一个单独的，不重叠的包中，或者指定要在@ComponentScan，即此注解类。
     */
}
