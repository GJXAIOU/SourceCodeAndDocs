---
Time: 2020-12-12
---

# Spring Boot Documentation

This section provides a brief overview of Spring Boot reference documentation. It serves as a map for the rest of the document.

该部分提供了 Spring Boot 参考文档的简介，它作为接下来的文档的一个指引。

## 1. About the Documentation（关于本文档）

The Spring Boot reference guide is available as:

- [Multi-page HTML](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/)
- [Single page HTML](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/htmlsingle/)
- [PDF](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/pdf/spring-boot-reference.pdf)

The latest copy is available at [docs.spring.io/spring-boot/docs/current/reference/](https://docs.spring.io/spring-boot/docs/current/reference/).

Copies of this document may be made for your own use and for distribution to others, provided that you do not charge any fee for such copies and further provided that each copy contains this Copyright Notice, whether distributed in print or electronically.

## 2. Getting Help（获取帮助）

If you have trouble with Spring Boot, we would like to help.

- Try the [How-to documents](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/howto.html#howto). They provide solutions to the most common questions.
- 最常见的问题的解决方案见 [How-to documents](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/howto.html#howto)。
- Learn the Spring basics. Spring Boot builds on many other Spring projects. Check the [spring.io](https://spring.io/) web-site for a wealth（众多） of reference documentation. If you are starting out with Spring, try one of the [guides](https://spring.io/guides).
- 学完  Spring 基础之后，Spring Boot 构建在很多其它 Spring 项目上，可以查阅 [spring.io](https://spring.io/) 网站获取更多的参考文档，如果从 Spring 启动，可以参考 [guides](https://spring.io/guides).
- Ask a question. We monitor [stackoverflow.com](https://stackoverflow.com/) for questions tagged with [`spring-boot`](https://stackoverflow.com/tags/spring-boot).
- 如果有疑问可以在 [stackoverflow.com](https://stackoverflow.com/) 上提出，并打上 `spring-boot` 的标记。
- Report bugs with Spring Boot at [github.com/spring-projects/spring-boot/issues](https://github.com/spring-projects/spring-boot/issues).
- 可以通过 [github.com/spring-projects/spring-boot/issues](https://github.com/spring-projects/spring-boot/issues) 报告有关 Spring Boot 的 Bug。

>All of Spring Boot is open source, including the documentation. If you find problems with the docs or if you want to improve them, please [get involved](https://github.com/spring-projects/spring-boot/tree/master).

## 3. Upgrading from an Earlier Version（从早期版本升级）

Instructions for how to upgrade from earlier versions of Spring Boot are provided on the project [wiki](https://github.com/spring-projects/spring-boot/wiki). Follow the links in the [release notes](https://github.com/spring-projects/spring-boot/wiki#release-notes) section to find the version that you want to upgrade to.

在项目的 [wiki](https://github.com/spring-projects/spring-boot/wiki) 中提供了如何从 Spring Boot 早期版本升级的说明。可以在 [release notes](https://github.com/spring-projects/spring-boot/wiki#release-notes) 部分选择想要升级的版本。

Upgrading instructions are always the first item in the release notes. If you are more than one release behind, please make sure that you also review the release notes of the versions that you jumped.

升级说明总是在发布文档的第一部分，如果你相比发布的版本落后不止一个版本，请同时阅读说要跳过的所有版本的说明文档。

You should always ensure that you are running a [supported version](https://github.com/spring-projects/spring-boot/wiki/Supported-Versions) of Spring Boot.

同时你要确认你运行的是 [supported version](https://github.com/spring-projects/spring-boot/wiki/Supported-Versions) 列表中所支持的版本。

> 补充：该部分为 supported version 部分的列表（截止 2020.12.13），最新的可以访问上述网址
>
> ### Released Versions
>
> The following releases are actively maintained:
>
> | Version | Released      | OSS Support Until | Expected End of Life |
> | ------- | ------------- | ----------------- | -------------------- |
> | 2.4.x   | November 2020 | November 2021     | August 2022          |
> | 2.3.x   | May 2020      | May 2021          | February 2022        |
> | 2.2.x   | October 2019  | October 2020      | July 2021            |
>
> The following releases are end of life:
>
> | Version | Released     | End of Life   | Notes                   |
> | ------- | ------------ | ------------- | ----------------------- |
> | 2.1.x   | October 2018 | November 2020 |                         |
> | 2.3.x   | March 2018   | April 2019    |                         |
> | 1.5.x   | January 2017 | August 2019   | Last in the `1.x` line. |

## 4. First Steps

If you are getting started with Spring Boot or 'Spring' in general, start with [the following topics](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started):

- **From scratch:** [Overview](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started-introducing-spring-boot) | [Requirements](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started-system-requirements) | [Installation](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started-installing-spring-boot)
- **Tutorial:** [Part 1](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started-first-application) | [Part 2](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started-first-application-code)
- **Running your example:** [Part 1](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started-first-application-run) | [Part 2](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/getting-started.html#getting-started-first-application-executable-jar)

## 5. Working with Spring Boot

Ready to actually start using Spring Boot? [We have you covered](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot):

- **Build systems:** [Maven](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-maven) | [Gradle](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-gradle) | [Ant](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-ant) | [Starters](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-starter)
- **Best practices:** [Code Structure](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-structuring-your-code) | [@Configuration](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-configuration-classes) | [@EnableAutoConfiguration](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-auto-configuration) | [Beans and Dependency Injection](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-spring-beans-and-dependency-injection)
- **Running your code:** [IDE](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-running-from-an-ide) | [Packaged](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-running-as-a-packaged-application) | [Maven](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-running-with-the-maven-plugin) | [Gradle](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-running-with-the-gradle-plugin)
- **Packaging your app:** [Production jars](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/using-spring-boot.html#using-boot-packaging-for-production)
- **Spring Boot CLI:** [Using the CLI](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-cli.html#cli)

## 6. Learning about Spring Boot Features（Spring Boot 特征）

Need more details about Spring Boot’s core features? [The following content is for you](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features):

- **Core Features:** [SpringApplication](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-spring-application) | [External Configuration](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-external-config) | [Profiles](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-profiles) | [Logging](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-logging)
- **Web Applications:** [MVC](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-spring-mvc) | [Embedded Containers](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-embedded-container)
- **Working with data:** [SQL](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-sql) | [NO-SQL](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-nosql)
- **Messaging:** [Overview](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-messaging) | [JMS](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-jms)
- **Testing:** [Overview](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-testing) | [Boot Applications](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications) | [Utils](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-test-utilities)
- **Extending:** [Auto-configuration](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-developing-auto-configuration) | [@Conditions](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/spring-boot-features.html#boot-features-condition-annotations)

## 7. Moving to Production

When you are ready to push your Spring Boot application to production, we have [some tricks](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready) that you might like:

- **Management endpoints:** [Overview](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready-endpoints)
- **Connection options:** [HTTP](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready-monitoring) | [JMX](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready-jmx)
- **Monitoring:** [Metrics](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready-metrics) | [Auditing](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready-auditing) | [HTTP Tracing](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready-http-tracing) | [Process](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/production-ready-features.html#production-ready-process-monitoring)

## 8. Advanced Topics

Finally, we have a few topics for more advanced users:

- **Spring Boot Applications Deployment:** [Cloud Deployment](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/deployment.html#cloud-deployment) | [OS Service](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/deployment.html#deployment-service)
- **Build tool plugins:** [Maven](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/build-tool-plugins.html#build-tool-plugins-maven-plugin) | [Gradle](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/build-tool-plugins.html#build-tool-plugins-gradle-plugin)
- **Appendix:** [Application Properties](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-application-properties.html#common-application-properties) | [Configuration Metadata](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-configuration-metadata.html#configuration-metadata) | [Auto-configuration Classes](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-auto-configuration-classes.html#auto-configuration-classes) | [Test Auto-configuration Annotations](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-test-auto-configuration.html#test-auto-configuration) | [Executable Jars](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-executable-jar-format.html#executable-jar) | [Dependency Versions](https://docs.spring.io/spring-boot/docs/2.4.2-SNAPSHOT/reference/html/appendix-dependency-versions.html#dependency-versions)