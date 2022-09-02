package com.example.swapi.config;


import com.example.swapi.dto.AuthParams;
import com.example.swapi.dto.CurrentUserDTO;
import com.example.swapi.dto.CustomClaims;
import com.example.swapi.dto.TokenUser;
import com.example.swapi.exception.InvalidTokenException;
import com.example.swapi.service.AnonymousUserService;
import com.example.swapi.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    public static final String ANONYMOUS_AUTH = "anonymous";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final List<RequestMatcher> DEFAULT_MATCHERS = new ArrayList(Collections.singletonList(new AntPathRequestMatcher("/actuator/**")));
    private RequestMatcher requestMatcher;
    private final JwtTokenUtil jwtTokenUtil;
    private final AnonymousUserService anonymousUserService;
    private CurrentUserDTO anonymousUser;

    public void setSecurityExclusionMatcher(List<RequestMatcher> requestMatchers, boolean replaceDefaultMatchers) {
        this.requestMatcher = new OrRequestMatcher(replaceDefaultMatchers ? requestMatchers : (List)Stream.concat(requestMatchers.stream(), DEFAULT_MATCHERS.stream()).collect(Collectors.toList()));
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        this.setAuthenticationContext(this.parseTokenHeader(request.getHeader("Authorization"), request));
        chain.doFilter(request, response);
    }

    protected AuthParams parseTokenHeader(String requestTokenHeader, HttpServletRequest request) {
        AuthParams params = new AuthParams(requestTokenHeader, request);
        if (requestTokenHeader != null) {
            if (requestTokenHeader.equals(BEARER_PREFIX + ANONYMOUS_AUTH)) {
                params.setUsername(ANONYMOUS_AUTH);
            } else if (requestTokenHeader.startsWith(BEARER_PREFIX)) {
                String jwtToken = requestTokenHeader.substring(7);
                params.setJwtToken(jwtToken);

                try {
                    CustomClaims claims = this.jwtTokenUtil.getCustomClaims(jwtToken);
                    params.setUsername(claims.getUsername());
                    params.setUserId(claims.getUserId());
                    params.setUuidToken(claims.getUuidToken());
                } catch (ExpiredJwtException var7) {
                    String login = var7.getClaims() != null && var7.getClaims().getSubject() != null ? var7.getClaims().getSubject() : null;
                    log.warn("JWT Token has expired login: {}, token: {}", login, jwtToken);
                    throw new InvalidTokenException("Your session has expired");
                } catch (Exception var8) {
                    log.warn("Unable to get JWT Token: {}", jwtToken, var8);
                    throw new InvalidTokenException("Incorrect access data provided");
                }
            }
        }

        return params;
    }

    protected void setAuthenticationContext(AuthParams params) {
        if (params.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (this.anonymousUser == null) {
                this.anonymousUser = this.anonymousUserService.getAnonymousUser();
            }

            TokenUser tokenUser;
            if ("anonymous".equals(params.getUsername())) {
                tokenUser = new TokenUser(this.anonymousUser.getLogin(), this.anonymousUser.getPwd(), this.anonymousUser.getId(), false, new ArrayList(), params.getRequestTokenHeader(), params.getUuidToken());
            } else {
                tokenUser = new TokenUser(params.getUsername(), "randomPass", params.getUserId(), false, new ArrayList(), params.getRequestTokenHeader(), params.getUuidToken());
            }

            if ("anonymous".equals(params.getUsername()) || this.validateToken(params.getJwtToken(), tokenUser)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenUser, (Object)null, tokenUser.getAuthorities());
                authenticationToken.setDetails((new WebAuthenticationDetailsSource()).buildDetails(params.getRequest()));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    protected boolean validateToken(String token, UserDetails details) {
        return token != null && this.jwtTokenUtil.validateToken(token, details);
    }

    protected boolean shouldNotFilter(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, AnonymousUserService anonymousUserService) {
        this.requestMatcher = new OrRequestMatcher(DEFAULT_MATCHERS);
        this.jwtTokenUtil = jwtTokenUtil;
        this.anonymousUserService = anonymousUserService;
    }
}
