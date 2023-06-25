package com.lscavalcante.blog.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperService {
    public <T, U> U map(T source, Class<U> destinationType) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destinationType);
    }

    // Others methods if is necessary
}
