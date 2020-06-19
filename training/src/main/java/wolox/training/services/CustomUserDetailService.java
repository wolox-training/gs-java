package wolox.training.services;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Users;
import wolox.training.repositories.UserRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private User response;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws RuntimeException {
        final Users user = userRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);

        response = new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(), new ArrayList<>());

        return response;
    }
}
