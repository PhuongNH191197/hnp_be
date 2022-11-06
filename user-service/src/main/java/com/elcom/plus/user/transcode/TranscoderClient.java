package com.elcom.plus.user.transcode;

import com.elcom.crbt.cmp.ws.service.BasicCmpWebServiceClient;

public class TranscoderClient extends BasicCmpWebServiceClient {

    private static final TranscoderClient _instance = new TranscoderClient();
    public static final TranscoderClient getInstance() {
        return _instance;
    }

    private TranscoderClient() {
//        super( VideoServiceImpl.getPathConfig("TranscodeURL"), "CMPWebServiceImplService");
        super("http://192.168.10.2:8020/transcode?wsdl", "CMPWebServiceImplService");
        System.out.println("webservice path==========> http://192.168.10.2:8020/transcode?wsdl");

    }


}