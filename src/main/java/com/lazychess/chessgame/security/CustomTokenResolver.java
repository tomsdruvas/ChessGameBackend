package com.lazychess.chessgame.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class CustomTokenResolver implements BearerTokenResolver {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[A-Z0-9-._~+/]+=*)$", Pattern.CASE_INSENSITIVE);
    private boolean allowFormEncodedBodyParameter = false;
    private boolean allowUriQueryParameter = true;
    private String bearerTokenHeaderName = "Authorization";

    public CustomTokenResolver() {
        // Blank for because of the way Spring Security is implemented
    }

    @Override
    public String resolve(final HttpServletRequest request) {
        String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
        String parameterToken = isParameterTokenSupportedForRequest(request) ? resolveFromRequestParameters(request) : null;
        if (authorizationHeaderToken != null) {
            if (parameterToken != null) {
                BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
                throw new OAuth2AuthenticationException(error);
            } else {
                return authorizationHeaderToken;
            }
        } else {
            return parameterToken != null && isParameterTokenEnabledForRequest(request) ? parameterToken : null;
        }
    }

    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(bearerTokenHeaderName);
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        } else {
            Matcher matcher = authorizationPattern.matcher(authorization);
            if (!matcher.matches()) {
                BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
                throw new OAuth2AuthenticationException(error);
            } else {
                return matcher.group("token");
            }
        }
    }

    private static String resolveFromRequestParameters(HttpServletRequest request) {
        String[] values = request.getParameterValues("access_token");
        if (values != null && values.length != 0) {
            if (values.length == 1) {
                return values[0];
            } else {
                BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
                throw new OAuth2AuthenticationException(error);
            }
        } else {
            return null;
        }
    }

    private boolean isParameterTokenSupportedForRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && "application/x-www-form-urlencoded".equals(request.getContentType()) || "GET".equals(request.getMethod());
    }

    private boolean isParameterTokenEnabledForRequest(final HttpServletRequest request) {
        return allowFormEncodedBodyParameter && "POST".equals(request.getMethod()) && "application/x-www-form-urlencoded".equals(request.getContentType()) || allowUriQueryParameter && "GET".equals(request.getMethod());
    }
}
