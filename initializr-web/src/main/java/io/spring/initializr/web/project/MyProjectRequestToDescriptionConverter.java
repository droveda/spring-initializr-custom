package io.spring.initializr.web.project;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.project.MyMutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.*;
import io.spring.initializr.metadata.support.MetadataBuildItemMapper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyProjectRequestToDescriptionConverter implements ProjectRequestToDescriptionConverter<MyProjectRequest> {

    private final ProjectRequestPlatformVersionTransformer platformVersionTransformer;

    public MyProjectRequestToDescriptionConverter(ProjectRequestPlatformVersionTransformer platformVersionTransformer) {
        Assert.notNull(platformVersionTransformer, "PlatformVersionTransformer must not be null");
        this.platformVersionTransformer = platformVersionTransformer;
    }

    @Override
    public ProjectDescription convert(MyProjectRequest request, InitializrMetadata metadata) {
        MyMutableProjectDescription description = new MyMutableProjectDescription();
        convert(request, description, metadata);
        return description;
    }

    public void convert(MyProjectRequest request, MyMutableProjectDescription description, InitializrMetadata metadata) {
        validate(request, metadata);
        Version platformVersion = getPlatformVersion(request, metadata);
        List<Dependency> resolvedDependencies = getResolvedDependencies(request, platformVersion, metadata);
        validateDependencyRange(platformVersion, resolvedDependencies);

        description.setApplicationName(request.getApplicationName());
        description.setArtifactId(cleanInputValue(request.getArtifactId()));
        description.setBaseDirectory(request.getBaseDir());
        description.setBuildSystem(getBuildSystem(request, metadata));
        description.setDescription(request.getDescription());
        description.setGroupId(cleanInputValue(request.getGroupId()));
        description.setLanguage(Language.forId(request.getLanguage(), request.getJavaVersion()));
        description.setName(cleanInputValue(request.getName()));
        description.setPackageName(cleanInputValue(request.getPackageName()));
        description.setPackaging(Packaging.forId(request.getPackaging()));
        description.setPlatformVersion(platformVersion);
        description.setVersion(request.getVersion());

        description.setCostCenter(request.getCostCenter());

        resolvedDependencies.forEach((dependency) -> description.addDependency(dependency.getId(),
                MetadataBuildItemMapper.toDependency(dependency)));

    }

    protected String cleanInputValue(String value) {
        return StringUtils.hasText(value) ? Normalizer.normalize(value, Normalizer.Form.NFKD).replaceAll("\\p{M}", "")
                : value;
    }

    private void validate(ProjectRequest request, InitializrMetadata metadata) {
        validatePlatformVersion(request, metadata);
        validateType(request.getType(), metadata);
        validateLanguage(request.getLanguage(), metadata);
        validatePackaging(request.getPackaging(), metadata);
        validateDependencies(request, metadata);
    }

    private void validatePlatformVersion(ProjectRequest request, InitializrMetadata metadata) {
        Version platformVersion = Version.safeParse(request.getBootVersion());
        InitializrConfiguration.Platform platform = metadata.getConfiguration().getEnv().getPlatform();
        if (platformVersion != null && !platform.isCompatibleVersion(platformVersion)) {
            throw new InvalidProjectRequestException("Invalid Spring Boot version '" + platformVersion
                    + "', Spring Boot compatibility range is " + platform.determineCompatibilityRangeRequirement());
        }
    }

    private void validateType(String type, InitializrMetadata metadata) {
        if (type != null) {
            Type typeFromMetadata = metadata.getTypes().get(type);
            if (typeFromMetadata == null) {
                throw new InvalidProjectRequestException("Unknown type '" + type + "' check project metadata");
            }
            if (!typeFromMetadata.getTags().containsKey("build")) {
                throw new InvalidProjectRequestException(
                        "Invalid type '" + type + "' (missing build tag) check project metadata");
            }
        }
    }

    private void validateLanguage(String language, InitializrMetadata metadata) {
        if (language != null) {
            DefaultMetadataElement languageFromMetadata = metadata.getLanguages().get(language);
            if (languageFromMetadata == null) {
                throw new InvalidProjectRequestException("Unknown language '" + language + "' check project metadata");
            }
        }
    }

    private void validatePackaging(String packaging, InitializrMetadata metadata) {
        if (packaging != null) {
            DefaultMetadataElement packagingFromMetadata = metadata.getPackagings().get(packaging);
            if (packagingFromMetadata == null) {
                throw new InvalidProjectRequestException(
                        "Unknown packaging '" + packaging + "' check project metadata");
            }
        }
    }

    private void validateDependencies(ProjectRequest request, InitializrMetadata metadata) {
        List<String> dependencies = request.getDependencies();
        dependencies.forEach((dep) -> {
            Dependency dependency = metadata.getDependencies().get(dep);
            if (dependency == null) {
                throw new InvalidProjectRequestException("Unknown dependency '" + dep + "' check project metadata");
            }
        });
    }

    private void validateDependencyRange(Version platformVersion, List<Dependency> resolvedDependencies) {
        resolvedDependencies.forEach((dep) -> {
            if (!dep.match(platformVersion)) {
                throw new InvalidProjectRequestException(
                        "Dependency '" + dep.getId() + "' is not compatible " + "with Spring Boot " + platformVersion);
            }
        });
    }

    private BuildSystem getBuildSystem(ProjectRequest request, InitializrMetadata metadata) {
        Map<String, String> typeTags = metadata.getTypes().get(request.getType()).getTags();
        String id = typeTags.get("build");
        String dialect = typeTags.get("dialect");
        return BuildSystem.forIdAndDialect(id, dialect);
    }

    private Version getPlatformVersion(ProjectRequest request, InitializrMetadata metadata) {
        String versionText = (request.getBootVersion() != null) ? request.getBootVersion()
                : metadata.getBootVersions().getDefault().getId();
        Version version = Version.parse(versionText);
        return this.platformVersionTransformer.transform(version, metadata);
    }

    private List<Dependency> getResolvedDependencies(ProjectRequest request, Version platformVersion,
                                                     InitializrMetadata metadata) {
        List<String> depIds = request.getDependencies();
        return depIds.stream().map((it) -> {
            Dependency dependency = metadata.getDependencies().get(it);
            return dependency.resolve(platformVersion);
        }).collect(Collectors.toList());
    }
}
