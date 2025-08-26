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