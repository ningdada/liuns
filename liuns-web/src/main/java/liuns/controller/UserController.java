package liuns.controller;

import liuns.business.UserManage;
import liuns.model.business.po.UserPO;
import liuns.model.business.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;

/**
 * 用户管理
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    public UserManage userManage;

    /**
     * 新增用户
     *
     * @param user
     * @return 用户id
     */
    @ResponseBody
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Object insert(UserVO user) {
        long userId = userManage.insert(getUserPO(user));
        return success(userId);
    }

    /**
     * 根据用户id获取用户
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id) {
        UserPO user = userManage.get(id);
        return success(getUserVO(user));
    }

    /**
     * vo -> po
     *
     * @param vo
     * @return po
     */
    private UserPO getUserPO(UserVO vo) {
        UserPO po = new UserPO();
        po.setId(vo.getId());
        po.setPwd(vo.getPwd());
        po.setUsername(vo.getUsername());
        return po;
    }


    public Object login(UserVO vo) {
        if (StringUtil.isEmpty(vo.getUsername()) || StringUtil.isEmpty(vo.getPwd())) {
            return fail("请填写用户名和密码！");
        }

        UserPO po = getUserPO(vo);
        UserPO currentUser = userManage.login(po);
        return success(currentUser);
    }

    private void loginHandler(UserPO user, HttpServletRequest req, HttpServletResponse resp) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER); // 不接收第三方cookie
        CookieHandler.setDefault(cookieManager);

        HttpCookie cookie = new HttpCookie("name", "value");
        cookie.setPath("/");
        cookie.setDomain(req.getServerName());
        cookie.setHttpOnly(Boolean.TRUE);
        cookie.setMaxAge(1000 * 60 * 60);
        cookieManager.getCookieStore();
//        cookie.setSecure();

    }

    /**
     * po -> vo
     *
     * @param po
     * @return vo
     */
    private UserVO getUserVO(UserPO po) {
        UserVO vo = new UserVO();
        vo.setId(po.getId());
        vo.setPwd(po.getPwd());
        vo.setUsername(po.getUsername());
        return vo;
    }

}
