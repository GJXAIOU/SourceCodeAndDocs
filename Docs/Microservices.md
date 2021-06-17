---
原文地址： https://martinfowler.com/articles/microservices.html#MicroservicesAndSoa
原文时间： 25 March 2014
译文地址： http://blog.cuicc.com/blog/2015/07/22/microservices/
---



"Microservices" - yet another new term on the crowded streets of software architecture. Although our natural inclination is to pass such things by with a contemptuous glance, this bit of terminology describes a style of software systems that we are finding more and more appealing. We've seen many projects use this style in the last few years, and results so far have been positive, so much so that for many of our colleagues this is becoming the default style for building enterprise applications. Sadly, however, there's not much information that outlines what the microservice style is and how to do it.

In short, the microservice architectural style [[1\]](https://martinfowler.com/articles/microservices.html#footnote-etymology) is an approach to developing a single application as a suite of small services, each running in its own process and communicating with lightweight mechanisms, often an HTTP resource API. These services are built around business capabilities and independently deployable by fully automated deployment machinery. There is a bare minimum of centralized management of these services, which may be written in different programming languages and use different data storage technologies.

[![img](https://martinfowler.com/microservices/microservices-sq.png)](https://martinfowler.com/microservices)

My [Microservices Resource Guide](https://martinfowler.com/microservices) provides links to the best articles, videos, books, and podcasts about microservices.

To start explaining the microservice style it's useful to compare it to the monolithic style: a monolithic application built as a single unit. Enterprise Applications are often built in three main parts: a client-side user interface (consisting of HTML pages and javascript running in a browser on the user's machine) a database (consisting of many tables inserted into a common, and usually relational, database management system), and a server-side application. The server-side application will handle HTTP requests, execute domain logic, retrieve and update data from the database, and select and populate HTML views to be sent to the browser. This server-side application is a *monolith* - a single logical executable[[2\]](https://martinfowler.com/articles/microservices.html#footnote-monolith). Any changes to the system involve building and deploying a new version of the server-side application.

Such a monolithic server is a natural way to approach building such a system. All your logic for handling a request runs in a single process, allowing you to use the basic features of your language to divide up the application into classes, functions, and namespaces. With some care, you can run and test the application on a developer's laptop, and use a deployment pipeline to ensure that changes are properly tested and deployed into production. You can horizontally scale the monolith by running many instances behind a load-balancer.

Monolithic applications can be successful, but increasingly people are feeling frustrations with them - especially as more applications are being deployed to the cloud . Change cycles are tied together - a change made to a small part of the application, requires the entire monolith to be rebuilt and deployed. Over time it's often hard to keep a good modular structure, making it harder to keep changes that ought to only affect one module within that module. Scaling requires scaling of the entire application rather than parts of it that require greater resource.

![img](https://martinfowler.com/articles/microservices/images/sketch.png)

Figure 1: Monoliths and Microservices

These frustrations have led to the microservice architectural style: building applications as suites of services. As well as the fact that services are independently deployable and scalable, each service also provides a firm module boundary, even allowing for different services to be written in different programming languages. They can also be managed by different teams .

We do not claim that the microservice style is novel or innovative, its roots go back at least to the design principles of Unix. But we do think that not enough people consider a microservice architecture and that many software developments would be better off if they used it.

## Characteristics of a Microservice Architecture

We cannot say there is a formal definition of the microservices architectural style, but we can attempt to describe what we see as common characteristics for architectures that fit the label. As with any definition that outlines common characteristics, not all microservice architectures have all the characteristics, but we do expect that most microservice architectures exhibit most characteristics. While we authors have been active members of this rather loose community, our intention is to attempt a description of what we see in our own work and in similar efforts by teams we know of. In particular we are not laying down some definition to conform to.

### Componentization via Services

For as long as we've been involved in the software industry, there's been a desire to build systems by plugging together components, much in the way we see things are made in the physical world. During the last couple of decades we've seen considerable progress with large compendiums of common libraries that are part of most language platforms.

When talking about components we run into the difficult definition of what makes a component. [Our definition](https://martinfowler.com/bliki/SoftwareComponent.html) is that a **component** is a unit of software that is independently replaceable and upgradeable.

Microservice architectures will use libraries, but their primary way of componentizing their own software is by breaking down into services. We define **libraries** as components that are linked into a program and called using in-memory function calls, while **services** are out-of-process components who communicate with a mechanism such as a web service request, or remote procedure call. (This is a different concept to that of a service object in many OO programs [[3\]](https://martinfowler.com/articles/microservices.html#footnote-service-object).)

One main reason for using services as components (rather than libraries) is that services are independently deployable. If you have an application [[4\]](https://martinfowler.com/articles/microservices.html#footnote-application) that consists of a multiple libraries in a single process, a change to any single component results in having to redeploy the entire application. But if that application is decomposed into multiple services, you can expect many single service changes to only require that service to be redeployed. That's not an absolute, some changes will change service interfaces resulting in some coordination, but the aim of a good microservice architecture is to minimize these through cohesive service boundaries and evolution mechanisms in the service contracts.

Another consequence of using services as components is a more explicit component interface. Most languages do not have a good mechanism for defining an explicit [Published Interface](https://martinfowler.com/bliki/PublishedInterface.html). Often it's only documentation and discipline that prevents clients breaking a component's encapsulation, leading to overly-tight coupling between components. Services make it easier to avoid this by using explicit remote call mechanisms.

Using services like this does have downsides. Remote calls are more expensive than in-process calls, and thus remote APIs need to be coarser-grained, which is often more awkward to use. If you need to change the allocation of responsibilities between components, such movements of behavior are harder to do when you're crossing process boundaries.

At a first approximation, we can observe that services map to runtime processes, but that is only a first approximation. A service may consist of multiple processes that will always be developed and deployed together, such as an application process and a database that's only used by that service.

### Organized around Business Capabilities

When looking to split a large application into parts, often management focuses on the technology layer, leading to UI teams, server-side logic teams, and database teams. When teams are separated along these lines, even simple changes can lead to a cross-team project taking time and budgetary approval. A smart team will optimise around this and plump for the lesser of two evils - just force the logic into whichever application they have access to. Logic everywhere in other words. This is an example of Conway's Law[[5\]](https://martinfowler.com/articles/microservices.html#footnote-conwayslaw) in action.

> Any organization that designs a system (defined broadly) will produce a design whose structure is a copy of the organization's communication structure.
>
> -- Melvin Conway, 1968

![img](https://martinfowler.com/articles/microservices/images/conways-law.png)

Figure 2: Conway's Law in action

The microservice approach to division is different, splitting up into services organized around **business capability**. Such services take a broad-stack implementation of software for that business area, including user-interface, persistant storage, and any external collaborations. Consequently the teams are cross-functional, including the full range of skills required for the development: user-experience, database, and project management.

![img](https://martinfowler.com/articles/microservices/images/PreferFunctionalStaffOrganization.png)

Figure 3: Service boundaries reinforced by team boundaries

### How big is a microservice?

Although “microservice” has become a popular name for this architectural style, its name does lead to an unfortunate focus on the size of service, and arguments about what constitutes “micro”. In our conversations with microservice practitioners, we see a range of sizes of services. The largest sizes reported follow Amazon's notion of the Two Pizza Team (i.e. the whole team can be fed by two pizzas), meaning no more than a dozen people. On the smaller size scale we've seen setups where a team of half-a-dozen would support half-a-dozen services.

This leads to the question of whether there are sufficiently large differences within this size range that the service-per-dozen-people and service-per-person sizes shouldn't be lumped under one microservices label. At the moment we think it's better to group them together, but it's certainly possible that we'll change our mind as we explore this style further.

One company organised in this way is [www.comparethemarket.com](http://www.comparethemarket.com/). Cross functional teams are responsible for building and operating each product and each product is split out into a number of individual services communicating via a message bus.

Large monolithic applications can always be modularized around business capabilities too, although that's not the common case. Certainly we would urge a large team building a monolithic application to divide itself along business lines. The main issue we have seen here, is that they tend to be organised around *too many* contexts. If the monolith spans many of these modular boundaries it can be difficult for individual members of a team to fit them into their short-term memory. Additionally we see that the modular lines require a great deal of discipline to enforce. The necessarily more explicit separation required by service components makes it easier to keep the team boundaries clear.

### Products not Projects

Most application development efforts that we see use a project model: where the aim is to deliver some piece of software which is then considered to be completed. On completion the software is handed over to a maintenance organization and the project team that built it is disbanded.

Microservice proponents tend to avoid this model, preferring instead the notion that a team should own a product over its full lifetime. A common inspiration for this is Amazon's notion of ["you build, you run it"](https://queue.acm.org/detail.cfm?id=1142065) where a development team takes full responsibility for the software in production. This brings developers into day-to-day contact with how their software behaves in production and increases contact with their users, as they have to take on at least some of the support burden.

The product mentality, ties in with the linkage to business capabilities. Rather than looking at the software as a set of functionality to be completed, there is an on-going relationship where the question is how can software assist its users to enhance the business capability.

There's no reason why this same approach can't be taken with monolithic applications, but the smaller granularity of services can make it easier to create the personal relationships between service developers and their users.

### Smart endpoints and dumb pipes

When building communication structures between different processes, we've seen many products and approaches that stress putting significant smarts into the communication mechanism itself. A good example of this is the Enterprise Service Bus (ESB), where ESB products often include sophisticated facilities for message routing, choreography, transformation, and applying business rules.

### Microservices and SOA

When we've talked about microservices a common question is whether this is just Service Oriented Architecture (SOA) that we saw a decade ago. There is merit to this point, because the microservice style is very similar to what some advocates of SOA have been in favor of. The problem, however, is that SOA means [too many different things](https://martinfowler.com/bliki/ServiceOrientedAmbiguity.html), and that most of the time that we come across something called "SOA" it's significantly different to the style we're describing here, usually due to a focus on ESBs used to integrate monolithic applications.

In particular we have seen so many botched implementations of service orientation - from the tendency to hide complexity away in ESB's [[6\]](https://martinfowler.com/articles/microservices.html#footnote-esb), to failed multi-year initiatives that cost millions and deliver no value, to centralised governance models that actively inhibit change, that it is sometimes difficult to see past these problems.

Certainly, many of the techniques in use in the microservice community have grown from the experiences of developers integrating services in large organisations. The [Tolerant Reader](https://martinfowler.com/bliki/TolerantReader.html) pattern is an example of this. Efforts to use the web have contributed, using simple protocols is another approach derived from these experiences - a reaction away from central standards that have reached a complexity that is, [frankly, breathtaking](http://wiki.apache.org/ws/WebServiceSpecifications). (Any time you need an ontology to manage your ontologies you know you are in deep trouble.)

This common manifestation of SOA has led some microservice advocates to reject the SOA label entirely, although others consider microservices to be one form of SOA [[7\]](https://martinfowler.com/articles/microservices.html#footnote-fine-grained), perhaps *service orientation done right*. Either way, the fact that SOA means such different things means it's valuable to have a term that more crisply defines this architectural style.

The microservice community favours an alternative approach: *smart endpoints and dumb pipes*. Applications built from microservices aim to be as decoupled and as cohesive as possible - they own their own domain logic and act more as filters in the classical Unix sense - receiving a request, applying logic as appropriate and producing a response. These are choreographed using simple RESTish protocols rather than complex protocols such as WS-Choreography or BPEL or orchestration by a central tool.

The two protocols used most commonly are HTTP request-response with resource API's and lightweight messaging[[8\]](https://martinfowler.com/articles/microservices.html#footnote-protobufs). The best expression of the first is

> Be of the web, not behind the web
>
> -- [Ian Robinson](https://www.amazon.com/gp/product/0596805829?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0596805829)

Microservice teams use the principles and protocols that the world wide web (and to a large extent, Unix) is built on. Often used resources can be cached with very little effort on the part of developers or operations folk.

The second approach in common use is messaging over a lightweight message bus. The infrastructure chosen is typically dumb (dumb as in acts as a message router only) - simple implementations such as RabbitMQ or ZeroMQ don't do much more than provide a reliable asynchronous fabric - the smarts still live in the end points that are producing and consuming messages; in the services.

In a monolith, the components are executing in-process and communication between them is via either method invocation or function call. The biggest issue in changing a monolith into microservices lies in changing the communication pattern. A naive conversion from in-memory method calls to RPC leads to chatty communications which don't perform well. Instead you need to replace the fine-grained communication with a coarser -grained approach.

### Decentralized Governance

One of the consequences of centralised governance is the tendency to standardise on single technology platforms. Experience shows that this approach is constricting - not every problem is a nail and not every solution a hammer. We prefer using the right tool for the job and while monolithic applications can take advantage of different languages to a certain extent, it isn't that common.

Splitting the monolith's components out into services we have a choice when building each of them. You want to use Node.js to standup a simple reports page? Go for it. C++ for a particularly gnarly near-real-time component? Fine. You want to swap in a different flavour of database that better suits the read behaviour of one component? We have the technology to rebuild him.

Of course, just because you *can* do something, doesn't mean you *should* - but partitioning your system in this way means you have the option.

Teams building microservices prefer a different approach to standards too. Rather than use a set of defined standards written down somewhere on paper they prefer the idea of producing useful tools that other developers can use to solve similar problems to the ones they are facing. These tools are usually harvested from implementations and shared with a wider group, sometimes, but not exclusively using an internal open source model. Now that git and github have become the de facto version control system of choice, open source practices are becoming more and more common in-house .

Netflix is a good example of an organisation that follows this philosophy. Sharing useful and, above all, battle-tested code as libraries encourages other developers to solve similar problems in similar ways yet leaves the door open to picking a different approach if required. Shared libraries tend to be focused on common problems of data storage, inter-process communication and as we discuss further below, infrastructure automation.

For the microservice community, overheads are particularly unattractive. That isn't to say that the community doesn't value service contracts. Quite the opposite, since there tend to be many more of them. It's just that they are looking at different ways of managing those contracts. Patterns like [Tolerant Reader](https://martinfowler.com/bliki/TolerantReader.html) and [Consumer-Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html) are often applied to microservices. These aid service contracts in evolving independently. Executing consumer driven contracts as part of your build increases confidence and provides fast feedback on whether your services are functioning. Indeed we know of a team in Australia who drive the build of new services with consumer driven contracts. They use simple tools that allow them to define the contract for a service. This becomes part of the automated build before code for the new service is even written. The service is then built out only to the point where it satisfies the contract - an elegant approach to avoid the 'YAGNI'[[9\]](https://martinfowler.com/articles/microservices.html#footnote-YAGNI) dilemma when building new software. These techniques and the tooling growing up around them, limit the need for central contract management by decreasing the temporal coupling between services.

### Many languages, many options

The growth of JVM as a platform is just the latest example of mixing languages within a common platform. It's been common practice to shell-out to a higher level language to take advantage of higher level abstractions for decades. As is dropping down to the metal and writing performance sensitive code in a lower level one. However, many monoliths don't need this level of performance optimisation nor are DSL's and higher level abstractions that common (to our dismay). Instead monoliths are usually single language and the tendency is to limit the number of technologies in use [[10\]](https://martinfowler.com/articles/microservices.html#footnote-many-languages).

Perhaps the apogee of decentralised governance is the build it / run it ethos popularised by Amazon. Teams are responsible for all aspects of the software they build including operating the software 24/7. Devolution of this level of responsibility is definitely not the norm but we do see more and more companies pushing responsibility to the development teams. Netflix is another organisation that has adopted this ethos[[11\]](https://martinfowler.com/articles/microservices.html#footnote-netflix-flowcon). Being woken up at 3am every night by your pager is certainly a powerful incentive to focus on quality when writing your code. These ideas are about as far away from the traditional centralized governance model as it is possible to be.

### Decentralized Data Management

Decentralization of data management presents in a number of different ways. At the most abstract level, it means that the conceptual model of the world will differ between systems. This is a common issue when integrating across a large enterprise, the sales view of a customer will differ from the support view. Some things that are called customers in the sales view may not appear at all in the support view. Those that do may have different attributes and (worse) common attributes with subtly different semantics.

### Battle-tested standards and enforced standards

It's a bit of a dichotomy that microservice teams tend to eschew the kind of rigid enforced standards laid down by enterprise architecture groups but will happily use and even evangelise the use of open standards such as HTTP, ATOM and other microformats.

The key difference is how the standards are developed and how they are enforced. Standards managed by groups such as the IETF only *become* standards when there are several live implementations of them in the wider world and which often grow from successful open-source projects.

These standards are a world apart from many in a corporate world, which are often developed by groups that have little recent programming experience or overly influenced by vendors.

This issue is common between applications, but can also occur *within* applications, particular when that application is divided into separate components. A useful way of thinking about this is the Domain-Driven Design notion of [Bounded Context](https://martinfowler.com/bliki/BoundedContext.html). DDD divides a complex domain up into multiple bounded contexts and maps out the relationships between them. This process is useful for both monolithic and microservice architectures, but there is a natural correlation between service and context boundaries that helps clarify, and as we describe in the section on business capabilities, reinforce the separations.

As well as decentralizing decisions about conceptual models, microservices also decentralize data storage decisions. While monolithic applications prefer a single logical database for persistant data, enterprises often prefer a single database across a range of applications - many of these decisions driven through vendor's commercial models around licensing. Microservices prefer letting each service manage its own database, either different instances of the same database technology, or entirely different database systems - an approach called [Polyglot Persistence](https://martinfowler.com/bliki/PolyglotPersistence.html). You can use polyglot persistence in a monolith, but it appears more frequently with microservices.

![img](https://martinfowler.com/articles/microservices/images/decentralised-data.png)



Decentralizing responsibility for data across microservices has implications for managing updates. The common approach to dealing with updates has been to use transactions to guarantee consistency when updating multiple resources. This approach is often used within monoliths.

Using transactions like this helps with consistency, but imposes significant temporal coupling, which is problematic across multiple services. Distributed transactions are notoriously difficult to implement and as a consequence microservice architectures [emphasize transactionless coordination between services](http://www.eaipatterns.com/ramblings/18_starbucks.html), with explicit recognition that consistency may only be eventual consistency and problems are dealt with by compensating operations.

Choosing to manage inconsistencies in this way is a new challenge for many development teams, but it is one that often matches business practice. Often businesses handle a degree of inconsistency in order to respond quickly to demand, while having some kind of reversal process to deal with mistakes. The trade-off is worth it as long as the cost of fixing mistakes is less than the cost of lost business under greater consistency.

### Infrastructure Automation

Infrastructure automation techniques have evolved enormously over the last few years - the evolution of the cloud and AWS in particular has reduced the operational complexity of building, deploying and operating microservices.

Many of the products or systems being build with microservices are being built by teams with extensive experience of [Continuous Delivery](https://martinfowler.com/bliki/ContinuousDelivery.html) and it's precursor, [Continuous Integration](https://martinfowler.com/articles/continuousIntegration.html). Teams building software this way make extensive use of infrastructure automation techniques. This is illustrated in the build pipeline shown below.

![img](https://martinfowler.com/articles/microservices/images/basic-pipeline.png)

Figure 5: basic build pipeline

Since this isn't an article on Continuous Delivery we will call attention to just a couple of key features here. We want as much confidence as possible that our software is working, so we run lots of **automated tests**. Promotion of working software 'up' the pipeline means we **automate deployment** to each new environment.

### Make it easy to do the right thing

One side effect we have found of increased automation as a consequence of continuous delivery and deployment is the creation of useful tools to help developers and operations folk. Tooling for creating artefacts, managing codebases, standing up simple services or for adding standard monitoring and logging are pretty common now. The best example on the web is probably [Netflix's set of open source tools](http://netflix.github.io/), but there are others including [Dropwizard](http://dropwizard.codahale.com/) which we have used extensively.

A monolithic application will be built, tested and pushed through these environments quite happlily. It turns out that once you have invested in automating the path to production for a monolith, then deploying *more* applications doesn't seem so scary any more. Remember, one of the aims of CD is to make deployment boring, so whether its one or three applications, as long as its still boring it doesn't matter[[12\]](https://martinfowler.com/articles/microservices.html#footnote-trickycd).

Another area where we see teams using extensive infrastructure automation is when managing microservices in production. In contrast to our assertion above that as long as deployment is boring there isn't that much difference between monoliths and microservices, the operational landscape for each can be strikingly different.

![img](https://martinfowler.com/articles/microservices/images/micro-deployment.png)

Figure 6: Module deployment often differs

### Design for failure

A consequence of using services as components, is that applications need to be designed so that they can tolerate the failure of services. Any service call could fail due to unavailability of the supplier, the client has to respond to this as gracefully as possible. This is a disadvantage compared to a monolithic design as it introduces additional complexity to handle it. The consequence is that microservice teams constantly reflect on how service failures affect the user experience. Netflix's [Simian Army](https://github.com/Netflix/SimianArmy) induces failures of services and even datacenters during the working day to test both the application's resilience and monitoring.

### The circuit breaker and production ready code

[Circuit Breaker](https://martinfowler.com/bliki/CircuitBreaker.html) appears in [Release It!](https://www.amazon.com/gp/product/B00A32NXZO?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=B00A32NXZO) alongside other patterns such as Bulkhead and Timeout. Implemented together, these patterns are crucially important when building communicating applications. This [Netflix blog entry](http://techblog.netflix.com/2012/02/fault-tolerance-in-high-volume.html) does a great job of explaining their application of them.

This kind of automated testing in production would be enough to give most operation groups the kind of shivers usually preceding a week off work. This isn't to say that monolithic architectural styles aren't capable of sophisticated monitoring setups - it's just less common in our experience.

Since services can fail at any time, it's important to be able to detect the failures quickly and, if possible, automatically restore service. Microservice applications put a lot of emphasis on real-time monitoring of the application, checking both architectural elements (how many requests per second is the database getting) and business relevant metrics (such as how many orders per minute are received). Semantic monitoring can provide an early warning system of something going wrong that triggers development teams to follow up and investigate.

This is particularly important to a microservices architecture because the microservice preference towards choreography and [event collaboration](https://martinfowler.com/eaaDev/EventCollaboration.html) leads to emergent behavior. While many pundits praise the value of serendipitous emergence, the truth is that emergent behavior can sometimes be a bad thing. Monitoring is vital to spot bad emergent behavior quickly so it can be fixed.

### Synchronous calls considered harmful

Any time you have a number of synchronous calls between services you will encounter the multiplicative effect of downtime. Simply, this is when the downtime of your system becomes the product of the downtimes of the individual components. You face a choice, making your calls asynchronous or managing the downtime. At www.guardian.co.uk they have implemented a simple rule on the new platform - one synchronous call per user request while at Netflix, their platform API redesign has built asynchronicity into the API fabric.

Monoliths can be built to be as transparent as a microservice - in fact, they should be. The difference is that you absolutely need to know when services running in different processes are disconnected. With libraries within the same process this kind of transparency is less likely to be useful.

Microservice teams would expect to see sophisticated monitoring and logging setups for each individual service such as dashboards showing up/down status and a variety of operational and business relevant metrics. Details on circuit breaker status, current throughput and latency are other examples we often encounter in the wild.

### Evolutionary Design

Microservice practitioners, usually have come from an evolutionary design background and see service decomposition as a further tool to enable application developers to control changes in their application without slowing down change. Change control doesn't necessarily mean change reduction - with the right attitudes and tools you can make frequent, fast, and well-controlled changes to software.

Whenever you try to break a software system into components, you're faced with the decision of how to divide up the pieces - what are the principles on which we decide to slice up our application? The key property of a component is the notion of independent replacement and upgradeability[[13\]](https://martinfowler.com/articles/microservices.html#footnote-RCA) - which implies we look for points where we can imagine rewriting a component without affecting its collaborators. Indeed many microservice groups take this further by explicitly expecting many services to be scrapped rather than evolved in the longer term.

The Guardian website is a good example of an application that was designed and built as a monolith, but has been evolving in a microservice direction. The monolith still is the core of the website, but they prefer to add new features by building microservices that use the monolith's API. This approach is particularly handy for features that are inherently temporary, such as specialized pages to handle a sporting event. Such a part of the website can quickly be put together using rapid development languages, and removed once the event is over. We've seen similar approaches at a financial institution where new services are added for a market opportunity and discarded after a few months or even weeks.

This emphasis on replaceability is a special case of a more general principle of modular design, which is to drive modularity through the pattern of change [[14\]](https://martinfowler.com/articles/microservices.html#footnote-beck-rate-of-change). You want to keep things that change at the same time in the same module. Parts of a system that change rarely should be in different services to those that are currently undergoing lots of churn. If you find yourself repeatedly changing two services together, that's a sign that they should be merged.

Putting components into services adds an opportunity for more granular release planning. With a monolith any changes require a full build and deployment of the entire application. With microservices, however, you only need to redeploy the service(s) you modified. This can simplify and speed up the release process. The downside is that you have to worry about changes to one service breaking its consumers. The traditional integration approach is to try to deal with this problem using versioning, but the preference in the microservice world is to [only use versioning as a last resort](https://martinfowler.com/articles/enterpriseREST.html#versioning). We can avoid a lot of versioning by designing services to be as tolerant as possible to changes in their suppliers.

## Are Microservices the Future?

Our main aim in writing this article is to explain the major ideas and principles of microservices. By taking the time to do this we clearly think that the microservices architectural style is an important idea - one worth serious consideration for enterprise applications. We have recently built several systems using the style and know of others who have used and favor this approach.

### [Microservice Trade-Offs](https://martinfowler.com/articles/microservice-trade-offs.html)

![img](https://martinfowler.com/articles/microservice-trade-offs/card.png)

Many development teams have found the microservices architectural style to be a superior approach to a monolithic architecture. But other teams have found them to be a productivity-sapping burden. Like any architectural style, microservices bring costs and benefits. To make a sensible choice you have to understand these and apply them to your specific context.

by Martin Fowler

1 Jul 2015

[Read more…](https://martinfowler.com/articles/microservice-trade-offs.html)

ARTICLE

[MICROSERVICES](https://martinfowler.com/tags/microservices.html)

Those we know about who are in some way pioneering the architectural style include Amazon, Netflix, [The Guardian](http://www.theguardian.com/), the [UK Government Digital Service](https://gds.blog.gov.uk/), [realestate.com.au](https://martinfowler.com/articles/realestate.com.au), Forward and [comparethemarket.com](http://www.comparethemarket.com/). The conference circuit in 2013 was full of examples of companies that are moving to something that would class as microservices - including Travis CI. In addition there are plenty of organizations that have long been doing what we would class as microservices, but without ever using the name. (Often this is labelled as SOA - although, as we've said, SOA comes in many contradictory forms. [[15\]](https://martinfowler.com/articles/microservices.html#footnote-already))

Despite these positive experiences, however, we aren't arguing that we are certain that microservices are the future direction for software architectures. While our experiences so far are positive compared to monolithic applications, we're conscious of the fact that not enough time has passed for us to make a full judgement.

Often the true consequences of your architectural decisions are only evident several years after you made them. We have seen projects where a good team, with a strong desire for modularity, has built a monolithic architecture that has decayed over the years. Many people believe that such decay is less likely with microservices, since the service boundaries are explicit and hard to patch around. Yet until we see enough systems with enough age, we can't truly assess how microservice architectures mature.

There are certainly reasons why one might expect microservices to mature poorly. In any effort at componentization, success depends on how well the software fits into components. It's hard to figure out exactly where the component boundaries should lie. Evolutionary design recognizes the difficulties of getting boundaries right and thus the importance of it being easy to refactor them. But when your components are services with remote communications, then refactoring is much harder than with in-process libraries. Moving code is difficult across service boundaries, any interface changes need to be coordinated between participants, layers of backwards compatibility need to be added, and testing is made more complicated.

[![img](https://martinfowler.com/articles/microservices/images/sam-book.jpg)](https://www.amazon.com/gp/product/1491950358?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=1491950358)

Our colleague Sam Newman spent most of 2014 working on a book that captures our experiences with building microservices. This should be your next step if you want a deeper dive into the topic.

Another issue is If the components do not compose cleanly, then all you are doing is shifting complexity from inside a component to the connections between components. Not just does this just move complexity around, it moves it to a place that's less explicit and harder to control. It's easy to think things are better when you are looking at the inside of a small, simple component, while missing messy connections between services.

Finally, there is the factor of team skill. New techniques tend to be adopted by more skillful teams. But a technique that is more effective for a more skillful team isn't necessarily going to work for less skillful teams. We've seen plenty of cases of less skillful teams building messy monolithic architectures, but it takes time to see what happens when this kind of mess occurs with microservices. A poor team will always create a poor system - it's very hard to tell if microservices reduce the mess in this case or make it worse.

One reasonable argument we've heard is that you shouldn't start with a microservices architecture. Instead [begin with a monolith](https://martinfowler.com/bliki/MonolithFirst.html), keep it modular, and split it into microservices once the monolith becomes a problem. (Although [this advice isn't ideal](https://martinfowler.com/articles/dont-start-monolith.html), since a good in-process interface is usually not a good service interface.)

So we write this with cautious optimism. So far, we've seen enough about the microservice style to feel that it can be [a worthwhile road to tread](https://martinfowler.com/microservices/). We can't say for sure where we'll end up, but one of the challenges of software development is that you can only make decisions based on the imperfect information that you currently have to hand.

------

## Footnotes

**1:** The term "microservice" was discussed at a workshop of software architects near Venice in May, 2011 to describe what the participants saw as a common architectural style that many of them had been recently exploring. In May 2012, the same group decided on "microservices" as the most appropriate name. James presented some of these ideas as a case study in March 2012 at 33rd Degree in Krakow in [Microservices - Java, the Unix Way](http://2012.33degree.org/talk/show/67) as did Fred George [about the same time](http://www.slideshare.net/fredgeorge/micro-service-architecure). Adrian Cockcroft at Netflix, describing this approach as "fine grained SOA" was pioneering the style at web scale as were many of the others mentioned in this article - Joe Walnes, Daniel Terhorst-North, Evan Botcher and Graham Tackley.

**2:** The term monolith has been in use by the Unix community for some time. It appears in [The Art of Unix Programming](https://www.amazon.com/gp/product/B003U2T5BA?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=B003U2T5BA) to describe systems that get too big.

**3:** Many object-oriented designers, including ourselves, use the term service object in the [Domain-Driven Design](https://www.amazon.com/gp/product/0321125215?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321125215) sense for an object that carries out a significant process that isn't tied to an entity. This is a different concept to how we're using "service" in this article. Sadly the term service has both meanings and we have to live with the polyseme.

**4:** We consider [an application to be a social construction](https://martinfowler.com/bliki/ApplicationBoundary.html) that binds together a code base, group of functionality, and body of funding.

**5:** The original paper can be found on Melvin Conway's website [here](http://www.melconway.com/Home/Committees_Paper.html)

**6:** We can't resist mentioning Jim Webber's statement that ESB stands for ["Egregious Spaghetti Box"](http://www.infoq.com/presentations/soa-without-esb).

**7:** Netflix makes the link explicit - until recently referring to their architectural style as fine-grained SOA.

**8:** At extremes of scale, organisations often move to binary protocols - [protobufs](https://code.google.com/p/protobuf/) for example. Systems using these still exhibit the characteristic of smart endpoints, dumb pipes - and trade off *transparency* for scale. Most web properties and certainly the vast majority of enterprises don't need to make this tradeoff - transparency can be a big win.

**9:** "YAGNI" or "You Aren't Going To Need It" is an [XP principle](http://c2.com/cgi/wiki?YouArentGonnaNeedIt) and exhortation to not add features until you know you need them.

**10:** It's a little disengenuous of us to claim that monoliths are single language - in order to build systems on todays web, you probably need to know JavaScript and XHTML, CSS, your server side language of choice, SQL and an ORM dialect. Hardly single language, but you know what we mean.

**11:** Adrian Cockcroft specifically mentions "developer self-service" and "Developers run what they wrote"(sic) in [this excellent presentation](http://www.slideshare.net/adrianco/flowcon-added-to-for-cmg-keynote-talk-on-how-speed-wins-and-how-netflix-is-doing-continuous-delivery) delivered at Flowcon in November, 2013.

**12:** We are being a little disengenuous here. Obviously deploying more services, in more complex topologies is more difficult than deploying a single monolith. Fortunately, patterns reduce this complexity - investment in tooling is still a must though.

**13:** In fact, Daniel Terhorst-North refers to this style as *Replaceable Component Architecture* rather than microservices. Since this seems to talk to a subset of the characteristics we prefer the latter.

**14:** Kent Beck highlights this as one his design principles in [Implementation Patterns](https://www.amazon.com/gp/product/0321413091?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321413091).

**15:** And SOA is hardly the root of this history. I remember people saying "we've been doing this for years" when the SOA term appeared at the beginning of the century. One argument was that this style sees its roots as the way COBOL programs communicated via data files in the earliest days of enterprise computing. In another direction, one could argue that microservices are the same thing as the Erlang programming model, but applied to an enterprise application context.

## References

While this is not an exhaustive list, there are a number of sources that practitioners have drawn inspiration from or which espouse a similar philosophy to that described in this article.

Blogs and online articles

- [Clemens Vasters’ blog on cloud at microsoft](http://blogs.msdn.com/b/clemensv/)
- [David Morgantini’s introduction to the topic on his blog](http://davidmorgantini.blogspot.com/2013/08/micro-services-introduction.htm)
- [12 factor apps from Heroku](http://12factor.net/)
- [UK Government Digital Service design principles](https://www.gov.uk/design-principles)
- [Jimmy Nilsson’s blog](http://jimmynilsson.com/blog/)[and article on infoq about Cloud Chunk Computing](http://www.infoq.com/articles/CCC-Jimmy-Nilsson)
- [Alistair Cockburn on Hexagonal architectures](http://alistair.cockburn.us/Hexagonal+architecture)

Books

- [Release it](https://www.amazon.com/gp/product/0978739213?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0978739213)
- [Rest in practice](https://www.amazon.com/gp/product/0596805829?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0596805829)
- [Web API Design (free ebook)](https://pages.apigee.com/web-api-design-ebook.html). Brian Mulloy, Apigee.
- [Enterprise Integration Patterns](https://www.amazon.com/gp/product/0321200683?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321200683)
- [Art of unix programming](https://www.amazon.com/gp/product/0131429019?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0131429019)
- [Growing Object Oriented Software, Guided by Tests](https://www.amazon.com/gp/product/0321503627?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321503627)
- [The Modern Firm: Organizational Design for Performance and Growth](https://www.amazon.com/gp/product/0198293755?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0198293755)
- [Continuous Delivery: Reliable Software Releases through Build, Test, and Deployment Automation](https://www.amazon.com/gp/product/0321601912?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321601912)
- [Domain-Driven Design: Tackling Complexity in the Heart of Software](https://www.amazon.com/gp/product/0321125215?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321125215)

Presentations

- [Architecture without Architects](https://www.youtube.com/watch?v=qVyt3qQ_7TA). Erik Doernenburg.
- [Does my bus look big in this?](http://www.infoq.com/presentations/soa-without-esb). Jim Webber and Martin Fowler, QCon 2008
- [Guerilla SOA](http://www.infoq.com/presentations/webber-guerilla-soa). Jim Webber, 2006
- [Patterns of Effective Delivery](http://vimeo.com/43659070). Daniel Terhorst-North, 2011.
- [Adrian Cockcroft's slideshare channel](http://www.slideshare.net/adrianco).
- [Hydras and Hypermedia](http://vimeo.com/28608667). Ian Robinson, JavaZone 2010
- [Justice will take a million intricate moves](https://martinfowler.com/articles/microservices.html). Leonard Richardson, Qcon 2008.
- [Java, the UNIX way](http://vimeo.com/74452550). James Lewis, JavaZone 2012
- [Micro services architecture](http://yow.eventer.com/yow-2012-1012/micro-services-architecture-by-fred-george-1286). Fred George, YOW! 2012
- [Democratising attention data at guardian.co.uk](http://gotocon.com/video#18). Graham Tackley, GOTO Aarhus 2013
- [Functional Reactive Programming with RxJava](http://gotocon.com/video#6). Ben Christensen, GOTO Aarhus 2013 (registration required).
- [Breaking the Monolith](http://www.infoq.com/presentations/Breaking-the-Monolith). Stefan Tilkov, May 2012.

Papers

- L. Lamport, "The Implementation of Reliable Distributed Multiprocess Systems", 1978 http:// research.microsoft.com/en-us/um/people/lamport/pubs/implementation.pdf
- L. Lamport, R. Shostak, M. Pease, "The Byzantine Generals Problem", 1982 (available at) http:// www.cs.cornell.edu/courses/cs614/2004sp/papers/lsp82.pdf
- R.T. Fielding, "Architectural Styles and the Design of Network-based Software Architectures", 2000 http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm
- E. A. Brewer, "Towards Robust Distributed Systems", 2000 http://www.cs.berkeley.edu/ ~brewer/cs262b-2004/PODC-keynote.pdf
- E. Brewer, "CAP Twelve Years Later: How the 'Rules' Have Changed", 2012, http:// www.infoq.com/articles/cap-twelve-years-later-how-the-rules-have-changed







在过去几年中，“微服务架构”这一术语如雨后春笋般涌现出来，**它描述了一种将软件应用程序设计为一组可独立部署的服务的特定方式**。虽然这种架构风格没有明确的定义，但在组织、业务能力上有一些共同的**特征：自动化部署，端点智能化，语言和数据的去中心化控制。**



“微服务” - 软件架构拥挤大街上的有一个新术语。虽然我们自然的倾向是轻蔑的一瞥将它一带而过，然而我们发现这一术语描述了一种越来越吸引人的软件系统风格。我们已看到，在过去的几年中有许多项目使用了这种风格，并且到目前为止结果都还不错，以致于这已变成了我们同事在构建企业级应用程序时默认使用的架构风格。然而，遗憾的是并没有太多的信息来概述什么是微服务风格以及怎样用这种风格。

简单来说，**微服务架构风格[[1\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn1)是一种将一个单一应用程序开发为一组小型服务的方法，每个服务运行在自己的进程中，服务间通信采用轻量级通信机制(通常用HTTP资源API)。这些服务围绕业务能力构建并且可通过全自动部署机制独立部署。<font color="#3498DB">这些服务共用一个最小型的集中式的管理，服务可用不同的语言开发，使用不同的数据存储技术。</font>**

与单体风格作对比有助于开始解释微服务风格：单体应用程序被构建为单一单元。企业级应用程序通常由三部分组成：客户端侧用户接口(由运行于开发机上的浏览器里的HTML页面和Javascript组成)，数据库(由插入到通用关系型数据库管理系统中的许多数据表格组成)，服务端应用程序。服务端应用程序处理HTTP请求，执行领域逻辑，从数据库中检索、更新数据，选择、填充将要发送到浏览器的HTTP视图。**服务端应用程序是一个单一的逻辑可执行单体[[2\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn2)。系统的任何改变都将牵涉到重新构建和部署服务端的一个新版本。**

这样的单体服务器是构建这样一个系统最自然的方式。处理请求的所有逻辑都运行在一个单一进程中，允许你使用编程语言的基本特性将应用程序划分类、函数和命名空间。你认真的在开发机上运行测试应用程序，并使用部署管道来保证变更已被正确地测试并部署到生产环境中。**该单体的水平扩展可以通过在负载均衡器后面运行多个实例来实现。**

单体应用程序可以是成功的，但人们日益对他们感到挫败，尤其是随着更多的应用程序被部署在云上。变更周期被捆绑在一起 —— 即使只变更应用程序的一部分，也需要重新构建并部署整个单体。长此以往，通常将很难保持一个良好的模块架构，这使得很难变更只发生在需要变更的模块内。程序扩展要求进行整个应用程序的扩展而不是需要更多资源的应用程序部分的扩展。

![单体和微服务](http://blog.cuicc.com/images/sketch.png)

图1: 单体和微服务

这些挫败导向了微服务架构风格：构建应用程序为服务套件。除了服务是可独立部署、可独立扩展的之外，每个服务都提供一个固定的模块边界。甚至允许不同的服务用不同的的语言开发，由不同的团队管理。

我们不会声称微服务风格是新颖的、创新的，其本质至少可以回溯到Unix的设计哲学。但我们的确认为没有足够的人仔细考虑微服务架构，并且如果使用它很多软件实现将会更好。

## 微服务架构的特征

我们无法给出微服务架构风格的一个正式定义，但我们可以尝试去描述我们看到的符合该架构的一些共性。就概述共性的任何定义来说，并非所有的微服务架构风格都有这些共性，但我们期望大多数微服务架构风格展现出大多数特性。虽然本文作者一直是这个相当松散的社区的活跃用户，我们的目的是试图描述我们工作中和我们知道的一些团队的相似努力中的所见所闻。特别是我们不会制定一些可遵守的定义。

### 通过服务组件化

只要我们一直从事软件行业，一个愿望就是通过把组件插在一起构建系统，如同我们看到的现实世界中事物的构造方式一样。在最近的二十年中，我们看到作为大多数语言平台一部分的公共库的大量汇编工作取得了很大的进展。

当谈到组件时，我们遭遇困难的定义：**组件是什么。我们的定义是：组件是一个可独立替换和独立升级的软件单元。**

微服务架构将使用库，但组件化软件的主要方式是分解成服务。我们把库定义为链接到程序并使用内存函数调用来调用的组件，而**服务是一种进程外的组件**，它通过web服务请求或rpc(远程过程调用)机制通信(这和很多面向对象程序中的服务对象的概念是不同的[[3\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn3)。)

使用服务作为组件而不是使用库的一个主要原因是服务**是可独立部署的**。如果你有一个应用程序[[4\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn4)是由单一进程里的多个库组成，任何一个组件的更改都导致必须重新部署整个应用程序。但如果应用程序可分解成多个服务，那么单个服务的变更只需要重新部署该服务即可。当然这也不是绝对的，一些变更将会改变服务接口导致一些协作，但一个好的微服务架构的目的是通过内聚服务边界和按合约演进机制来最小化这些协作。

使用服务作为组件的另一个结果是一个**更加明确的组件接口**。大多数语言没有一个好的机制来定义一个明确的[发布接口](http://martinfowler.com/bliki/PublishedInterface.html)。通常只有文档和规则来预防客户端打破组件的封装，这导致组件间过于紧耦合。服务通过明确的远程调用机制可以很容易的避免这些。

像这样使用服务确实有一些缺点，远程调用比进程内调用更昂贵，因此远程API被设计成粗粒度，这往往更不便于使用。如果你需要更改组件间的责任分配，当你跨进程边界时，这样的行为动作更难达成。

直观的估计，我们观察到服务与运行时进程一一映射，但这仅仅是直观的估计而已。**一个服务可能由多进程组成**，这些进程总是被一起开发和部署，比如只被这个服务使用的应用进程和数据库。

### 围绕业务能力组织

当想要把大型应用程序拆分成部件时，通常管理层聚焦在技术层面，导致UI团队、服务侧逻辑团队、数据库团队的划分。当团队按这些技术线路划分时，即使是简单的更改也会导致跨团队的时间和预算审批。一个聪明的团队将围绕这些优化，两害取其轻 - 只把业务逻辑强制放在它们会访问的应用程序中。换句话说，逻辑无处不在。这是Conway法则[[5\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn5)在起作用的一个例子。

> 任何设计系统(广泛定义的)的组织将产生一种设计，他的结构就是该组织的通信结构。
>
> **-- Melvyn Conway**1967

![Conway法则](http://blog.cuicc.com/images/conways-law.png)

图2: Conway法则在起作用

**微服务采用不同的分割方法，划分成围绕业务能力组织的服务**。这些服务采取该业务领域软件的宽栈实现，包括用户接口、持久化存储和任何外部协作。因此，团队都是跨职能的，包括开发需要的全方位技能：用户体验、数据库、项目管理。

![img](http://blog.cuicc.com/images/PreferFunctionalStaffOrganization.png)

图3: 团队边界增强的服务边界

[www.comparethemarket.com](http://blog.cuicc.com/blog/2015/07/22/microservices/www.comparethemarket.com)是按这种方式组织的一个公司。跨职能团队负责创建和运营产品，产品被划分成若干个体服务，这些服务通过消息总线通信。

大型单体应用程序也总是可以围绕业务能力来模块化，虽然这不是常见的情况。当然，我们将敦促创建单体应用程序的大型团队将团队本身按业务线拆分。我们看到这种情况的主要问题是他们趋向于围绕太多的上下文进行组织。如果单体横跨了多个模块边界，对团队个体成员来说，很难把它们装进他们的短期记忆里。另外，我们看到模块化的路线需要大量的规则来强制实施。服务组件所要求的更加明确的分离，使得它更容易保持团队边界清晰。

> ### 侧边栏：微服务有多大？
>
> 虽然，“微服务”已成为这种架构风格的代称，这个名字确实会导致不幸的聚焦于服务的大小，并为“微”由什么组成争论不休。在与微服务实践者的对话中，我们发现有各种大小的服务。最大的服务报道遵循亚马逊两匹萨团队(也就是，整个团队吃两个披萨就吃饱了)的理念，这意味着团队不超过12个人。在更小的规模大小上，我们看到这样的安排，6人团队将支持6个服务。
>
> 这导致这样一个问题，在服务每12个人和服务每1个人的大小范围内，是否有足够大的不同使他们不能被集中在同一微服务标签下。目前，我们认为最好把它们组合在一起。但随着深入探索这种风格，我们一定有可能改变我们的看法。

### 是产品不是项目

我们看到大多数应用程序开发工作使用一个项目模式：目标是交付将要完成的一些软件。完成后的软件被交接给维护组织，然后它的构建团队就解散了。

微服务支持者倾向于避免这种模式，而是**认为一个团队应该负责产品的整个生命周期**。对此一个共同的启示是亚马逊的理念 [“you build, you run it”](https://queue.acm.org/detail.cfm?id=1142065) ，开发团队负责软件的整个产品周期。这使开发者经常接触他们的软件在生产环境如何工作，并增加与他们的用户联系，因为他们必须承担至少部分的支持工作。

产品思想与业务能力紧紧联系在一起。要持续关注软件如何帮助用户提升业务能力，而不是把软件看成是将要完成的一组功能。

没有理由说为什么同样的方法不能用在单体应用程序上，但服务的粒度更小，使得它更容易在服务开发者和用户之间建立个人关系。

### 智能端点和哑管道

当在不同进程间创建通信结构时，我们已经看到了很多的产品和方法，把显著的智慧强压进通信机制本身。一个很好的例子就是企业服务总线(ESB)，在ESB产品中通常为消息路由、编排(choreography)、转化和应用业务规则引入先进的设施。

微服务社区主张另一种方法：智能端点和哑管道。**基于微服务构建的应用程序的目标是尽可能的解耦和尽可能的内聚** - 他们拥有自己的领域逻辑，他们的行为更像经典UNIX理念中的过滤器 - 接收请求，应用适当的逻辑并产生响应。使用简单的REST风格的协议来编排他们，而不是使用像WS-Choreography或者BPEL或者通过中心工具编制(orchestration)等复杂的协议。

最常用的两种协议是使用资源API的HTTP请求-响应和轻量级消息传送[[6\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn6)。对第一种协议最好的表述是

> 本身就是web，而不是隐藏在web的后面。
>
> **--**[Ian Robinson](http://www.amazon.com/gp/product/0596805829?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0596805829)

微服务团队使用的规则和协议，正是构建万维网的规则和协议(在更大程度上，是UNIX的)。从开发者和运营人员的角度讲，通常使用的资源可以很容易的缓存。

第二种常用方法是在轻量级消息总线上传递消息。选择的基础设施是典型的哑的(哑在这里只充当消息路由器) - 像RabbitMQ或ZeroMQ这样简单的实现仅仅提供一个可靠的异步交换结构 - 在服务里，智能仍旧存活于端点中，生产和消费消息。

单体应用中，组件都在同一进程内执行，它们之间通过方法调用或函数调用通信。把单体变成微服务最大的问题在于通信模式的改变。一种幼稚的转换是从内存方法调用转变成RPC，这导致频繁通信且性能不好。相反，你需要用粗粒度通信代替细粒度通信。

### 去中心化治理

集中治理的一个后果是单一技术平台的标准化发展趋势。经验表明，这种方法正在收缩 - 不是每个问题都是钉子，不是每个问题都是锤子。我们更喜欢使用正确的工具来完成工作，而单体应用程序在一定程度上可以利用语言的优势，这是不常见的。

把单体的组件分裂成服务，在构建这些服务时可以有自己的选择。你想使用Node.js开发一个简单的报告页面？去吧。用C++实现一个特别粗糙的近乎实时的组件？好极了。你想换用一个更适合组件读操作数据的不同风格的数据库？我们有技术来重建它。

当然，仅仅因为你可以做些什么，而不意味着你应该这样做 - 但用这种方式划分系统意味着你可以选择。

团队在构建微服务时也更喜欢用不同的方法来达标。他们更喜欢生产有用的工具这种想法，而不是写在纸上的标准，这样其他开发者可以用这些工具解决他们所面临的相似的问题。有时，这些工具通常在实施中收获并与更广泛的群体共享，但不完全使用一个内部开源模型。现在git和github已经成为事实上的版本控制系统的选择，在内部开放源代码的实践也正变得越来越常见。

> ### 侧边栏：微服务和SOA
>
> 当我们谈论微服务时，一个常见问题是它是否仅仅是十年前我们看到的面向服务的架构(SOA)。这一点是有可取之处的，因为微服务风格和SOA赞同的某些主张十分相似。然而，问题是SOA意味着[很多不同的东西](http://martinfowler.com/bliki/ServiceOrientedAmbiguity.html)，而大多数时候，我们遇到的所谓的SOA和这里我们描述的风格明显不同，这种不同通常由于SOA专注于用于集成单体应用的ESB。
>
> 特别是我们已看到太多的搞砸的服务导向的实现，从趋向于隐藏ESB中的复杂性[[7\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn7)，到花费数百万并不产生任何价值的失败的多年举措，到积极抑制变化的集中治理模型，这有时很难看到过去的这些问题。
>
> 当然，微服务社区用到的许多技术从开发者在大型组织机构整合服务的经验中成长。[Tolerant Reader](http://martinfowler.com/bliki/TolerantReader.html)模式就是这样的一个例子。使用简单协议是衍生自这些经验的另一个方法，使用网络的努力已做出远离中央标准的反应，坦率地说，中心标准已达到[令人叹为观止](http://wiki.apache.org/ws/WebServiceSpecifications)的复杂性。(任何时候，你需要一个本体来管理你的本体，你知道你深陷困境。)
>
> SOA的这种常见表现使得一些微服务倡导者完全拒绝SOA标签，尽管其他人认为微服务是SOA的一种形式[[8\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn8)，也许服务导向做得对。无论哪种方式，事实上，SOA意味着如此不同的事情，这意味着有一个术语来更清晰地定义这种架构风格是有价值的。

Netflix是遵守这一理念的很好的例子。尤其是，以库的形式分享有用的且经过市场检验的代码，这激励其他开发者用类似的方式解决相似的问题，同时还为采用不同方法敞开了大门。共享库倾向于聚焦在数据存储、进程间通信和我们接下来要深入讨论的基础设施自动化的共性问题。

对为服务社区来说，开销特别缺乏吸引力。这并不是说社区不重视服务合约。恰恰相反，因为他们有更多的合约。只是他们正在寻找不同的方式来管理这些合约。像[Tolerant Reader](http://martinfowler.com/bliki/TolerantReader.html)和消费者驱动的契约([Consumer-Driven Contracts](http://martinfowler.com/articles/consumerDrivenContracts.html))这样的模式通常被用于微服务。
这些援助服务合约在独立进化。执行消费者驱动的合约作为构建的一部分，增加了信心并对服务是否在运作提供了更快的反馈。事实上，我们知道澳大利亚的一个团队用消费者驱动的合约这种模式来驱动新业务的构建。他们使用简单的工具定义服务的合约。这已变成自动构建的一部分，即使新服务的代码还没写。服务仅在满足合约的时候才被创建出来 - 这是在构建新软件时避免"YAGNI"[[9\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn9)困境的一个优雅的方法。围绕这些成长起来的技术和工具，通过减少服务间的临时耦合，限制了中心合约管理的需要。

> ### 侧边栏：许多语言，许多选项
>
> JVM作为平台的成长就是在一个共同平台内混合语言的最新例子。几十年来，破壳到高级语言利用高层次抽象的优势已成为一种普遍的做法。如同下拉到机器硬件，用低层次语言写性能敏感的代码一样。然而，很多单体不需要这个级别的性能优化和常见的更高层次的抽象，也不是DSL的。相反，单体通常是单一语言的并趋向于限制使用的技术的数量[[10\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn10)。

也许去中心化治理的最高境界就是亚马逊广为流传的build it/run it理念。团队要对他们构建的软件的各方面负责，包括7*24小时的运营。这一级别的责任下放绝对是不规范的，但我们看到越来越多的公司让开发团队负起更多责任。Netflix是采用这一理念的另一家公司[[11\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn11)。每天凌晨3点被传呼机叫醒无疑是一个强有力的激励，使你在写代码时关注质量。这是关于尽可能远离传统的集中治理模式的一些想法。

### 去中心化数据管理

数据管理的去中心化有许多不同的呈现方式。在最抽象的层面上，这意味着使系统间存在差异的世界概念模型。在整合一个大型企业时，客户的销售视图将不同于支持视图，这是一个常见的问题。客户的销售视图中的一些事情可能不会出现在支持视图中。它们确实可能有不同的属性和(更坏的)共同属性，这些共同属性在语义上有微妙的不同。

这个问题常见于应用程序之间，但也可能发生在应用程序内部，尤其当应用程序被划分成分离的组件时。一个有用的思维方式是有界上下文([Bounded Context](http://martinfowler.com/bliki/BoundedContext.html))内的领域驱动设计(Domain-Driven Design, DDD)理念。DDD把一个复杂域划分成多个有界的上下文，并且映射出它们之间的关系。这个过程对单体架构和微服务架构都是有用的，但在服务和上下文边界间有天然的相关性，边界有助于澄清和加强分离，就像业务能力部分描述的那样。

> ### 侧边栏：久经考验的标准和执行标准
>
> 这有一点分裂，微服务团队倾向于避开企业架构组规定的那种严格的执行标准，但又很乐意使用甚至传教开放标准，比如HTTP、ATOM和其他威格士。
>
> 关键的区别是如何定制标准和如何执行。由诸如IETF等组织管理的标准仅当在世界范围内有几个有用的实现时才变成标准，这往往会从成功的开源项目成长起来。
>
> 这些标准是远离企业世界的标准。往往被一个几乎没有近期编程经验的或受供应商过度影响的组织开发的。

和概念模型的去中心化决策一样，微服务也去中心化数据存储决策。虽然单体应用程序更喜欢单一的逻辑数据库做持久化存储，但企业往往倾向于一系列应用程序共用一个单一的数据库 - 这些决定是供应商授权许可的商业模式驱动的。微服务更倾向于让每个服务管理自己的数据库，或者同一数据库技术的不同实例，或完全不同的数据库系统 - 这就是所谓的混合持久化([Polyglot Persistence](http://martinfowler.com/bliki/PolyglotPersistence.html))。你可以在单体应用程序中使用混合持久化，但它更常出现在为服务里。

![去中心化数据](http://blog.cuicc.com/images/decentralised-data.png)

对跨微服务的数据来说，去中心化责任对管理升级有影响。处理更新的常用方法是在更新多个资源时使用事务来保证一致性。这个方法通常用在单体中。

像这样使用事务有助于一致性，但会产生显著地临时耦合，这在横跨多个服务时是有问题的。分布式事务是出了名的难以实现，因此微服务架构强调[服务间的无事务协作](http://www.eaipatterns.com/ramblings/18_starbucks.html)，对一致性可能只是最后一致性和通过补偿操作处理问题有明确的认知。

对很多开发团队来说，选择用这样的方式管理不一致性是一个新的挑战，但这通常与业务实践相匹配。通常业务处理一定程度的不一致，以快速响应需求，同时有某些类型的逆转过程来处理错误。这种权衡是值得的，只要修复错误的代价小于更大一致性下损失业务的代价。

### 基础设施自动化

在过去的几年中，基础设施自动化已经发生了巨大的变化，特别是云和AWS的演化已经降低了构建、部署和运维微服务的操作复杂度。

许多用微服务构建的产品或系统是由在[持续部署](http://martinfowler.com/bliki/ContinuousDelivery.html)和它的前身[持续集成](http://martinfowler.com/articles/continuousIntegration.html)有丰富经验的团队构建的。团队用这种方式构建软件，广泛使用了基础设施自动化。如下面的构建管线图所示：

![基础构建管道](http://blog.cuicc.com/images/basic-pipeline.png)

图5: 基础构建管道

因为这不是一篇关于持续交付的文章，我们这里将之光住几个关键特性。我们希望有尽可能多的信心，我们的软件正在工作，所以我们运行大量的**自动化测试**。促进科工作软件沿管道线“向上”意味着我们**自动化部署**到每个新的环境中。

一个单体应用程序可以十分愉快地通过这些环境被构建、测试和推送。事实证明，一旦你为单体投入了自动化生产之路，那么部署更多的应用程序似乎也不会更可怕。请记住，持续部署的目标之一是使部署枯燥，所以无论是一个或三个应用程序，只要它的部署仍然枯燥就没关系[[12\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn12)。

> ### 侧边栏：使它容易做正确的事情
>
> 我们发现，作为持续交付和持续部署的一个后果，增加自动化的一个副作用是创造有用的工具，以帮助开发人员和运营人员。用于创造人工制品、管理代码库、起立(standing up)简单服务或添加标准监控和日志记录的工具现在都是很常见的。web上最好的例子可能是[Netflix的开源工具集](http://netflix.github.io/)，但也有其他我们广泛使用的工具，如[Dropwizard](http://dropwizard.codahale.com/)。

我们看到团队使用大量的基础设施自动化的另一个领域是在生产环境中管理微服务时。与我们上面的断言(只要部署是枯燥的)相比，单体和微服务没有太大的差别，各运营场景可以明显不同。

![微部署](http://blog.cuicc.com/images/micro-deployment.png)

图6: 模块部署常常不同

### 为失效设计

使用服务作为组件的一个结果是，应用程序需要被设计成能够容忍服务失效。任何服务调用都可能因为供应者不可用而失败，客户端必须尽可能优雅的应对这种失败。与单体应用设计相比这是一个劣势，因为它引入额外的复杂性来处理它。结果是，微服务团队不断反思服务失效如何影响用户体验。Netflix的[Simian Army](https://github.com/Netflix/SimianArmy)在工作日诱导服务甚至是数据中心故障来测试应用程序的弹性和监测。

在生产环境中的这种自动化测试足够给大多数运营团队那种不寒而栗，通常在结束一周的工作之前。这不是说单体风格不能够进行完善的监测设置，只是在我们的经验中比较少见。

> ### 侧边栏：断路器和产品就绪代码
>
> [断路器(Circuit Breaker)](http://martinfowler.com/bliki/CircuitBreaker.html)与其他模式如Bulkhead和Timeout出现在[《Release it!》](http://www.amazon.com/gp/product/B00A32NXZO?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=B00A32NXZO)中。这些模式是被一起实现的，在构建通信应用程序时，它们是至关重要的。[这篇Netflix博文](http://techblog.netflix.com/2012/02/fault-tolerance-in-high-volume.html)很好的解释了使用这些模式的应用程序。

既然服务随时都可能失败，那么能够快速检测故障，如果可能的话，能自动恢复服务是很重要的。微服务应用程序投入大量比重来进行应用程序的实时监测，既检查构形要素(每秒多少次数据请求)，又检查业务相关指标(例如每分钟收到多少订单)。语义监测可以提供一套早期预警系统，触发开发团队跟进和调查。

这对微服务架构特别重要，因为微服务偏好编排和事件协作，这会带来突发行为。虽然很多专家称赞偶然涌现的价值，事实的真相是，突发行为有时可能是一件坏事请。监测对于快速发现不良突发行为是至关重要的，所以它可以被修复。

单体可以被构建成和微服务一样透明 - 事实上，它们应该是透明的。不同的是，你绝对需要知道在不同进程中运行的服务是否断开。对同一进程中的库来说，这种透明性是不大可能有用的。

> ### 侧边栏：同步调用被认为是有害的
>
> 任何时候，在服务间有大量的同步调用，你将遇到停机的乘法效应。简单地说，就是你的系统的停机时间编程各个组件停机时间的乘积。你面临一个选择，让你的调用变成异步或者管理停机时间。[在www.guardian.co.uk](http://xn--www-lp6e.guardian.co.uk/)，他们已在新平台实现了一个简单的规则 - 每个用户请求一个同步调用，而在Netflix，他们的平台API重设计成在API交换结构(fabric)建立异步性。

微服务团队希望看到为每个单独的服务设置的完善的监控和日志记录，比如控制面板上显示启动/关闭状态和各种各样的运营和业务相关指标。断路器状态、当前吞吐量和时延的详细信息是我们经常遇到的其他例子。

### 进化式设计

微服务从业者，通常有进化式设计背景并且把服务分解看做是进一步的工具，使应用程序开发者能够控制他们应用程序中的变更而不减缓变更。变更控制并不一定意味着变更的减少 - 用正确的态度和工具，你可以频繁、快速且控制良好的改变软件。

当你试图把软件系统组件化时，你就面临着如何划分成块的决策 - 我们决定分割我们的应用的原则是什么？组件的关键特性是独立的更换和升级的理念[[13\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn13) - 这意味着我们要找到这样的点，我们可以想象重写组件而不影响其合作者。事实上很多微服务群组通过明确地预期许多服务将被废弃而不是长期演进来进一步找到这些点。

卫报网站是被设计和构建成单体应用程序的一个好例子，但它已向微服务方向演化。网站的核心仍是单体，但他们喜欢通过使用调用单体API构建的微服务添加新功能。这种方法对天然临时性的特性特别方便，比如处理体育赛事的专题页面。网站的这样一部分可以使用快速开发语言迅速的被放在一起，并且一旦赛事结束立即删除。在金融机构中，我们看到类似的方法，为一个市场机会添加新服务，并在几个月甚至几周后丢弃掉。

强调可替代性是模块设计更一般原则的一个特例，它是通过变更模式来驱动模块化的[[14\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn14)。你想保持在同一模块中相同时间改变的事情。系统中很少变更的部分应该和正在经历大量扰动的部分放在不同的服务里。如果你发现你自己不断地一起改变两个服务，这是它们应该被合并的一个标志。

把组件放在服务中，为更细粒度的发布计划增加了一个机会。对单体来说，任何变更都需要完整构建和部署整个应用程序。而对微服务来说，你只需要重新部署你修改的服务。这可以简化和加速发布过程。坏处是，你必须担心一个服务的变化会阻断其消费者。传统的集成方法试图使用版本管理解决这个问题，但是微服务世界的偏好是[只把版本管理作为最后的手段](http://martinfowler.com/articles/enterpriseREST.html#versioning)。我们可以避免大量的版本管理，通过把服务设计成对他们的提供者的变化尽可能的宽容。

## 微服务是未来吗？

我们写这篇文章的主要目的是讲解微服务的主要思想和原则。通过花时间做这件事情，我们清楚地认为微服务架构风格是一个重要的思想 - 它值得为企业应用程序认真考虑。我们最近用这种风格构建了一些系统，也知道别人用这种风格并赞成这种风格。

那些我们知道的以某种方式开拓这种架构风格的包括亚马逊，Netflix，[卫报](http://www.theguardian.com/)，[英国政府数字服务部门](https://gds.blog.gov.uk/)，[realestate.com.au](http://martinfowler.com/articles/realestate.com.au)，前锋和[comparethemarket.com](http://www.comparethemarket.com/)。2013年的会议电路中全是正向微服务类别转移的公司 - 包括Travis CI。此外还有大量的组织长期以来一直在做可归为微服务类别的事情，但是还没有使用这个名字。(这通常被称为SOA - 虽然，正如我们说过的，SOA有许多矛盾的形式。[[15\]](http://blog.cuicc.com/blog/2015/07/22/microservices/#fn15))

尽管有这些积极的经验，但是，我们并不认为我们确信微服务是软件架构的未来发展方向。虽然到目前为止，与单体应用程序相比，我们的经验是正面的，但我们意识到这样的事实，并没有经过足够的时间使我们做出充分的判断。

通常，你的架构决策的真正后果是在你做出这些决定的几年后才显现的。我们已经看到对模块化有强烈愿望的一个好团队用单体架构构建的项目，已经衰败了多年。很多人相信微服务是不太可能出现这种衰败的，因为服务界限是明确的，并且很难围绕它打补丁。然而，知道我们看到经过足够岁月的足够的系统，我们不能真正评估微服务架构有多么成熟。

人们当然有理由希望微服务是多么不成熟。在组件化中做任何努力，成功取决于软件在多大程度上适用于组件化。很难弄清楚组件边界在哪里。进化式设计承认获取正确边界的困难性和使它们易于重构的重要性。但当你的组件是带有远程通信的服务时，那么重构它比重构带有进程内库的服务难很多。跨服务边界移动代码是很困难的，任何接口变更都需要在参与者之间进行协调，需要添加向后兼容层，并且测试也变得更加复杂。

> ### 侧边栏：《构建微服务》
>
> 我们的同事Sam Newman花费2014年的大部分时间写了[一本书](http://www.amazon.com/gp/product/1491950358?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=1491950358)，捕捉了我们构建微服务的经验。如果你想深入到这个话题中，这应该是你的下一步。

另一个问题是，如果组件不组成的干净利索，那么所有你做的是将复杂度从组件内部转移到组件之间的连接。不仅仅是把复杂性移到周围，它将复杂性移动到一个不太明确、难以控制的地方。在没有服务间的凌乱连接的情况下，当你在看一个小的、简单的组件内部时，你可以很容易的认为事情是更好的。

最后，有团队技能的因素。更熟练的团队倾向于采用新技术。但是对更熟练的团队更有效的一种技术不一定适合于不太熟练的团队。我们已经看到大量的例子，不太熟练的团队构建了凌乱的单体架构，但这需要时间去看当微服务发生这种凌乱时会发生什么。一个差的团队总是创建一个差的系统 - 很难讲在这个例子中微服务会减少这种凌乱还是使它更糟糕。

我们听到的一个合理的说法是，你不应该从微服务架构开始。相反，从单体开始，使它保持模块化，一旦单体成为问题时把它分解成微服务。(虽然这个建议是不理想的，因为一个好的进程内接口通常不是一个好的服务接口。)

所以我们怀着谨慎乐观的态度写了这篇文章。到目前为止，我们已经看到关于微服务风格足以觉得这是一条值得探索的路。我们不能肯定地说，我们将在哪里结束，但软件开发的挑战之一是，你只能基于目前能拿到手的不完善的信息作出决定。

------

1. 2011年5月在威尼斯召开的软件架构研讨会上，“微服务”这一术语被讨论用来描述参与者一直在探索的一种常见的架构风格。2012年5月，该研讨会决定使用“微服务”作为最合适的名字。2012年3月在波兰克拉科夫市举办的33届Degree大会上，James介绍了这些想法作为一个案例研究[微服务 - Java，Unix方式](http://2012.33degree.org/talk/show/67)，Fred George也[差不多在同一时间](http://www.slideshare.net/fredgeorge/micro-service-architecure)提出。Netflix的Adrian Cockcroft把这种方法描述为“细粒度的SOA”，在网域级开拓了这一风格，还有在该文中提到的许多人 - Joe Walnes, Dan North, Evan Botcher 和 Graham Tackley。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref1)
2. 单体这一术语已被Unix社区使用了一段时间，在[《Unix编程艺术》](http://www.amazon.com/gp/product/B003U2T5BA?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=B003U2T5BA)中用它来描述非常大的系统。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref2)
3. 很多面向对象的设计人员，包括我们自己，在[领域驱动设计](http://www.amazon.com/gp/product/0321125215?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321125215)意义上使用服务对象术语，该对象不依赖于实体执行一个重要进程。这和我们在本文中如何使用“服务”是不同的概念。不幸的是，服务这个词有两个含义，我们不得不忍受这个多义词。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref3)
4. 我们认为[应用程序是一个社会结构](http://martinfowler.com/bliki/ApplicationBoundary.html)，它由代码基、功能组、资金体组合在一起。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref4)
5. 原文可在Melvyn Conway的网站上找到，[在这里](http://www.melconway.com/Home/Committees_Paper.html)。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref5)
6. 在极端规模下，组织通常移至二进制协议并权衡规模的透明度。例如[protobufs](http://blog.cuicc.com/blog/2015/07/22/microservices/)。使用二进制协议的系统仍旧展现出智能端点、哑管道。大多数网站，当然绝大多数企业不需要做这种权衡，透明度可以是一个很大的胜利。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref6)
7. 我们忍不住提起Jim Webber的说法，ESB全称是[“令人震惊的意大利面条盒”](http://www.infoq.com/presentations/soa-without-esb) [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref7)
8. Netflix使这种联系清晰起来 - 直到最近作为细粒度SOA提及他们的架构风格。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref8)
9. “YAGNI”也就是“You Aren’t Going To Need It(你将不需要它)”是一个[XP原则](http://c2.com/cgi/wiki?YouArentGonnaNeedIt)和劝诫，在你知道你需要它们时才添加特性 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref9)
10. 我们声称单体是单一语言的，这有一点不诚实 - 要在现在web上构建系统，你可能需要知道JavaScript、XHTML、CSS、选择的服务器语言、SQL和ORM方言。很难只用单一语言，但是你知道我的意思。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref10)
11. 在2013年11月的Flowcon大会上提交的[这个出色演讲中](http://www.slideshare.net/adrianco/flowcon-added-to-for-cmg-keynote-talk-on-how-speed-wins-and-how-netflix-is-doing-continuous-delivery)，Adrian Cockcroft特别提到“开发者自助服务”和“开发者运行他们自己写的代码”(原文如此)。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref11)
12. 我们这里有一点不诚实。显然在更复杂的拓扑结构中部署更多的服务要比部署单一单体更困难。幸运的是，模式减少了这种复杂性 - 在工具上的投资仍是必须的。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref12)
13. 事实上，Dan North提到这种风格是可更换的组件架构而不是微服务。因为这似乎是在讨论我们更喜欢的后者的一个特征子集。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref13)
14. Kent Beck强调这是他[《实现模式》](http://www.amazon.com/gp/product/0321413091?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321413091)一书中的设计原则之一。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref14)
15. SOA几乎是这段历史的根源。我记得当SOA这一术语出现在本世纪初时，有人说“多年来我们一直这样做”。一个理由是，这种风格看其根源是在企业计算早期COBOL程序通过数据文件通信的方式。在另一个方向，有人可能会说微服务和Erlang编程模型相同，但被应用于企业应用程序上下文。 [↩](http://blog.cuicc.com/blog/2015/07/22/microservices/#fnref15)





## 译文二：

## “微服务”博客中译完整版

［按：

The link of the original article by James Lewis and Marin Fowler: http://martinfowler.com/articles/microservices.html. 本文作者为笔者在ThoughtWorks的两位同事James Lewis和老马(Martin Fowler)。老马已经邮件许可笔者进行翻译，并要求笔者在译文中提供原文链接和翻译日期。点击“阅读原文”查看老马的“微服务”博客英文版。**本译文欢迎转载，转载时请注明下面两个链接：**

1）“微服务”原文链接：http://martinfowler.com/articles/microservices.html

2）“微服务”译文链接：http://mp.weixin.qq.com/s?__biz=MjM5MjEwNTEzOQ==&mid=401500724&idx=1&sn=4e42fa2ffcd5732ae044fe6a387a1cc3#rd

］

------

**微服务**

有关这个新的技术架构术语的定义

“微服务架构”这个术语最近几年横空出世，来描述这样一种特定的软件设计方法，即以若干组可独立部署的服务的方式进行软件应用系统的设计。尽管这种架构风格尚无精确的定义，但其在下述方面还是存在一定的共性，即围绕业务功能的组织、自动化部署、端点智能、和在编程语言和数据方面进行去中心化的控制。



> 2014年3月25日
>
> **作者**：
>
> **![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKO8ILibb3icicicBV9ddl0UtfLjemGd9CmpWxQfatyMQe8yAMUFx4kJc41aQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
> James Lewis**是ThoughtWorks首席咨询师，而且是该公司的技术顾问委员会成员。James对于采用相互协作的小型服务来构建应用系统的兴趣，源自于他的整合大规模企业系统的工作背景。他已经使用微服务构建了许多系统，而且几年以来已经成为正在成长的微服务社区的积极参与者。
>
> **![img](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)
> Martin Fowler**是一位作者和演讲者，并且在软件开发行业中，他通常是最能说的那一位。他长期以来一直困惑于这样的问题，即如何才能将软件系统进行组件化。那些声称已将软件进行组件化的声音他听到了很多，但是很少有能让他满意的。他希望微服务不要辜负其倡导者们对它的最初的期望。
>
> 
>
> ***目录***
>
> 微服务架构的九大特性
> 
>
>   *特性一：“组件化”与“多服务”*
>
>   特性二：\*围绕“业务功能”组织团队
>
>   特性三：\*“做产品”而不是“做项目”*
>
>  *特性四：\*“智能端点”与“傻瓜管道”*
>
> 特性五：\*“去中心化”地治理技术*
>
>   **特性六：\*“去中心化”地管理数据*
>
>   **特性七：\*“基础设施”自动化*
>
>   **特性八：\*“容错”设计*
>
>   **特性九：\*“演进式”设计
> *
>
> *未来的方向是“微服务”吗？
> *
>
> ***扩展阅读\****
> *
>
>   *一个微服务应该有多大？*
>
>   *微服务与SOA*
>
>   *多种编程语言，多种选择可能*
>
>   *“实战检验”的标准与“强制执行”的标准*
>
>   *让“方向正确地做事”更容易*
>
>   *"断路器"与“可随时上线的代码”*
>
>   *“同步调用”有害*



“微服务”——这是在软件架构这条熙熙攘攘的大街上出现的又一个新词儿。我们自然会对它投过轻蔑的一瞥，但是这个小小的术语却描述了一种引人入胜的软件系统的风格。最近几年，我们已经看到许多项目使用了这种风格，而且至今其结果都是良好的，以至于对于我们许多ThoughtWorks的同事来说，它正在成为构建企业应用系统的缺省的风格。然而，很不幸的是，我们找不到有关它的概要信息，即什么是微服务风格，以及如何设计微服务风格的架构。

简而言之，微服务架构风格**[1]**这种开发方法，是以开发一组小型服务的方式来开发一个独立的应用系统的。其中每个小型服务都运行在自己的进程中，并经常采用HTTP资源API这样轻量的机制来相互通信。这些服务围绕业务功能进行构建，并能通过全自动的部署机制来进行独立部署。这些微服务可以使用不同的语言来编写，并且可以使用不同的数据存储技术。对这些微服务我们仅做最低限度的集中管理。

在开始介绍微服务风格之前，将其与单块（monolithic）风格进行对比还是很有用的：一个单块应用系统是以一个单个单元的方式来构建的。企业应用系统经常包含三个主要部分：客户端用户界面、数据库和服务端应用系统。客户端用户界面包括HTML页面和运行在用户机器的浏览器中的JavaScript。数据库中包括许多表，这些表被插入一个公共的且通常为关系型的数据库管理系统中。这个服务端的应用系统就是一个单块应用——一个单个可执行的逻辑程序**[2]**。对于该系统的任何改变，都会涉及构建和部署上述服务端应用系统的一个新版本。

> 在我的“微服务资源指南”中(http://martinfowler.com/microservices/)能找到有关微服务最好的文章、视频、图书和播客媒体。

这样的单块服务器是构建上述系统的一种自然的方式。处理用户请求的所有逻辑都运行在一个单个的进程内，从而能使用编程语言的基本特性，来把应用系统划分为类、函数和命名空间。通过精心设计，就能在开发人员的笔记本电脑上运行和测试这样的应用系统，并且使用一个部署流水线来确保变更被很好地进行了测试，并被部署到生产环境中。通过负载均衡器运行许多实例，来将这个单块应用进行横向扩展。

单块应用系统可以被成功地实现，但是渐渐地，特别是随着越来越多的应用系统正被部署到云端，人们对它们开始表现出不满。软件变更受到了很大的限制，应用系统的一个很小的部分的一处变更，也需要将整个单块应用系统进行重新构建和部署。随着时间的推移，单块应用开始变得经常难以保持一个良好的模块化结构，这使得它变得越来越难以将一个模块的变更的影响控制在该模块内。当对系统进行扩展时，不得不扩展整个应用系统，而不能仅扩展该系统中需要更多资源的那些部分。



![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKO3tOPg8jmcGZ82ya0PdaWC51pKVKXCrJ4Uz4f8dXqY4mia3E1u16pcJQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

图1: 单块应用和微服务

这些不满导致了微服务架构风格的诞生：以构建一组小型服务的方式来构建应用系统。除了这些服务能被独立地部署和扩展，每一个服务还能提供一个稳固的模块边界，甚至能允许使用不同的编程语言来编写不同的服务。这些服务也能被不同的团队来管理。

我们并不认为微服务风格是一个新颖或创新的概念，它的起源至少可以追溯到Unix的设计原则。但是我们觉得，考虑微服务架构的人还不够多，并且如果对其加以使用，许多软件的开发工作能变得更好。

------

**[1]**2011年5月在威尼斯附近的一个软件架构工作坊中，大家开始讨论“微服务”这个术语，因为这个词可以描述参会者们在架构领域进行探索时所见到的一种通用的架构风格。2012年5月，这群参会者决定将“微服务”作为描述这种架构风格的最贴切的名字。在2012年3月波兰的克拉科夫市举办的“33rd Degree”技术大会上，本文作者之一James在其“Microservices - Java, the Unix Way”演讲中以案例的形式谈到了这些微服务的观点（http://2012.33degree.org/talk/show/67），与此同时，Fred George也表达了同样的观点（http://www.slideshare.net/fredgeorge/micro-service-architecure）。Netflix公司的Adrian Cockcroft将这种方法描述为“细粒度的SOA”，并且作为先行者和本文下面所提到的众人已经着手在Web领域进行了实践——Joe Walnes, Dan North, Evan Botcher 和 Graham Tackley。



**[2]**"单块"(monolith)这个术语已经被Unix社区使用一段时间了。它出现在The Art of Unix Programming一书中，来描述那些变得庞大的系统。

### **微服务架构的九大特性**



虽然不能说存在微服务架构风格的正式定义，但是可以尝试描述我们所见到的能够被贴上微服务标签的那些架构的共性。下面所描述的所有这些共性，并不是所有的微服务架构都完全具备，但是我们确实期望大多数微服务架构都具备这些共性中的大多数特性。尽管我们两位作者已经成为这个相当松散的社区的活跃成员，但我们的本意还是试图描述我们两人在自己和自己所了解的团队的工作中所看到的情况。特别要指出，我们不会制定大家需要遵循的微服务的定义。



#### 特性一：“组件化”与“多服务”



自我们开始从事软件业已来，发现大家都有一个把组件插在一起来构建系统的愿望，就像在物理世界中所看到的那样。在过去几十年中，我们已经看到，在公共软件库方面已经取得了相当大的进展，这些软件库是大多数编程语言平台的组成部分。

当谈到组件时，就会碰到一个有关定义的难题，即什么是组件？我们的定义是，一个**组件**就是一个可以独立更换和升级的软件单元。

微服务架构也会使用软件库，但其将自身软件进行组件化的主要方法是将软件分解为诸多服务。我们将**软件库**(libraries)定义为这样的组件，即它能被链接到一段程序，且能通过内存中的函数来进行调用。然而，**服务**(services)是进程外的组件，它们通过诸如web service请求或远程过程调用这样的机制来进行通信（这不同于许多面向对象的程序中的service object概念**[3]**）。

以使用服务（而不是以软件库）的方式来实现组件化的一个主要原因是，服务可被独立部署。如果一个应用系统**[4]**由在单个进程中的多个软件库所组成，那么对任一组件做一处修改，都不得不重新部署整个应用系统。但是如果该应用系统被分解为多个服务，那么对于一个服务的多处修改，仅需要重新部署这一个服务。当然这也不是绝对的，一些变更服务接口的修改会导致多个服务之间的协同修改。但是一个良好的微服务架构的目的，是通过内聚的服务边界和服务协议方面的演进机制，来将这样的修改变得最小化。

以服务的方式来实现组件化的另一个结果，是能获得更加显式的（explicit）组件接口。大多数编程语言并没有一个良好的机制来定义显式的发布接口（Published Interface，http://martinfowler.com/bliki/PublishedInterface.html）。通常情况下，这样的接口仅仅是文档声明和团队纪律，来避免客户端破坏组件的封装，从而导致组件间出现过度紧密的耦合。通过使用显式的远程调用机制，服务能更容易地避免这种情况发生。

如此这般地使用服务，也会有不足之处。比起进程内调用，远程调用更加昂贵。所以远程调用API接口必须是粗粒度的，而这往往更加难以使用。如果需要修改组件间的职责分配，那么当跨越进程边界时，这种组件行为的改动会更加难以实现。

近似地，我们可以把一个个服务映射为一个个运行时的进程，但这仅仅是一个近似。一个服务可能包括总是在一起被开发和部署的多个进程，比如一个应用系统的进程和仅被该服务使用的数据库。

------

**[3]**许多面向对象的设计者，包括我们自己，都使用领域驱动设计中service object这个术语，来描述那种执行一段未被绑定到一个entity对象上的重要的逻辑过程的对象。这不同于本文所讨论的"service"的概念。可悲的是，service这个术语同时具有这两个含义，我们必须忍受这样的多义词。

**[4]**我们认为一个应用系统是一个社会性的构建单元（http://martinfowler.com/bliki/ApplicationBoundary.html），来将一个代码库、功能组和资金体（body of funding）结合起来。

------



**特性二：围绕“业务功能”组织团队**

当在寻求将一个大型应用系统分解成几部分时，公司管理层往往会聚焦在技术层面上，这会导致组建用户界面团队、服务器端团队和数据库团队。当团队沿着这些技术线分开后，即使要实现软件一个简单的变更，也会导致跨团队的项目时延和预算审批。在这种情况下，聪明的团队会进行局部优化，“两害相权取其轻”，来直接把代码逻辑塞到他们能访问到的任意应用系统中。换句话说，这种情况会导致代码逻辑散布在系统各处。这就是康威定律**[5]**在起作用的活生生的例子。

> 任何设计（广义上的）系统的组织，都会产生这样一个设计，即该设计的结构与该组织的沟通结构相一致。
>
> ​                *——梅尔文•康威（Melvyn Conway）, 1967年*


![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOnpNJDT2LOgNjDUAXt2mrNFfhLhltAf4icAhVo2fzUgwWUvQnlgHHvFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
图2：康威定律在起作用



微服务使用不同的方法来分解系统，即根据**业务功能**（business capability）来将系统分解为若干服务。这些服务针对该业务领域提供多层次广泛的软件实现，包括用户界面、持久性存储以及任何对外的协作性操作。因此，团队是跨职能的，它拥有软件开发所需的全方位的技能：用户体验、数据库和项目管理。


![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOukpBNGu0hs8FjX367V2cXcEu0kbQomOYyibibKiaZQ3sOgoiclmRzcWQBQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
图3：被团队边界所强化的服务边界

以上述方式来组织团队的公司是www.comparethemarket.com。跨职能团队负责构建和运维每个产品，而每个产品被拆分为多个独立的服务，彼此通过一个消息总线来通信。

> **一个微服务应该有多大？**
>
> 尽管对于这种架构风格，“微服务”已经成为一个流行的名字，但是这个名字确实会不幸地导致大家对服务规模的关注，并且产生了有关什么是“微”的争论。在与微服务从业者的交谈中，我们看到了有关服务的一系列规模。所听到的最大的一个服务的规模，是遵循了亚马逊的“两个比萨团队”（即一个团队可以被两个比萨所喂饱）的理念，这意味着这个团队不会多于12人。对于规模较小的服务，我们已经看到一个6人的团队在支持6个服务。
>
> 
>
> 这引出了一个问题，即“每12人做一个服务”和“每人做一个服务”这样有关服务规模的差距，是否已经大到不能将两者都纳入微服务之下？此时，我们认为最好还是把它们归为一类，但是随着进一步探索这种架构风格，绝对有可能我们将来会改变主意。

大型单块应用系统也可以始终根据业务功能来进行模块化设计，虽然这并不常见。当然，我们会敦促构建单块应用系统的大型团队根据业务线来将自己分解为若干小团队。在这方面，我们已经看到的主要问题是，他们往往是一个团队包含了太多的业务功能。如果这个“单块”跨越了许多模块的边界，那么这个团队的每一个成员都难以记忆所有这些模块的业务功能。此外，我们看到这些模块的边界需要大量的团队纪律性来强制维持。而实现组件化的服务所必要的更加显式的边界，能更加容易地保持团队边界的清晰性。

------

**[5]**原始论文参见梅尔文•康威的网站：http://www.melconway.com/Home/Committees_Paper.html

------



#### 特性三：“做产品”而不是“做项目”

我们所看的的大部分应用系统的开发工作都使用项目模型：目标是交付某一块软件，之后就认为完工了。一旦完工后，软件就被移交给维护团队，接着那个构建该软件的项目团队就会被解散。

微服务的支持者们倾向于避免使用上述模型，而宁愿采纳“一个团队在一个产品的整个生命周期中都应该保持对其拥有”这样的理念。通常认为这一点源自亚马逊的“谁构建，谁运行”（https://queue.acm.org/detail.cfm?id=1142065）的理念，即一个开发团队对一个在生产环境下运行的软件负全责。这会使开发人员每天都会关注软件是如何在生产环境下运行的，并且增进他们与用户的联系，因为他们必须承担某些支持工作。

这样的“产品”理念，是与业务功能的联动绑定在一起的。它不会将软件看作是一个待完成的功能集合，而是认为存在这样一个持续的关系，即软件如何能助其客户来持续增进业务功能。

当然，单块应用系统的开发工作也可以遵循上述“产品”理念，但是更细粒度的服务，能让服务的开发者与其用户之间的个人关系的创建变得更加容易。

#### 特性四：“智能端点”与“傻瓜管道”

当在不同的进程之间构建各种通信结构时，我们已经看到许多产品和方法，来强调将大量的智能特性纳入通信机制本身。这种状况的一个典型例子，就是“企业服务总线”(Enterprise Service Bus, ESB)。ESB产品经常包括高度智能的设施，来进行消息的路由、编制(choreography)、转换，并应用业务规则。

微服务社区主张采用另一种做法：智能端点(smart endpoints)和傻瓜管道(dumb pipes)。使用微服务所构建的各个应用的目标，都是尽可能地实现“高内聚和低耦合”——他们拥有自己的领域逻辑，并且更多地是像经典Unix的“过滤器”(filter)那样来工作——即接收一个请求，酌情对其应用业务逻辑，并产生一个响应。这些应用通过使用一些简单的REST风格的协议来进行编制，而不去使用诸如下面这些复杂的协议，即"WS-编制"(WS-Choreography)、BPEL或通过位于中心的工具来进行编排(orchestration)。

微服务最常用的两种协议是：带有资源API的HTTP“请求－响应”协议，和轻量级的消息发送协议**[6]**。对于前一种协议的最佳表述是：

> 成为Web，而不是躲着Web (Be of the web, not behind the web)
>
> ​                 ——Ian Robinson

这些微服务团队在开发中，使用在构建万维网(world wide web)时所使用的原则和协议（并且在很大程度上，这些原则和协议也是在构建Unix系统时所使用的）。那些被使用过的HTTP资源，通常能被开发或运维人员轻易地缓存起来。

最常用的第二种协议，是通过一个轻量级的消息总线来进行消息发送。此时所选择的基础设施，通常是“傻瓜”(dumb)型的(仅仅像消息路由器所做的事情那样傻瓜)——像RabbitMQ或ZeroMQ那样的简单实现，即除了提供可靠的异步机制(fabric)以外不做其他任何事情——智能功能存在于那些生产和消费诸多消息的各个端点中，即存在于各个服务中。

在一个单块系统中，各个组件在同一个进程中运行。它们相互之间的通信，要么通过方法调用，要么通过函数调用来进行。将一个单块系统改造为若干微服务的最大问题，在于对通信模式的改变。仅仅将内存中的方法调用转换为RPC调用这样天真的做法，会导致微服务之间产生繁琐的通信，使得系统表现变糟。取而代之的是，需要用更粗粒度的协议来替代细粒度的服务间通信。

------

**[6]** 在极度强调高效性(Scale)的情况下，一些组织经常会使用一些二进制的消息发送协议——例如protobuf。即使是这样，这些系统仍然会呈现出“智能端点和傻瓜管道”的特点——来在易读性(transparency)与高效性之间取得平衡。当然，大多数Web属性和绝大多数企业并不需要作出这样的权衡——获得易读性就已经是一个很大的胜利了。

------



#### 特性五：“去中心化”地治理技术

使用中心化的方式来对开发进行治理，其中一个后果，就是趋向于在单一技术平台上制定标准。经验表明，这种做法会带来局限性——不是每一个问题都是钉子，不是每一个方案都是锤子。我们更喜欢根据工作的不同来选用合理的工具。尽管那些单块应用系统能在一定程度上利用不同的编程语言，但是这并不常见。

如果能将单块应用的那些组件拆分成多个服务，那么在构建每个服务时，就可以有选择不同技术栈的机会。想要使用Node.js来搞出一个简单的报表页面？尽管去搞。想用C++来做一个特别出彩儿的近乎实时的组件？没有问题。想要换一种不同风格的数据库，来更好地适应一个组件的读取数据的行为？可以重建。

> **微服务和SOA**
>
> 
>
> 当我们谈起微服务时，一个常见的问题就会出现：是否微服务仅仅是十多年前所看到的“面向服务的架构”(Service Oriented Architecture, SOA)？这样问是有道理的，因为微服务风格非常类似于一些支持SOA的人所赞成的观点。然而，问题在于SOA这个词儿意味着太多不同的东西(http://martinfowler.com/bliki/ServiceOrientedAmbiguity.html)。而且大多数时候，我们所遇到的某些被称作"SOA"的事物，明显不同于本文所描述的风格。这通常由于它们专注于ESB，来集成各个单块应用。
>
> 
>
> 特别地，我们已经看到如此之多的面向服务的拙劣实现——从将系统复杂性隐藏于ESB中的趋势**[7]**，到花费数百万进行多年却没有交付任何价值的失败项目，到顽固抑制变化发生的中心化技术治理模型——以至于有时觉得其所造成的种种问题真的不堪回首。
>
> 
>
> 当然，在微服务社区投入使用的许多技术，源自各个开发人员将各种服务集成到各个大型组织的经验。“容错读取”(Tolerant Reader, http://martinfowler.com/bliki/TolerantReader.html)模式就是这样一个例子。对于Web的广泛使用，使得人们不再使用一些中心化的标准，而使用一些简单的协议。坦率地说，这些中心化的标准，其复杂性已经达到令人吃惊的程度（http://wiki.apache.org/ws/WebServiceSpecifications）。（任何时候，如果需要一个本体（ontology）来管理其他各个本体，那么麻烦就大了。）
>
> 
>
> 这种常见的SOA的表现，已使得一些微服务的倡导者完全拒绝将自己贴上SOA的标签。尽管其他人会将微服务看作是SOA的一种形式**[8]**，也许微服务就是以正确的形式来实现面向服务的SOA。不管是哪种情况，SOA意味着如此之多的不同事物，这表明用一个更加干净利落的术语来命名这种架构风格是很有价值的。

当然，仅仅能做事情，并不意味着这些事情就应该被做——不过用微服务的方法把系统进行拆分后，就拥有了技术选型的机会。

相比选用业界一般常用的技术，构建微服务的那些团队更喜欢采用不同的方法。与其选用一组写在纸上已经定义好的标准，他们更喜欢编写一些有用的工具，来让其他开发者能够使用，以便解决那些和他们所面临的问题相似的问题。这些工具通常源自他们的微服务实施过程，并且被分享到更大规模的组织中，这种分享有时会使用内部开源的模式来进行。现在，git和github已经成为事实上的首选版本控制系统。在企业内部，开源的做法正在变得越来越普遍。

Netflix公司是遵循上述理念的好例子。将实用且经过实战检验的代码以软件库的形式共享出来，能鼓励其他开发人员以相似的方式来解决相似的问题，当然也为在需要的时候选用不同的方案留了一扇门。共享软件库往往集中在解决这样的常见问题，即数据存储、进程间的通信和下面要进一步讨论的基础设施的自动化。

对于微服务社区来说，日常管理开销这一点不是特别吸引人。这并不是说这个社区并不重视服务契约。恰恰相反，它们在社区里出现得更多。这正说明这个社区正在寻找对其进行管理的各种方法。像“容错读取”和“消费者驱动的契约”(Consumer-Driven Contracts, http://martinfowler.com/articles/consumerDrivenContracts.html)这样的模式，经常被运用到微服务中。这些都有助于服务契约进行独立演进。将执行“消费者驱动的契约”做为软件构建的一部分，能增强开发团队的信心，并提供所依赖的服务是否正常工作的快速反馈。实际上，我们了解到一个在澳洲的团队就是使用“消费者驱动的契约”来驱动构建多个新服务的。他们使用了一些简单的工具，来针对每一个服务定义契约。甚至在新服务的代码编写之前，这件事就已经成为自动化构建的一部分了。接下来服务仅被构建到刚好能满足契约的程度——这是一个在构建新软件时避免YAGNI**[9]**困境的优雅方法。这些技术和工具在契约周边生长出来，由于减少了服务之间在时域(temporal)上的耦合，从而抑制了对中心契约管理的需求。

> **多种编程语言，多种选择可能**
>
> 
>
> 做为一个平台，JVM的发展仅仅是一个将各种编程语言混合到一个通用平台的最新例证。近十年以来，在平台外层实现更高层次的编程语言，来利用更高层次的抽象，已经成为一个普遍做法。同样，在平台底层以更低层次的编程语言编写性能敏感的代码也很普遍。然而，许多单块系统并不需要这种级别的性能优化，另外DSL和更高层次的抽象也不常用（这令我们感到失望）。相反，许多单块应用通常就使用单一编程语言，并且有对所使用的技术数量进行限制的趋势**[10]**。

或许去中心化地治理技术的极盛时期，就是亚马逊的“谁构建，谁运行”的风气开始普及的时候。各个团队负责其所构建的软件的所有方面的工作，其中包括7 x 24地对软件进行运维。将运维这一级别的职责下放到团队这种做法，目前绝对不是主流。但是我们确实看到越来越多的公司，将运维的职责交给各个开发团队。Netflix就是已经形成这种风气的另一个组织**[11]**。避免每天凌晨3点被枕边的寻呼机叫醒，无疑是在程序员编写代码时令其专注质量的强大动力。而这些想法，与那些传统的中心化技术治理的模式具有天壤之别。

------

**[7]** 忍不住要提一下Jim Webber的说法：ESB表示Egregious Spaghetti Box（一盒极烂的意大利面条，http://www.infoq.com/presentations/soa-without-esb）。

**[8]** Netflix让SOA与微服务之间的联系更加明确——直到最近这家公司还将他们的架构风格称为“细粒度的SOA”。

**[9]** "YAGNI" 或者 "You Aren't Going To Need It"（你不会需要它）是极限编程的一条原则（http://c2.com/cgi/wiki?YouArentGonnaNeedIt）和劝诫，指的是“除非到了需要的时候，否则不要添加新功能”。

**[10]** 单块系统使用单一编程语言，这样讲有点言不由衷——为了在今天的Web上构建各种系统，可能要了解JavaScript、XHTML、CSS、服务器端的编程语言、SQL和一种ORM的方言。很难说只有一种单一编程语言，但是我们的意思你是懂得的。

**[11]** Adrian Cockcroft在他2013年11月于Flowcon技术大会所做的一次精彩的演讲（http://www.slideshare.net/adrianco/flowcon-added-to-for-cmg-keynote-talk-on-how-speed-wins-and-how-netflix-is-doing-continuous-delivery）中，特别提到了“开发人员自服务”和“开发人员运行他们写的东西”（原文如此）。

------



#### 特性六：“去中心化”地管理数据

去中心化地管理数据，其表现形式多种多样。从最抽象的层面看，这意味着各个系统对客观世界所构建的概念模型，将彼此各不相同。当在一个大型的企业中进行系统集成时，这是一个常见的问题。比如对于“客户”这个概念，从销售人员的视角看，就与从支持人员的视角看有所不同。从销售人员的视角所看到的一些被称之为“客户”的事物，或许在支持人员的视角中根本找不到。而那些在两个视角中都能看到的事物，或许各自具有不同的属性。更糟糕的是，那些在两个视角中具有相同属性的事物，或许在语义上有微妙的不同。

上述问题在不同的应用程序之间经常出现，同时也会出现在这些应用程序内部，特别是当一个应用程序被分成不同的组件时就会出现。思考这类问题的一个有用的方法，就是使用领域驱动设计（Domain-Driven Design, DDD）中的“限界上下文”（Bounded Context，http://martinfowler.com/bliki/BoundedContext.html）的概念。DDD将一个复杂的领域划分为多个限界上下文，并且将其相互之间的关系用图画出来。这一划分过程对于单块和微服务架构两者都是有用的，而且就像前面有关“业务功能”一节中所讨论的那样，在服务和各个限界上下文之间所存在的自然的联动关系，能有助于澄清和强化这种划分。

> **“实战检验”的标准与“强制执行”的标准**
>
> 
>
> 微服务的下述做法有点泾渭分明的味道，即他们趋向于避开被那些企业架构组织所制定的硬性实施的标准，而愉快地使用甚至传播一些开放标准，比如HTTP、ATOM和其他微格式的协议。
>
> 
>
> 这里的关键区别是，这些标准是如何被制定以及如何被实施的。像诸如IETF这样的组织所管理的各种标准，只有达到下述条件才能称为标准，即该标准在全球更广阔的地区有一些正在运行的实现案例，而且这些标准经常源自一些成功的开源项目。
>
> 
>
> 这些标准组成了一个世界，它区别于来自下述另一个世界的许多标准，即企业世界。企业世界中的标准，经常由这样特点的组织来开发，即缺乏用较新技术进行编程的经验，或受到供应商的过度影响。

如同在概念模型上进行去中心化的决策一样，微服务也在数据存储上进行去中心化的决策。尽管各个单块应用更愿意在逻辑上各自使用一个单独的数据库来持久化数据，但是各家企业往往喜欢一系列单块应用共用一个单独的数据库——许多这样的决策是被供应商的各种版权商业模式所驱动出来的。微服务更喜欢让每一个服务来管理其自有数据库。其实现可以采用相同数据库技术的不同数据库实例，也可以采用完全不同的数据库系统。这种方法被称作“多语种持久化”(Polyglot Persistence, http://martinfowler.com/bliki/PolyglotPersistence.html)。在一个单块系统中也能使用多语种持久化，但是看起来这种方法在微服务中出现得更加频繁。




![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOdNQs5kMk0bicns2ZQuS0NYJj75qQUHwXYs7cZibqSzU9ISG7E3OoS6bA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
在各个微服务之间将数据的职责进行“去中心化”的管理，会影响软件更新的管理。处理软件更新的常用方法，是当更新多个资源的时候，使用事务来保证一致性。这种方法经常在单块系统中被采用。

像这样地使用事务，有助于保持数据一致性。但是在时域上会引发明显的耦合，这样当在多个服务之间处理事务时会出现一致性问题。分布式事务实现起来难度之大是臭名远扬的。为此，微服务架构更强调在各个服务之间进行“无事务”的协调(http://www.enterpriseintegrationpatterns.com/ramblings/18_starbucks.html)。这源自微服务社区明确地认识到下述两点，即数据一致性可能只要求数据在最终达到一致，并且一致性问题能够通过补偿操作来进行处理。

对于许多开发团队来说，选择这种方式来管理数据的“非一致性”，是一个新的挑战。但这也经常符合在商业上的实践做法。通常情况下，为了快速响应需求，商家们都会处理一定程度上的数据“非一致性”，来通过做某种反向过程进行错误处理。只要修复错误的成本，与在保持更大的数据一致性却导致丢了生意所产生的成本相比，前者更低，那么这种“非一致性”地管理数据的权衡就是值得的。



**特性七：“基础设施”自动化**

基础设施自动化技术在过去几年里已经得到长足的发展。云的演进，特别是AWS的发展，已经降低了构建、部署和运维微服务的操作复杂性。

许多使用微服务构建的产品和系统，正在被这样的团队所构建，即他们都具备极其丰富的“持续交付”(http://martinfowler.com/bliki/ContinuousDelivery.html)和其前身“持续集成”(http://martinfowler.com/articles/continuousIntegration.html)的经验。用这种方法构建软件的各个团队，广泛采用了基础设施的自动化技术。如下图的构建流水线所示：

![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOY5FYDWBLOticGYAEscwkibibzdUTnuOkBoH71Ps5YBic6z4VZwYzDFOmkg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

图5：基本的构建流水线

因为本文并不是一篇有关持续交付的文章，所以下面仅提请大家注意两个持续交付的关键特点。为了尽可能地获得对正在运行的软件的信心，需要运行大量的**自动化测试**。让可工作的软件达到“晋级”(Promotion)状态从而“推上”流水线，就意味着可以在每一个新的环境中，对软件进行**自动化部署**。

一个单块应用程序，能够相当愉快地在上述各个环境中，被构建、测试和推送。其结果是，一旦在下述工作上进行了投入，即针对一个单块系统将其通往生产环境的通道进行自动化，那么部署更多的应用系统似乎就不再可怕。记住，持续交付的目的之一，是让“部署”工作变得“无聊”。所以不管是一个还是三个应用系统，只要部署工作依旧很“无聊”，那么就没什么可担心的了**[12]**。



> **让“方向正确地做事”更容易**
>
> 
>
> 那些因实现持续交付和持续集成所增加的自动化工作的副产品，是创建一些对开发和运维人员有用的工具。现在，能完成下述工作的工具已经相当常见了，即创建工件(artefacts)、管理代码库、启动一些简单的服务、或增加标准的监控和日志功能。Web上最好的例子可能是Netflix提供的一套开源工具集(http://netflix.github.io/)，但也有其他一些好工具，包括我们已经广泛使用的Dropwizard (http://dropwizard.codahale.com/)。

我们所看到的各个团队在广泛使用基础设施自动化实践的另一个领域，是在生产环境中管理各个微服务。与前面我们对比单块系统和微服务所说的正相反，只要部署工作很无聊，那么在这一点上单块系统和微服务就没什么区别。然而，两者在运维领域的情况却截然不同。




![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOWBkCjicXIeoMd2au2w99PMSb7nS5oHFtvRBPIDp9ZgYXmHRfDZo3ibvw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
图6：两者的模块部署经常会有差异

------

**[12]** 这里我们又有点言不由衷了。 很明显，在更复杂的网络拓扑里，部署更多的服务，会比部署一个单独的单块系统要更加困难。幸运的是，有一些模式能够减少其中的复杂性——但对于工具的投资还是必须的。





#### 特性八：“容错”设计

使用各个微服务来替代组件，其结果是各个应用程序需要设计成能够容忍这些服务所出现的故障。如果服务提供方不可用，那么任何对该服务的调用都会出现故障。客户端要尽可能优雅地应对这种情况。与一个单块设计相比，这是一个劣势。因为这会引人额外的复杂性来处理这种情况。为此，各个微服务团队在不断地反思：这些服务故障是如何影响用户体验的。Netflix公司所研发的开源测试工具Simian Army(https://github.com/Netflix/SimianArmy)，能够诱导服务发生故障，甚至能诱导一个数据中心在工作日发生故障，来测试该应用的弹性和监控能力。

这种在生产环境中所进行的自动化测试，能足以让大多数运维组织兴奋得浑身颤栗，就像在一周的长假即将到来前那样。这并不是说单块架构风格不能构建先进的监控系统——只是根据我们的经验，这在单块系统中并不常见罢了。



> **"断路器"与“可随时上线的代码”**
>
> 
>
> “断路器”(Circuit Breaker, http://martinfowler.com/bliki/CircuitBreaker.html)一词与其他一些模式一起出现在《发布！》(Release It!, http://www.amazon.com/gp/product/B00A32NXZO?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=B00A32NXZO)一书中，例如隔板(Bulkhead)和超时(Timeout)。当构建彼此通信的应用系统时，将这些模式加以综合运用就变得至关重要。Netflix公司的这篇很精彩的博客(http://techblog.netflix.com/2012/02/fault-tolerance-in-high-volume.html)解释了这些模式是如何应用的。

因为各个服务可以在任何时候发生故障，所以下面两件事就变得很重要，即能够快速地检测出故障，而且在可能的情况下能够自动恢复服务。各个微服务的应用都将大量的精力放到了应用程序的实时监控上，来检查“架构元素指标”（例如数据库每秒收到多少请求）和“业务相关指标”（例如系统每分钟收到多少订单）。当系统某个地方出现问题，语义监控系统能提供一个预警，来触发开发团队进行后续的跟进和调查工作。

这对于一个微服务架构是尤其重要的，因为微服务对于服务编制(choreography)和事件协作(http://martinfowler.com/eaaDev/EventCollaboration.html)的偏好，会导致“突发行为”。尽管许多权威人士对于偶发事件的价值持积极态度，但事实上，“突发行为”有时是一件坏事。在能够快速发现有坏处的“突发行为”并进行修复的方面，监控是至关重要的。

单块系统也能构建得像微服务那样来实现透明的监控系统——实际上，它们也应该如此。差别是，绝对需要知道那些运行在不同进程中的服务，在何时断掉了。而如果在同一个进程内使用软件库的话，这种透明的监控系统就用处不大了。



> **“同步调用”有害**
>
> 
>
> 一旦在一些服务之间进行多个同步调用，就会遇到宕机的乘法效应。简而言之，这意味着整个系统的宕机时间，是每一个单独模块各自宕机时间的乘积。此时面临着一个选择：是让模块之间的调用异步，还是去管理宕机时间？在英国卫报网站www.guardian.co.uk，他们在新平台上实现了一个简单的规则——每一个用户请求都对应一个同步调用。然而在Netflix公司，他们重新设计的平台API将异步性构建到API的机制(fabric)中。

那些微服务团队希望在每一个单独的服务中，都能看到先进的监控和日志记录装置。例如显示“运行/宕机”状态的仪表盘，和各种运维和业务相关的指标。另外我们经常在工作中会碰到这样一些细节，即断路器的状态、当前的吞吐率和延迟，以及其他一些例子。

#### 特性九：“演进式”设计

那些微服务的从业者们，通常具有演进式设计的背景，而且通常将服务的分解，视作一个额外的工具，来让应用开发人员能够控制应用系统中的变化，而无须减少变化的发生。变化控制并不一定意味着要减少变化——在正确的态度和工具的帮助下，就能在软件中让变化发生得频繁、快速且经过了良好的控制。

每当试图要将软件系统分解为各个组件时，就会面临这样的决策，即如何进行切分——我们决定切分应用系统时应该遵循的原则是什么？一个组件的关键属性，是具有独立更换和升级的特点**[13]**——这意味着，需要寻找这些点，即想象着能否在其中一个点上重写该组件，而无须影响该组件的其他合作组件。事实上，许多做微服务的团队会更进一步，他们明确地预期许多服务将来会报废，而不是守着这些服务做长期演进。

英国卫报网站是一个好例子。原先该网站是一个以单块系统的方式来设计和构建的应用系统，然而它已经开始向微服务方向进行演进了。原先的单块系统依旧是该网站的核心，但是在添加新特性时他们愿意以构建一些微服务的方式来进行添加，而这些微服务会去调用原先那个单块系统的API。当在开发那些本身就带有临时性特点的新特性时，这种方法就特别方便，例如开发那些报道一个体育赛事的专门页面。当使用一些快速的开发语言时，像这样的网站页面就能被快速地整合起来。而一旦赛事结束，这样页面就可以被删除。在一个金融机构中，我们已经看到了一些相似的做法，即针对一个市场机会，一些新的服务可以被添加进来。然后在几个月甚至几周之后，这些新服务就作废了。

这种强调可更换性的特点，是模块化设计一般性原则的一个特例，通过“变化模式”(pattern of change)**[14]**来驱动进行模块化的实现。大家都愿意将那些能在同时发生变化的东西，放到同一个模块中。系统中那些很少发生变化的部分，应该被放到不同的服务中，以区别于那些当前正在经历大量变动(churn)的部分。如果发现需要同时反复变更两个服务时，这就是它们两个需要被合并的一个信号。

把一个个组件放入一个个服务中，增大了作出更加精细的软件发布计划的机会。对于一个单块系统，任何变化都需要做一次整个应用系统的全量构建和部署。然而，对于一个个微服务来说，只需要重新部署修改过的那些服务就够了。这能简化并加快发布过程。但缺点是：必须要考虑当一个服务发生变化时，依赖它并对其进行消费的其他服务将无法工作。传统的集成方法是试图使用版本化来解决这个问题。但在微服务世界中，大家更喜欢将版本化作为最后万不得已的手段(http://martinfowler.com/articles/enterpriseREST.html#versioning)来使用。我们可以通过下述方法来避免许多版本化的工作，即把各个服务设计得尽量能够容错，来应对其所依赖的服务所发生的变化。

------

**[13]** 事实上，Dan North将这种架构风格称作“可更换的组件架构”，而不是微服务。因为这看起来似乎是在谈微服务特性的一个子集，所以我们选择将其归类为微服务。

**[14]** Kent Beck在《实现模式》(Implementation Patterns, http://www.amazon.com/gp/product/0321413091?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321413091)一书中，将其作为他的一条设计原则而强调出来。

------



### 未来的方向是“微服务”吗？



我们写这篇文章的主要目的，是来解释有关微服务的主要思路和原则。在花了一点时间做了这件事后，我们清楚地认识到，微服务架构风格是一个重要的理念——在研发企业应用系统时，值得对它进行认真考虑。我们最近已经使用这种风格构建了一些系统，并且了解到其他一些团队也在已经使用并赞同这种方法。



我们所了解到的那些在某种程度上做为这种架构风格的实践先驱包括：亚马逊、Netflix、英国卫报(http://www.theguardian.com/international)、英国政府数字化服务中心(https://gds.blog.gov.uk/)、realestate.com.au、Forward和comparethemarket.com。在2013年的技术大会圈子充满了各种各样的正在转向可归类为微服务的公司案例——包括Travis CI。另外还有大量的组织，它们长期以来一直在做着我们可以归类为微服务的产品，却从未使用过这个名字（这通常被标记为SOA——尽管正如我们所说，SOA会表现出各种自相矛盾的形式**[15]**）。



尽管有这些正面的经验，然而并不是说我们确信微服务是软件架构的未来的方向。尽管到目前为止，与单块应用系统相比，我们对于所经历过的微服务的评价是积极的，但是我们也意识到这样的事实，即能供我们做出完整判断的时间还不够长。



通常，架构决策所产生的真正效果，只有在该决策做出若干年后才能真正显现。我们已经看到由带着强烈的模块化愿望的优秀团队所做的一些项目，最终却构建出一个单块架构，并在几年之内不断腐化。许多人认为，如果使用微服务就不大可能出现这种腐化，因为服务的边界是明确的，而且难以随意搞乱。然而，对于那些开发时间足够长的各种系统，除非我们已经见识得足够多，否则我们无法真正评价微服务架构是如何成熟的。



![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKO7ByYaxsgQ1DlHhcha7UQHJSFiahI1C1PF9e8pic7oparduZvicop70IZA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
我们的同事Sam Newman花了2014年的大部分时间撰写了一本书(http://www.amazon.com/gp/product/1491950358?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=1491950358)，来记述我们构建微服务的经验。如果想对这个话题深入下去，下一步就应该是阅读这本书。



有人觉得微服务或许很难成熟起来，这当然是有原因的。在组件化上所做的任何工作的成功与否，取决于软件与组件的匹配程度。准确地搞清楚某个组件的边界的位置应该出现在哪里，是一件困难的工作。演进式设计承认难以对边界进行正确定位，所以它将工作的重点放到了易于对边界进行重构之上。但是当各个组件成为各个进行远程通信的服务后，比起在单一进程内进行各个软件库之间的调用，此时的重构就变得更加困难。跨越服务边界的代码移动就变得困难起来。接口的任何变化，都需要在其各个参与者之间进行协调。向后兼容的层次也需要被添加进来。测试也会变得更加复杂。



另一个问题是，如果这些组件不能干净利落地组合成一个系统，那么所做的一切工作，仅仅是将组件内的复杂性转移到组件之间的连接之上。这样做的后果，不仅仅是将复杂性搬了家，它还将复杂性转移到那些不再明确且难以控制的边界之上。当在观察一个小型且简单的组件内部时，人们很容易觉得事情已经变得更好了，然而他们却忽视了服务之间杂乱的连接。



最后，还有一个团队技能的因素。新技术往往会被技术更加过硬的团队所采用。对于技术更加过硬的团队而更有效的一项技术，不一定适用于一个技术略逊一筹的团队。我们已经看到大量这样的案例，那些技术略逊一筹的团队构建出了杂乱的单块架构。当这种杂乱发生到微服务身上时，会出现什么情况？这需要花时间来观察。一个糟糕的团队，总会构建一个糟糕的系统——在这种情况下，很难讲微服务究竟是减少了杂乱，还是让事情变得更糟。



我们听到一个合理的说法，是说不要一上来就以微服务架构做为起点。相反，要用一个单块系统做为起点(http://martinfowler.com/bliki/MonolithFirst.html)，并保持其模块化。当这个单块系统出现了问题后，再将其分解为微服务。（尽管这个建议并不理想(http://martinfowler.com/articles/dont-start-monolith.html)，因为一个良好的单一进程内的接口，通常不是一个良好的服务接口。）



因此，我们持谨慎乐观的态度来撰写此文。到目前为止，我们已经看到足够多的有关微服务风格的事物，并且觉得这是一条有价值去跋涉的道路(http://martinfowler.com/microservices/)。我们不能肯定地说，道路的尽头在哪里。但是，软件开发的挑战之一，就是只能基于“目前手上拥有但还不够完善”的信息来做出决策。



------

**[15]** SOA很难讲是这段历史的根源。当SOA这个词儿在本世纪初刚刚出现时，我记得有人说：“我们很多年以来一直是这样做的。”有一派观点说，SOA这种风格，将企业级计算早期COBOL程序通过数据文件来进行通信的方式，视作自己的“根”。在另一个方向上，有人说“Erlang编程模型”与微服务是同一回事，只不过它被应用到一个企业应用的上下文中去了。

------



**参考资料**



尽管下面不是一个详尽的清单，但是它们是微服务从业者们获取灵感的一些来源，或者是那些倡导的理念与本文所述内容相似的一些资料。



博客和线上文章

![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOZkowVfLHQibqtFgQEiaial0LBsBYnGN7UDJ8oGc6LIUDM6bbCh6YWoc4Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

图书

![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOX8HCVBhRxPFIZrW240s7avUot9A7p6qTKF4roea5bYEaVQl5apsSPA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

演讲

![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKOFRtO6kZ0pU2sgiaviaAQ0G4DDy1CCPFibCInFdXdRH6sL3kTouBRSoxlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

论文

![图片](http://mmbiz.qpic.cn/mmbiz/ibUw2IFC9orGwMfqAPGrg1rUJzpqqZyKObqF31NFJkZV6ZOhU3afrgibDYey1CvbbXLrFFdQ1qED1XBYgOuZHBFg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**深入阅读**



上表列出了我们在2014年上半年最初撰写这篇文章时所使用的参考资料。若欲获取最新参考资料列表以得到更多信息，请参见微服务资源指南(http://martinfowler.com/microservices/)。



------

［按："微服务"博客中译完整版。现已译完。从2016年1月8日到3月4日，持续交付历时近2个月。感谢各位捧场～



​        2016.03.04

​        于ThoughtWorks北京办公室］

