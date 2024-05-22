package com.moderatePerson.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.moderatePerson.domain.VO.ItemInfo;
import com.moderatePerson.domain.pojo.Message;
import com.moderatePerson.service.ItemService;
import com.moderatePerson.utils.AliPay.AlipayUtil;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
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
     * 用户支付功能，在用户支付后，更新数据库中level等级和用户积分信息等
     */

    //商户订单号，商户网站订单系统中唯一订单号，必填 WIDout_trade_no
    //付款金额，必填  WIDtotal_amount
    //订单名称，必填  WIDsubject
    //商品描述，可空  WIDbody
    @RequestMapping("/pay")
    public void pay(String WIDout_trade_no, String WIDtotal_amount, String WIDsubject, String WIDbody, HttpServletResponse response) throws  Exception{
        //1.获得初始化的AlipayClient
        //2.设置请求参数AlipayTradePagePayRequest
        alipayTradePagePayRequest.setReturnUrl(AlipayUtil.return_url);
        alipayTradePagePayRequest.setNotifyUrl(AlipayUtil.notify_url);

        alipayTradePagePayRequest.setBizContent(
                "{\"out_trade_no\":\""+ WIDout_trade_no +"\","
                        + "\"total_amount\":\""+ WIDtotal_amount +"\","
                        + "\"subject\":\""+ WIDsubject +"\","
                        + "\"body\":\""+ WIDbody +"\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        String result = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
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
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
            //商户订单号
            String out_trade_no =request.getParameter("out_trade_no");
            //支付宝交易号
            String trade_no = request.getParameter("trade_no");
            //交易状态
            String trade_status = request.getParameter("trade_status");
            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            out.println("success");

        }else {//验证失败
            out.println("fail");
            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }
    }
    @RequestMapping("/returnUrl")
    public  void returnUrl(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        //获取支付宝GET过来反馈信息
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
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //RSA2验证
        boolean signVerified = AlipaySignature.rsaCheckV2(params, AlipayUtil.alipay_public_key, AlipayUtil.charset, AlipayUtil.sign_type); //调用SDK验证签名
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            //支付宝交易号
            String trade_no = request.getParameter("trade_no");
            //付款金额
            String total_amount = request.getParameter("total_amount");
            out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
        }else {
            out.println("验签失败");
        }
    }
}
