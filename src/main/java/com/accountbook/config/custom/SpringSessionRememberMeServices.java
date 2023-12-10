package com.accountbook.config.custom;

import com.accountbook.request.LoginInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class SpringSessionRememberMeServices extends org.springframework.session.security.web.authentication.SpringSessionRememberMeServices {

    private final ObjectMapper objectMapper;

    private static final Log logger = LogFactory.getLog(org.springframework.session.security.web.authentication.SpringSessionRememberMeServices.class);

    public SpringSessionRememberMeServices(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        LoginInfo loginInfo = (LoginInfo) request.getAttribute("loginInfo");

        String rememberMe = loginInfo.getRemember();
        if (rememberMe == null || !rememberMe.equalsIgnoreCase("true") && !rememberMe.equalsIgnoreCase("on") && !rememberMe.equalsIgnoreCase("yes") && !rememberMe.equals("1")) {
            if (logger.isDebugEnabled()) {
                logger.debug("Did not send remember-me cookie (principal did not set parameter '" + parameter + "')");
            }

            return false;
        } else {
            return true;
        }
    }
}
