package nextstep.jwp.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {

    private static final int FIRST_WORD_INDEX = 0;
    private static final int SECOND_WORD_INDEX = 1;
    private static final int START_INDEX = 0;
    private static final int FORM_DATA_KEY_INDEX = 0;
    private static final int FORM_DATA_VALUE_INDEX = 1;
    private static final int QUERY_PARAMS_KEY_INDEX = 0;
    private static final int QUERY_PARAMS_VALUE_INDEX = 1;

    private final String statusLine;
    private final String bodyLine;
    private List<String> headerLines;

    public HttpRequest(String statusLine, List<String> headerLines, String bodyLine) {
        this.statusLine = statusLine;
        this.headerLines = headerLines;
        this.bodyLine = bodyLine;
    }

    public boolean isEmptyLine() {
        return Objects.isNull(statusLine) && headerLines.isEmpty() && Objects.isNull(bodyLine);
    }

    public String extractURI() {
        return statusLine.split(" ")[SECOND_WORD_INDEX];
    }

    public Map<String, String> extractURIQueryParams() {
        String uri = extractURI();
        Map<String, String> queryParams = new HashMap<>();
        int index = uri.indexOf("?");
        if (index != -1) {
            String queryString = uri.substring(index + 1);
            String[] splitQueryStrings = queryString.split("&");

            for (String splitQueryString : splitQueryStrings) {
                String[] splitParam = splitQueryString.split("=");
                String key = splitParam[QUERY_PARAMS_KEY_INDEX];
                String value = splitParam[QUERY_PARAMS_VALUE_INDEX];
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }

    public String extractURIPath() {
        String uri = extractURI();
        int queryStringDelimiterIndex = uri.indexOf("?");
        if (queryStringDelimiterIndex != -1) {
            return uri.substring(START_INDEX, queryStringDelimiterIndex);
        }
        return uri;
    }

    public HttpMethod extractHttpMethod() {
        String httpMethodName = statusLine.split(" ")[FIRST_WORD_INDEX];
        return HttpMethod.findHttpMethod(httpMethodName);
    }

    public Map<String, String> extractFormData() {
        String[] splitFormData = bodyLine.split("&");
        Map<String, String> result = new HashMap<>();
        for (String s : splitFormData) {
            String[] split = s.split("=");
            result.put(split[FORM_DATA_KEY_INDEX], split[FORM_DATA_VALUE_INDEX]);
        }
        return result;
    }
}
