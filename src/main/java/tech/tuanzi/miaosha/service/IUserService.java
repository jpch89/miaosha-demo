package tech.tuanzi.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.vo.LoginVo;
import tech.tuanzi.miaosha.vo.RespBean;

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
    RespBean doLogin(LoginVo loginVo);
}
