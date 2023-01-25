# Alfresco Script Root Object

This is an ACS project for Alfresco SDK 4.5 (ACS 7.3).

The project adds a new JavaScript Root Object `sysAdmin` using [Alfresco Repo Extension Point](https://docs.alfresco.com/content-services/latest/develop/repo-ext-points/javascript-root-objects/)

Alfresco Repository Spring Beans can be used from JS scripts accessing to Alfresco Web Application Context.

```javascript
var context = Packages.org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
var sysAdminParams = context.getBean('sysAdminParams', Packages.org.alfresco.repo.admin.SysAdminParams);
logger.log(sysAdminParams.getAlfrescoHost());
```

However, when copying JS scripts to default folder `Repository > Data Dictionary > Scripts`, the execution environment is considered *unsafe* and source code accessing to Spring Beans using `Packages` is blocked.

Allowing the execution of this code requires the JS script deployed with Alfresco Repository or creating an Alfresco Script Root Object to expose required methods. This project exposes some methods from `sysAdminParams` Repository Spring Bean so it can be used from JS scripts available in `Repository > Data Dictionary > Scripts` in the following way:

```javascript
logger.log(sysAdmin.getAlfrescoHost());
```

## Building

Build the code as a regular Maven project.

```
$ mvn clean package
$ ls target/
alfresco-script-root-object-1.0.0.jar
```

## Deploying

Deploy this addon as a regular JAR library to Alfresco Repository WAR.

```
$ cp alfresco-script-root-object-1.0.0.jar $TOMCAT_DIR/webapps/alfresco/WEB-INF/lib
```