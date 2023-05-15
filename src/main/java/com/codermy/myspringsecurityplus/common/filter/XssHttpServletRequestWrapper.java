package com.codermy.myspringsecurityplus.common.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-05-15  11:17
 * @Description: XSS过滤器
 * @Version: 1.0
 */
@Component
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if (StringUtils.isEmpty(header)) {
            return header;
        }
        return HtmlUtils.htmlEscape(header);
    }

    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        return HtmlUtils.htmlEscape(parameter);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        for (String parameterValue : parameterValues) {
            parameterValue = HtmlUtils.htmlEscape(parameterValue);
        }
        return parameterValues;
    }
}
