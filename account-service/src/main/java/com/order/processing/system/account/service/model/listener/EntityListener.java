package com.order.processing.system.account.service.model.listener;

import com.order.processing.system.account.service.model.Role;
import com.order.processing.system.account.service.model.UserRole;
import com.order.processing.system.account.service.model.Users;
import com.order.processing.system.account.service.repository.UserRepository;
import com.order.processing.system.account.service.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.util.UUID;

@Component
@Slf4j
public class EntityListener {
    private static UserRepository userRepository;

    @Autowired
    public void setAccountsRepo(UserRepository userRepository) {
        EntityListener.userRepository = userRepository;
    }

    @PrePersist
    private void beforeCreate(Object data) {
        if (data instanceof Users) {
            Users users = (Users) data;
            users.setUuid(UUID.randomUUID().toString());
        } else if (data instanceof Role) {
            Role role = (Role) data;
            role.setUuid(UUID.randomUUID().toString());
        } else if (data instanceof UserRole) {
            UserRole userRole = (UserRole) data;
            userRole.setUuid(UUID.randomUUID().toString());
        }
    }

    @PostPersist
    private void afterCreate(Object data) {
        if (data instanceof Users) {
            Users users = (Users) data;
            users.setUserToken(SecurityUtils.encode(users.getEmail() + users.getId()));
            userRepository.save(users);
        }
    }
}
