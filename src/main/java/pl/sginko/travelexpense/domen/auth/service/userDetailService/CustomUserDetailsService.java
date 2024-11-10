package pl.sginko.travelexpense.domen.auth.service.userDetailService;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domen.auth.entity.Roles;
import pl.sginko.travelexpense.domen.auth.entity.UserEntity;
import pl.sginko.travelexpense.domen.auth.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRoles().name());
//
//        return new User(
//                user.getEmail(),
//                user.getPassword(),
//                List.of(authority));
////                List.of(new SimpleGrantedAuthority(user.getRoles())));
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = findUserByEmail(email);
        return buildUserDetails(user);
    }

    private UserEntity findUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private UserDetails buildUserDetails(UserEntity userEntity) {
        List<GrantedAuthority> authorities = mapRolesToAuthorities(userEntity.getRoles());
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities
        );
    }

    private List<GrantedAuthority> mapRolesToAuthorities(Roles roles) {
        return List.of(new SimpleGrantedAuthority(roles.name()));
    }
}
