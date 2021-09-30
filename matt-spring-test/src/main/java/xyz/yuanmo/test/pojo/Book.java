package xyz.yuanmo.test.pojo;

import lombok.*;

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


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", price='" + price + '\'' +
                ", hashCode='" + hashCode() + '\'' +
                '}';
    }
}
