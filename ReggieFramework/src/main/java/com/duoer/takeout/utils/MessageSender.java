package com.duoer.takeout.utils;

import com.aliyun.tea.TeaException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSender {
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static void send(String signName, String templateCode, String phoneNumbers, String param) throws Exception {
        log.info("send {} to {}", param, phoneNumbers);

        com.aliyun.dysmsapi20170525.Client client = MessageSender.createClient(
                "AccessKey ID", "AccessKey Secret");
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest =
                new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName(signName) // "阿里云短信测试"
                .setTemplateCode(templateCode) // "SMS_154950909"
                .setPhoneNumbers(phoneNumbers) // "15326337715"
                .setTemplateParam("{\"code\":\"" + param + "\"}"); // 1234
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}
