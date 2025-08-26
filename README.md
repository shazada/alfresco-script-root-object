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

# Alfresco Base64 JavaScript Root Object

Adds a small Java‑backed **Repository JavaScript Root Object** named `base64` for Alfresco Content Services. It lets your RhinoJS scripts (Rules, Actions, Repo Web Scripts, Workflows) **encode content to Base64** and **decode Base64 back to bytes** without resorting to `Packages` (which is blocked in unsafe contexts like *Data Dictionary > Scripts*).

## What you get

A single root object available to Rhino scripts:

| API | Description |
|---|---|
| `base64.encode(node)` | Streams the node’s primary content (`cm:content`) and returns a Base64 **string**. |
| `base64.encodeBytes(value)` | Encodes **raw bytes** you already have (e.g., `document.content`) and returns a Base64 string. |
| `base64.decodeToBytes(base64String)` | Decodes a Base64 string and returns **byte[]** (Rhino sees this as a Java byte array). |

### Why this exists

Scripts under *Data Dictionary > Scripts* cannot access Spring beans via `Packages.org.springframework...` for security reasons. Exposing a tiny, focused root object is the **supported** and **safe** way to extend the JS API for rules and other contexts.

## Usage examples (Rhino JS)

```javascript
// Encode the primary content of the current document (streaming, memory‑friendly)
var b64 = base64.encode(document);
logger.log("B64 size: " + b64.length);

// If you already have raw bytes (e.g., document.content), encode them:
var b64FromBytes = base64.encodeBytes(document.content);

// Decode back to bytes (Java byte[])
var bytes = base64.decodeToBytes(b64);
logger.log("Decoded bytes: " + bytes.length);
```

> Tip: prefer `base64.encode(document)` for large files — it streams from the repository and avoids loading the full content into memory.

## Requirements

- **Alfresco Content Services**: 25.2.x (built and tested with `${alfresco.platform.version}` 25.2.0)
- **Java**: 17
- **Maven**: 3.8+
- **Packaging**: Repository **JAR** module (to be placed in the `alfresco.war`)

> Should also work with nearby ACS 25.x versions. For older versions, re‑compile against the desired BOM and Java level.

## Build

```bash
mvn clean package
ls target/
# alfresco-b64-jscript-root-1.0-SNAPSHOT.jar
```

## Deploy

Deploy like any other repository JAR module.

### Classic Tomcat/WAR

Copy the JAR into the repository webapp:

```bash
cp target/alfresco-b64-jscript-root-*.jar $TOMCAT/webapps/alfresco/WEB-INF/lib/
```

Restart Alfresco; you should see the bean `base64Script` registered and the JS root object `base64` available in scripts.

### Docker image (recommended)

Create a tiny image that layers the JAR into the repo webapp:

```dockerfile
# Dockerfile
FROM alfresco/alfresco-content-repository-community:25.2.0
COPY target/alfresco-b64-jscript-root-*.jar /usr/local/tomcat/webapps/alfresco/WEB-INF/lib/
```

Then build and run with your existing Compose stack, or add a service override that uses the custom image.

## Testing it quickly

Create a test script under **Repository > Data Dictionary > Scripts > test-b64.js**:

```javascript
if (!document) {
  throw "Run this as a rule on a document.";
}
var s = base64.encode(document);
logger.log("Base64 starts with: " + s.substring(0, 60) + "...");
```

Attach a **rule** to a folder: *when items are created or enter this folder > Execute script > `test-b64.js`*. Upload a file and watch `alfresco.log` for the output.

## License

Apache License 2.0 (see source headers).

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