package com.mun.theatrems.controller;

import com.mun.theatrems.model.ELocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    private String name;
    private String code;
    private ELocation type;
    private String parentCode;
}