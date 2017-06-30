package me.ziven.ssm.common.exception;

import me.ziven.ssm.common.bean.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    @ResponseBody
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOG.warn("访问" + request.getRequestURI() + " 发生错误", ex);
        ModelAndView error = new ModelAndView();
        if (ex instanceof BizException) {
            BizException bizException = ((BizException) ex);
            switch (bizException.getType()) {
                case page:
                    //跳转到定制化的错误页面
                    error.setViewName("/page/error");
                    error.addObject("code",bizException.getCode());
                    error.addObject("msg",bizException.getMessage());
                    return error;
                case json:
                    //返回json格式的错误信息
                    /*  使用response返回    */
                    response.setStatus(HttpStatus.OK.value()); //设置状态码
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
                    response.setCharacterEncoding("UTF-8"); //避免乱码
                    response.setHeader("Cache-Control", "no-cache, must-revalidate");
                    try {
                        response.getWriter().write(Result.error(bizException.getCode(),bizException.getMessage()).toJson());
                    } catch (IOException e) {
                        LOG.warn("数据返回异常:"+ e.getMessage(), e);
                    }
                    break;
            }
        }else {
            //返回json格式的错误信息
            /*  使用response返回    */
            response.setStatus(HttpStatus.OK.value()); //设置状态码
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
            response.setCharacterEncoding("UTF-8"); //避免乱码
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            try {
                response.getWriter().write(Result.error(-1,"发生了未知错误").toJson());
            } catch (IOException e) {
                LOG.warn("数据返回异常:"+ e.getMessage(), e);
            }
        }
        return error;
    }

}
