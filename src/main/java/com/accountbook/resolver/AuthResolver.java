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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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
        //String accessToken = webRequest.getHeader("Authorization");
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if(null == request){
            log.error("HttpServletRequest is null ");
            throw new Unauthorized();
        }

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            log.info(">>>>>>>>> cookie.getValue() {} ", cookie.getValue().toString());
        }


        if(cookies.length == 0){
            log.error("cookies.length == 0 ");
            throw new Unauthorized();
        }
        log.info(">>>>>> cookie.length = {}", cookies.length);

        String token = cookies[0].getValue();

        Session session = sessionRepository.findByToken(token)
                .orElseThrow(Unauthorized::new);
        return new UserSession(session);


//        //Session null이면 401
//        if(accessToken == null || accessToken.isEmpty()){
//            throw new AuthException();
//        }
//
//
//        Session session = sessionRepository.findByToken(accessToken)
//                .orElseThrow(Unauthorized::new);

    }
}
