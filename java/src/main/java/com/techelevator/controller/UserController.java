package com.techelevator.controller;

import com.techelevator.dao.JdbcImageDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Image;
import com.techelevator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserDao userDao;

    public UserController(UserDao userDao){
        this.userDao = userDao;
    }

    @Autowired
    private JdbcImageDao jdbcImageDao;


    @RequestMapping(path = "/all",method = RequestMethod.GET)
    public List<User> getUsers(){
        return userDao.getUsers();
    }

    @RequestMapping(path = "/{id}",method = RequestMethod.GET)
    public User getUserById(@PathVariable int id){
        return  userDao.getUserById(id);
    }
    @RequestMapping(method = RequestMethod.GET)
    public User getUserByEmail(@RequestParam("email") String email){
        return  userDao.getUserByEmail(email);
    }

    @RequestMapping(path="/profile/{username}", method=RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username){
        return userDao.getUserByUsername(username);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path="/avatar" ,method = RequestMethod.PUT)
    public User updateUserAvatar(
            @RequestParam(value = "image", required = false) MultipartFile file,
            Principal principal
    ){
        User currentUser = userDao.getUserByUsername(principal.getName());

        if (currentUser != null) {
            if (file != null) {
                String contentType = file.getContentType();

                if (null == contentType)
                    throw new IllegalArgumentException("Unsupported content type");

                String imageType = contentType.split("/")[1];

                String avatarId = jdbcImageDao.createUserAvatar(file, imageType);

                String path = String.format("avatar-%s.%s", avatarId, imageType);
                currentUser.setAvatar(path);

                userDao.updateUser(currentUser, currentUser.getId());
            }
        }

        return userDao.getUserByUsername(principal.getName());
}

    @RequestMapping(path="/{id}", method= RequestMethod.DELETE)
    public void deleteUserById(@PathVariable int id){
        userDao.deleteUserById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path="/update", method=RequestMethod.PUT)
    public void updateUser(@RequestBody User user, Principal principal){
        int currentUserId = userDao.getUserByUsername(principal.getName()).getId();
        userDao.updateUser(user, currentUserId);
    }
}
