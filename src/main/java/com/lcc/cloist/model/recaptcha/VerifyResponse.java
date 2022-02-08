package com.lcc.cloist.model.recaptcha;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class VerifyResponse {
    private boolean success;
    private float score;
    private String action;
    private Date challenge_ts;
    private String hostname;
}
