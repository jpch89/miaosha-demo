package tech.tuanzi.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.exception.GlobalException;
import tech.tuanzi.miaosha.mapper.UserMapper;
import tech.tuanzi.miaosha.service.IUserService;
import tech.tuanzi.miaosha.utils.CookieUtil;
import tech.tuanzi.miaosha.utils.MD5Util;
import tech.tuanzi.miaosha.utils.UUIDUtil;
import tech.tuanzi.miaosha.vo.LoginVo;
import tech.tuanzi.miaosha.vo.RespBean;
import tech.tuanzi.miaosha.vo.RespBeanEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author Patrick Ji
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        // 根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (null == user) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        // 判断密码是否正确
        String passwordHash1 = MD5Util.formPassToDBPass(password, user.getSalt());
        String passwordHash2 = user.getPassword();
        if (!passwordHash1.equals(passwordHash2)) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        // 生成 cookie
        String ticket = UUIDUtil.uuid();
        // 将用户信息存入 redis 中
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}
