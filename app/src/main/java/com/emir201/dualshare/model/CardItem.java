package com.emir201.dualshare.model;

import com.emir201.dualshare.enums.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardItem {

    public  Type Type;
    private String uri;

}
