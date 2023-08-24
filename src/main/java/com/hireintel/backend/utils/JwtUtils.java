package com.hireintel.backend.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.billx.backend.beans.BillxToken;
import com.billx.backend.beans.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    private static final String HS_512_ALGORITHM = "HS512";

    public BillxToken decodeJwtToken(String jwtToken) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);

            DecodedJWT decodedToken = JWT.decode(jwtToken);
            if (decodedToken.getAlgorithm().equals(HS_512_ALGORITHM) && decodedToken.getClaims() != null) {
                JWTVerifier verifier = getJwtVerifier(algorithm);
                DecodedJWT verifiedJwt = verifier.verify(jwtToken);
                if (verifiedJwt == null) {
                    throw new Exception("Invalid JWT");
                }
                return buildUserFromClaim(decodedToken.getClaims());

            } else {
                throw new Exception("Invalid JWT");
            }

        } catch (Exception e) {
            throw new Exception("Invalid JWT");
        }
    }

    private BillxToken buildUserFromClaim(Map<String, Claim> claims) {
        BillxToken token = new BillxToken();
        token.setId(claims.get("id").asLong());
        return token;
    }


    public String getTokenForUser(User user) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        String jwtToken = JWT.create()
                .withClaim("id", user.getId())
                .withClaim("time_stamp", timeFormat.format(new Date()))
                .sign(algorithm);
        return jwtToken;
    }

    private JWTVerifier getJwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }
}