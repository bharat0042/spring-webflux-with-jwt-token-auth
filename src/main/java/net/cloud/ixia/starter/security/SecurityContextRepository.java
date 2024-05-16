package net.cloud.ixia.starter.security;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@AllArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {
  private JwtUtils jwtUtils;

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    return Mono.error(new UnsupportedOperationException("Not to be saved!!"));
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authHeader != null) {
      String authType = authHeader.split(" ")[0];
      String authValue = authHeader.split(" ")[1];
      if (authType.equals("Bearer") && this.jwtUtils.validateToken(authValue)) {
        return Mono.just(this.jwtUtils.assembleUser(authValue))
            .flatMap(
                userMono ->
                    Mono.just(
                        new SecurityContextImpl(
                            new UsernamePasswordAuthenticationToken(
                                userMono, null, List.of(new SimpleGrantedAuthority("Basic"))))));
      }
    }
    return Mono.empty();
  }
}
