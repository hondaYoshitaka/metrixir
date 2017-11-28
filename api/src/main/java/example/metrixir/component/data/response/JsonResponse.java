package example.metrixir.component.data.response;

import enkan.collection.Headers;
import enkan.data.DefaultHttpResponse;

public class JsonResponse<T> extends DefaultHttpResponse<T> {

    protected JsonResponse(int status, Headers headers) {
        super(status, headers);
    }

    public static <T> JsonResponse of(T body) {
        final Headers headers = Headers.of("Content-Type", "application/json; charset=utf-8");

        final JsonResponse response = new JsonResponse(200, headers);
        response.setBody(body);

        return response;
    }
}
