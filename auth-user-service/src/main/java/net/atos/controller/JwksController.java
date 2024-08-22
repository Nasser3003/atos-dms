package net.atos.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import net.atos.configuration.rsa.RSAKeyProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class JwksController {

    private final RSAKey rsaKey;

    public JwksController(RSAKeyProperties keyProperties) {
        this.rsaKey = new RSAKey.Builder(keyProperties.getPublicKey())
                .keyID(keyProperties.getKeyId())
                .build();
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return jwkSet.toJSONObject();
    }
}