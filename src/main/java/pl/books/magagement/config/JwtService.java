package pl.books.magagement.config;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import pl.books.magagement.model.entity.UserEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final KeyFactory keyFactory;

    private static final String privateKeyFilePath = "rsa/private_key.pkcs8";
    private static final String publicKeyFilePath = "rsa/public_key.x509";
    private static final String audience = "books";
    private static final String issuer = "books";

    public JwtService() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        keyFactory = KeyFactory.getInstance("RSA");

        privateKey = getPrivateKey();
        publicKey = getPublicKey();
    }

    private void generatePairs() throws IOException, NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privKey = keyPair.getPrivate();
        PublicKey pubKey = keyPair.getPublic();

        byte[] privateKeyBytes = privKey.getEncoded();

        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);
        Files.write(Paths.get(privateKeyFilePath), privateKeyBase64.getBytes());

        byte[] publicKeyBytes = pubKey.getEncoded();

        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
        Files.write(Paths.get(publicKeyFilePath), publicKeyBase64.getBytes());
    }

    private byte[] getKeyBytes(String filePath) throws IOException {

        InputStream keyFileInputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        byte[] keyBytes = keyFileInputStream.readAllBytes();

        return Base64.getDecoder().decode(keyBytes);
    }

    private PrivateKey getPrivateKey() throws IOException, InvalidKeySpecException {

        byte[] keyData = getKeyBytes(privateKeyFilePath);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyData);

        return keyFactory.generatePrivate(spec);
    }

    private PublicKey getPublicKey() throws IOException, InvalidKeySpecException {

        byte[] keyData = getKeyBytes(publicKeyFilePath);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyData);

        return keyFactory.generatePublic(spec);
    }

    private JwtParser getParser(){

        return Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .requireAudience(audience)
            .requireIssuer(issuer)
            .build();
    }

    private Claims getTokenClaims(String token){

        return getParser()
            .parseClaimsJws(token)
            .getBody();
    }

    public void verifyToken(String token) throws IllegalArgumentException{

        try {
            getParser().parse(token);
        }
        catch (RuntimeException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String getUsername(String token){

        return (String) getTokenClaims(token)
            .get("username");
    }

    public UUID getTokenId(String token){

        String rawId = getTokenClaims(token).getId();

        return UUID.fromString(rawId);
    }

    public UUID getUserId(String token){

        String rawId = getTokenClaims(token).getSubject();

        return UUID.fromString(rawId);
    }

    public LocalDateTime getExpirationTime(String token){

        Date rawExpirationTime = getTokenClaims(token).getExpiration();

        return LocalDateTime.ofInstant(rawExpirationTime.toInstant(), ZoneId.systemDefault());
    }

    public String generateAccessToken(UserEntity user){

        Map<String, Object> claims = new HashMap<>();

        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());

        return generateToken(user, accessTokenExpiration, claims);
    }

    public String generateRefreshToken(UserEntity user){

        return generateToken(user, refreshTokenExpiration, new HashMap<>());
    }

    private String generateToken(UserEntity user, long tokenExpiration, Map<String, Object> claims){

        Date actualDate = new Date();
        Date expirationDate = new Date(actualDate.getTime() + tokenExpiration);

        return Jwts.builder()
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setSubject(user.getId().toString())
            .setAudience(audience)
            .setIssuer(issuer)
            .setIssuedAt(actualDate)
            .setExpiration(expirationDate)
            .signWith(privateKey)
            .compact();
    }
}
