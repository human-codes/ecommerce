package uz.pdp.salemartpro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.entity.Role;
import uz.pdp.salemartpro.entity.enums.RoleName;
import uz.pdp.salemartpro.entity.User;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("username",user.getUsername());
        claims.put("roles",user.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.joining(",")));

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .signWith(signKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .compact();
        return token;
    }

    private SecretKey signKey() {
        return Keys.hmacShaKeyFor(
                "01234567890123456789012345678901".getBytes());
    }


    public boolean isValid(String token) {
        try{
            getClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public List<Role> getRoles(String token) {
        Claims claims = getClaims(token);
        String strRoles = (String) claims.get("roles");

        return Arrays.stream(strRoles.split(","))
                .map(roleName -> new Role(null, RoleName.valueOf(roleName)))
                .collect(Collectors.toList());
    }

}
