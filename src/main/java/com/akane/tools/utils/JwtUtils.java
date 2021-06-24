package com.akane.tools.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT处理
 *
 * @author akane
 */
@Slf4j
public class JwtUtils
{
    /**
     * 解析jwt
     * @param jsonWebToken
     * @return
     */
    public static DecodedJWT parseJWT(String jsonWebToken)
    {
        try {
            DecodedJWT decodeToken = JWT.decode(jsonWebToken);
            return decodeToken;
        } catch (Exception ex) {
            log.error("Parse token error, -{}", ex.getMessage());
            return null;
        }
    }


}
