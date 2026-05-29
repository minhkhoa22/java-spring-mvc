package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ui.Model;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // homePage
    @RequestMapping("/")
    public String getHomePage(Model model) {
        return "hello";
    }

    // vieUserPage
    @RequestMapping(value = "/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users1", users);
        return "admin/user/show";
    }

    // getViewUserDetail
    @RequestMapping(value = "/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User userDetail = this.userService.getUserById(id);
        model.addAttribute("userDetail", userDetail);
        return "admin/user/detail";
    }

    // getViewCreateUser
    @RequestMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    // getViewUpdateUser
    @RequestMapping(value = "/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    // create user
    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String handleCreateUserPage(Model model, @ModelAttribute("newUser") User myLove) {
        this.userService.handleSaveUser(myLove);
        return "redirect:/admin/user";
    }

    // update user
    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User userUpdate) {
        User currentUser = this.userService.getUserById(userUpdate.getId());
        if (currentUser != null) {
            System.out.println("run here");
            currentUser.setAddress(userUpdate.getAddress());
            currentUser.setPhone(userUpdate.getPhone());
            currentUser.setFullName(userUpdate.getFullName());
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    // delete
    @GetMapping("/admin/user/delete/{id}")
    public String postDeleteUser(@PathVariable long id) {
        this.userService.deleteUser(id);
        return "redirect:/admin/user";
    }

}
