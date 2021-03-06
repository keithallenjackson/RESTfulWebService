package JavaREST.Framework;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author: Keith Jackson
 * Date: 4/18/2016
 * License: MIT
 *
 */


public class HttpResponseFactory {
    private String protocol = "HTTP",
            version = "1.1";
    private HttpStatus status;
    private MimeType contentType = MimeType.text;
    private HttpResponseBody body;
    private HttpStatusService statusService;
    private Map<String, List<String>> headers;

    public HttpResponseFactory(HttpStatusService statusService) {
        this.statusService = statusService;
        this.headers = new LinkedHashMap<>();
    }

    public HttpResponseFactory() {
        this(new HttpStatusService());
    }

    public HttpResponseFactory protocol(String protocol) throws IllegalArgumentException {
        if(protocol == null || protocol.isEmpty()) throw new IllegalArgumentException("protocol");
        this.protocol = protocol.toUpperCase().trim();
        return this;
    }

    public HttpResponseFactory version(String version) throws IllegalArgumentException{
        if(version.isEmpty()) throw new IllegalArgumentException("version");
        this.version = version;
        return this;
    }

    public HttpResponseFactory status(HttpStatus status) {
        this.status = status;
        return this;
    }

    public HttpResponseFactory status(HttpStatusCode code) throws IllegalArgumentException {
        HttpStatus httpStatus = statusService.get(code);
        if(httpStatus == null) throw new IllegalArgumentException("code");
        return status(httpStatus);
    }

    public HttpResponseFactory contentType(MimeType type) {
        contentType = type;
        return this;
    }

    public HttpResponseFactory body(HttpResponseBody body) {
        this.body = body;
        return this;
    }

    public HttpResponseFactory header(String header, String... values) {
        headers.putIfAbsent(header, new LinkedList<>());
        for(String value : values)
            headers.get(header).add(value);

        return this;
    }


    // build
    public HttpResponse build() throws IllegalStateException {
        // validation
        if(protocol == null || protocol.isEmpty())
            throw new IllegalStateException("Variable 'protocol' cannot be empty or null");
        if(version == null || version.isEmpty())
            throw new IllegalStateException("Variable 'version' cannot be empty or null");
        if(this.status == null) throw new IllegalStateException("Variable 'status' cannot be null");
        if(this.contentType == null) throw new IllegalStateException("Variable 'contentType cannot be null");

        // creation
        return new HttpResponse(protocol, version, status, contentType, headers, body);
    }

}
