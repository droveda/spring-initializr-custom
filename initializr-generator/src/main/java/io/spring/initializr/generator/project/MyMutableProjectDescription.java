/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.project;

/**
 * A mutable implementation of {@link ProjectDescription}.
 *
 * @author Diegues Roveda
 */
public class MyMutableProjectDescription extends MutableProjectDescription {


    private String costCenter;


    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    @Override
    public MyMutableProjectDescription createCopy() {
        return new MyMutableProjectDescription(this);
    }

    public MyMutableProjectDescription(String costCenter) {
        this.costCenter = costCenter;
    }

    public MyMutableProjectDescription() {
    }

    protected MyMutableProjectDescription(MyMutableProjectDescription source) {
        this.platformVersion = source.getPlatformVersion();
        this.buildSystem = source.getBuildSystem();
        this.packaging = source.getPackaging();
        this.language = source.getLanguage();
        this.requestedDependencies.putAll(source.getRequestedDependencies());
        this.groupId = source.getGroupId();
        this.artifactId = source.getArtifactId();
        this.version = source.getVersion();
        this.name = source.getName();
        this.description = source.getDescription();
        this.applicationName = source.getApplicationName();
        this.packageName = source.getPackageName();
        this.baseDirectory = source.getBaseDirectory();
        this.costCenter = source.getCostCenter();
    }
}
