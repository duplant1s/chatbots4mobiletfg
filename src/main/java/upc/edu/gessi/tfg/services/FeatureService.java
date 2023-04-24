package upc.edu.gessi.tfg.services;

import java.util.Optional;

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

    //FeatureS
    public Iterable<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    public Optional<Feature> getFeatureById(long id) {
        return featureRepository.findById(id);
    }

    public Feature createFeature(Feature feature) {
        return featureRepository.save(feature);
    }

    public Feature updateFeature(long id, Feature feature) {
        Feature existingFeature = featureRepository.findById(id).orElse(null);
        if (existingFeature == null)
            return null;
        existingFeature.setName(feature.getName());
        existingFeature.setParameters(feature.getParameters());
        return featureRepository.save(existingFeature);
    }

    public void deleteFeature(long id) {
        featureIntegrationRepository.deleteById(id);
    }

    //Feature_INTEGRATIONS
    public Iterable<FeatureIntegration> getAllFeatureIntegrations() {
        return featureIntegrationRepository.findAll();
    }

    public Optional<FeatureIntegration> getFeatureIntegrationById(long id) {
        return featureIntegrationRepository.findById(id);
    }

    public FeatureIntegration createFeatureIntegration(FeatureIntegration featureIntegration) {
        return featureIntegrationRepository.save(featureIntegration);
    }

    public FeatureIntegration updateFeatureIntegration(long id, FeatureIntegration param) {
        FeatureIntegration existingFeatureIntegration = featureIntegrationRepository.findById(id).orElse(null);
        if (existingFeatureIntegration == null)
            return null;
        existingFeatureIntegration.setName(param.getName());
        existingFeatureIntegration.setSourceFeature(param.getSourceFeature());
        existingFeatureIntegration.setTargetFeature(param.getTargetFeature());
        return featureIntegrationRepository.save(existingFeatureIntegration);
    }

    public void deleteFeatureIntegration(long id) {
        featureIntegrationRepository.deleteById(id);
    }
}
