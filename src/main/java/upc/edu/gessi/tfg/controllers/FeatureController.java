package upc.edu.gessi.tfg.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Iterable<Feature>> getAllParameters() {
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

    // @GetMapping("/integrations/{id}")
    // public ResponseEntity<FeatureIntegration> getFeatureIntegration(@PathVariable long id) {
    //     return featureService.getFeatureIntegrationById(id)
    //         .map(ResponseEntity::ok)
    //         .orElse(ResponseEntity.notFound().build());
    // }

    // @PostMapping("/integration")
    // public ResponseEntity<FeatureIntegration> createFeatureIntegration(@RequestBody FeatureIntegration featureIntegration) {
    //     return new ResponseEntity<>(featureService.createFeatureIntegration(featureIntegration), HttpStatus.CREATED);
    // }

    // @PutMapping("/integrations/{id}")
    // public ResponseEntity<FeatureIntegration> updateFeatureIntegration(@PathVariable long id, @RequestBody FeatureIntegration featureIntegration) {
    //     return featureService.getFeatureIntegrationById(id)
    //     .map(existingFeatureIntegration -> {
    //         featureIntegration.setId(existingFeatureIntegration.getId());
    //         return new ResponseEntity<>(featureService.createFeatureIntegration(featureIntegration), HttpStatus.OK);
    //     })
    //     .orElse(ResponseEntity.notFound().build());
    // }

    // @DeleteMapping("/integrations/{id}")
    // public ResponseEntity<FeatureIntegration> deleteFeatureIntegration(@PathVariable long id) {
    //     featureService.deleteFeatureIntegration(id);
    //     return ResponseEntity.noContent().build();
    // }
}
