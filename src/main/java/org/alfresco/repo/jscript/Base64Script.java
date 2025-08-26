package org.alfresco.repo.jscript;

import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.model.ContentModel;
import org.mozilla.javascript.Scriptable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Script extends BaseProcessorExtension {

    private ContentService contentService;

    public String encode(Object nodeOrScriptNode) {
        NodeRef nodeRef = ScriptNodeHelper.toNodeRef(nodeOrScriptNode);
        if (nodeRef == null) throw new IllegalArgumentException("Unsupported argument; expected node.");
        ContentReader reader = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
        if (reader == null || !reader.exists()) return null;

        try (InputStream in = reader.getContentInputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             OutputStream b64 = Base64.getEncoder().wrap(bos)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) b64.write(buf, 0, r);
            b64.flush();
            return bos.toString(StandardCharsets.US_ASCII);
        } catch (IOException e) {
            throw new RuntimeException("Base64 encode failed", e);
        }
    }

    public String encodeBytes(Object jsValue) {
        byte[] bytes = RhinoBytes.coerce(jsValue);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public byte[] decodeToBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    // --- helpers ---

    static class ScriptNodeHelper {
        static NodeRef toNodeRef(Object o) {
            // Accept ScriptNode, NodeRef, or string store protocol://id
            if (o instanceof org.alfresco.repo.jscript.ScriptNode sn) return sn.getNodeRef();
            if (o instanceof NodeRef nr) return nr;
            if (o instanceof String s && NodeRef.isNodeRef(s)) return new NodeRef(s);
            return null;
        }
    }

    static class RhinoBytes {
        static byte[] coerce(Object jsValue) {
            // Handle Java byte[], String, and typical Rhino typed arrays
            if (jsValue instanceof byte[] b) return b;
            if (jsValue instanceof String s) return s.getBytes(StandardCharsets.UTF_8);
            if (jsValue instanceof Scriptable sc) {
                // naive fallback for a JS array-like of numbers
                int len = (int) org.mozilla.javascript.ScriptRuntime.toNumber(
                        org.mozilla.javascript.ScriptableObject.getProperty(sc, "length"));
                byte[] out = new byte[len];
                for (int i = 0; i < len; i++) {
                    Object v = org.mozilla.javascript.ScriptableObject.getProperty(sc, String.valueOf(i));
                    out[i] = (byte) org.mozilla.javascript.Context.toNumber(v);
                }
                return out;
            }
            throw new IllegalArgumentException("Unsupported byte source for encodeBytes()");
        }
    }
}