package io.spring.initializr.web.project;

import io.spring.initializr.metadata.InitializrMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyProjectRequest extends ProjectRequest {

    private final Map<String, Object> parameters = new LinkedHashMap<>();

    private String costCenter;

    /**
     * Return the additional parameters that can be used to further identify the request.
     *
     * @return the parameters
     */
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * Initialize the state of this request with defaults defined in the
     * {@link InitializrMetadata metadata}.
     *
     * @param metadata the metadata to use
     */
    public void initialize(InitializrMetadata metadata) {
        BeanWrapperImpl bean = new BeanWrapperImpl(this);
        metadata.defaults().forEach((key, value) -> {
            if (bean.isWritableProperty(key)) {
                // We want to be able to infer a package name if none has been
                // explicitly set
                if (!key.equals("packageName") && StringUtils.isBlank((String)bean.getPropertyValue(key))) {
                    bean.setPropertyValue(key, value);
                }
            }
        });
    }

}
