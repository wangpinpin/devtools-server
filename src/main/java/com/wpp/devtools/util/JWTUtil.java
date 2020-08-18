package com.wpp.devtools.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.JWTConfig;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-07-06
 **/
@Slf4j
public class JWTUtil {

    /**
     * 解析jwt
     */
    public static JSONObject parseJWT(String token) {
        if (null == token || !token.startsWith(JWTConfig.JWT_BEARER)) {
            throw new CustomException(ExceptionCodeEnums.JWT_VALID_ERROR);
        }
        token = token.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(JWTConfig.JWT_BASE64SECRET))
                    .parseClaimsJws(token).getBody();
            if (null == claims) {
                throw new CustomException(ExceptionCodeEnums.JWT_VALID_ERROR);
            }
            return JSONObject.parseObject(JSON.toJSONString(claims));
        } catch (ExpiredJwtException e) {
            throw new CustomException(ExceptionCodeEnums.JWT_VALID_OVERDUE);
        } catch (Exception e) {
            throw new CustomException(ExceptionCodeEnums.JWT_ERROR);
        }

    }

    /**
     * 创建JWT
     *
     * @param userId    用户ID
     * @param expMillis 秒
     * @return
     */
    public static String createJWT(String userId, long expMillis) {
        if (null == userId) {
            throw new CustomException(ExceptionCodeEnums.ID_NULL);
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWTConfig.JWT_BASE64SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim(JWTConfig.JWT_USER_ID_KEY, userId)
                .setIssuer(JWTConfig.JWT_NAME)
                .setAudience(JWTConfig.JWT_CLIENTID)
                .signWith(signatureAlgorithm, signingKey);

        //添加Token过期时间
        expMillis = nowMillis + expMillis * 1000;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);

        //生成JWT
        return JWTConfig.JWT_BEARER + builder.compact();
    }

    /**
     * 创建JWT
     *
     * @param userId
     * @return
     */
    public static String createJWT(String userId) {
        long expMillis = JWTConfig.JWT_EXPIRESSECOND;
        return createJWT(userId, expMillis);
    }

    public static void main(String[] args) {
        System.out.println(createJWT("67829ecb-c5a0-11ea-8431-00163e009fe8"));
    }

}