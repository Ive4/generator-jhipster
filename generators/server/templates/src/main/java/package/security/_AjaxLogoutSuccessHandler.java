package <%=packageName%>.security;
<% if (authenticationType == 'oauth2') { %>
import org.apache.commons.lang3.StringUtils;<% } %>
import org.springframework.security.core.Authentication;<% if (authenticationType == 'oauth2') { %>
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;<% } %>
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
    implements LogoutSuccessHandler {
    <%_ if (authenticationType == 'oauth2') { _%>

    public static final String BEARER_AUTHENTICATION = "Bearer ";

    private final TokenStore tokenStore;

    public AjaxLogoutSuccessHandler(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }
    <%_ } _%>

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {<% if (authenticationType == 'oauth2') { %>

        // Request the token
        String token = request.getHeader("authorization");
        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
            final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(StringUtils.substringAfter(token, BEARER_AUTHENTICATION));

            if (oAuth2AccessToken != null) {
                tokenStore.removeAccessToken(oAuth2AccessToken);
            }
        }
<% } %>
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
