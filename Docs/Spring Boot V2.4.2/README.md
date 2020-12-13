---
Time: 2020-12-12
---

# Spring Boot Reference Documentation

该部分为 Spring Boot V2.4.2 参考文档的中英混合版本，翻译与阅读说明如下：

- 英文文档官网为：[Spring Boot V2.4.2 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- 在线 Gitbook 阅读：[Spring Boot V2.4.2 中英文档在线阅读](https://gjxaiou.gitbook.io/springboot/)
- 文档中代码样式：[文档对应示例源代码](https://github.com/GJXAIOU/SourceCodeAndDocs/tree/master/SourceCode/SpringBootDocs)
- 中文排版规范：[中文文档排版指北](https://github.com/sparanoid/chinese-copywriting-guidelines)

翻译过程中不删除原版英文，推荐直接阅读英文原文（部分生僻词语加上了中文译文）。翻译结构为按段翻译，同时翻译是建立在自己阅读并「顺便」翻译的前提下，因此整体翻译可能偏向于口语化，便于理解。

全文目前仅个人独自翻译，因水平有限无法达到逐字逐句精准翻译，如果有同学想加入或者校对请联系：gjxaiou@gmail.com



The reference documentation consists of the following sections:

参考文档包括以下部分：

| 文档 标题 | 简介|
| --------------- | ------------------------ |
| [Legal](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/legal.html#legal) | Legal information. |
| [Documentation Overview](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/documentation-overview.html#boot-documentation) | About the Documentation, Getting Help, First Steps, and more. |
| [Getting Started](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started) | Introducing Spring Boot, System Requirements, Servlet Containers, Installing Spring Boot, Developing Your First Spring Boot Application |
| [Using Spring Boot](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot) | Build Systems, Structuring Your Code, Configuration, Spring Beans and Dependency Injection, DevTools, and more. |
| [Spring Boot Features](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features) | Profiles, Logging, Security, Caching, Spring Integration, Testing, and more. |
| [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready) | Monitoring, Metrics, Auditing, and more.                     |
| [Deploying Spring Boot Applications](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/deployment.html#deployment) | Deploying to the Cloud, Installing as a Unix application.    |
| [Spring Boot CLI](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-cli.html#cli) | Installing the CLI, Using the CLI, Configuring the CLI, and more. |
| [Build Tool Plugins](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/build-tool-plugins.html#build-tool-plugins) | Maven Plugin, Gradle Plugin, Antlib, and more.               |
| [“How-to” Guides](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/howto.html#howto) | Application Development, Configuration, Embedded Servers, Data Access, and many more. |

The reference documentation has the following appendices（附录）:

参考文献包括以下附录文件：

| 文档 标题 | 简介|
| ------------- | --------------- |
| [Application Properties](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-application-properties.html#common-application-properties) | Common application properties that can be used to configure your application. |
| [Configuration Metadata](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-configuration-metadata.html#configuration-metadata) | Metadata used to describe configuration properties.          |
| [Auto-configuration Classes](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-auto-configuration-classes.html#auto-configuration-classes) | Auto-configuration classes provided by Spring Boot.          |
| [Test Auto-configuration Annotations](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-test-auto-configuration.html#test-auto-configuration) | Test-autoconfiguration annotations used to test slices of your application. |
| [Executable Jars](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-executable-jar-format.html#executable-jar) | Spring Boot’s executable jars, their launchers, and their format. |
| [Dependency Versions](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-dependency-versions.html#dependency-versions) | Details of the dependencies that are managed by Spring Boot. |