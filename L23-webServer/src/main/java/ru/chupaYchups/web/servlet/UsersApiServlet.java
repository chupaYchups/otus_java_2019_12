package ru.chupaYchups.web.servlet;

import com.google.gson.Gson;
import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;
    public static final String USER_NAME_PARAM = "userName";
    public static final String USER_LOGIN_PARAM = "userLogin";
    public static final String USER_PASSWORD_PARAM = "userPassword";
    public static final String USERS_PAGE_PATH = "/users";

    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = dbServiceUser.getUser(extractIdFromRequest(request)).orElse(null);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.setName(req.getParameter(USER_NAME_PARAM));
        user.setLogin(req.getParameter(USER_LOGIN_PARAM));
        user.setPassword(req.getParameter(USER_PASSWORD_PARAM));
        dbServiceUser.saveUser(user);
        resp.sendRedirect(USERS_PAGE_PATH);
    }


    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}
