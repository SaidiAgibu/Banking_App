package com.saidi.banking_app.config;

import com.saidi.banking_app.models.User;
import com.saidi.banking_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmailIgnoreCase(username);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User with " + username + " not found");
        }

        return new  CustomUserDetails (user.get());
    }
}
