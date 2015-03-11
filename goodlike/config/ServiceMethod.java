package com.goodlike.config;

import java.util.Arrays;
import java.util.List;

class ServiceMethod {

    private String name;
    public void setName(String name) {
        this.name = name;
    }
    private String port;
    public void setPort(String port) {
        this.port = port;
    }
    private String request;
    public void setRequest(String request) {
        this.request = request;
    }
    private String response;
    public void setResponse(String response) {
        this.response = response;
    }
    private String serviceName;
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceMethod() {}

    @Override
    public String toString() {
        return "[name=" + name + ", port=" + port + ", request=" + request +
                ", response=" + response + ", serviceName=" + serviceName + "]";
    }

    public List<String> toList() {
        return Arrays.asList(serviceName, name, port, request, response);
    }
}
