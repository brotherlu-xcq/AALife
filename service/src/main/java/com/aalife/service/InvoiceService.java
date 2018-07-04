package com.aalife.service;

/**
 * @author mosesc
 * @date 2018-07-04
 */
public interface InvoiceService {
    /**
     * 获取语音接口的Token
     * @return
     */
    String getToken();

    String getInvoiceContent(String token, String fileType, byte[] content);
}
