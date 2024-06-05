package com.moderatePerson.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.moderatePerson.domain.PO.Order;
import com.moderatePerson.domain.VO.ItemInfo;
import com.moderatePerson.domain.pojo.Message;
import com.moderatePerson.service.ItemService;
import com.moderatePerson.service.OrderService;
import com.moderatePerson.service.UserService;
import com.moderatePerson.utils.AliPay.AlipayUtil;
import com.moderatePerson.utils.jwt.JwtUtil;
import com.moderatePerson.utils.order.OrderIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Resource
    private AlipayClient alipayClient;
    @Resource
    private AlipayTradePagePayRequest alipayTradePagePayRequest;
    /**
     * 套餐界面: 套餐名称,套餐介绍信息,套餐价格(原始价格*discount),截止日期
     * 套餐包括包月套餐（每日赠送一定积分，当日有效），或直接购买一定量积分。
     */

    @GetMapping("/info")
    public ResponseEntity<?> info(){
        List<ItemInfo> infoList = itemService.selectItemInfo();
        Message message = new Message();
        message.setStatus(200);
        message.setData(infoList);
        return ResponseEntity.ok().body(message);
    }
    /**
     * 用户支付功能,用户点击及支付按钮时,
     * 前端需要在请求数据中添加下面的payment,orderName,itemMsg
     * payment与info接口中的price相同(与转换成String类型)，orderName可以与itemName相同，itemMsg与info接口中一致
     * 其中orderId在用户支付后，更新数据库中level等级和用户积分信息等
     */

    //商户订单号，商户网站订单系统中唯一订单号，必填 orderId 在后端自动生成唯一Id
    //付款金额，必填  payment 与info数据中price一致
    //订单名称，必填  orderName 与itemName必须一致便于数据操作
    //商品描述，可空  itemMsg 与info数据中一致
    @RequestMapping("/pay")
    public void pay(String payment, String orderName, String itemMsg, HttpServletResponse response) throws  Exception{
        //1.获得初始化的AlipayClient
        //2.设置请求参数AlipayTradePagePayRequest
        alipayTradePagePayRequest.setReturnUrl(AlipayUtil.return_url);
        alipayTradePagePayRequest.setNotifyUrl(AlipayUtil.notify_url);
        // 生成orderId
        String orderId = OrderIdUtil.generateUniqueOrderId();
        alipayTradePagePayRequest.setBizContent(
                "{\"out_trade_no\":\""+ orderId +"\","
                        + "\"total_amount\":\""+ payment +"\","
                        + "\"subject\":\""+ orderName +"\","
                        + "\"body\":\""+ itemMsg +"\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderName(orderName);
        order.setPayment(payment);
        order.setItemMsg(itemMsg);
        // 获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();
        // 转换为Date类型
        Date currentDate = new Date(currentTimestamp);
        order.setCreationTime(currentDate);
        // 默认设为-1，代表未支付
        order.setStatus(-1);
        //请求
        String result = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
        // 在数据库中插入订单
        orderService.insertOrder(order);
        //输出
//        Message message = new Message();
//        message.setData(result);
//        message.setStatus(200);
//        message.setMsg("支付界面加载成功");
        //输出
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(result);
    }
    // 异步通知配置,将支付宝反馈信息返回到服务器
    @RequestMapping("/notifyUrl")
    public ResponseEntity<Message> notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        Iterator<String> iter = requestParams.keySet().iterator();
        while(iter.hasNext()){
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayUtil.alipay_public_key, AlipayUtil.charset, AlipayUtil.sign_type); //调用SDK验证签名
	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断trade_no是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为orderId这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        if(signVerified) {//验证成功
            Order order = new Order();
            //商户订单号
            String out_trade_no = params.get("out_trade_no");
            //交易金额
            String totalAmount = params.get("total_amount");
            //支付宝交易号
            String trade_no = params.get("trade_no");
//            if(trade_status!=null&&trade_status.equals("TRADE_SUCCESS")){
//                // 当订单支持退款时，trade_status不为空
//            }
            // 获取当前时间戳
            long currentTimestamp = System.currentTimeMillis();
            // 转换为Date类型
            Date currentDate = new Date(currentTimestamp);
            orderService.updateOrderBYId(trade_no,totalAmount,currentDate,1,out_trade_no);
            // 执行数据库修改操作
            String itemName = orderService.selectOrderNameById(out_trade_no);// 通过id查询到orderName与itemName等价
            String phoneNumber = JwtUtil.getPhoneNumber(request);// 从token中获取手机号
            userService.upgradeUserPermissions(phoneNumber,itemName); // 更新用户权限
//            out.println("success");
            Message message = new Message();
            message.setStatus(200);
            message.setMsg("支付成功,套餐已开通");
            return ResponseEntity.ok().body(message);
        }else {//验证失败
//            out.println("fail");// 校验失败返回fail结果
            Message message = new Message();
            message.setStatus(200);
            message.setMsg("支付失败");
            return ResponseEntity.ok().body(message);
            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }
    }
//    @RequestMapping("/returnUrl")
//    public  void returnUrl(HttpServletRequest request, HttpServletResponse response) throws  Exception{
//        //获取支付宝GET过来反馈信息
//        Map<String,String> params = new HashMap<String,String>();
//        Map<String,String[]> requestParams = request.getParameterMap();
//        Iterator<String> iter = requestParams.keySet().iterator();
//        while(iter.hasNext()){
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            params.put(name, valueStr);
//        }
//
//        //RSA2验证
//        boolean signVerified = AlipaySignature.rsaCheckV2(params, AlipayUtil.alipay_public_key, AlipayUtil.charset, AlipayUtil.sign_type); //调用SDK验证签名
//        response.setContentType("text/html;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        //——请在这里编写您的程序（以下代码仅作参考）——
//        if(signVerified) {
//            //商户订单号
//            String out_trade_no = request.getParameter("out_trade_no");
//            //支付宝交易号
//            String trade_no = request.getParameter("trade_no");
//            //付款金额
//            String total_amount = request.getParameter("total_amount");
//            out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
//        }else {
//            out.println("验签失败");
//        }
}
