package org.example.fintect.user;

import lombok.RequiredArgsConstructor;
import org.example.fintect.customer.Status;
import org.example.fintect.customer.utils.CustomerEndPointUtils;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.user.utils.UsersEndPointUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(UsersEndPointUtils.GET_USER)
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam(required = false) String name,
                                                   @RequestParam (required = false) String email,
                                                   @RequestParam (required = false) String ContactNumber,
                                                   @RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return new  ResponseEntity<>(userService.fetchAllUsers(name,email,ContactNumber,page,size), HttpStatus.OK);
    }
}
