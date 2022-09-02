package com.example.swapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Data
public class CurrentUserDTO {

    // todo change fields as per dto

    @ApiModelProperty(
            value = "User ID",
            example = "2"
    )
    private Long id;

    @ApiModelProperty(
            value = "Login",
            example = "admin"
    )
    private String login;

    @JsonIgnore
    private String pwd;
    @ApiModelProperty(
            value = "Name of user",
            example = "Admin А.A."
    )
    private String name;

    @ApiModelProperty(
            value = "First Name",
            example = "Admin"
    )
    private String firstName;

    @ApiModelProperty(
            value = "Middle Name",
            example = "Adminovich"
    )
    private String middleName;

    @ApiModelProperty(
            value = "Last Name",
            example = "Adminov"
    )
    private String lastName;
    @ApiModelProperty(
            value = "Needs to change password",
            example = "false"
    )
    private Boolean changePassRequired;

    @ApiModelProperty(
            value = "E-mail",
            example = "admin@ibs.ru"
    )
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String email;

    @ApiModelProperty(
            value = "Password expiration date",
            example = "2022-08-20T04:00:00+00:00"
    )
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss[+00:00]"
    )
    private LocalDateTime expireDate;

    public CurrentUserDTO(Long id, String login, String pwd, String name, Boolean changePassRequired) {
        this.id = id;
        this.login = login;
        this.pwd = pwd;
        this.name = name;
        this.changePassRequired = changePassRequired;
    }
}
