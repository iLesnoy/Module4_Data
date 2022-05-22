package com.epam.esm.gifts.security;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

public class OAuth2AuthorizedUser {

    private  String principalName;
    private  OAuth2AccessToken accessToken;
    private  OAuth2RefreshToken refreshToken;

}
