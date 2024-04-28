package io.spring.initializr.web.controller;

import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.MyProjectRequest;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Map;

public class CustomProjectGenerationController extends ProjectGenerationController<MyProjectRequest> {

    public CustomProjectGenerationController(InitializrMetadataProvider metadataProvider,
                                             ProjectGenerationInvoker<MyProjectRequest> projectGenerationInvoker) {
        super(metadataProvider, projectGenerationInvoker);
    }

    @Override
    public MyProjectRequest projectRequest(Map<String, String> headers) {
        MyProjectRequest request = new MyProjectRequest();
        request.getParameters().putAll(headers);
        //request.initialize(getMetadata());
        return request;
    }

    @PostMapping(path = "/generate.zip",
            produces = "application/x-compress")
    public ResponseEntity<byte[]> springTgz(@RequestBody MyProjectRequest request) throws IOException {
        request.initialize(getMetadata());
        return springZip(request);
    }
}
