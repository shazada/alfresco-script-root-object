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

# Alfresco Global properties Root Object

```javascript
// get mail poller folder from alfresco-global.properties
var host = globalProperties.get("alfresco.host");
logger.log(host);

// print all properties in the JavaScript Console
// if you want to use the logger.log you'll propably need to loop through everything
print(globalProperties.all);
```

# [Alfresco Base64 JavaScript Root Object](Alfresco_Base64_JavaScript_Root_Object.md)
# [Hyland Automate Process JavasScript Object](Hyland_Automate_Process_JavaScript_Root_Object.md)
# [Alfresco Repository JavaScript Root Object for RenditionService2](Alfresco_Repository_JavaScript_Root_Object_for_RenditionService2.md)

# Troubleshooting "Packages" is not defined

Before 25 version you could have folder-rule which triggerd a JavaScript file with the following content:

```javascript
var context = Packages.org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
var sysAdminParams = context.getBean('sysAdminParams', Packages.org.alfresco.repo.admin.SysAdminParams);
logger.log(sysAdminParams.getAlfrescoHost());
```

Now this gives the following error:

```
2025-08-26T21:27:56,820 [] ERROR [framework.webscripts.ResourceWebScriptPost] [http-nio-8080-exec-4] Exception 03fc708f-02e3-4748-bc70-8f02e30748a6. Request /alfresco/api/-default-/public/alfresco/versions/1/nodes/150398b3-7f82-4cf6-af63-c450ef6c5eb8/move executed by admin returned status code 500 with message: 07260073 Failed to execute script 'workspace://SpacesStore/08ace007-6e11-4e45-ace0-076e111e4500': 07260072 ReferenceError: "Packages" is not defined. 
```

So the following script wil now work
```javascript
var ctx = packagesScript.getContext();
var sysAdminParams = ctx.getBean('sysAdminParams', packagesScript.getClass('org.alfresco.repo.admin.SysAdminParams'));
logger.log(sysAdminParams.getAlfrescoHost());
```

## What is done?

There is a new Root Object named packagesScript
This one has the following methods
- getContext(), similar to the old Packages.org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
- getClass(), similar to Packages.org.alfresco.repo.admin.<class>
- getMethods(<class>), this will print all methods for the class

```javascript
print(packagesScript.getMethods('org.alfresco.repo.admin.SysAdminParams'));
```
Will print
```
0 : public abstract boolean org.alfresco.repo.admin.SysAdminParams.getAllowWrite()
1 : public abstract int org.alfresco.repo.admin.SysAdminParams.getMaxUsers()
2 : public abstract java.util.List org.alfresco.repo.admin.SysAdminParams.getAllowedUserList()
3 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getSitePublicGroup()
4 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.subsituteHost(java.lang.String)
5 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getAlfrescoProtocol()
6 : public abstract int org.alfresco.repo.admin.SysAdminParams.getAlfrescoPort()
7 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getAlfrescoContext()
8 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getShareProtocol()
9 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getShareHost()
10 : public abstract int org.alfresco.repo.admin.SysAdminParams.getSharePort()
11 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getShareContext()
12 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getAlfrescoHost()
13 : public abstract java.lang.String org.alfresco.repo.admin.SysAdminParams.getApiExplorerUrl()
```

# This is an ACS project for Alfresco SDK 4.11.0.

Run with `./run.sh build_start` or `./run.bat build_start` and verify that it

 * Runs Alfresco Content Service (ACS)
 * (Optional) Runs Alfresco Share
 * Runs Alfresco Search Service (ASS)
 * Runs PostgreSQL database
 * Deploys the JAR assembled module
 
All the services of the project are now run as docker containers. The run script offers the next tasks:

 * `build_start`. Build the whole project, recreate the ACS docker image, start the dockerised environment composed by ACS, Share (optional), ASS 
 and PostgreSQL and tail the logs of all the containers.
 * `build_start_it_supported`. Build the whole project including dependencies required for IT execution, recreate the ACS docker image, start the dockerised environment 
 composed by ACS, Share (optional), ASS and PostgreSQL and tail the logs of all the containers.
 * `start`. Start the dockerised environment without building the project and tail the logs of all the containers.
 * `stop`. Stop the dockerised environment.
 * `purge`. Stop the dockerised container and delete all the persistent data (docker volumes).
 * `tail`. Tail the logs of all the containers.
 * `reload_acs`. Build the ACS module, recreate the ACS docker image and restart the ACS container.
 * `build_test`. Build the whole project, recreate the ACS docker image, start the dockerised environment, execute the integration tests and stop 
 the environment.
 * `test`. Execute the integration tests (the environment must be already started).

# Few things to notice

 * No parent pom
 * No WAR projects, the jars are included in the custom docker images
 * No runner project - the Alfresco environment is now managed through [Docker](https://www.docker.com/)
 * Standard JAR packaging and layout
 * Works seamlessly with Eclipse and IntelliJ IDEA
 * JRebel for hot reloading, JRebel maven plugin for generating rebel.xml [JRebel integration documentation]
 * AMP as an assembly
 * Persistent test data through restart thanks to the use of Docker volumes for ACS, ASS and database data
 * Resources loaded from META-INF
 * Web Fragment (this includes a sample servlet configured via web fragment)

# TODO

  * Create an official release
  * Clean-up the Readme.MD
  * Include other Root JavaScript Objects to make this library more richer
