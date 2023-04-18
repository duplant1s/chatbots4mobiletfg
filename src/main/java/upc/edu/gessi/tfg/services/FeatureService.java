package upc.edu.gessi.tfg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.Feature;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.repositories.GraphDBService;

@Service
public class FeatureService {

    @Autowired 
    private GraphDBService graphDBService;

    //FEATURES
    public Feature getFeatureById(long id) {
        return graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature", "id", id));
    }

    public Feature createFeature(Feature feature) {
        return graphDBService.save(feature);
    }

    public Feature updateFeature(long id, Feature feature) {
        Feature existingFeature = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature", "id", id));
        existingFeature.setName(feature.getName());
        existingFeature.setDescription(feature.getDescription());
        return graphDBService.save(existingFeature);
    }

    public Feature deleteFeature(long id) {
        Feature existingFeature = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature", "id", id));
        graphDBService.delete(existingFeature);
        return existingFeature;
    }

    //FEATURE_INTEGRATIONS
    public FeatureIntegration getFeatureIntegrationById(long id) {
        return graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureIntegration", "id", id));
    }

    public FeatureIntegration createFeatureIntegration(FeatureIntegration featureIntegration) {
        return graphDBService.save(featureIntegration);
    }

    public FeatureIntegration updateFeatureIntegration(long id, FeatureIntegration featureIntegration) {
        FeatureIntegration existingFeatureIntegration = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureIntegration", "id", id));
        existingFeatureIntegration.setFeature(featureIntegration.getFeature());
        existingFeatureIntegration.setIntegration(featureIntegration.getIntegration());
        return graphDBService.save(existingFeatureIntegration);
    }

    public FeatureIntegration deleteFeatureIntegration(long id) {
        FeatureIntegration existingFeatureIntegration = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureIntegration", "id", id));
        graphDBService.delete(existingFeatureIntegration);
        return existingFeatureIntegration;
    }
}
