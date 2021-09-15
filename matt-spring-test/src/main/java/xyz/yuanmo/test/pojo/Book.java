package xyz.yuanmo.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/15 16:03
 * @since 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String bookName;

    private String price;

}
