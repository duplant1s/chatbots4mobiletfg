package upc.edu.gessi.tfg.services;

import java.util.List;
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
        featureRepository.updateFeature(id, feature);
    }

    public void deleteFeature(String id) {
        featureRepository.deleteFeature(id);
    }

    //Feature_INTEGRATIONS
    // public Iterable<FeatureIntegration> getAllFeatureIntegrations() {
    //     return featureIntegrationRepository.findAll();
    // }

    // public Optional<FeatureIntegration> getFeatureIntegrationById(long id) {
    //     return featureIntegrationRepository.findById(id);
    // }

    // public FeatureIntegration createFeatureIntegration(FeatureIntegration featureIntegration) {
    //     return featureIntegrationRepository.save(featureIntegration);
    // }

    // public FeatureIntegration updateFeatureIntegration(long id, FeatureIntegration param) {
    //     FeatureIntegration existingFeatureIntegration = featureIntegrationRepository.findById(id).orElse(null);
    //     if (existingFeatureIntegration == null)
    //         return null;
    //     existingFeatureIntegration.setName(param.getName());
    //     existingFeatureIntegration.setSourceFeature(param.getSourceFeature());
    //     existingFeatureIntegration.setTargetFeature(param.getTargetFeature());
    //     return featureIntegrationRepository.save(existingFeatureIntegration);
    // }

    // public void deleteFeatureIntegration(long id) {
    //     featureIntegrationRepository.deleteById(id);
    // }
}
