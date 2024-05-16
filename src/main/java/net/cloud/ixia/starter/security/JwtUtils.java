package net.cloud.ixia.starter.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cloud.ixia.starter.properties.IxiaProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@AllArgsConstructor
public class JwtUtils {
  private IxiaProperties ixiaProperties;

  public boolean validateToken(String token) {
    try {
      byte[] publicKeyByteArr = Base64.getDecoder().decode(ixiaProperties.getPubKey());
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      RSAPublicKey rsaPublicKey =
          (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteArr));
      Claims claims =
          Jwts.parser().verifyWith(rsaPublicKey).build().parseSignedClaims(token).getPayload();

      if (claims.getExpiration() != null
          && claims.getExpiration().after(Date.from(Instant.now()))) {
        return true;
      } else {
        throw new RuntimeException("Token Expired");
      }
    } catch (Exception e) {
      log.error("Error validating token , " + e.getMessage());
      return false;
    }
  }

  public Mono<User> assembleUser(String token) {
    return Mono.just(token)
        .map(
            token1 -> {
              JsonObject payload =
                  new JsonParser()
                      .parse(new String(Base64.getDecoder().decode(token.split("\\.")[1])))
                      .getAsJsonObject();
              return User.builder()
                  .firstName(payload.get("firstName").getAsString())
                  .lastName(payload.get("lastName").getAsString())
                  .userName(payload.get("userName").getAsString())
                  .uuid(payload.get("uuid").getAsString())
                  .build();
            })
        .doOnError(ex -> log.error("Error validating token", ex));
  }
}
