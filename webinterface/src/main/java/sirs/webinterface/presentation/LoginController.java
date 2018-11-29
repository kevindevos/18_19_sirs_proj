package sirs.webinterface.presentation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sirs.app.ws.UserView;
import sirs.webinterface.domain.User;
import sirs.webinterface.domain.UsersManager;

@Controller
@RequestMapping(value = "/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String loginForm(Model model) {
        model.addAttribute("user", new UserView());

        return "login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String loginSubmit(UserView userData) {
        String logString = "loginSubmit: username:" + userData.getUsername()
                + ", password: " + userData.getPassword() ;

        User user = UsersManager.getInstance().getUser(userData.getUsername());

        if(user != null && user.getPassword().equals(userData.getPassword())){
            return "redirect:/dashboard/"+userData.getUsername();
        }

        return "redirect:/login";

    }

}
