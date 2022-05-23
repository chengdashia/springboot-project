package com.example;

import cn.hutool.core.date.DateUtil;
import com.example.pojo.PdfForm;
import com.example.test.GenerateContract;
import com.example.utils.Base64Utils;
import com.example.utils.NumberUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
class SpringbootPdfApplicationTests {

    @Test
    void contextLoads() throws IOException {
        String imagePath = "E:\\test\\pdf\\"+System.currentTimeMillis()+".jpg";
        String base = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD//gAfQ29tcHJlc3NlZCBieSBqcGVnLXJlY29tcHJlc3P/2wCEAAQEBAQEBAQEBAQGBgUGBggHBwcHCAwJCQkJCQwTDA4MDA4MExEUEA8QFBEeFxUVFx4iHRsdIiolJSo0MjRERFwBBAQEBAQEBAQEBAYGBQYGCAcHBwcIDAkJCQkJDBMMDgwMDgwTERQQDxAUER4XFRUXHiIdGx0iKiUlKjQyNEREXP/CABEIALkBowMBIgACEQEDEQH/xAAdAAEBAQEBAAMBAQAAAAAAAAAABwgFBgIDBAkB/9oACAEBAAAAANXUMAAADx/sAAAAJ77fsAAAAAAAAEJr3YAAAAAAAAITXuwAAAAAAAAQmvdgAAAAAAAAhNe7AAeM7/UZe1CACSccFw+YABCa92AA5eSNlobcgDNuknmvxeC96Qy5+yAAITXuwADGmw/0ABEej9UsQPQurGda368AAhNe7ADwnc77zmdtWfh+f6wHxw3uYyRrcwlu0AAQmvdgB+L+a1x+/wDTmy9fbrv9wBnzxpA9meNmWj6qAAITXuwAZF10Y91/8+b42iAAx1sUAABCe/VwDJWtSTc6z4H3r9wZu8t9p+f7oLbf387o/V8tZfcABmPQnYAfD+Ztom+6PW4Za29gnFHAIBiz+lHqgAAITXuwAz97Cowuhey/nzbdMmPdhAJFE/v5V0sYAAITXuwAwhu+T8OhRr75Bu99GW9Vgk+bbNoTJWtcz+R2L/oAAhNe7AGdO/bMRVOtxnSsdmGsZ94+4iNZ6s2hDIOvnkMfW7QQABCa92ATOG6+S3FP9KBJc8cDeP73l8sVi7hivagjmfNaeyAAhNe7AM7aJAAACN2QAABCa92AAAAAAAACE17sAAAAAAAAEJr3YAAAAAAAAITXuwAAAAAAAAQn3vswAAAPNelAAAAyxX6cAAABNqSAAABwv//EABQBAQAAAAAAAAAAAAAAAAAAAAD/2gAIAQIQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//xAAUAQEAAAAAAAAAAAAAAAAAAAAA/9oACAEDEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP/8QANxAAAQMEAgECAgkDAgcAAAAABAMFBgECBxIIExQAMBAYFRYgN0BQVld2FyFYEYUiIzM2OEZ1/9oACAEBAAESAG1jJKeb2It4SuAazCnrpDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4OPNoYBLej5lUV997g482hgEt6PmVRX33uDjzaGAS3o+ZVFffe4aCmifRwK74kqyhOTicigHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wcebQwCW9HzKor773Bx5tDAJb0fMqivvvcHHm0MAlvR8yqK++9wzTl5hDAZGIuHlNoAg4gy7UYBV4dQrB9T0RA1llvzOc49yu+Sl0dI1OPAal+noFajAKvDqFYPqeiIGsst+ZznHuV3yUujpGpx4DUv09ArUYBV4dQrB9T0RA1llvzOc49yu+Sl0dI1OPAal+noFajAKvDqFYPqeiIGsst+ZznHuV3yUujpGpx4DUv09ArUYBV4dQrB9T0RA1llvbyJkVhxhG75TI0DlALSUhqWRqUs0xYGuSx0yhLY4I9qKvwwjm6Qv0yluMco1DGlYJy1A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";

        Base64Utils.GenerateImage(base);
    }

    @Test
    void testPdfForm(){
        PdfForm pdfForm = new PdfForm();
        pdfForm.setPartA("牛龙星");
        pdfForm.setPartB("李文龙");
        pdfForm.setType("苹果");
        pdfForm.setVarieties("红富士");
        pdfForm.setNums("1.5kg");
        pdfForm.setPrice("100");
        pdfForm.setTotalPrice("1002341");
        pdfForm.setTotal(NumberUtil.digitCapital(1002341.98));
        pdfForm.setPartAAddress("湖北省武汉市洪山区");
        pdfForm.setPartBAddress("河南省郑州市郑东新区");
        pdfForm.setPartAPhone("18888888888");
        pdfForm.setPartBPhone("18888888888");
        pdfForm.setYear(String.valueOf(DateUtil.year(DateUtil.date())));
        pdfForm.setMonth(String.valueOf(DateUtil.month(DateUtil.date()) + 1));
        pdfForm.setDay(String.valueOf(DateUtil.dayOfMonth(DateUtil.date())));

        GenerateContract.wordsFillTemplate(pdfForm);
    }

}
