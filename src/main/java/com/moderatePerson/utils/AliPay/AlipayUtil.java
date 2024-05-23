package com.moderatePerson.utils.AliPay;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayUtil {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "9021000137624320";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPyqe5Ot9gSq1dyDSNRqiTxMSsec2wY4kHeHlZg+E0CwMwvOYRa3Ai7E1mdIHCF1U4VGWMzNeW4b87MsPYVnwTdy06nOSbwO9l593IEb2KRRT9gPZx7NdaepZRY9Y3rSB3+L6L4Ie4ut4C278/eDhlloMmPWVl6I05MNNT+HEB0lHX1ZIHdZsq/gmh3n8/k6NGPYbS7zy0Zmjth2HnWf6Y9A7ZZCZlkE4bUF2YdYucePHPPRO+JbdFUUdOS9xIAwfN8r73rEDd3JV7sUC3DTbNA6sk4I4dVMEyoeAhEwSKRAcamF4IchH8EZXWa6Gr1nxnJQUGC6n0evnOKD+aC/RLAgMBAAECggEAGDu6OqBuZwcO4sBMAwdGf+jokHzSqoiL3oNySCDOwnucpQkg6QynUGtsu/ikqbZO+XhXyPv41YfkN7DdisdAj/NE3xzz9j/02MLUANTkkboEyJiEcDzPKoOAc+UUhDucPjvguSg1F2/+3JJ2T9cOAij0n2w3e45OxrWw1/UF7p1n05n+nPr15hO6/+V0mWedoaJ/lt85L/bPk376n+KDO/JUvKD602OwFUE30VUgk1llOOp47QBsDM85G+r8ZXWtyO906b3Si7Yue98Oun5leFZOPCF5NbH9KJAjmxh4pmEYSbIC2898UfA0VabLu/nf0k0qUTYbuAgmNWA79B43kQKBgQDGIh3uYvEvbf2JKlQOnBJGDKf0ckojwCSqbiYVPFcCE15QjBfwiyXWe2Gdv86P5rWTXheG7LpA3k9YbpZCuNjeORFy2tg6oiteGS7CB71RJ6y65xVB45javDsb5CMCb6CKz2mvRFh1UtcwipbORpS+6+Hiwz2+t2nYstmjqd1dEwKBgQC5yY/QifEspaufXyuVokWcVSPJRwO1VMUI0m/USP02XPzA2jnXyea8fNWTlj+0YWtfUMYzJ6geLpAhp0Ta7wzKVE0CMZQdvxIqPeBILkcdDBesNEBoZ+xGo1Yk4m0PFk9tTWx2TwsrSb8Da1O9MkZDuU9PonMh9VWXXxGqscKK6QKBgGE6HLX+l5xOvTuyKc2BiPYW/9RUwU0J3PRFWSrSkV/TYVTlaln/7nFx2lUu6o5zCGdopOLxB/UNIqKiodncG4/xMT7LZZBgRCfI88kDQ0Ov1HNjMmtC5J9x/w+QxB1N6do0Lghz4XSxUiff2mEfAZ8u4YoFhScxmh9cpLBLivVBAoGBAInAOTGWFYvVCnsQRXCc8KAJQI9saAN65tyzqtbrh1NXftA+/cRRnxMCPBlBvdowoTv61/n13WEDhTpum075D7K07qRBvmCP2xgW6MScC34uCR9VXqwK4dU+JhETEWmuURXp4hGWaeGevFH8sgoFzLNKItQYzuTO0rd5dc/qHSSBAoGAa5adgaHMnNNl8HppKdGVcCig4WKqcxlnuE6QVaACH/A4XclrTrLQfN7EDcr4XGBiwgeyqhY6rF+hQVuzJS68/AwhENbhntoTuEMZFiccW1vvWtLst6eH0h10orlo7Fvq2ZdD4W2mWejK1MAZoCSftjWmGmdxeaFFgS43KOVEgkQ=";

    // 支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnkV8Xbxydt1Metjz6/xKkK5C2PUPaVGrmykvcvgP0m8+s8ILpzGZtzZwxyT75Ru6veGi+iLKnNZgMa/eTBGsQoa6eLJvA9mWjCrIbXPpi71DIQYftD+L76h2+ZF1cOzFSL1BgCuBXTxtwXAecE2/nbY6BfYylmiWT4B5NYQVB1HPfw66sF8NT/r8Jpg5HqR7iWRf1K+j1eGK8XNYgQjvI89B5lC5hcxaPAY+sYg4r3YXs8ev+nK1pCCoxfMMMQkCRDm8FvrmY2n82VdGLNbIeum6mZ1WPz46PPdXdso7O0BdXhefNmp7FJgxY+DUodA6SHp+gVMi2dq8Exws/cXsnwIDAQAB";

    // 服务器异步通知页面路径
    //需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url =  "http://xe9che.natappfree.cc/item/notifyUrl";
    // 页面跳转同步通知页面路径
    //需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://xe9che.natappfree.cc/item/notifyUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关,注意这些使用的是沙箱的支付宝网关，与正常网关的区别是多了dev
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
