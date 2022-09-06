package org.apache.coyote.http11.controller;

import java.util.Map;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.NotFoundException;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.http.HttpRequest;
import org.apache.coyote.http11.http.HttpResponse;
import org.apache.coyote.http11.http.domain.ContentType;
import org.apache.coyote.http11.http.domain.HttpMethod;
import org.apache.coyote.http11.http.domain.MessageBody;
import org.apache.coyote.http11.util.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public HttpResponse service(final HttpRequest httpRequest) {
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        System.out.println(httpMethod);
        if (httpMethod.equals(HttpMethod.GET)) {
            return doGet(httpRequest);
        }
        if (httpMethod.equals(HttpMethod.POST)) {
            return doPost(httpRequest);
        }
        return HttpResponse.methodNotAllowed();
    }

    private HttpResponse doGet(final HttpRequest httpRequest) {
        String uri = httpRequest.getRequestLine().getRequestTarget().getUri();
        String responseBody = FileReader.read(uri + ".html");
        return HttpResponse.ok(ContentType.from(uri), new MessageBody(responseBody));
    }

    private HttpResponse doPost(final HttpRequest httpRequest) {
        Map<String, String> queryParameters = httpRequest.getRequestLine()
                .getRequestTarget()
                .getQueryParameters();
        String account = queryParameters.get("account");
        String password = queryParameters.get("password");
        User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new NotFoundException("User not found."));
        if (user.checkPassword(password)) {
            log.info("User : {}", user);
            return HttpResponse.found();
        }
        return HttpResponse.unauthorized();
    }
}