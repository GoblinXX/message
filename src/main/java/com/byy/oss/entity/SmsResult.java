package com.byy.oss.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: qugai
 * @description: 短信返回字段
 * @author: Goblin
 * @create: 2019-08-05 20:24
 **/
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsResult {

    @JsonProperty("Message")
    private String message;
    @JsonProperty("RequestId")
    private String requestId;
    private String bizId;
    @JsonProperty("Code")
    private String code;
}
