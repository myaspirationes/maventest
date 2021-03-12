package org.RabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/12 11:52
 *
 * O單刷卡，推送消息
 */
public class ProducerForYDF {

    private static final String RABBIT_HOST = "172.16.200.68";

    private static final String RABBIT_USERNAME = "worth1";

    private static final String RABBIT_PASSWORD = "123";

    private static final String QUEUE_NAME="nemp_transactions_mq_sync";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectUtil.getConnection(RABBIT_HOST,RABBIT_USERNAME,RABBIT_PASSWORD);
        System.out.println(connection);
        //创建通道
        Channel channel = connection.createChannel(1);
        /*
         * 声明（创建）队列
         * 参数1：队列名称
         * 参数2：为true时server重启队列不会消失
         * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
         * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
         * 参数5：建立队列时的其他参数
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        /**
         * chFinTime:时间为当前时间之前
         * trackingNo:  其中时间部分应与chFinTime相同
         */
        String message = "{\n" +
                "trxcode:\"TRX_DV_CONSUME\",\n" +
                "skCard:\"068C999C41C72C132E4FE86333FA34FA\",\n" +
                "result:\"00\",\n" +
                "possno:\"60880400000572\",\n" +
                "jsprice:9962,\n" +
                "merchantname:\"张晨晟\",\n" +
                "bank_name:\"中国邮政储蓄银行\",\n" +
                "shrate:\"0.38\",\n" +
                "chFinTime:\"20210310092539\",\n" +
                "addr:\"上海市 浦东新区 金科路 靠近上海张江高科技园区(金科路)\",\n" +
                "merchant_no:\"641611560389237\",\n" +
                "txrate:0,\n" +
                "otherJson:\"{\\\"userFee\\\":\\\"38\\\",\\\"city\\\":\\\"上海市\\\",\\\"model_type\\\":\\\"无\\\",\\\"blackFlag\\\":0,\\\"trafficType\\\":\\\"0\\\",\\\"frozenFee\\\":\\\"0\\\",\\\"version\\\":\\\"V1.0.2\\\",\\\"whiteFlag\\\":0,\\\"locDesc\\\":\\\"单基站定位信息\\\",\\\"areaCode\\\":\\\"2900\\\",\\\"province\\\":\\\"上海市\\\",\\\"longlatitude\\\":\\\"121.6022835,31.174509\\\",\\\"locat\\\":\\\"18BE,4FBA,460,00\\\",\\\"userRate\\\":\\\"0.38\\\",\\\"appid\\\":\\\"TM000001\\\",\\\"trafficAmt\\\":\\\"0\\\",\\\"imei\\\":\\\"864793043919625\\\",\\\"locWay\\\":\\\"loc\\\",\\\"customerAmount\\\":9962.0,\\\"firstFlag\\\":\\\"0\\\",\\\"addr\\\":\\\"上海市 浦东新区 金科路 靠近上海张江高科技园区(金科路)\\\",\\\"simno\\\":\\\"89860454111970886352\\\"}\",\n" +
                "handcharge:38,\n" +
                "cardno:\"19347C39966F3454CE61C71BC436AB26\",\n" +
                "whiteFlag:0,\n" +
                "merchantcode:\"3F5FCD131B025C343EC7A8F442503A11\",\n" +
                "sepatType:0,\n" +
                "trackingNo:\"xf00420210310092539ifOdM5\",\n" +
                "merchantid:3833099,\n" +
                "paymoney:10000,\n" +
                "cardTypeFlag:\"D\",\n" +
                "intTxnDt:20210308,\n" +
                "trancde:\"100\",\n" +
                "merchantNo:\"641611560389237\",\n" +
                "sepatamt:0\n" +
                "}";

        //message = message ;
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        Date now = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = ft.format(now);
        Thread.sleep(100);

        System.out.println("时间："+time+" 生产者 发送 ："+message);
        channel.close();
        connection.close();
    }
}
