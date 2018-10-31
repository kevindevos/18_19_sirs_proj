package sirs.webinterface.presentation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import services.local.dataobjects.UserData;
import sirs.app.ws.cli.AppClient;
import sirs.app.ws.cli.AppClientException;

import java.util.Map;

@Controller
@RequestMapping(value = "/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String loginForm(Model model) {
        model.addAttribute("user", new UserData());

        return "login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String loginSubmit(UserData userData) {
        String logString = "loginSubmit: username:" + userData.getUsername()
                + ", password: " + userData.getPassword() ;

        logger.info(logString);

        try {
            AppClient appClient = new AppClient("http://localhost:8081/app-ws/endpoint");
            appClient.testPing(logString);
        } catch (AppClientException e) {
            e.printStackTrace();
        }

        return "redirect:/login";

    }

}
