There are 3 projects in Single-Sign-On, include sso, sso.common and app1.

Proejct sso is a core project. Provide user login service, user authentication check service and user logout service by restful api and dubbo(it is a alibaba rpc project) api.

Project sso.common is a basic project. Provide some utils, javabean, interface for project sso. The most important is that it contains sso-access-client(SsoClient.java) for invoke sso service(login service, user authentication check service and user logout service).Also provide interface AuthenticationService for dubbo.

Project app1 is an example project. That show you how to use sso.common in new project.

How to start sso server

1. git clone https://github.com/worldmaomao/Single-Sign-On.git

2. cd sso.common

3. mvn clean install

4. cd sso

5. import src/main/sql/init.sql into mysql

6. configure src/main/resources/application-dev.yml. You must configure mysql database info correctly.

7. mvn spring-boot:run













