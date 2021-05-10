package org.DB;

import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 两倍长的加密工具:结果与DES算法工具一致,与C写的工具互相加解密验证成功
 * @author 
 * @TODO 描述
 */
public class DESDOUtil {
    
      private static String mainKey="7955A92F09E34C3682E13665B4624DF5";
    
     public static void main(String[] args) {
    	 try {
    	     //加密
    	     //签到
//    	     System.out.println(encECB3Des("appid=TM000001&version=v1.0.1&simno=1111&imei=333&sn=86831021100001&mer=20191111111c", "B9F9B97117CE8ECEB9F9B97117CE8ECE"));
    	     //签字
//    	     System.out.println(encECB3Des("appid=TM000001&version=v1.0.1&simno=1111&imei=333&mer=20191111111&billid=xf003202002141556172CQ9mm&data=000001000000006A00000040000000807F0000480EE7EC73D5F9348E11C69F3F8F62E04DA72C61DE826948AA2FEBF6F08F2FFA00BC6A1F7468E36E637CDAF17823FD7E665CD3CAD534C8FC53B91808DC567B1CC059CF2246779EA9250B7CC776D8CDECE7F276F0010B5461DAF280FF02", "B9F9B97117CE8ECEB9F9B97117CE8ECE"));
    	     //交易查询
//    	     System.out.println(encECB3Des("appid=TM000001&version=v1.0.1&simno=1111&imei=333&mer=20191111111&f11=111111&sn=86831021100001", "B9F9B97117CE8ECEB9F9B97117CE8ECE"));
    	   //消费
    	     String date =encECB3Des("appid=TM000001&version=v1.0.1&simno=123&imei=123&mkey=qrPay&f4=14550&f11=000001&sn=86837040000005&loc=18BE,5028,460,00&mac=123456&mer=631000015537670&f59=1232321&pvip=1", "B9F9B97117CE8ECEB9F9B97117CE8ECE");
    	     
           System.out.println(date);
//    	     String data2 =decECB3Des(date, "B9F9B97117CE8ECEB9F9B97117CE8ECE");
//    	     //解密
//    	     System.out.println(data2);
    	     
//    	     data2 =decECB3Des("DB3244CB8DC76C7852BF9D66E3C61E05FB44CA6BF61AF4DE2DE3BCA496D0A0F8CD9692DA9AE5FEA4C294E188A2ADF1EE5C92A537816B962DE906A79A39AADE17F33BFCD606DA951736F91545C1BF25E4", "B9F9B97117CE8ECEB9F9B97117CE8ECE");
//             //解密
//             System.out.println(data2);
    	     
//             //分散因子  1111111111111111
//             String randomKey  = "1558944342417000";
//
//             String workKey= getWorkKey(randomKey);
//             if(workKey != null && workKey.length() == 32){
//
//                 System.out.println("workKey:"+workKey);
//
//                 //加解密测试
//
//                 String miwen="E1E64CC90AAEE22A417B1576309156131E55B4022748E123";
//                 String mess= decECB3Des(miwen, workKey);
//
//                 System.out.println("length:"+mess.length()+",["+mess+"]");
//
//
//                 //加密
//                 String mingwen="openid=1212121211";
//                 String mess2= encECB3Des(mingwen, workKey);
//
//                 System.out.println(mess2);
//
//             }
//             String workKey= DESDOUtil.getWorkKey("1560138693543000");
//             String mess= DESDOUtil.decECB3Des("00f337c9ba5c233e4af6c4d18a4016fa6a6fdb42", workKey);
//             System.out.println(mess);
		} catch (Exception e) {
			e.printStackTrace();
		}
     }
    
    /**
     *  功能:计算工作秘钥
     *  参数: 传入分散因子 16个字节的 0-9 A-F
     * @return 工作秘钥
     * @throws Exception
     */
    public static String getWorkKey(String randomKey) throws Exception{
        
        //分散因子长度为16个字节
        if(StringUtils.isEmpty(randomKey) || randomKey.length() != 16){
            System.out.println("randomKey:"+randomKey+" 格式错");
            return null;
        }
        
        //分散因子 范围是0-9 A-F
        Pattern pattern = Pattern.compile("[0-9a-fA-F]*"); 
        Matcher isNum = pattern.matcher(randomKey);
        if( !isNum.matches() ){
            System.out.println("randomKey:"+randomKey+" 格式错");
            return null;
        } 
        
        byte[] Scatter =hexStringToBytes(randomKey);
     
        //使用主密钥加密得到工作秘钥的前半部分
        String workKey1 = encECB3Des(Scatter,mainKey);
        
        byte[] da=new byte[8];
        
        //分散因子取反
        for(int i=0;i<8;i++){
            da[i] = (byte)(~Scatter[i]);
        }
        
        //使用主密钥加密得到工作秘钥的后半部分
        String workKey2 = encECB3Des(da,mainKey);
        
        String workKey=workKey1+workKey2;
        return workKey.toUpperCase();
    }
    
    /**
     * 3des 两倍长加密
     * @param src
     * @param key
     * @return
     */
    public static String encECB3Des(byte[] src,String key) throws Exception{ 
        byte[] temp = null; 
        byte[] temp1 = null; 
        
        temp1 = encryptDes(hexStringToBytes(key.substring(0, 16)), src); 
        temp = decryptDes(hexStringToBytes(key.substring(16, 32)), temp1); 
        temp1 = encryptDes(hexStringToBytes(key.substring(0, 16)), temp); 
        return bytesToHexString(temp1); 
    } 
    
