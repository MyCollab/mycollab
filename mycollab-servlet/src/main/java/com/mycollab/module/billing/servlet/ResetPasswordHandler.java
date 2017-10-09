package com.mycollab.module.billing.servlet;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.configuration.EnDecryptHelper;
import com.mycollab.core.InvalidPasswordException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.PasswordCheckerUtil;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.servlet.GenericHttpServlet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = "/user/recoverypassword/action/*", name = "updateUserPasswordServlet")
public class ResetPasswordHandler extends GenericHttpServlet {

    @Autowired
    private UserService userService;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            PasswordCheckerUtil.checkValidPassword(password);
        } catch (InvalidPasswordException e) {
            throw new UserInvalidInputException(e.getMessage());
        }

        User user = userService.findUserByUserName(username);
        if (user == null) {
            throw new UserInvalidInputException(LocalizationHelper.getMessage(Locale.US,
                    ErrorI18nEnum.ERROR_USER_IS_NOT_EXISTED, username));
        } else {
            user.setPassword(EnDecryptHelper.encryptSaltPassword(password));
            userService.updateWithSession(user, username);
        }
    }
}
