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

  * Abstract assembly into a dependency so we don't have to ship the assembly in the archetype

  * Functional/remote unit tests
