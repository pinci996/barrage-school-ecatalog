package net.barrage.school.java.ecatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@Accessors(chain = true)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String image;

    private Double price;

    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @JsonIgnore
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        Field[] fields = this.getClass().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> keys.add(field.getName()));

        return keys;
    }
}
