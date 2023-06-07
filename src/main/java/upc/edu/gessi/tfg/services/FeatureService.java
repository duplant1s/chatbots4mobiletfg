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
    //CRUDS/////////////////
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
        if (feature.getIdentifier() == null)
            feature.setIdentifier(id);
        if (feature.getParameters() == null)
            feature.setParameters(null);
        featureRepository.updateFeature(id, feature);
    }

    public void deleteFeature(String id) {
        featureRepository.deleteFeature(id);
    }

    //Feature_INTEGRATIONS
    //CRUDS/////////////////
    public List<FeatureIntegration> getAllFeatureIntegrations() {
        return featureIntegrationRepository.getAllFeatureIntegrations();
    }

    public FeatureIntegration getFeatureIntegration(String id) {
        return featureIntegrationRepository.getFeatureIntegration(id);
    }

    public void createFeatureIntegration(FeatureIntegration featureIntegration) {
        if (featureIntegration.getIdentifier() == null)
            featureIntegration.setIdentifier(featureIntegration.getSourceFeature() + "-" + featureIntegration.getTargetFeature());
        if (featureIntegration.getName() == null)
            featureIntegration.setName(featureIntegration.getSourceFeature() + "-" + featureIntegration.getTargetFeature());
        featureIntegrationRepository.createFeatureIntegration(featureIntegration);
    }

    public void updateFeatureIntegration(String id, FeatureIntegration featureIntegration) {
        featureIntegrationRepository.updateFeatureIntegration(id, featureIntegration);
    }

    public void deleteFeatureIntegration(String id) {
        featureIntegrationRepository.deleteFeatureIntegration(id);
    }
}
