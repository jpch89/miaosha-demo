package tech.tuanzi.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.vo.LoginVo;
import tech.tuanzi.miaosha.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author Patrick Ji
 */
public interface IUserService extends IService<User> {
    /**
     * 登录
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据 cookie 获取用户
     */
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
