package dev.luanfernandes.domain.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.luanfernandes.domain.request.TokenRequest;
import dev.luanfernandes.domain.response.TokenResponse;
import dev.luanfernandes.domain.response.UserResponse;
import dev.luanfernandes.webclient.request.KeyCloakTokenRequest;
import dev.luanfernandes.webclient.response.KeyCloakAttributes;
import dev.luanfernandes.webclient.response.KeyCloakTokenResponse;
import dev.luanfernandes.webclient.response.KeyCloakUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = SPRING)
public interface KeycloakMapper {

    TokenResponse map(KeyCloakTokenResponse value);

    KeyCloakTokenRequest map(TokenRequest tokenRequest);

    @Mapping(target = "name", source = "firstName")
    @Mapping(target = "address", source = "attributes", qualifiedByName = "mapAddress")
    @Mapping(target = "phone", source = "attributes", qualifiedByName = "mapPhone")
    UserResponse map(KeyCloakUserResponse value);

    @Named("mapAddress")
    default String mapAddress(KeyCloakAttributes attributes) {
        return attributes != null && attributes.address() != null
                ? attributes.address().getFirst()
                : null;
    }

    @Named("mapPhone")
    default String mapPhone(KeyCloakAttributes attributes) {
        return attributes != null && attributes.phone() != null
                ? attributes.phone().getFirst()
                : null;
    }
}
