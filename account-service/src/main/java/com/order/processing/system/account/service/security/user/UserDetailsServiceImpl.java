package com.order.processing.system.account.service.security.user;

import com.order.processing.system.account.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.order.processing.system.account.service.utils.AppMessages.EMAIL_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsImpl.build(
                userRepository.findUserByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format(EMAIL_NOT_FOUND, username)))
        );
    }

}
