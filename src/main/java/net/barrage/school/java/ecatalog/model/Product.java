package net.barrage.school.java.ecatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@Accessors(chain = true)
public class Product {
    private UUID id;
    private String name;
    private String description;
    private String image;
    private double price;

    @JsonIgnore
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        Field[] fields = this.getClass().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> keys.add(field.getName()));

        return keys;
    }
}
