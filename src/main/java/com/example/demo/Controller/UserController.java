package com.example.demo.Controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.demo.Entity.DTO.UserRequestDto;
import com.example.demo.Entity.DTO.UserResponseDto;
import com.example.demo.Entity.Task;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//import javax.validation.Valid;

@RestController
@AllArgsConstructor

@RequestMapping("/api/v1/users")
//@RequestMapping(value = "/api/v1/users", produces = "application/json", consumes = "application/json")
public class UserController {
    private final UserService userService;
    // add CRUD methods
    // add ExceptionHandling
    // create custom exceptions NotFoundException, DataNotValidException, etc.
    // BONUS : Add Swagger documentation (OpenAPI)!

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {

        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto userDto) {
        try {
            return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
        } catch (UserService.DataNotValidException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Can't create a user");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserRequestDto userDto){


        try {
            UserResponseDto userUpdated = userService.updateUser(userId, userDto);
            return ResponseEntity.ok(userUpdated);
        } catch (UserService.UserUpdateException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for the given ID");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
    }

    @PatchMapping("/updateExisting/{userId}")
    public ResponseEntity<?> updateExistingUser(@PathVariable Long userId, @RequestBody UserRequestDto userRequestDto){
        try {
            UserResponseDto userUpdated = userService.updateExistingUser(userId, userRequestDto);
            return ResponseEntity.ok(userUpdated);
        } catch (UserService.UserUpdateExistingUserException e) {

            return ResponseEntity.notFound().build();
        } catch (Exception  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (UserService.UserDeleteException e) {
            return ResponseEntity.notFound().build();

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user.");
        }
    }


}
