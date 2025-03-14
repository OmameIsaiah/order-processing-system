package com.order.processing.system.account.service.model;

import com.order.processing.system.account.service.enums.OnboardingStage;
import com.order.processing.system.account.service.enums.UserType;
import com.order.processing.system.account.service.model.listener.EntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.order.processing.system.account.service.utils.AppMessages.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(EntityListener.class)
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Basic(optional = false)
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "last_modified")
    @UpdateTimestamp
    private LocalDateTime lastModified;
    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @Column(name = "name")
    @Size(max = 100, message = MAX_NAME_LIMIT_EXCEEDED)
    private String name;
    @Column(name = "email", unique = true, nullable = false)
    @Email(message = INVALID_EMAIL,
            flags = Pattern.Flag.CASE_INSENSITIVE,
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = EMPTY_EMAIL)
    @NotNull(message = NULL_EMAIL)
    private String email;
    @Column(name = "password")
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String password;
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(name = "user_token")
    private String userToken;
    @Column(name = "onboarding_stage")
    @Enumerated(EnumType.STRING)
    private OnboardingStage onboardingStage = OnboardingStage.STARTED;
    @Column(name = "verified")
    private Boolean verified = false;
    @Column(name = "is_online")
    private Boolean isOnline = false;
    @Column(name = "otp_code")
    private String otpCode;
    @Column(name = "otp_expire_time")
    private String otpExpireTime;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "userrole")
    private List<UserRole> userRoles;
}
