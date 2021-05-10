

public class ChannelTradeContorllerTest {

//	private static final String url = "http://localhost:9327/spp-ws-service/channel";
	private static final String url = "http://172.16.200.84:9898/spp-ws-service/channel";
	
//	public static void main(String[] args) {
//		/**交易*/
//		trade(50000,50);
//		/**交易冲正*/
////		tradeCz("xf210327161749641706507");
//		/**代付*/
////		daifu("xf210330175817981628001","200.00","196.00");
//		/**代付查询*/
////		daifuQuery("df210327171608320629", "xf210327170601402128184");
//		/**手工代付*/
////		munalDaifu("df210327171608320629", "xf210327170601402128184");
//	}
	
//	public static void trade(int count,int threadCount){
//		CountDownLatch latch = new CountDownLatch(count);
//		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//		for(int i=1;i<=count;i++){
//			Runnable runnable = new Runnable() {
//				@Override
//				public void run() {
//					try {
//						String field11 = BillIdGenerateUtil.generateBillIdDelyy("xf", 6);
//						Map<String, String> body = new HashMap<>();
//						//交易类型 100:T0,101:T1
//						body.put("trancde","100");
//						body.put("field11",field11);
//						//小用户:001,大用户:002
//						body.put("useridType","002");
//						body.put("agentType","002");
//						body.put("agentNo","22290,22298,22324");
//						//用户id
//						body.put("userid","5537652");
//						//手机号
//						body.put("usercode","18712149008");
//						//账户ID
//						body.put("accountId","50039");
//						//sn号
//						body.put("ksn","86010000000001");
//						//交易卡号
//						body.put("field2","6259760933693370");
//						//金额 单位：分
//						body.put("field4","000000040000");
//						//卡有效期
//						body.put("field14","2209");
//						//服务点输入方式码
//						body.put("field22","051");
//						//卡片序列号
//						body.put("field23","000");
//						//二磁
//						body.put("field35","6259760933693370=260320100000178FFFFF");
//						//三磁
//						body.put("field36","");
//						//商户号
//						body.put("field42","831290075380577");
//						//终端号
//						body.put("field41","31746848");
//						//PBOC IC卡数据
//						body.put("field55","9F2608698C935EDFA9761A9F2701809F101307070103A00000010A01000000000032C225019F370405174EA39F3602003C950500000000009A032103299C01009F02060000000400005F2A02015682027C009F1A0201569F03060000000000009F3303E0E9009F3501229F1E0830303030303030318408A0000003330101029F090200209F410400000282");
//						//密码
//						body.put("field52","D758DFAB4858FE87");
//						//经纬度 格式”12.231234,123.023459”
//						body.put("longlatitude","12.231234,123.023459");
//						//交易手续费(元)
//						body.put("merchantFee","1.00");
//						//提现手续费(元)
//						body.put("merchantTXFee","3.00");
//						//手续费优惠金额(元)
//						body.put("discountAmount","0.00");
//						//冻结交易金额
//						body.put("frozenFee","196.00");
//						//终端信息
//						body.put("termIP","172.16.80.152");
//						//1：标准商户，2：非标准商户，3：银联二维码商户
//						body.put("merchantType","1");
//
//						String req_url = url+"/trade.do";
//
//						Map<String, String> header = new HashMap<>();
//			        	String resp = HttpClientUtil.post(req_url, header, body, "application/x-www-form-urlencoded");
//			        	String s = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_1);
//			        	System.out.println("field11:"+field11+",返回时间:"+s+",返回内容:"+resp);
//					} catch (Exception e) {
//						e.printStackTrace();
//					} finally{
//						latch.countDown();
//					}
//				}
//			};
//			executor.execute(runnable);
//		}
//		try {
//			latch.await();
//			executor.shutdown();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        System.out.println("latch.await() 执行完毕");
//	}
//
//	public static void tradeCz(String trackingNo){
//		Map<String, String> body = new HashMap<>();
//		body.put("trancde","109");
//		body.put("trackingNo",trackingNo);
//		String req_url = url+"/tradeCz.do";
//		try {
//			Map<String, String> header = new HashMap<>();
//        	String resp = HttpClientUtil.post(req_url, header, body, "application/x-www-form-urlencoded");
//        	String s = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_1);
//        	System.out.println("返回时间:"+s+",返回内容:"+resp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void daifu(String orgTrackNo,String settleAmt,String frozenFee){
//		Map<String, String> body = new HashMap<>();
//		body.put("trancde","102");
//		//入账账号
//		body.put("field2","6216261000000000018");
//		//交易金额(分) 消费交易金额-手续费(交易手续费+提现手续费)-冻结金额 = 该金额字段
//		settleAmt = CalculateUtil.multiply(settleAmt, CalculateUtil.ALIQUOT_100, CalculateUtil.zero, CalculateUtil.ROUND_HALF_UP).toString();
//		settleAmt = StringUtils.leftPad(settleAmt, 12, "0");
//		body.put("field4",settleAmt);
//		//订单号
//		body.put("field11",BillIdGenerateUtil.generateBillIdDelyy("df", 3));
//		//原消费订单号
//		body.put("original_field11",orgTrackNo);
//		//入账户名
//		body.put("field62","全渠道");
//		//开户行代码
//		body.put("bankCode","301290000007");
//		//开户行名称
//		body.put("bankName","交通银行");
//		//特殊计费(入冻结196),单位:分
//		body.put("frozenFee",frozenFee);
//		String req_url = url+"/daifu.do";
//		try {
//			Map<String, String> header = new HashMap<>();
//        	String resp = HttpClientUtil.post(req_url, header, body, "application/x-www-form-urlencoded");
//        	String s = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_1);
//        	System.out.println("返回时间:"+s+",返回内容:"+resp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void daifuQuery(String trackingNo,String origTrackingNo){
//		Map<String, String> body = new HashMap<>();
//		body.put("trancde","106");
//		//卡号
//		body.put("card","6216261000000000018");
//		//交易金额,单位:分
//		body.put("trade_amt","000000100000");
//		//交易日期
//		body.put("trade_date",DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_3));
//		//手机号
//		body.put("usercode","18121016071");
//		//代付订单号
//		body.put("trade_trace",trackingNo);
//		//原消费订单号
//		body.put("original_trade_trace",origTrackingNo);
//		String req_url = url+"/daifuQuery.do";
//		try {
//			Map<String, String> header = new HashMap<>();
//        	String resp = HttpClientUtil.post(req_url, header, body, "application/x-www-form-urlencoded");
//        	String s = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_1);
//        	System.out.println("返回时间:"+s+",返回内容:"+resp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void munalDaifu(String trackingNo,String origTrackingNo){
//		Map<String, String> body = new HashMap<>();
//		body.put("trancde","105");
//		body.put("original_trade_date",DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_3));
//		//入账账号
//		body.put("card","6216261000000000018");
//		//交易金额(分)
//		body.put("trade_amt","000000009600");
//		//入账户名
//		body.put("card_name","全渠道");
//		//代付订单号
//		body.put("trade_trace",trackingNo);
//		//原消费订单号
//		body.put("original_trade_trace",origTrackingNo);
//		//开户行代码
//		body.put("bankCode","301290000007");
//		//开户行名称
//		body.put("bankName","交通银行");
//		//特殊计费(入冻结196),单位:分
//		body.put("frozenFee","196.00");
//		String req_url = url+"/munalDaifu.do";
//		try {
//			Map<String, String> header = new HashMap<>();
//        	String resp = HttpClientUtil.post(req_url, header, body, "application/x-www-form-urlencoded");
//        	String s = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_1);
//        	System.out.println("返回时间:"+s+",返回内容:"+resp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
