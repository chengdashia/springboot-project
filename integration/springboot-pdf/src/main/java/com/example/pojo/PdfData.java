package com.example.pojo;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PdfData {
    private String buyerName;
    private String sellerName;

    private String buyerPhone;
    private String sellerPhone;

    private String buyerAddress;
    private String sellerAddress;



    private String weight;
    private String goodsName;



}
