package com.ericsson.isf.mqttclient.profiles.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.ObjectMapper;

//overriding default spring serializer/deserializer
@Configuration
public class JacksonConfiguration {
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //settng every null entry in int to not give error and set default value given in modal to int variable
        mapper.configOverride(Integer.class).setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.SKIP));

        //mapper.configOverride(String.class).setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.SKIP));
        return mapper;
    }

}
