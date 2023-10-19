package com.project.projectboard.config;

import com.project.projectboard.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext()) // SecurityContextHolder는 시큐리티에 대한 모든 정보를 들고있는 class
                .map(SecurityContext::getAuthentication)  // authentication 정보 획득
                .filter(Authentication::isAuthenticated) // 인증 되었는지 확인
                .map(Authentication::getPrincipal)  // 인증 정보가 들어있음 (Object)
                .map(BoardPrincipal.class::cast) // 타입 캐스팅
                .map(BoardPrincipal::getUsername);
    }
}
