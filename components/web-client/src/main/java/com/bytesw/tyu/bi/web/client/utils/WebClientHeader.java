package com.bytesw.tyu.bi.web.client.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebClientHeader {
    private String name;
    private String value;
}
