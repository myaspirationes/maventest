//package org.YDF;
//
//import com.shiyi.encrypt.manage.EncryptManager;
//import com.shiyi.encrypt.manage.JiangNan_TermKeyManager;
//
///**
// * @author tiger.wang
// * @version 1.0
// * @date 2021/3/25 13:52
// */
//public class term {
//
//    private static JiangNan_TermKeyManager jiangNan_TermKeyManager = new JiangNan_TermKeyManager();
//
//
//
//    public static void main(String[] args) {
//        try {
//            long start = System.currentTimeMillis();
//            String[] rtn = jiangNan_TermKeyManager.getTermKey("0201");//生成主密鑰
//            if (rtn != null && rtn.length == 3) {
//                System.out.println(rtn[0]);
//                System.out.println(rtn[1]);
//                System.out.println(rtn[2]);
//            } else {
//                System.out.println("error");
//            }
////12345678HD00X29A0DAC05EF6A8A740A5B524BF0923E7XAB51B94FCB0BD22223F4686CB9A5B3B110032280B8D58DBB
////12345678HD00XBB9C44349F957A7D6E3BD8B5F5E7D83CX1A3B776A568148AD0767E5A03A7AE456672315F46F1BA166
//            long end = System.currentTimeMillis() - start;
//            System.out.println("Take:" + end);
//            EncryptManager.stopEncryptCilent();
//        } catch (Exception var6) {
//            var6.printStackTrace();
//        }
//
//    }
//
//    static {
//        try {
//            EncryptManager.startEncryptCilent(0, 5);
//        } catch (Exception var1) {
//            var1.printStackTrace();
//        }
//
//    }
//
//}
