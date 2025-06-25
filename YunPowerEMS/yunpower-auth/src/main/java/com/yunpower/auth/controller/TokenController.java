package com.yunpower.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.auth.form.LoginBody;
import com.yunpower.auth.form.RegisterBody;
import com.yunpower.auth.service.SysLoginService;
import com.yunpower.common.core.domain.R;
import com.yunpower.common.core.utils.JwtUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.security.auth.AuthUtil;
import com.yunpower.common.security.service.TokenService;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.model.LoginUser;

/**
 * token 控制
 *
 * @author yunpower
 */
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    // 是否启用单浏览器登录限制
    @Value("${app.auth.login.single-browser}")
    private Boolean singleBrowser;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        userInfo.setFlag(form.getBrowserFlag());

        // 如果开启单浏览器登录限制，那么就把非当前用户踢掉
        if (singleBrowser) {
            tokenService.kickOutUser(form.getUsername(), form.getBrowserFlag());
        }

        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);

            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);

            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }
}
