# WX_PAY

wx_pay 是一个未发布的sdkwx小程序,H5 支付包,

只用自动注入 支付类 
配置好配置文件
@Autowired
Payment paymen;
配置类 WxPayAppletConfig  
   
```Java
    @RequestMapping("/order/create")
    @ResponseBody
    public PayResponse createOrder(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        // 创建请求
        PayRequest payRequest = new PayRequest();
        payRequest.setOrderId(OrderIdUtil.createOrderId());
        payRequest.setOpenid(openId);
        payRequest.setOrderAmount(1);
        payRequest.setOrderName("test");
        // 然后选择支付渠道
        payRequest.setPayChannel(PayChannel.WXPAY_APPLET);
        log.info("支付請求={}", request);
         PayResponse 里面会得到相应的预支付定单信息
        PayResponse payment = paymen.payment(payRequest);
        log.info("支付返回={}", payment);
        return payment;
    }
    // 下面的接口也如此
    //查询
        @RequestMapping("/order/queryOrder")
    @ResponseBody
    public PayResponse queryOrder(HttpServletRequest request) {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        orderQueryRequest.setPayChannel(PayChannel.WXPAY_APPLET);
        orderQueryRequest.setOutOrderId("订单id");
        OrderQueryResponse query = paymen.query(orderQueryRequest);
        log.info(query.toString());
        return query;
    }
    // 通知
        @RequestMapping("/order/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("进入通知  : 微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        outSteam.close();
        inStream.close();
        log.info("xml文件 : " + resultxml);
        PayResponse payResponse = paymen.asyncNotify(resultxml);
        log.info("异步通知结束:{}", payResponse.toString());
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write("<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg></xml>".getBytes());
    }
    
    // 退款
    
     @RequestMapping("/order/refund")
    public void refund(HttpServletRequest request,String orderId) throws IOException {
        log.info("进入退款流程 ");
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setPayChannel(PayChannel.WXPAY_APPLET);
        refundRequest.setOrderAmount(1);
        refundRequest.setOrderId(orderId);
        RefundResponse refund = paymen.refund(refundRequest);
        log.info("退款完成返回结果: "+refund.toString());
    }
    /**
     * 退款通知接口
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/refund/notify")
    public void refundNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("进入退款通知");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        outSteam.close();
        inStream.close();
        log.info("xml文件 : " + resultxml);
        WxRefundNotifyDataResponse wxRefundNotifyDataResponse = paymen.refundAsyncNotify(resultxml);
        log.info("退款通知结束返回结果:{}", wxRefundNotifyDataResponse.toString());
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write("<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg></xml>".getBytes());
    }
```
很简单的使用方式  欢迎大家一起code
