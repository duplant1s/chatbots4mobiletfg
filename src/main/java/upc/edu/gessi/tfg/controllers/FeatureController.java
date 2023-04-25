// package upc.edu.gessi.tfg.controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import upc.edu.gessi.tfg.models.Feature;
// import upc.edu.gessi.tfg.models.FeatureIntegration;
// import upc.edu.gessi.tfg.services.FeatureService;

// @RequestMapping("/features")
// @RestController
// public class FeatureController {
//     @Autowired
//     private FeatureService featureService;

//     @GetMapping
//     public ResponseEntity<Iterable<Feature>> getAllParameters() {
//         return ResponseEntity.ok(featureService.getAllFeatures());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Feature> getFeature(@PathVariable long id) {
//         return featureService.getFeatureById(id)
//             .map(ResponseEntity::ok)
//             .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping
//     public ResponseEntity<Feature> createFeature(@RequestBody Feature feature) {
//         return new ResponseEntity<>(featureService.createFeature(feature), HttpStatus.CREATED);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Feature> updateFeature(@PathVariable long id, @RequestBody Feature feature) {
//         return featureService.getFeatureById(id)
//         .map(existingFeature -> {
//             feature.setId(existingFeature.getId());
//             return new ResponseEntity<>(featureService.createFeature(feature), HttpStatus.OK);
//         })
//         .orElse(ResponseEntity.notFound().build());
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Feature> deleteUser(@PathVariable long id) {
//         featureService.deleteFeature(id);
//         return ResponseEntity.noContent().build();
//     }

//     @GetMapping("/integration/{id}")
//     public ResponseEntity<FeatureIntegration> getFeatureIntegration(@PathVariable long id) {
//         return featureService.getFeatureIntegrationById(id)
//             .map(ResponseEntity::ok)
//             .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping("/integration")
//     public ResponseEntity<FeatureIntegration> createFeatureIntegration(@RequestBody FeatureIntegration featureIntegration) {
//         return new ResponseEntity<>(featureService.createFeatureIntegration(featureIntegration), HttpStatus.CREATED);
//     }

//     @PutMapping("/integration/{id}")
//     public ResponseEntity<FeatureIntegration> updateFeatureIntegration(@PathVariable long id, @RequestBody FeatureIntegration featureIntegration) {
//         return featureService.getFeatureIntegrationById(id)
//         .map(existingFeatureIntegration -> {
//             featureIntegration.setId(existingFeatureIntegration.getId());
//             return new ResponseEntity<>(featureService.createFeatureIntegration(featureIntegration), HttpStatus.OK);
//         })
//         .orElse(ResponseEntity.notFound().build());
//     }

//     @DeleteMapping("/integration/{id}")
//     public ResponseEntity<FeatureIntegration> deleteFeatureIntegration(@PathVariable long id) {
//         featureService.deleteFeatureIntegration(id);
//         return ResponseEntity.noContent().build();
//     }
// }
