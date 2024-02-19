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
import java.util.Objects;
import java.util.SplittableRandom;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepo userRepo;


    public class UserRetrievalException extends RuntimeException {
        public UserRetrievalException(String message){
            super(message);
        }
    }
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

    public class UserUpdateException extends RuntimeException{
        public UserUpdateException(String message){
            super(message);
        }
    }

    public class UserUpdateExistingUserException extends RuntimeException{
        public UserUpdateExistingUserException(String message){
            super(message);
        }
    }

    public class UserDeleteException extends RuntimeException {
        public UserDeleteException(String message){
            super(message);
        }
    }
    public List<UserResponseDto> getAllUsers() throws UserRetrievalException {

        try {
            return userRepo.findAll()
                    .stream()
                    .map(MappingProfile::mapToUserDto).toList();
        } catch (UserRetrievalException e) {
            throw new UserRetrievalException("Error retrieving users from the repository");
        }
    }

    public UserResponseDto createUser(UserRequestDto userDto) throws DataNotValidException {
        if (!userDto.isValid()){
            throw new DataNotValidException("Invalid email");
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

            public List<Object> tasks = Collections.singletonList(user.getTasks().
                    stream().map(task -> new Object() {
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


    public UserResponseDto updateUser(Long id, UserRequestDto userDto) throws UserUpdateException {
        var user = userRepo.findById(id).orElseThrow(() -> new UserUpdateException("An error has occurred while updating user"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        return MappingProfile.mapToUserDto(userRepo.save(user));
    }
    public UserResponseDto updateExistingUser(Long id, UserRequestDto userDto) throws UserUpdateExistingUserException {
        var user = userRepo.findById(id).orElseThrow(() -> new UserUpdateExistingUserException("An error has occurred while updating user"));
        //user.setFirstName(userDto.getFirstName());
        //user.setLastName(userDto.getLastName());
        //user.setEmail(userDto.getEmail());
        if (isChanged(user, userDto)){


        if (userDto.getFirstName()!=null){
            user.setFirstName(userDto.getFirstName());
        }

        if (userDto.getLastName()!=null){
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail()!=null){
            user.setEmail(userDto.getEmail());
        }
        return MappingProfile.mapToUserDto(userRepo.save(user));
    }
        else {
            return MappingProfile.mapToUserDto(user);
    }  }
    public void deleteUser(Long id) throws UserDeleteException {
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

        var user = userRepo.findById(id).orElseThrow(() ->
                new UserDeleteException("An error has occurred while deleting user" + +id));
        userRepo.delete(user);
    }

    private boolean isChanged(User existingUser,UserRequestDto userDto){
        return !Objects.equals(existingUser.getFirstName(),userDto.getFirstName())
                || Objects.equals(existingUser.getLastName(), userDto.getLastName())
                || Objects.equals(existingUser.getEmail(), userDto.getEmail());

    }

    private boolean containsSpecialChar(String input){

        String specialCharactersRegex = "[!@#$%^&*(),.?\":{}|<>]";
        return input.matches(".*" + specialCharactersRegex + ".*");

    }




}