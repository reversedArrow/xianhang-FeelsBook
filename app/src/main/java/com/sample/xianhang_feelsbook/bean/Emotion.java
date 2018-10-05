package com.sample.xianhang_feelsbook.bean;

import java.io.Serializable;

/**
 * emotion object
 */
public class Emotion implements Serializable{
    public long id; // id
    public int emotion; // emotion type
    public String comment; // comment
    public String time; // create/update time
}
