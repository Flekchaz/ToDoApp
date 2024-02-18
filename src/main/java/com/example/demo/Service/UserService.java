package com.example.demo.Service;

import com.example.demo.Entity.DTO.UserRequestDto;
import com.example.demo.Entity.DTO.UserResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Utils.MappingProfile;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepo userRepo;

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message){
            super(message);
        }

    }

    public class DataNotValidException extends RuntimeException{
        public DataNotValidException(String message){
            super(message);
        }
    }
    public List<UserResponseDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(MappingProfile::mapToUserDto).toList();
    }

    public UserResponseDto createUser(UserRequestDto userDto) {
        if (!userDto.isValid()){
            throw new InvalidRequestStateException("Invalid email");
        }

        if(containsSpecialChar(userDto.getFirstName()) || containsSpecialChar(userDto.getLastName())){
            throw new DataNotValidException("First name or last name contains invalid characters");
        }
        var user = MappingProfile.mapToUserEntity(userDto);
        return MappingProfile.mapToUserDto(userRepo.save(user));
    }

    public Object getUserById(Long id) throws UserNotFoundException {
        //var user = userRepo.findById(id).orElseThrow(() -> new Exception("User not found"));
        var user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new Object() {
            public Long id = user.getId();
            public String fullName = user.getLastName().toUpperCase() + ", " + user.getFirstName();
            public String email = user.getEmail();

            public List<Object> tasks = Collections.singletonList(user.getTasks().stream().map(task -> new Object() {
                public Long id = task.getId();
                public String title = task.getTitle();
                public String description = task.getDescription();
                public String status = task.getStatus();
                public String dueDate = task.getDueDate().toString();
                public String createdAt = task.getCreatedAt().toString();
                public String updatedAt = task.getUpdatedAt().toString();
            }).toList());
        };
    }

    public UserResponseDto addUser(UserRequestDto userDto) {
        var user = MappingProfile.mapToUserEntity(userDto);
        return MappingProfile.mapToUserDto(userRepo.save(user));
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userDto) throws Exception {
        var user = userRepo.findById(id).orElseThrow(() -> new Exception("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        return MappingProfile.mapToUserDto(userRepo.save(user));
    }

    public void deleteUser(Long id) throws UserNotFoundException {
       /*try {
            Object userObject = getUserById(id);

            if (userObject instanceof User) {
                User userToDelete = (User) userObject;
                userRepo.delete(userToDelete);
            } else {
                throw new UserNotFoundException("This id is not found"+id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        var user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"+id));
        userRepo.delete(user);
    }

    private boolean containsSpecialChar(String input){

        String specialCharactersRegex = "[!@#$%^&*(),.?\":{}|<>]";
        return input.matches(".*" + specialCharactersRegex + ".*");

    }




}