package com.example.security1.config.auth;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingURL("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행이 된다.
@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepository;

    // 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null) {
            System.out.println("==================== 로그인 성공 ====================");
            System.out.println("userEntity.getUsername() = " + userEntity.getUsername());
            System.out.println("userEntity.getPassword() = " + userEntity.getPassword());
            return new PrincipalDetails(userEntity);
        }
        System.out.println("==================== 로그인 실패 ====================");
        return null;
    }
}
