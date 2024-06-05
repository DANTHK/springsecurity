package top.shang.springsecurity.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.shang.springsecurity.utils.RsaUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author shangzp
 * @since 2022/10/19 14:42
 */
@Data
@Component
@ConfigurationProperties(prefix = "rsa.key")
public class RsaKeyProperties {
    private String publicKeyPath;

    private String privateKeyPath;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @PostConstruct
    public void createKey() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(privateKeyPath);
    }
}
