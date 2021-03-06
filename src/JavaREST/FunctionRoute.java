
package JavaREST;

import JavaREST.Framework.HttpResponse;
import JavaREST.Framework.Route;
import JavaREST.Framework.RouteLambda;
import JavaREST.Framework.RouteNotFoundException;
import JavaREST.Framework.HttpRequest;

/**
 * Author: Keith Jackson
 * Date: 4/18/2016
 * License: MIT
 *
 */


public class FunctionRoute extends Route {
    private RouteLambda handler;

    public FunctionRoute(String uriPattern, RouteLambda handler) {
        super(uriPattern);
        this.handler = handler;
    }

    public boolean isMatch(HttpRequest request) {
        return parser.isMatch(request.getUri().getRawPath());
    }

    public HttpResponse route(HttpRequest request) throws RouteNotFoundException {
        if(isMatch(request)) {
            return handler.func(request);
        } else {
            throw new RouteNotFoundException();
        }
    }

    @Override
    public boolean equals(Object o) {
        try {
            return isMatch((HttpRequest)o);
        } catch(ClassCastException e) {
            return false;
        }
    }

}
