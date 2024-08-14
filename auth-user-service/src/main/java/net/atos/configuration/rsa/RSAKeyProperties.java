package net.atos.configuration.rsa;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Data
@Component
public class RSAKeyProperties {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;
    private final String keyId;


    public RSAKeyProperties() {
        KeyPair pair = KeyGeneratorUtility.generateRSAKey();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
        this.keyId = UUID.randomUUID().toString();
    }
}
