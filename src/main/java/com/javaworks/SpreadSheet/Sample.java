package com.javaworks.SpreadSheet;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Sample {
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;
}
