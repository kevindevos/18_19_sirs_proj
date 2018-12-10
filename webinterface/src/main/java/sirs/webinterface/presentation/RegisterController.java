package sirs.webinterface.presentation;


import common.sirs.ws.UserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sirs.webinterface.domain.User;
import sirs.webinterface.domain.UsersManager;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {
    private static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String registerForm(Model model) {
        model.addAttribute("user", new UserView());

        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerSubmit(UserView userData) {
        if(!UsersManager.getInstance().userExists(userData.getUsername())){
            User user = new User(userData.getUsername(), userData.getPassword());
            UsersManager.getInstance().addUser(user);
        }

        System.out.println("registered User");
        return "redirect:/login";
    }
}
