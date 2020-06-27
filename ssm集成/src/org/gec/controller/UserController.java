package org.gec.controller;

import org.gec.pojo.Users;
import org.gec.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


/*org.springframework.beans.factory.BeanCreationException:
Error creating bean with name 'userController': Injection of autowired dependencies failed;
 nested exception is org.springframework.beans.factory.BeanCreationException:
  Could not autowire field: private org.gec.service.UserService org.gec.controller.UserController.userService;
  nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException:
  No qualifying bean of type [org.gec.service.UserService] found for dependency:
  expected at least 1 bean which qualifies as autowire candidate for this dependency. Dependency annotations:
  {@org.springframework.beans.factory.annotation.Autowired(required=true)}*/
@Controller
public class UserController {
    @Autowired(required=false)
    private UserService userService;

    @RequestMapping("/login")
    public String  login(String username, String password, HttpSession session){
        System.out.println("-------------login--------------");
        Users user = userService.login(username,password);
        //如果用户不为空，存入session
        if(user  != null){
            session.setAttribute("user_session",user);
            return "forward:toCinema";
        }else{
            return "login";
        }
    }
}
