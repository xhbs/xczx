package com.xuecheng.auth.test1;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test01 {

    //生成一个jwt令牌
    @Test
    public void testCreateJwt() {
        //证书文件
        String key_location = "xc.keystore";
        //密钥库密码
        String keystore_password = "xuechengkeystore";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "xuecheng";
        //密钥别名
        String alias = "xckey";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "123");
        tokenMap.put("name", "mrt");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token=" + token);
    }

    //资源服务使用公钥验证jwt的合法性，并对jwt解码
    @Test
    public void testVerify() {
        //jwt令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6InN1cGVyIiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiLotoXnuqfnrqHnkIblkZgiLCJ1dHlwZSI6IjEwMTAwMyIsImlkIjoiNDYiLCJleHAiOjE1ODUwNzkwMDksImF1dGhvcml0aWVzIjpbInhjX3N5c21hbmFnZXJfZG9jIiwieGNfc3lzbWFuYWdlcl9yb2xlIiwieGNfc3lzbWFuYWdlcl91c2VyX3ZpZXciLCJ4Y19zeXNtYW5hZ2VyX3JvbGVfZWRpdCIsInhjX3N5c21hbmFnZXJfdXNlcl9hZGQiLCJ4Y19zeXNtYW5hZ2VyX21lbnUiLCJ4Y19zeXNtYW5hZ2VyX2NvbXBhbnkiLCJ4Y19zeXNtYW5hZ2VyX3VzZXJfZGVsZXRlIiwieGNfc3lzbWFuYWdlcl9yb2xlX2FkZCIsInhjX3N5c21hbmFnZXJfcm9sZV9wZXJtaXNzaW9uIiwieGNfc3lzbWFuYWdlcl91c2VyIiwiY291cnNlX2dldF9iYXNlaW5mbyIsInhjX3N5c21hbmFnZXIiLCJ4Y19zeXNtYW5hZ2VyX2xvZyIsInhjX3N5c21hbmFnZXJfdXNlcl9lZGl0IiwieGNfc3lzbWFuYWdlcl9tZW51X2VkaXQiLCJ4Y19zeXNtYW5hZ2VyX21lbnVfYWRkIiwieGNfc3lzbWFuYWdlcl9tZW51X2RlbGV0ZSIsInhjX3N5c21hbmFnZXJfcm9sZV9kZWxldGUiXSwianRpIjoiYTM0NTNkMjEtZDg3Yy00MGIxLTk1NjgtNmIxMDQ2MGYwODk4IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.Vo1Rs7ARoNFmobwZiWP7DKjfelii9O2y9Gs2vsDzzxDikJg6fViHf-PZws72VhRZtUjo1ZeeXz68VIrPbDaAA5tIjorCSn00UxnmTl3003BP-BWP8JIR52mPv9YLUc8qiwOvC4jtwSNnyM65QUpNef4HAgMTjGhBTlLt4N9ZTYQ6jitrJbvwNI756x4o6tGGt6a3h_sUjRIbh34DoM1wTkLNhe4cZEuzBDm3I-FBMOzx8ZTi1IhWtxzjnLSeoAYPEyPXt1q4vu45WC7MBkaYe1XGLTrf-Mi-4G-OrMtfBCcO5zt_xYXsdc-lD5EvvDplUFThmiRx5lI8gjYt_Wr0lw";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo7C8OTVhUZYE8l1loHznIMU7Q+Ok8niHCwWDcQFqMitvHjJuhtmyGYP9+aTBz7hPkL0XXqrXU86/WdSSF5WhnVvulZW2qN8xxRwxeHOpLSn4CDMLsMkafxRUJMon/FY0jLh+jOhszddQ1pX53NZ/DCiX0QhRtqQWgwmsRonXQDZOPkVyPmgsA7pYS1i3jv3PMSkx0r2QScf7eGNU4QfMNIl+fE+QkXkE4BZmyrrow6SGQo9rr+YF2ZYchbPCNao0uajLNy8bnnnyOT+IcwgImutEFXRbpAS9HZdAtrqwqrFm6XiDKnfscdPKCCTWQMp+EjZ1Os2+NH+Mnhpmr8RyGwIDAQAB-----END PUBLIC KEY-----";
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        //定义key
        String key = "user_token:9734b68f-cf5e-456f-9bd6-df578c711390";
        //定义Map
        Map<String, String> mapValue = new HashMap<>();
        mapValue.put("id", "101");
        mapValue.put("username", "itcast");
        String value = JSON.toJSONString(mapValue);
        //向redis中存储字符串
        stringRedisTemplate.boundValueOps(key).set(value, 60, TimeUnit.SECONDS);
        //读取过期时间，已过期返回-2
        Long expire = stringRedisTemplate.getExpire(key);
        //根据key获取value
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testClient() {
        //采用客户端负载均衡，从eureka获取认证服务的ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = serviceInstance.getUri();
        String authUrl = uri + "/auth/oauth/token";
        //URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType
        // url就是 申请令牌的url /oauth/token
        //method http的方法类型
        //requestEntity请求内容
        //responseType，将响应的结果生成的类型
        //请求的内容分两部分
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        String httpbasic = httpbasic("XcWebApp", "XcWebApp");
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpbasic);
        //2、包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", "itcast");
        body.add("password", "123");
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<MultiValueMap<String, String>>(body, headers);
        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        //远程调用申请令牌
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }

    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
