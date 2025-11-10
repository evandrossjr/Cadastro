package com.essjr.Cadastro.AppUser.Mapper;

import com.essjr.Cadastro.AppUser.AppUser;
import com.essjr.Cadastro.AppUser.dtos.AppUserDTO;

public class AppUserMapper {

    public static AppUserDTO toDTO(AppUser appUser) {
        if (appUser == null) {
            return null;
        }

        return new AppUserDTO(
                appUser.getId(),
                appUser.getName(),
                appUser.getEmail(),
                appUser.getRole());
    }

    public static AppUser toEntity(AppUserDTO appUserDTO) {
        if (appUserDTO == null) {
            return null;
        }

        return new AppUser(appUserDTO.id(), appUserDTO.name(), appUserDTO.email(), appUserDTO.role());
    }
}
