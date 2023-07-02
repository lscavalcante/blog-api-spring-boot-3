package com.lscavalcante.blog.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
public class ModelMapperService {
    public <T, U> U map(T source, Class<U> destinationType) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(source, destinationType);
    }
    public <T, U> void map(T source, U destinationType) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.map(source, destinationType);
    }


    // Others methods if is necessary
}
