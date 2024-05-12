package sample.custom.contributor;

import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;


public class MyContributor extends SingleResourceProjectContributor {

    public MyContributor() {
        super("my-file.txt", "classpath:template/file-to-add.txt");
    }
}
