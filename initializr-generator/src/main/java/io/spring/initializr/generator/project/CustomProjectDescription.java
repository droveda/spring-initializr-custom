package io.spring.initializr.generator.project;

public interface CustomProjectDescription extends ProjectDescription {

    String getCostCenter();
    
    void setCostCenter(String costCenter);

}
