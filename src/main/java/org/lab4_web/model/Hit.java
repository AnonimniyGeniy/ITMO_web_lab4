package org.lab4_web.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Hit {
    private Double x;
    private Double y;
    private Double r;

    public Hit(){

    }

    public Hit(Double x, Double y, Double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

}
