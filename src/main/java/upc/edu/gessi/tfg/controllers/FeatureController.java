package upc.edu.gessi.tfg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upc.edu.gessi.tfg.models.Feature;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.services.FeatureService;

@RequestMapping("/features")
@RestController
public class FeatureController {
    
    @Autowired
    private FeatureService featureService;

    @GetMapping
    public ResponseEntity<List<Feature>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feature> getFeature(@PathVariable String id) {
        Feature feature = featureService.getFeatureById(id);
        if (feature == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(feature);
    }

    @PostMapping
    public ResponseEntity<Feature> createFeature(@RequestBody Feature feature) {
        featureService.createFeature(feature);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feature> updateFeature(@PathVariable String id, @RequestBody Feature feature) {
        featureService.updateFeature(id, feature);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Feature> deleteUser(@PathVariable String id) {
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/apps")
    public ResponseEntity<List<String>> getAppsByFeature(@PathVariable String id) {
        if (featureService.getFeatureById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(featureService.getAppsByFeature(id));
    }

    //INTEGRATIONS

    @GetMapping("/integrations")
    public ResponseEntity<List<FeatureIntegration>> getAllFeatureIntegrations() {
        return ResponseEntity.ok(featureService.getAllFeatureIntegrations());
    }

    @GetMapping("/integrations/{id}")
    public ResponseEntity<FeatureIntegration> getFeatureIntegration(@PathVariable String id) {
        FeatureIntegration featureIntegration = featureService.getFeatureIntegration(id);
        if (featureIntegration == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(featureIntegration);
    }

    @PostMapping("/integrations")
    public ResponseEntity<FeatureIntegration> createFeatureIntegration(@RequestBody FeatureIntegration featureIntegration) {
        featureService.createFeatureIntegration(featureIntegration);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/integrations/{id}")
    public ResponseEntity<FeatureIntegration> updateFeatureIntegration(@PathVariable String id, @RequestBody FeatureIntegration featureIntegration) {
        featureService.updateFeatureIntegration(id, featureIntegration);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/integrations/{id}")
    public ResponseEntity<FeatureIntegration> deleteFeatureIntegration(@PathVariable String id) {
        featureService.deleteFeatureIntegration(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @RequestMapping("/integrations/source/{id}")
    public ResponseEntity<List<String>> getTargetFeatures(@PathVariable String id) {
        if (featureService.getFeatureById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(featureService.getFeatureIntegrationsBySourceFeature(id));
    }
}
