package web.entities;

import web.annotation.Column;
import web.annotation.ManyToOne;
import web.annotation.TableDB;
import lombok.Data;

@TableDB(name = "user")
@Data
public class User extends Entity{
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "sername")
    private String sername;
    @Column(name = "name")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role_id")
    private Integer roleId;
    @ManyToOne(foreignTable ="role", foreignColumnDB ="role_id")
    private Role role; //role_id
}
