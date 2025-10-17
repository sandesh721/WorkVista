//package com.task.WorkVista.configuration;
//
//import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JacksonConfig {
//    @Bean
//    public Hibernate5Module hibernate5Module() {
//        Hibernate5Module module = new Hibernate5Module();
//        module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
//        module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
//        return module;
//    }
//}
