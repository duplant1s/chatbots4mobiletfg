package upc.edu.gessi.tfg.services;

import java.util.List;
import java.util.Optional;

import org.eclipse.rdf4j.query.algebra.If;
import org.eclipse.rdf4j.query.algebra.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.Feature;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.repositories.FeatureIntegrationRepository;
import upc.edu.gessi.tfg.repositories.FeatureRepository;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private FeatureIntegrationRepository featureIntegrationRepository;

    //Features
    public List<Feature> getAllFeatures() {
        return featureRepository.getAllFeatures();
    }

    public Feature getFeatureById(String id) {
        return featureRepository.getFeature(id);
    }

    public void createFeature(Feature feature) {
        featureRepository.createFeature(feature);
    }

    public void updateFeature(String id, Feature feature) {
        if (feature.getId() == null)
            feature.setId(id);
        if (feature.getParameters() == null)
            feature.setParameters(null);
        featureRepository.updateFeature(id, feature);
    }

    public void deleteFeature(String id) {
        featureRepository.deleteFeature(id);
    }

    //Feature_INTEGRATIONS
    public List<FeatureIntegration> getAllFeatureIntegrations() {
        return featureIntegrationRepository.getAllFeatureIntegrations();
    }

    public FeatureIntegration getFeatureIntegration(String id) {
        return featureIntegrationRepository.getFeatureIntegration(id);
    }

    public void createFeatureIntegration(FeatureIntegration featureIntegration) {
        featureIntegration.setName(featureIntegration.getSourceFeature()+"-"+featureIntegration.getTargetFeature());
        featureIntegration.setId(featureIntegration.getName());
        featureIntegrationRepository.createFeatureIntegration(featureIntegration);
    }

    public void updateFeatureIntegration(String id, FeatureIntegration param) {
        if (param.getName() == null)
            param.setName(param.getSourceFeature()+"-"+param.getTargetFeature());
        if (param.getId() == null)
            param.setId(param.getName());
        featureIntegrationRepository.updateFeatureIntegration(id, param);
    }

    public void deleteFeatureIntegration(String id) {
        featureIntegrationRepository.deleteFeatureIntegration(id);
    }
}
