package com.accountbook.resolver;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.accountbook.config.data.UserSession;
import com.accountbook.domain.Session;
import com.accountbook.exception.Unauthorized;
import com.accountbook.repository.SessionRepository;
import com.accountbook.response.SessionResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.security.auth.message.AuthException;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");

        //Session null이면 401
        if(accessToken == null || accessToken.isEmpty()){
            throw new AuthException();
        }


        Session session = sessionRepository.findByToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return new UserSession(session);
    }
}
