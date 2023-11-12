package gamesystem.services.impl;

import gamesystem.dtos.UserDto;
import gamesystem.models.Game;
import gamesystem.models.Order;
import gamesystem.models.User;
import gamesystem.repositories.UserRepository;
import gamesystem.services.api.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private Validator validator;
    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.getValidator();
        this.modelMapper = new ModelMapper();
    }

    private void getValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public void registerUser(User user, String confirmPassword) {
        if (checkIfUserExist(user.getEmail())) {
            throw new IllegalArgumentException("Use already exist!");
        }
        if (!user.getPassword().equals(confirmPassword)) {
            throw new IllegalArgumentException("Password do not match!");
        }
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (constraintViolations.size() == 0) {
            this.userRepository.save(user);
        } else {
            for (ConstraintViolation<User> constraintViolation : constraintViolations) {
                System.out.println(constraintViolation.getMessage());
            }
        }
    }

    @Override
    public boolean checkIfUserExist(String email) {
        User user = this.userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        UserDto userDto = new UserDto();
        this.modelMapper.map(user, userDto);
        return userDto;
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void buyItems(Collection<Game> product, String email) {
        User user = this.userRepository.findByEmail(email);
        user.setGames(product);
        this.userRepository.save(user);
    }

}
