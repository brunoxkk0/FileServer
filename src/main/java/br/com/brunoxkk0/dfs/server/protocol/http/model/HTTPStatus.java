package br.com.brunoxkk0.dfs.server.protocol.http.model;

public class HTTPStatus {

    public final static HTTPStatus Continue =                        of(100, "Continue");
    public final static HTTPStatus SwitchingProtocol =               of(101, "Switching Protocol");
    public final static HTTPStatus Processing =                      of(102, "Processing");
    public final static HTTPStatus EarlyHints =                      of(103, "EarlyHints");

    public final static HTTPStatus Ok =                              of(200, "OK");
    public final static HTTPStatus Created =                         of(201, "Created");
    public final static HTTPStatus Accepted =                        of(202, "Accepted");
    public final static HTTPStatus NonAuthoritativeInformation =     of(203, "Non-Authoritative Information");
    public final static HTTPStatus NoContent =                       of(204, "No Content");
    public final static HTTPStatus ResetContent =                    of(205, "Reset Content");
    public final static HTTPStatus PartialContent =                  of(206, "Partial Content");
    public final static HTTPStatus MultiStatus_207 =                 of(207, "Multi-Status");
    public final static HTTPStatus MultiStatus_208 =                 of(208, "Multi-Status");
    public final static HTTPStatus IMUsed =                          of(226, "IM Used");

    public final static HTTPStatus MultipleChoices =                 of(300, "Multiple Choices");
    public final static HTTPStatus MovedPermanently =                of(301, "Moved Permanently");
    public final static HTTPStatus Found =                           of(302, "Found");
    public final static HTTPStatus SeeOther =                        of(303, "See Other");
    public final static HTTPStatus NotModified =                     of(304, "Not Modified");
    public final static HTTPStatus TemporaryRedirect =               of(307, "Temporary Redirect");
    public final static HTTPStatus PermanentRedirect =               of(308, "Permanent Redirect");

    public final static HTTPStatus BadRequest =                      of(400, "Bad Request");
    public final static HTTPStatus Unauthorized =                    of(401, "Unauthorized");
    public final static HTTPStatus PaymentRequired =                 of(402, "Payment Required");
    public final static HTTPStatus Forbidden =                       of(403, "Forbidden");
    public final static HTTPStatus NotFound =                        of(404, "Not Found");
    public final static HTTPStatus MethodNotAllowed =                of(405, "Method Not Allowed");
    public final static HTTPStatus NotAcceptable =                   of(406, "Not Acceptable");
    public final static HTTPStatus ProxyAuthenticationRequired =     of(407, "Proxy Authentication Required");
    public final static HTTPStatus RequestTimeout =                  of(408, "Request Timeout");
    public final static HTTPStatus Conflict =                        of(409, "Conflict");
    public final static HTTPStatus Gone =                            of(410, "Gone");
    public final static HTTPStatus LengthRequired =                  of(411, "Length Required");
    public final static HTTPStatus PreconditionFailed =              of(412, "Precondition Failed");
    public final static HTTPStatus PayloadTooLarge =                 of(413, "Payload Too Large");
    public final static HTTPStatus URITooLong =                      of(414, "URI Too Long");
    public final static HTTPStatus UnsupportedMediaType =            of(415, "Unsupported Media Type");
    public final static HTTPStatus RangeNotSatisfiable =             of(416, "Range Not Satisfiable");
    public final static HTTPStatus ExpectationFailed =               of(417, "Expectation Failed");
    public final static HTTPStatus ImATeapot =                       of(418, "I'm a teapot");
    public final static HTTPStatus MisdirectedRequest =              of(421, "Misdirected Request");
    public final static HTTPStatus UnprocessableEntity =             of(422, "Unprocessable Entity");
    public final static HTTPStatus Locked =                          of(423, "Locked");
    public final static HTTPStatus FailedDependency =                of(424, "Failed Dependency");
    public final static HTTPStatus TooEarly =                        of(425, "TooEarly");
    public final static HTTPStatus UpgradeRequired =                 of(426, "Upgrade Required");
    public final static HTTPStatus PreconditionRequired =            of(428, "Precondition Required");
    public final static HTTPStatus TooManyRequests =                 of(429, "Too Many Requests");
    public final static HTTPStatus RequestHeaderFieldsTooLarge =     of(431, "Request Header Fields Too Large");
    public final static HTTPStatus UnavailableForLegalReasons =      of(451, "Unavailable For Legal Reasons");

    public final static HTTPStatus InternalServerError =             of(500, "Internal Server Error");
    public final static HTTPStatus NotImplemented =                  of(501, "Not Implemented");
    public final static HTTPStatus BadGateway =                      of(502, "Bad Gateway");
    public final static HTTPStatus ServiceUnavailable =              of(503, "Service Unavailable");
    public final static HTTPStatus GatewayTimeout =                  of(504, "Gateway Timeout");
    public final static HTTPStatus HTTPVersionNotSupported =         of(505, "HTTP Version Not Supported");
    public final static HTTPStatus VariantAlsoNegotiates =           of(506, "Variant Also Negotiates");
    public final static HTTPStatus InsufficientStorage =             of(507, "Insufficient Storage");
    public final static HTTPStatus LoopDetected =                    of(508, "Loop Detected");
    public final static HTTPStatus NotExtended =                     of(510, "Not Extended");

    private final int code;
    private final String message;

    private HTTPStatus(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "" + code + " " + message;
    }

    public static HTTPStatus of(int code, String message){
        return new HTTPStatus(code, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HTTPStatus status = (HTTPStatus) o;

        return code == status.code;
    }

    @Override
    public int hashCode() {
        return code;
    }
}
