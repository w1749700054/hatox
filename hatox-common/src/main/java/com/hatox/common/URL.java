package com.hatox.common;

import java.io.Serializable;
import java.util.Map;

public class URL implements Serializable {

    private static final long serialVersionUID = -3051163265788821442L;
    private String host;
    private int port;
    private String protocol;
    private String username;
    private String password;
    private String path;
    private Map<String,String> parameters;
}