    /**
     * 3des 两倍长加密
     * @param src
     * @param key
     * @return
     */
    public static String encECB3Des(String src,String key) throws Exception{ 
        byte[] temp = null; 
        byte[] temp1 = null; 
        
//        System.out.println("src.length:"+src.length());
//        System.out.println("src.getBytes().length:"+src.getBytes("utf-8").length);
        
        byte[] dataBytes = src.getBytes("utf-8");
        int num = 8 - dataBytes.length % 8;
        byte[] encData = Arrays.copyOf(dataBytes, dataBytes.length + num);
        
        //不足8字节的倍数 右补空格
        for (int i = dataBytes.length; i < encData.length; i++) {
            encData[i] = Integer.valueOf(32).byteValue();
        }
        
        temp1 = encryptDes(hexStringToBytes(key.substring(0, 16)), encData); 
        
//        System.out.println("temp1.length:"+temp1.length);
        temp = decryptDes(hexStringToBytes(key.substring(16, 32)), temp1); 
//        System.out.println("temp.length:"+temp.length);
        temp1 = encryptDes(hexStringToBytes(key.substring(0, 16)), temp); 
        return bytesToHexString(temp1).toUpperCase(); 
    } 
    
    /**
     * 3DES(双倍长) 解密
     * 
     * @param key
     * @param src
     * @return
     */ 
    public static String decECB3Des(String src,String key) throws Exception{ 
        byte[] temp2 = decryptDes(hexStringToBytes(key.substring(0, 16)), hexStringToBytes(src)); 
        byte[] temp1 = encryptDes(hexStringToBytes(key.substring(16, 32)), temp2); 
        byte[] dest = decryptDes(hexStringToBytes(key.substring(0, 16)), temp1); 
        return new String(dest); 
    } 
   
    public static String bytesToHexString(byte[] src) throws Exception{
           StringBuilder stringBuilder = new StringBuilder("");
           if (src == null || src.length <= 0) {
                return null;
           }
           for (int i = 0; i < src.length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                     stringBuilder.append(0);
                }
                stringBuilder.append(hv);
           }
           return stringBuilder.toString();
     }
   
 
    public static byte[] hexStringToBytes(String hexString) throws Exception{
           if (hexString == null || hexString.equals("")) {
                return null;
           }
           hexString = hexString.toUpperCase();
           int length = hexString.length() / 2;
           char[] hexChars = hexString.toCharArray();
           byte[] d = new byte[length];
           for (int i = 0; i < length; i++) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
           }
           return d;
     }
     private static byte charToByte(char c) {
           return (byte) "0123456789ABCDEF".indexOf(c);
     }
     
    /**
    * DES加密
    * 
    */ 
   public static byte[] encryptDes(byte[] key, byte[] src) throws Exception{ 
       try { 
           // 创建一个DESKeySpec对象 
           DESKeySpec desKey = new DESKeySpec(key); 
           // 创建一个密匙工厂 
           SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
           // 将DESKeySpec对象转换成SecretKey对象 
           SecretKey secretKey = keyFactory.generateSecret(desKey); 
           // Cipher对象实际完成解密操作 
           Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding"); 
           // 用密匙初始化Cipher对象 
           cipher.init(Cipher.ENCRYPT_MODE, secretKey); 
           // 现在，获取数据并加密 
           // 正式执行加密操作 
           return cipher.doFinal(src); 
       } catch (Exception e) { 
           e.printStackTrace(); 
       } 
       return null; 
   } 
 
   /**
    * des解密
    * 
    * @param key
    * @param src
    * @return
    */ 
   public static byte[] decryptDes(byte[] key, byte[] src) throws Exception{ 
       try { 
           // DES算法要求有一个可信任的随机数源 
           SecureRandom random = new SecureRandom(); 
           // 创建一个DESKeySpec对象 
           DESKeySpec desKey = new DESKeySpec(key); 
           // 创建一个密匙工厂 
           SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
           // 将DESKeySpec对象转换成SecretKey对象 
           SecretKey secretKey = keyFactory.generateSecret(desKey); 
           // Cipher对象实际完成解密操作 
           Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding"); 
           // 用密匙初始化Cipher对象 
           cipher.init(Cipher.DECRYPT_MODE, secretKey, random); 
           // 现在，获取数据并加密 
           // 正式执行加密操作 
           return cipher.doFinal(src); 
       } catch (Exception e) { 
           e.printStackTrace(); 
       } 
       return null; 
   }

   public static byte[] hexStringToByteArray(String text) {
       if (text == null)
           return null;
       byte[] result = new byte[text.length() / 2];
       for (int i = 0; i < result.length; ++i) {
           int x = Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16);
           result[i] = x <= 127 ? (byte) x : (byte) (x - 256);
       }
       return result;
   }

   public static String byteArrayToHexString(byte data[]) {
       String result = "";
       for (int i = 0; i < data.length; i++) {
           int v = data[i] & 0xFF;
           String hv = Integer.toHexString(v);
           if (hv.length() < 2) {
               result += "0";
           }
           result += hv;
       }
       return result;
   }

   
   public static byte[] str2Bcd(String asc) {

       asc = asc.trim();
       int len = asc.length();
       int mod = len % 2;

       if (mod != 0) {
           asc = "0" + asc;
           len = asc.length();
       }

       byte[] ret = new byte[len / 2];
       for (int i = 0; i < ret.length; i++) {
           try {
               ret[i] = (Integer.valueOf(asc.substring(i + i, (i + i + 2)), 16)).byteValue();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return ret;
   }
}