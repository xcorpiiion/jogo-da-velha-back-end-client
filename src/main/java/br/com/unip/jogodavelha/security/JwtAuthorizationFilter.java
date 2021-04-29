package br.com.unip.jogodavelha.security;

import br.com.unip.jogodavelha.security.utils.JwtUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static br.com.unip.jogodavelha.constante.Constantes.getAUTHORIZATION;
import static br.com.unip.jogodavelha.constante.Constantes.getBEARER;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Getter
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String requestHeader = request.getHeader(getAUTHORIZATION());
        if (!StringUtils.isEmpty(requestHeader) && requestHeader.startsWith(getBEARER())) {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(requestHeader.substring(7));
            if (authenticationToken != null) {
                getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (this.getJwtUtil().isTokenValido(token)) {
            String username = this.getJwtUtil().getUsername(token);
            UserDetails user = this.getUserDetailsService().loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return null;
    }
}
