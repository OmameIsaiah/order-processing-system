package com.order.processing.system.account.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.processing.system.account.service.enums.Permissions;
import com.order.processing.system.account.service.model.converters.PermissionsConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Role implements Serializable {
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
    @Column(unique = true, name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "permissions", columnDefinition = "jsonb", nullable = false, updatable = true)
    @Type(JsonBinaryType.class)
    //@JdbcTypeCode(SqlTypes.JSON)
    //@Convert(converter = PermissionsConverter.class)
    private Set<Permissions> permissions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleid")
    private List<UserRole> userRoles;
}
