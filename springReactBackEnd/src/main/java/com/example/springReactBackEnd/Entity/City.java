package com.example.springReactBackEnd.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity
@Scope("prototype")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @Column(name = "cityID")
    private Integer cityId;

    @Column(name = "cityName", length = 100)
    private String cityName;
}
