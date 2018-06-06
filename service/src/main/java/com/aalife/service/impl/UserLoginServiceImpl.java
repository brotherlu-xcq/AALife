package com.aalife.service.impl;

import com.aalife.constant.SystemConstant;
import com.aalife.dao.entity.User;
import com.aalife.dao.entity.UserLogin;
import com.aalife.dao.repository.UserLoginRepository;
import com.aalife.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * @author brother lu
 * @date 2018-06-05
 */
@Service
@Transactional
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserLoginRepository userLoginReposity;

    @Override
    public void createLoginLog(User user) {
        UserLogin userLogin = new UserLogin();
        userLogin.setEntryDate(new Date());
        userLogin.setUser(user);
        userLogin.setEntryId(SystemConstant.SYSTEM_ID);
        userLoginReposity.save(userLogin);
    }
}
